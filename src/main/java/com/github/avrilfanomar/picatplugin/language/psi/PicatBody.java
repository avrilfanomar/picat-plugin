// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatBody extends PsiElement {

  @NotNull
  List<PicatGoal> getGoalList();

  @NotNull
  List<PicatGoalSeparator> getGoalSeparatorList();

}
