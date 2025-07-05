// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatBaseExpression extends PsiElement {

  @Nullable
  PicatAsPatternExpression getAsPatternExpression();

  @Nullable
  PicatAtom getAtom();

  @Nullable
  PicatDollarTermConstructor getDollarTermConstructor();

  @Nullable
  PicatLambdaExpression getLambdaExpression();

  @Nullable
  PicatListComprehensionExpression getListComprehensionExpression();

  @Nullable
  PicatListExpression getListExpression();

  @Nullable
  PicatMap getMap();

  @Nullable
  PicatNumber getNumber();

  @Nullable
  PicatParenthesizedExpression getParenthesizedExpression();

  @Nullable
  PicatSimpleNumberRange getSimpleNumberRange();

  @Nullable
  PicatTermConstructorExpression getTermConstructorExpression();

  @Nullable
  PicatTuple getTuple();

  @Nullable
  PsiElement getString();

}
