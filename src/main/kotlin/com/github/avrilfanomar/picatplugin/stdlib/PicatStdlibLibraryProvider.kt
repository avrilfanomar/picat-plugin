package com.github.avrilfanomar.picatplugin.stdlib

import com.github.avrilfanomar.picatplugin.stdlib.PicatStdlibUtil.resolveLibVfsDir
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary

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
        val vDir = resolveLibVfsDir(project)
        val library: SyntheticLibrary? = vDir?.let { dir ->
            // Create a synthetic library with the lib directory as its source root.
            object : SyntheticLibrary() {
                override fun getSourceRoots() = listOf(dir)
                override fun equals(other: Any?): Boolean {
                    if (this === other) return true
                    if (other !is SyntheticLibrary) return false
                    return this.sourceRoots == other.sourceRoots
                }

                override fun hashCode(): Int = sourceRoots.hashCode()
                override fun toString(): String = "Picat Standard Library: " + dir.presentableUrl
            }
        }
        return if (library != null) listOf(library) else emptyList()
    }
}
