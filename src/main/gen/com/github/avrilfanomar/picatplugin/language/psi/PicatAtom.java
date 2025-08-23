// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.github.avrilfanomar.picatplugin.language.references.PicatNameIdentifierOwner;

public interface PicatAtom extends PicatNameIdentifierOwner {

  @Nullable
  PicatDollarEscapedAtom getDollarEscapedAtom();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getSingleQuotedAtom();

}
