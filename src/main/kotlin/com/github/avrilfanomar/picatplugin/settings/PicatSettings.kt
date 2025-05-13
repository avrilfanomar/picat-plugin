package com.github.avrilfanomar.picatplugin.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Persistent settings for the Picat plugin.
 * Stores the path to the Picat executable.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "PicatSettings",
    storages = [Storage("picatSettings.xml")]
)
class PicatSettings : PersistentStateComponent<PicatSettings> {
    var picatExecutablePath: String = ""

    override fun getState(): PicatSettings = this

    override fun loadState(state: PicatSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        @JvmStatic
        fun getInstance(project: Project): PicatSettings =
            project.getService(PicatSettings::class.java)
    }
}