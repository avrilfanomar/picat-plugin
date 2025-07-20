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

public class PicatFunctionRuleImpl extends ASTWrapperPsiElement implements PicatFunctionRule {

  public PicatFunctionRuleImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitFunctionRule(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PicatArgument getArgument() {
    return findNotNullChildByClass(PicatArgument.class);
  }

  @Override
  @Nullable
  public PicatBody getBody() {
    return findChildByClass(PicatBody.class);
  }

  @Override
  @Nullable
  public PicatCondition getCondition() {
    return findChildByClass(PicatCondition.class);
  }

  @Override
  @NotNull
  public PicatRuleHead getRuleHead() {
    return findNotNullChildByClass(PicatRuleHead.class);
  }

}
