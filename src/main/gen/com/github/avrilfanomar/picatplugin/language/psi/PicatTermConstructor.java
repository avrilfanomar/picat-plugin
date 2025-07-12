// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatTermConstructor extends PsiElement {

  @NotNull
  List<PicatArgument> getArgumentList();

  @Nullable
  PicatGoal getGoal();

  @Nullable
  PsiElement getIdentifier();

}
