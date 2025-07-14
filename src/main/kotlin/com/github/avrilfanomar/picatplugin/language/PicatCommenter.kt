package com.github.avrilfanomar.picatplugin.language

import com.intellij.lang.Commenter

/**
 * Provides comment functionality for Picat language.
 * Supports both line comments (%) and block comments (/* */).
 */
class PicatCommenter : Commenter {
    
    override fun getLineCommentPrefix(): String = "%"
    
    override fun getBlockCommentPrefix(): String = "/*"
    
    override fun getBlockCommentSuffix(): String = "*/"
    
    override fun getCommentedBlockCommentPrefix(): String? = null
    
    override fun getCommentedBlockCommentSuffix(): String? = null
}