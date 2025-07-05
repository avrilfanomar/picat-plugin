// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatConditionalExpression extends PsiElement {

  @NotNull
  PicatBiconditionalExpression getBiconditionalExpression();

  @NotNull
  List<PicatExpression> getExpressionList();

}
