// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatGoal extends PsiElement {

  @Nullable
  PicatAssignment getAssignment();

  @Nullable
  PicatBreakStmt getBreakStmt();

  @Nullable
  PicatCaseExpression getCaseExpression();

  @Nullable
  PicatComparison getComparison();

  @Nullable
  PicatContinueStmt getContinueStmt();

  @Nullable
  PicatCutGoal getCutGoal();

  @Nullable
  PicatExpression getExpression();

  @Nullable
  PicatFailGoal getFailGoal();

  @Nullable
  PicatFalseGoal getFalseGoal();

  @Nullable
  PicatForeachLoop getForeachLoop();

  @Nullable
  PicatIfThenElse getIfThenElse();

  @Nullable
  PicatListComprehensionGoal getListComprehensionGoal();

  @Nullable
  PicatLoopWhileStatement getLoopWhileStatement();

  @Nullable
  PicatNegation getNegation();

  @Nullable
  PicatPassGoal getPassGoal();

  @Nullable
  PicatProcedureCall getProcedureCall();

  @Nullable
  PicatReturnStmt getReturnStmt();

  @Nullable
  PicatThrowStmt getThrowStmt();

  @Nullable
  PicatTrueGoal getTrueGoal();

  @Nullable
  PicatTryCatch getTryCatch();

  @Nullable
  PicatTypeAnnotation getTypeAnnotation();

  @Nullable
  PicatUnification getUnification();

  @Nullable
  PicatWhileLoop getWhileLoop();

}
