// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatEnclosedGoal extends PsiElement {

  @Nullable
  PicatExpressionWithRelations getExpressionWithRelations();

  @Nullable
  PicatForeachLoop getForeachLoop();

  @Nullable
  PicatIfThenElse getIfThenElse();

  @Nullable
  PicatLoopWhile getLoopWhile();

  @Nullable
  PicatTryCatch getTryCatch();

  @Nullable
  PicatTypeAnnotation getTypeAnnotation();

  @Nullable
  PicatWhileLoop getWhileLoop();

}
