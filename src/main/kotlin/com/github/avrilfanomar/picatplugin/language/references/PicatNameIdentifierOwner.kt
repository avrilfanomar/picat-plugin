package com.github.avrilfanomar.picatplugin.language.references

import com.intellij.psi.PsiNameIdentifierOwner

/**
 * Marker interface for Picat PSI elements that own a name identifier.
 * 
 * This interface extends PsiNameIdentifierOwner to provide Picat-specific
 * name identifier functionality for elements that can be renamed or referenced.
 */
interface PicatNameIdentifierOwner : PsiNameIdentifierOwner
