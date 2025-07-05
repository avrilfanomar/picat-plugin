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

public class PicatAtomExpressionImpl extends ASTWrapperPsiElement implements PicatAtomExpression {

  public PicatAtomExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitAtomExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PicatBaseExpression getBaseExpression() {
    return findNotNullChildByClass(PicatBaseExpression.class);
  }

  @Override
  @NotNull
  public List<PicatPostfixExpression> getPostfixExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatPostfixExpression.class);
  }

}
