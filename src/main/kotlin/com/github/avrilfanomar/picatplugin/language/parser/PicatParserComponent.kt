package com.github.avrilfanomar.picatplugin.language.parser

/**
 * Interface for all Picat parser components.
 * Defines the contract that all parser components must fulfill.
 */
interface PicatParserComponent {
    /**
     * Initialize this parser component with references to other parser components.
     * This method should be called before using the parser component.
     */
    fun initialize(parserContext: PicatParserContext)
}
