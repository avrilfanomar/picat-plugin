// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatBaseExpr extends PsiElement {

  @Nullable
  PicatArrayExpr getArrayExpr();

  @Nullable
  PicatAsPattern getAsPattern();

  @Nullable
  PicatAtomNoArgs getAtomNoArgs();

  @Nullable
  PicatFunctionCall getFunctionCall();

  @Nullable
  PicatLambdaTerm getLambdaTerm();

  @Nullable
  PicatListExpr getListExpr();

  @Nullable
  PicatParenthesizedGoal getParenthesizedGoal();

  @Nullable
  PicatTermConstructor getTermConstructor();

  @Nullable
  PicatVariableIndex getVariableIndex();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getInteger();

  @Nullable
  PsiElement getString();

  @Nullable
  PsiElement getVariable();

}
