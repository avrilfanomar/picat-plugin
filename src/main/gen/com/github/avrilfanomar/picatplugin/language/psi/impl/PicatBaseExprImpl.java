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

public class PicatBaseExprImpl extends ASTWrapperPsiElement implements PicatBaseExpr {

  public PicatBaseExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitBaseExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatArrayExpr getArrayExpr() {
    return findChildByClass(PicatArrayExpr.class);
  }

  @Override
  @Nullable
  public PicatAsPattern getAsPattern() {
    return findChildByClass(PicatAsPattern.class);
  }

  @Override
  @Nullable
  public PicatAtomNoArgs getAtomNoArgs() {
    return findChildByClass(PicatAtomNoArgs.class);
  }

  @Override
  @Nullable
  public PicatFunctionCall getFunctionCall() {
    return findChildByClass(PicatFunctionCall.class);
  }

  @Override
  @Nullable
  public PicatLambdaTerm getLambdaTerm() {
    return findChildByClass(PicatLambdaTerm.class);
  }

  @Override
  @Nullable
  public PicatListExpr getListExpr() {
    return findChildByClass(PicatListExpr.class);
  }

  @Override
  @Nullable
  public PicatParenthesizedGoal getParenthesizedGoal() {
    return findChildByClass(PicatParenthesizedGoal.class);
  }

  @Override
  @Nullable
  public PicatTermConstructor getTermConstructor() {
    return findChildByClass(PicatTermConstructor.class);
  }

  @Override
  @Nullable
  public PicatVariableIndex getVariableIndex() {
    return findChildByClass(PicatVariableIndex.class);
  }

  @Override
  @Nullable
  public PsiElement getFloat() {
    return findChildByType(FLOAT);
  }

  @Override
  @Nullable
  public PsiElement getInteger() {
    return findChildByType(INTEGER);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

  @Override
  @Nullable
  public PsiElement getVariable() {
    return findChildByType(VARIABLE);
  }

}
