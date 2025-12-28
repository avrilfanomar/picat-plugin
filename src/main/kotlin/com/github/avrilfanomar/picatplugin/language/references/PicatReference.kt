package com.github.avrilfanomar.picatplugin.language.references

import com.github.avrilfanomar.picatplugin.cache.PicatPsiCache
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgument
import com.github.avrilfanomar.picatplugin.language.psi.PicatArgumentListTail
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCall
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallNoDot
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallNoDotSimple
import com.github.avrilfanomar.picatplugin.language.psi.PicatFunctionCallSimple
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.language.psi.PicatImportItem
import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reference for Picat predicate/function atoms.
 *
 * Resolves to matching heads (predicate/function) in the same file by
 * name AND arity. multiResolve() returns all candidates; resolve() prefers
 * a single candidate when available.
 *
 * Resolution priority:
 * 1. Local definitions in the current file
 * 2. Imported module definitions
 * 3. Standard library definitions
 * 4. Built-in primitives
 */
class PicatReference(element: PsiElement, rangeInElement: TextRange) :
    PsiPolyVariantReferenceBase<PsiElement>(element, rangeInElement, false) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val file = element.containingFile
        val identifier = file?.let { IdentifierExtractor.extract(element, rangeInElement) }
        if (file == null || identifier == null) return ResolveResult.EMPTY_ARRAY

        val usageArity = ArityComputer.computeFromUsage(element, rangeInElement)
        val matches = HeadMatcher.findMatches(HeadCollector.collectAll(file, element.project), identifier, usageArity)
        return matches.map { PsiElementResolveResult(resolveTarget(it)) as ResolveResult }.toTypedArray()
    }

    override fun resolve(): PsiElement? = multiResolve(false).firstOrNull()?.element

    override fun getVariants(): Array<Any> {
        val file = element.containingFile ?: return emptyArray()
        val headCollections = HeadCollector.collectAll(file, element.project)
        val allHeads = headCollections.all()

        return allHeads
            .mapNotNull { head -> head.atomName()?.let { it to head.arity() } }
            .distinct()
            .map { (name, arity) ->
                LookupElementBuilder.create(name).withTypeText("/$arity", true)
            }
            .toTypedArray()
    }

    /**
     * Returns the target element for navigation (the identifier within the head).
     */
    private fun resolveTarget(head: PicatHead): PsiElement =
        head.atom.identifier ?: head.atom
}

/**
 * Extracts the identifier text from a PSI element reference.
 */
private object IdentifierExtractor {

    /**
     * Extracts the identifier name from the element using the given range.
     * Returns null if the identifier cannot be determined or is invalid.
     */
    fun extract(element: PsiElement, rangeInElement: TextRange): String? {
        val slicedText = extractSlicedText(element, rangeInElement)
        val trimmed = trimQuotes(slicedText)

        return when {
            trimmed.isNotEmpty() && !containsParentheses(trimmed) -> trimmed
            else -> extractFromAtom(element)
        }?.takeIf { it.isNotBlank() }
    }

    private fun extractSlicedText(element: PsiElement, range: TextRange): String {
        val rawText = element.text
        return try {
            rawText.substring(range.startOffset, range.endOffset)
        } catch (_: Exception) {
            rawText
        }
    }

    private fun containsParentheses(text: String): Boolean =
        text.contains('(') || text.contains(')')

    private fun extractFromAtom(element: PsiElement): String? {
        val atom = (element as? PicatAtomOrCallNoLambda)?.atom
            ?: element as? PicatAtom
            ?: return null

        return atom.identifier?.text
            ?: atom.singleQuotedAtom?.text?.let { trimQuotes(it) }
    }
}

/**
 * Computes the arity (number of arguments) at a usage site.
 */
private object ArityComputer {

    /**
     * Computes the arity from the PSI context at the usage site.
     * Returns null if arity cannot be determined.
     */
    fun computeFromUsage(element: PsiElement, rangeInElement: TextRange): Int? =
        computeFromPsi(element) ?: computeFromRawText(element, rangeInElement)

    private fun computeFromPsi(element: PsiElement): Int? = when (element) {
        is PicatAtomOrCallNoLambda -> countCallArgs(element.argument, element.argumentListTail)
        else -> computeFromAtomContext(element)
    }

    private fun computeFromAtomContext(element: PsiElement): Int? {
        val atom = element as? PicatAtom ?: element.parent as? PicatAtom ?: return null
        // Check if atom is part of a head definition, or try various call node shapes
        return atom.parentOfType<PicatHead>()?.arity() ?: findCallArityFromParent(atom)
    }

    private fun findCallArityFromParent(atom: PsiElement): Int? =
        arityFromAtomOrCallNoLambda(atom)
            ?: arityFromFunctionCall(atom)
            ?: arityFromFunctionCallNoDot(atom)
            ?: arityFromFunctionCallNoDotSimple(atom)
            ?: arityFromFunctionCallSimple(atom)

    private fun arityFromAtomOrCallNoLambda(atom: PsiElement): Int? =
        atom.parentOfType<PicatAtomOrCallNoLambda>()?.let { call ->
            countCallArgs(call.argument, call.argumentListTail)
        }

    private fun arityFromFunctionCall(atom: PsiElement): Int? =
        atom.parentOfType<PicatFunctionCall>()?.let { fn ->
            val simple = fn.functionCallSimple
            val qualified = fn.qualifiedFunctionCall
            val first = simple?.argument ?: qualified?.argument
            val tail = simple?.argumentListTail ?: qualified?.argumentListTail
            countCallArgs(first, tail)
        }

    private fun arityFromFunctionCallNoDot(atom: PsiElement): Int? =
        atom.parentOfType<PicatFunctionCallNoDot>()?.let { fn ->
            val simple = fn.functionCallNoDotSimple
            countCallArgs(simple?.argument, simple?.argumentListTail)
        }

    private fun arityFromFunctionCallNoDotSimple(atom: PsiElement): Int? =
        atom.parentOfType<PicatFunctionCallNoDotSimple>()?.let { fn ->
            countCallArgs(fn.argument, fn.argumentListTail)
        }

    private fun arityFromFunctionCallSimple(atom: PsiElement): Int? =
        atom.parentOfType<PicatFunctionCallSimple>()?.let { fn ->
            countCallArgs(fn.argument, fn.argumentListTail)
        }

    /**
     * Fallback: derive arity from raw file text after the reference name.
     */
    private fun computeFromRawText(element: PsiElement, rangeInElement: TextRange): Int? =
        element.containingFile?.text?.let { fileText ->
            computeAfterNameOffset(element, rangeInElement)
                .takeIf { it in 0..fileText.length }
                ?.let { fileText.substring(it) }
                ?.takeIf { it.startsWith("(") }
                ?.let { parseArityFromParentheses(it) }
        }

    private fun computeAfterNameOffset(element: PsiElement, range: TextRange): Int = try {
        element.textRange.startOffset + range.endOffset
    } catch (_: Exception) {
        element.textRange.endOffset
    }

    private fun parseArityFromParentheses(tail: String): Int? {
        val closeIndex = tail.indexOf(')')
        if (closeIndex < 0) return null

        val inside = tail.substring(1, closeIndex)
        return if (inside.isBlank()) 0 else inside.count { it == ',' } + 1
    }

    private fun countCallArgs(first: PicatArgument?, tail: PicatArgumentListTail?): Int =
        if (first == null) 0 else 1 + countTail(tail)

    private tailrec fun countTail(tail: PicatArgumentListTail?, acc: Int = 0): Int =
        if (tail == null) acc else countTail(tail.argumentListTail, acc + 1)
}

/**
 * Container for head collections from different sources with priority ordering.
 */
private data class HeadCollections(
    val local: List<PicatHead>,
    val imported: Set<PicatHead>,
    val stdlib: Set<PicatHead>,
    val primitives: Set<PicatHead>
) {
    fun all(): List<PicatHead> = local + imported + stdlib + primitives
}

/**
 * Collects heads from various sources: local file, imports, stdlib, and primitives.
 */
private object HeadCollector {

    fun collectAll(file: PsiFile, project: Project): HeadCollections {
        val cache = PicatPsiCache.getInstance(project)
        return HeadCollections(
            local = cache.getFileHeads(file),
            imported = collectFromImports(file, cache, project),
            stdlib = cache.getStdlibHeads(),
            primitives = cache.getPrimitiveHeads()
        )
    }

    private fun collectFromImports(file: PsiFile, cache: PicatPsiCache, project: Project): Set<PicatHead> {
        val importedModuleNames = PsiTreeUtil.findChildrenOfType(file, PicatImportItem::class.java)
            .mapNotNull { it.importModuleName() }
            .distinct()

        return importedModuleNames
            .mapNotNull { moduleName -> findModulePsiFile(project, moduleName) }
            .flatMap { psiFile -> cache.getFileHeads(psiFile) }
            .toSet()
    }

    private fun findModulePsiFile(project: Project, moduleName: String): PsiFile? =
        PicatStdlibUtil.findStdlibModulePsiFile(project, moduleName)
            ?: findModuleInPicatPath(project, moduleName)
            ?: findModuleInProjectRoots(project, moduleName)

    private fun findModuleInPicatPath(project: Project, moduleName: String): PsiFile? {
        val settings = PicatSettings.getInstance(project)
        val picatPathDirs = settings.getPicatPathDirectories()
        if (picatPathDirs.isEmpty()) return null

        val psiManager = PsiManager.getInstance(project)
        val fileName = "$moduleName.pi"
        val vfm = VirtualFileManager.getInstance()

        return picatPathDirs
            .firstNotNullOfOrNull { dirPath ->
                findDirectoryByPath(vfm, dirPath)?.let { dir ->
                    findModuleFile(dir, fileName)?.let { vf ->
                        psiManager.findFile(vf)?.takeIf { it.isValid }
                    }
                }
            }
    }

    /**
     * Finds a directory VirtualFile by path, trying multiple VFS schemes.
     * Supports both real file system paths and temp:// paths used in tests.
     */
    private fun findDirectoryByPath(vfm: VirtualFileManager, path: String): VirtualFile? {
        if (path.contains("://")) {
            return vfm.findFileByUrl(path)?.takeIf { it.isDirectory }
        }
        // Try file:// then temp:// URL forms (temp:// is used in tests)
        return (
            vfm.findFileByUrl("file://$path")
                ?: vfm.findFileByUrl("temp://$path")
                ?: vfm.findFileByUrl("temp:///" + path.trimStart('/'))
            )?.takeIf { it.isDirectory }
    }

    private fun findModuleInProjectRoots(project: Project, moduleName: String): PsiFile? {
        val psiManager = PsiManager.getInstance(project)
        val fileName = "$moduleName.pi"
        val rootManager = ProjectRootManager.getInstance(project)
        val allRoots = (rootManager.contentSourceRoots.toList() + rootManager.contentRoots.toList()).distinct()

        return allRoots
            .firstNotNullOfOrNull { root ->
                findModuleFile(root, fileName)?.let { vf ->
                    psiManager.findFile(vf)?.takeIf { it.isValid }
                }
            }
    }

    private fun findModuleFile(dir: VirtualFile, fileName: String): VirtualFile? {
        if (!dir.isValid || !dir.isDirectory) return null

        // Check lib subdirectory first, then direct child
        val libDir = dir.findChild("lib")
        val inLib = libDir?.takeIf { it.isValid && it.isDirectory }?.findChild(fileName)
        val directChild = dir.findChild(fileName)

        return listOfNotNull(inLib, directChild)
            .firstOrNull { it.isValid && !it.isDirectory }
    }
}

/**
 * Matches heads by name and arity with priority-based resolution.
 */
private object HeadMatcher {

    /**
     * Finds matching heads with priority: local > imported > stdlib > primitives.
     * Falls back to name-only matching if exact name+arity match fails.
     */
    fun findMatches(collections: HeadCollections, name: String, arity: Int?): List<PicatHead> =
        findExactMatch(collections, name, arity).ifEmpty {
            findNameOnlyMatch(collections, name, arity).ifEmpty {
                findTextualMatch(collections.local, name)
            }
        }

    private fun findExactMatch(collections: HeadCollections, name: String, arity: Int?): List<PicatHead> {
        val sources = listOf(
            collections.local,
            collections.imported.toList(),
            collections.stdlib.toList(),
            collections.primitives.toList()
        )

        for (source in sources) {
            val matches = filterByNameAndArity(source, name, arity)
            if (matches.isNotEmpty()) return matches
        }

        return emptyList()
    }

    private fun findNameOnlyMatch(collections: HeadCollections, name: String, arity: Int?): List<PicatHead> {
        val sources = listOf(
            collections.local,
            collections.imported.toList(),
            collections.stdlib.toList(),
            collections.primitives.toList()
        )

        for (source in sources) {
            val matches = source.filter { it.atomName() == name }
            if (matches.isNotEmpty()) return prioritizeByArity(matches, arity)
        }

        return emptyList()
    }

    private fun findTextualMatch(localHeads: List<PicatHead>, name: String): List<PicatHead> {
        // Try to find a local head that textually matches "name("
        val textualMatch = localHeads
            .filter { h -> h.text.startsWith("$name(") || h.text.contains("$name(") }
            .minByOrNull { it.textOffset }

        if (textualMatch != null) return listOf(textualMatch)

        // Loose match: name contains identifier
        val looseMatch = localHeads.firstOrNull { h ->
            h.atomName()?.contains(name) == true
        }

        return listOfNotNull(looseMatch)
    }

    private fun filterByNameAndArity(heads: Collection<PicatHead>, name: String, arity: Int?): List<PicatHead> {
        val nameMatches = heads.filter { it.atomName() == name }
        if (nameMatches.isEmpty()) return emptyList()

        return if (arity != null) {
            nameMatches.sortedWith(
                compareBy<PicatHead> { it.arity() != arity }.thenBy { it.textOffset }
            )
        } else {
            nameMatches.sortedBy { it.textOffset }
        }
    }

    private fun prioritizeByArity(heads: List<PicatHead>, arity: Int?): List<PicatHead> =
        if (arity == null) {
            heads.sortedBy { it.textOffset }
        } else {
            heads.sortedWith(
                compareBy<PicatHead> { it.arity() != arity }.thenBy { it.textOffset }
            )
        }
}

// Extension functions for PicatHead

private fun PicatHead.arity(): Int = this.headArgs?.argumentList?.size ?: 0

private fun PicatHead.atomName(): String? {
    val a = this.atom
    return a.identifier?.text ?: a.singleQuotedAtom?.text?.let { trimQuotes(it) }
}

// Extension function for import items

private fun PicatImportItem.importModuleName(): String? {
    val atom = this.atom ?: return null
    val raw = atom.identifier?.text ?: atom.singleQuotedAtom?.text?.let { trimQuotes(it) }
    return raw?.trim()?.takeIf { it.isNotEmpty() }
}

// Utility functions

/**
 * Trims quote characters (backtick, single quote, double quote) from both ends of a string.
 */
private fun trimQuotes(text: String): String {
    var start = 0
    var end = text.length
    while (start < end && text[start] in QUOTE_CHARS) start++
    while (end > start && text[end - 1] in QUOTE_CHARS) end--
    return if (start == 0 && end == text.length) text else text.substring(start, end)
}

private val QUOTE_CHARS = setOf('`', '\'', '"')

private inline fun <reified T : PsiElement> PsiElement.parentOfType(): T? {
    var curr: PsiElement? = this.parent
    while (curr != null) {
        if (curr is T) return curr
        curr = curr.parent
    }
    return null
}
