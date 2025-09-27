package com.github.avrilfanomar.picatplugin.stdlib

import com.github.avrilfanomar.picatplugin.settings.PicatSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

/**
 * Utilities to work with Picat standard library modules located under <picatHome>/lib.
 */
object PicatStdlibUtil {
    /**
     * Finds a VirtualFile for a stdlib module file (e.g., moduleName "basic" => basic.pi),
     * according to the configured Picat executable path. Returns null if not found
     * or if the path is not configured.
     */
    fun findStdlibModuleVFile(project: Project, moduleName: String): VirtualFile? {
        val settings = PicatSettings.getInstance(project)
        val rawPath = settings.picatExecutablePath.trim()
        if (rawPath.isEmpty()) return null

        val vfm = VirtualFileManager.getInstance()
        val baseVf = if (rawPath.contains("://")) {
            vfm.findFileByUrl(rawPath)
        } else {
            // Try file:// then temp:// URL forms
            vfm.findFileByUrl("file://" + rawPath)
                ?: vfm.findFileByUrl("temp://" + rawPath)
                ?: vfm.findFileByUrl("temp:///" + rawPath.trimStart('/'))
        }
        val picatHome = when {
            baseVf == null -> null
            baseVf.isDirectory -> baseVf
            else -> baseVf.parent
        }
        val libDir = picatHome?.findChild("lib")
        return libDir?.findChild("$moduleName.pi")
    }

    /**
     * Convenience to get PSI file for a stdlib module by name.
     */
    fun findStdlibModulePsiFile(project: Project, moduleName: String): PsiFile? {
        val vf = findStdlibModuleVFile(project, moduleName)
        return vf?.let { PsiManager.getInstance(project).findFile(it) }
    }
}
