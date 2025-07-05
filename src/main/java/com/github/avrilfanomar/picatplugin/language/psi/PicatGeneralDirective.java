// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatGeneralDirective extends PsiElement {

  @Nullable
  PicatCompilationDirective getCompilationDirective();

  @Nullable
  PicatExportStatement getExportStatement();

  @Nullable
  PicatImportStatement getImportStatement();

  @Nullable
  PicatIncludeStatement getIncludeStatement();

  @Nullable
  PicatUsingStatement getUsingStatement();

}
