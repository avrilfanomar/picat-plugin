// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatTerm extends PsiElement {

  @Nullable
  PicatArrayExpr getArrayExpr();

  @Nullable
  PicatAtomOrCallNoLambda getAtomOrCallNoLambda();

  @Nullable
  PicatFunctionCallNoDot getFunctionCallNoDot();

  @Nullable
  PicatListExprNoComprehension getListExprNoComprehension();

  @Nullable
  PicatTerm getTerm();

  @Nullable
  PicatTermConstructor getTermConstructor();

  @Nullable
  PicatVariableAsPattern getVariableAsPattern();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getInteger();

  @Nullable
  PsiElement getString();

  @Nullable
  PsiElement getVariable();

}
