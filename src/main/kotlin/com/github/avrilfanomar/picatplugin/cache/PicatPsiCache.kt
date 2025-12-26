package com.github.avrilfanomar.picatplugin.cache

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.github.avrilfanomar.picatplugin.language.psi.PicatHead
import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil

/**
 * Project-level cache for expensive PSI operations in the Picat plugin.
 *
 * Uses IntelliJ's CachedValuesManager for proper invalidation when:
 * - PSI tree modifications occur (file edits)
 * - Project structure changes
 * - Settings change (e.g., Picat executable path)
 *
 * Note: For stdlib and primitives, we don't cache PSI elements directly as they
 * can become invalid when underlying files are disposed. Instead, we recompute
 * on each access for these project-wide lookups to ensure validity.
 */
@Service(Service.Level.PROJECT)
class PicatPsiCache(private val project: Project) {

    /** Companion object for accessing PicatPsiCache instances. */
    companion object {
        /** Gets the PicatPsiCache instance for the given project. */
        @JvmStatic
        fun getInstance(project: Project): PicatPsiCache =
            project.getService(PicatPsiCache::class.java)
    }

    /**
     * Get all PicatHead elements in a file, cached with PSI modification tracking.
     * Uses file-level UserData for per-file caching, which is automatically
     * invalidated when the file changes or is disposed.
     */
    fun getFileHeads(file: PsiFile): List<PicatHead> {
        if (!file.isValid) return emptyList()
        return CachedValuesManager.getCachedValue(file) {
            val heads = PsiTreeUtil.findChildrenOfType(file, PicatHead::class.java).toList()
            CachedValueProvider.Result.create(heads, file)
        }
    }

    /**
     * Get primitives PSI file, created on demand.
     * Since this is a synthetic file created from bundled resources, we create
     * a fresh instance each time to avoid caching issues with PSI validity.
     * The file creation is lightweight (just parsing a small bundled file).
     */
    fun getPrimitivesPsiFile(): PsiFile? {
        return createPrimitivesPsiFile()
    }

    /**
     * Get all heads from primitives file.
     * Recomputed on each access since primitives file is synthetic.
     */
    fun getPrimitiveHeads(): Set<PicatHead> {
        val primitivesFile = getPrimitivesPsiFile() ?: return emptySet()
        return PsiTreeUtil.findChildrenOfType(primitivesFile, PicatHead::class.java).toSet()
    }

    /**
     * Get stdlib lib directory VirtualFile.
     * Not cached as directory lookup is cheap and VirtualFile validity can change.
     */
    fun getStdlibLibDir(): VirtualFile? {
        return resolveLibVfsDirInternal()
    }

    /**
     * Get all heads from stdlib modules.
     * Recomputed on each access as stdlib files may be disposed between calls.
     * Uses getFileHeads() for per-file caching where possible.
     */
    fun getStdlibHeads(): Set<PicatHead> {
        val libDir = getStdlibLibDir() ?: return emptySet()
        val psiManager = PsiManager.getInstance(project)

        return libDir.children
            .filter { it.isValid && it.extension == "pi" }
            .mapNotNull { vf ->
                val psiFile = psiManager.findFile(vf)
                if (psiFile != null && psiFile.isValid) psiFile else null
            }
            .flatMap { psiFile -> getFileHeads(psiFile) }
            .toSet()
    }

    private fun createPrimitivesPsiFile(): PsiFile? {
        val resourcePath = "/stdlib/primitives.pi"
        val content = PicatPsiCache::class.java.getResourceAsStream(resourcePath)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: return null

        return PsiFileFactory.getInstance(project)
            .createFileFromText("primitives.pi", PicatFileType, content)
    }

    private fun resolveLibVfsDirInternal(): VirtualFile? {
        val settings = PicatSettings.getInstance(project)
        val rawPath = settings.picatExecutablePath.trim()
        if (rawPath.isEmpty()) return null

        val vfm = VirtualFileManager.getInstance()
        val baseVf = if (rawPath.contains("://")) {
            vfm.findFileByUrl(rawPath)
        } else {
            vfm.findFileByUrl("file://$rawPath")
                ?: vfm.findFileByUrl("temp://$rawPath")
                ?: vfm.findFileByUrl("temp:///" + rawPath.trimStart('/'))
                ?: LocalFileSystem.getInstance().findFileByPath(rawPath)
        }

        val home = when {
            baseVf == null -> null
            baseVf.isDirectory -> baseVf
            else -> baseVf.parent
        }
        val lib = home?.findChild("lib")
        return if (lib != null && lib.isValid && lib.isDirectory) lib else null
    }
}
