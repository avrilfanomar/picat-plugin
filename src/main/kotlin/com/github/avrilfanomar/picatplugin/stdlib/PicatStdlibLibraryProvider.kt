package com.github.avrilfanomar.picatplugin.stdlib

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

/**
 * Exposes Picat standard modules as a read-only library attached to the project.
 *
 * The stdlib is discovered next to the configured Picat executable path:
 *   <picat-path>/lib
 * or if <picat-path> is a directory, then
 *   <picat-path>/lib
 *
 * All files under the lib directory are attached as additional (read-only) library roots,
 * similar to how the Java SDK is attached. This enables navigation and indexing of
 * standard modules.
 */
class PicatStdlibLibraryProvider : AdditionalLibraryRootsProvider() {
    override fun getAdditionalProjectLibraries(project: Project): Collection<SyntheticLibrary> {
        val settings = PicatSettings.getInstance(project)
        val exePath = settings.picatExecutablePath.trim()
        if (exePath.isEmpty()) return emptyList()

        val vDir = resolveLibVfsDir(exePath) ?: return emptyList()

        // Create a synthetic library with the lib directory as its source root.
        val library = object : SyntheticLibrary() {
            override fun getSourceRoots() = listOf(vDir)
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is SyntheticLibrary) return false
                return this.sourceRoots == other.sourceRoots
            }
            override fun hashCode(): Int = sourceRoots.hashCode()
            override fun toString(): String = "Picat Standard Library: " + vDir.presentableUrl
        }
        return listOf(library)
    }

    private fun resolveLibVfsDir(executablePath: String): VirtualFile? {
        val vfm = VirtualFileManager.getInstance()
        val baseVf = if (executablePath.contains("://")) {
            vfm.findFileByUrl(executablePath)
        } else {
            com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(executablePath)
                ?: vfm.findFileByUrl("temp://$executablePath")
                ?: vfm.findFileByUrl("temp:///$executablePath")
        } ?: return null
        val home = if (baseVf.isDirectory) baseVf else baseVf.parent ?: return null
        val lib = home.findChild("lib") ?: return null
        return if (lib.isDirectory) lib else null
    }
}
