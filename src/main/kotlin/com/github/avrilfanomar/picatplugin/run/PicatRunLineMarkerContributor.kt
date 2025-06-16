package com.github.avrilfanomar.picatplugin.run

import com.github.avrilfanomar.picatplugin.language.PicatFileType
import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement

/**
 * Contributes run line markers for Picat files.
 * This allows running Picat programs directly from the editor.
 */
class PicatRunLineMarkerContributor : RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        val file = element.containingFile

        // Only add run marker to the first element in Picat files
        if (file != null && file.fileType is PicatFileType && element.textOffset == 0) {
            return Info(
                AllIcons.RunConfigurations.TestState.Run,
                ExecutorAction.getActions(1)
            ) { "Run Picat program" }
        }

        return null
    }
}
