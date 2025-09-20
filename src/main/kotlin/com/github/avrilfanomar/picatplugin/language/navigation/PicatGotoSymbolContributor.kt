package com.github.avrilfanomar.picatplugin.language.navigation

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * Goto Symbol contributor for Picat.
 *
 * Indexes Picat predicate/function heads so users can navigate via Navigate | Symbol.
 *
 * Currently uses a light-weight project scan over Picat files. For large projects,
 * this can be replaced with a proper stub index when available.
 */
class PicatGotoSymbolContributor : ChooseByNameContributor {

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val names = LinkedHashSet<String>()
        forEachPicatHead(project, includeNonProjectItems) { head, _ ->
            headAtomName(head)?.let { name ->
                names.add(name)
                // Also expose name/arity for precise lookup, e.g., p/2
                names.add("$name/${head.arity()}")
            }
        }
        return names.toTypedArray()
    }

    override fun getItemsByName(
        name: String,
        pattern: String,
        project: Project,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val result = ArrayList<NavigationItem>()
        val (targetName, targetArity) = parseNameAndArity(name)
        forEachPicatHead(project, includeNonProjectItems) { head, _ ->
            val n = headAtomName(head)
            if (n == targetName) {
                if (targetArity == null || head.arity() == targetArity) {
                    if (head is NavigationItem) {
                        result.add(head)
                    }
                }
            }
        }
        return result.toTypedArray()
    }

    private fun forEachPicatHead(
        project: Project,
        includeNonProjectItems: Boolean,
        consumer: (head: PicatHead, file: VirtualFile) -> Unit
    ) {
        val scope = if (includeNonProjectItems) GlobalSearchScope.everythingScope(project)
        else GlobalSearchScope.projectScope(project)
        val files = FileTypeIndex.getFiles(PicatFileType, scope)
        val psiManager = PsiManager.getInstance(project)
        files.forEach { vf ->
            val psiFile = psiManager.findFile(vf) ?: return@forEach
            // Simple PSI walk: collect PicatHead children
            psiFile.childrenRecursiveOfType(PicatHead::class.java).forEach { head ->
                consumer(head, vf)
            }
        }
    }
}

private fun PicatHead.arity(): Int = this.headArgs?.argumentList?.size ?: 0

private fun headAtomName(head: PicatHead): String? {
    val atom = head.atom
    val ident = atom.identifier?.text
    if (ident != null) return ident
    val quoted = atom.singleQuotedAtom?.text
    return quoted?.trim('\'', '"', '`')
}

private fun parseNameAndArity(raw: String): Pair<String, Int?> {
    val idx = raw.lastIndexOf('/')
    if (idx > 0 && idx < raw.length - 1) {
        val name = raw.take(idx)
        val arityStr = raw.substring(idx + 1)
        val arity = arityStr.toIntOrNull()
        return name to arity
    }
    return raw to null
}

private fun <T> com.intellij.psi.PsiElement.childrenRecursiveOfType(clazz: Class<T>): List<T> {
    val out = ArrayList<T>()
    fun walk(e: com.intellij.psi.PsiElement) {
        e.children.forEach { child ->
            if (clazz.isInstance(child)) out.add(clazz.cast(child))
            walk(child)
        }
    }
    walk(this)
    return out
}
