package com.github.avrilfanomar.picatplugin.language

/**
 * Placeholder for Picat Spellchecking strategy.
 *
 * The actual spellchecking integration relies on the IntelliJ Spellchecker plugin API.
 * To keep the project buildable across IDE versions where the Spellchecker plugin
 * may not be resolvable on the Gradle classpath, we avoid hard compile-time
 * dependency here. The extension is also not registered in plugin.xml.
 *
 * If/when Spellchecker dependency is restored, reintroduce the implementation
 * that extends com.intellij.spellchecker.tokenizer.SpellcheckingStrategy and
 * registers under the 'com.intellij.spellchecker.support' EP.
 */
class PicatSpellcheckingStrategy
