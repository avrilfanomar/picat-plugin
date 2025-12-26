package com.github.avrilfanomar.picatplugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ModificationTracker
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Transient

/**
 * Persistent settings for the Picat plugin.
 * Stores the path to the Picat executable.
 *
 * Implements ModificationTracker to allow caches to depend on settings changes.
 */
@Service(Service.Level.PROJECT)
@State(
    name = "PicatSettings",
    storages = [Storage("picatSettings.xml")]
)
class PicatSettings : PersistentStateComponent<PicatSettings>, ModificationTracker {

    @Transient
    private var modificationCount: Long = 0

    /** Path to the Picat executable. */
    var picatExecutablePath: String = ""
        set(value) {
            if (field != value) {
                field = value
                modificationCount++
            }
        }

    /** Whether to enable code annotations and inspections. */
    var enableAnnotations: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                modificationCount++
            }
        }

    override fun getModificationCount(): Long = modificationCount

    override fun getState(): PicatSettings = this

    override fun loadState(state: PicatSettings) {
        XmlSerializerUtil.copyBean(state, this)
        modificationCount++
    }

    /** Companion object for accessing PicatSettings instances. */
    companion object {
        /** Gets the PicatSettings instance for the given project. */
        @JvmStatic
        fun getInstance(project: Project): PicatSettings =
            project.getService(PicatSettings::class.java)
    }
}
