// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatDotBaseExpression extends PsiElement {

  @Nullable
  PicatArrayExpression getArrayExpression();

  @Nullable
  PicatAsPattern getAsPattern();

  @Nullable
  PicatAtomOrCall getAtomOrCall();

  @Nullable
  PicatListExpression getListExpression();

  @Nullable
  PicatParenthesizedGoal getParenthesizedGoal();

  @Nullable
  PicatVariableIndex getVariableIndex();

}
