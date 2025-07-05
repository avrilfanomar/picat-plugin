// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatConcatenationExpression extends PsiElement {

  @NotNull
  List<PicatConcatenationExpressionRest> getConcatenationExpressionRestList();

  @NotNull
  PicatShiftExpression getShiftExpression();

}
