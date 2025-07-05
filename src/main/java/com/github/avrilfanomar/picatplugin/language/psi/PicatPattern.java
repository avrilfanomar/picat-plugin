// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatPattern extends PsiElement {

  @Nullable
  PicatAsPattern getAsPattern();

  @Nullable
  PicatAtom getAtom();

  @Nullable
  PicatListPattern getListPattern();

  @Nullable
  PicatNumber getNumber();

  @Nullable
  PicatStructurePattern getStructurePattern();

  @Nullable
  PicatTuplePattern getTuplePattern();

}
