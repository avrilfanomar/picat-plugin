package com.github.avrilfanomar.picatplugin.language

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * Icons used by the Picat plugin.
 */
object PicatIcons {
    // Using a placeholder icon from the IntelliJ platform
    // In a real implementation, we would create and use custom icons
    
    /**
     * Icon used for Picat files in the IDE file tree and editor tabs.
     */
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/picat.svg", PicatIcons::class.java)
}
