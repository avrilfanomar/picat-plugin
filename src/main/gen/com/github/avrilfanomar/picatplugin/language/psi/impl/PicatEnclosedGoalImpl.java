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

public class PicatEnclosedGoalImpl extends ASTWrapperPsiElement implements PicatEnclosedGoal {

  public PicatEnclosedGoalImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitEnclosedGoal(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatEquivalence getEquivalence() {
    return findChildByClass(PicatEquivalence.class);
  }

  @Override
  @Nullable
  public PicatExpressionWithRelations getExpressionWithRelations() {
    return findChildByClass(PicatExpressionWithRelations.class);
  }

  @Override
  @Nullable
  public PicatForeachLoop getForeachLoop() {
    return findChildByClass(PicatForeachLoop.class);
  }

  @Override
  @Nullable
  public PicatGoal getGoal() {
    return findChildByClass(PicatGoal.class);
  }

  @Override
  @Nullable
  public PicatIfThenElse getIfThenElse() {
    return findChildByClass(PicatIfThenElse.class);
  }

  @Override
  @Nullable
  public PicatLoopWhile getLoopWhile() {
    return findChildByClass(PicatLoopWhile.class);
  }

  @Override
  @Nullable
  public PicatThrowStatement getThrowStatement() {
    return findChildByClass(PicatThrowStatement.class);
  }

  @Override
  @Nullable
  public PicatTryCatch getTryCatch() {
    return findChildByClass(PicatTryCatch.class);
  }

  @Override
  @Nullable
  public PicatTypeAnnotation getTypeAnnotation() {
    return findChildByClass(PicatTypeAnnotation.class);
  }

  @Override
  @Nullable
  public PicatWhileLoop getWhileLoop() {
    return findChildByClass(PicatWhileLoop.class);
  }

}
