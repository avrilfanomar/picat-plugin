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

public class PicatGoalImpl extends ASTWrapperPsiElement implements PicatGoal {

  public PicatGoalImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitGoal(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatAssignment getAssignment() {
    return findChildByClass(PicatAssignment.class);
  }

  @Override
  @Nullable
  public PicatBreakStmt getBreakStmt() {
    return findChildByClass(PicatBreakStmt.class);
  }

  @Override
  @Nullable
  public PicatCaseExpression getCaseExpression() {
    return findChildByClass(PicatCaseExpression.class);
  }

  @Override
  @Nullable
  public PicatComparison getComparison() {
    return findChildByClass(PicatComparison.class);
  }

  @Override
  @Nullable
  public PicatContinueStmt getContinueStmt() {
    return findChildByClass(PicatContinueStmt.class);
  }

  @Override
  @Nullable
  public PicatCutGoal getCutGoal() {
    return findChildByClass(PicatCutGoal.class);
  }

  @Override
  @Nullable
  public PicatExpression getExpression() {
    return findChildByClass(PicatExpression.class);
  }

  @Override
  @Nullable
  public PicatFailGoal getFailGoal() {
    return findChildByClass(PicatFailGoal.class);
  }

  @Override
  @Nullable
  public PicatFalseGoal getFalseGoal() {
    return findChildByClass(PicatFalseGoal.class);
  }

  @Override
  @Nullable
  public PicatForeachLoop getForeachLoop() {
    return findChildByClass(PicatForeachLoop.class);
  }

  @Override
  @Nullable
  public PicatIfThenElse getIfThenElse() {
    return findChildByClass(PicatIfThenElse.class);
  }

  @Override
  @Nullable
  public PicatListComprehensionGoal getListComprehensionGoal() {
    return findChildByClass(PicatListComprehensionGoal.class);
  }

  @Override
  @Nullable
  public PicatLoopWhileStatement getLoopWhileStatement() {
    return findChildByClass(PicatLoopWhileStatement.class);
  }

  @Override
  @Nullable
  public PicatNegation getNegation() {
    return findChildByClass(PicatNegation.class);
  }

  @Override
  @Nullable
  public PicatPassGoal getPassGoal() {
    return findChildByClass(PicatPassGoal.class);
  }

  @Override
  @Nullable
  public PicatReturnStmt getReturnStmt() {
    return findChildByClass(PicatReturnStmt.class);
  }

  @Override
  @Nullable
  public PicatThrowStmt getThrowStmt() {
    return findChildByClass(PicatThrowStmt.class);
  }

  @Override
  @Nullable
  public PicatTrueGoal getTrueGoal() {
    return findChildByClass(PicatTrueGoal.class);
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
  public PicatUnification getUnification() {
    return findChildByClass(PicatUnification.class);
  }

  @Override
  @Nullable
  public PicatWhileLoop getWhileLoop() {
    return findChildByClass(PicatWhileLoop.class);
  }

}
