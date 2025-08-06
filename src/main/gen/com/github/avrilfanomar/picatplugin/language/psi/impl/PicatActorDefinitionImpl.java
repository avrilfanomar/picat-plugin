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

public class PicatActorDefinitionImpl extends ASTWrapperPsiElement implements PicatActorDefinition {

  public PicatActorDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitActorDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PicatActionRule> getActionRuleList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatActionRule.class);
  }

  @Override
  @NotNull
  public List<PicatNonbacktrackablePredicateRule> getNonbacktrackablePredicateRuleList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatNonbacktrackablePredicateRule.class);
  }

}
