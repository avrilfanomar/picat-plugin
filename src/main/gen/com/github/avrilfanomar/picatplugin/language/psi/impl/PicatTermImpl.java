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

public class PicatTermImpl extends ASTWrapperPsiElement implements PicatTerm {

  public PicatTermImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitTerm(this);
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
  public PicatAtomOrCallNoLambda getAtomOrCallNoLambda() {
    return findChildByClass(PicatAtomOrCallNoLambda.class);
  }

  @Override
  @Nullable
  public PicatFunctionCallNoDot getFunctionCallNoDot() {
    return findChildByClass(PicatFunctionCallNoDot.class);
  }

  @Override
  @Nullable
  public PicatListExprNoComprehension getListExprNoComprehension() {
    return findChildByClass(PicatListExprNoComprehension.class);
  }

  @Override
  @Nullable
  public PicatTerm getTerm() {
    return findChildByClass(PicatTerm.class);
  }

  @Override
  @Nullable
  public PicatTermConstructor getTermConstructor() {
    return findChildByClass(PicatTermConstructor.class);
  }

  @Override
  @Nullable
  public PicatVariableAsPattern getVariableAsPattern() {
    return findChildByClass(PicatVariableAsPattern.class);
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
