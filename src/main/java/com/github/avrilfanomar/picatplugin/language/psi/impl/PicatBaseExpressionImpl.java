// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.avrilfanomar.picatplugin.language.psi.PicatTokenTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.avrilfanomar.picatplugin.language.psi.*;

public class PicatBaseExpressionImpl extends ASTWrapperPsiElement implements PicatBaseExpression {

  public PicatBaseExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitBaseExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatAsPatternExpression getAsPatternExpression() {
    return findChildByClass(PicatAsPatternExpression.class);
  }

  @Override
  @Nullable
  public PicatAtom getAtom() {
    return findChildByClass(PicatAtom.class);
  }

  @Override
  @Nullable
  public PicatDollarTermConstructor getDollarTermConstructor() {
    return findChildByClass(PicatDollarTermConstructor.class);
  }

  @Override
  @Nullable
  public PicatLambdaExpression getLambdaExpression() {
    return findChildByClass(PicatLambdaExpression.class);
  }

  @Override
  @Nullable
  public PicatListComprehensionExpression getListComprehensionExpression() {
    return findChildByClass(PicatListComprehensionExpression.class);
  }

  @Override
  @Nullable
  public PicatListExpression getListExpression() {
    return findChildByClass(PicatListExpression.class);
  }

  @Override
  @Nullable
  public PicatMap getMap() {
    return findChildByClass(PicatMap.class);
  }

  @Override
  @Nullable
  public PicatNumber getNumber() {
    return findChildByClass(PicatNumber.class);
  }

  @Override
  @Nullable
  public PicatParenthesizedExpression getParenthesizedExpression() {
    return findChildByClass(PicatParenthesizedExpression.class);
  }

  @Override
  @Nullable
  public PicatSimpleNumberRange getSimpleNumberRange() {
    return findChildByClass(PicatSimpleNumberRange.class);
  }

  @Override
  @Nullable
  public PicatStructure getStructure() {
    return findChildByClass(PicatStructure.class);
  }

  @Override
  @Nullable
  public PicatTermConstructorExpression getTermConstructorExpression() {
    return findChildByClass(PicatTermConstructorExpression.class);
  }

  @Override
  @Nullable
  public PicatTuple getTuple() {
    return findChildByClass(PicatTuple.class);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

}
