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

public class PicatGeneralDirectiveImpl extends ASTWrapperPsiElement implements PicatGeneralDirective {

  public PicatGeneralDirectiveImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitGeneralDirective(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatCompilationDirective getCompilationDirective() {
    return findChildByClass(PicatCompilationDirective.class);
  }

  @Override
  @Nullable
  public PicatExportStatement getExportStatement() {
    return findChildByClass(PicatExportStatement.class);
  }

  @Override
  @Nullable
  public PicatImportStatement getImportStatement() {
    return findChildByClass(PicatImportStatement.class);
  }

  @Override
  @Nullable
  public PicatIncludeStatement getIncludeStatement() {
    return findChildByClass(PicatIncludeStatement.class);
  }

  @Override
  @Nullable
  public PicatUsingStatement getUsingStatement() {
    return findChildByClass(PicatUsingStatement.class);
  }

}
