// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatBiconditionalExpression extends PsiElement {

  @NotNull
  List<PicatBiconditionalExpressionRest> getBiconditionalExpressionRestList();

  @NotNull
  PicatLogicalOrExpression getLogicalOrExpression();

}
