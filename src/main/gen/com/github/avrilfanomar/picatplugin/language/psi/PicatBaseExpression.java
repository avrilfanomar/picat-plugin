// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatBaseExpression extends PsiElement {

  @Nullable
  PicatArrayExpression getArrayExpression();

  @Nullable
  PicatAsPattern getAsPattern();

  @Nullable
  PicatAtomWithoutArgs getAtomWithoutArgs();

  @Nullable
  PicatFunctionCall getFunctionCall();

  @Nullable
  PicatLambdaTerm getLambdaTerm();

  @Nullable
  PicatListExpression getListExpression();

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
  PsiElement getVariable();

}
