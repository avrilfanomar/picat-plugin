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

public class PicatPostfixExpressionImpl extends ASTWrapperPsiElement implements PicatPostfixExpression {

  public PicatPostfixExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitPostfixExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatFieldAccessSuffix getFieldAccessSuffix() {
    return findChildByClass(PicatFieldAccessSuffix.class);
  }

  @Override
  @Nullable
  public PicatFunctionCallSuffix getFunctionCallSuffix() {
    return findChildByClass(PicatFunctionCallSuffix.class);
  }

  @Override
  @Nullable
  public PicatIndexAccessSuffix getIndexAccessSuffix() {
    return findChildByClass(PicatIndexAccessSuffix.class);
  }

}
