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

public class PicatProgramItemImpl extends ASTWrapperPsiElement implements PicatProgramItem {

  public PicatProgramItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitProgramItem(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatActorDefinition getActorDefinition() {
    return findChildByClass(PicatActorDefinition.class);
  }

  @Override
  @Nullable
  public PicatFunctionDefinition getFunctionDefinition() {
    return findChildByClass(PicatFunctionDefinition.class);
  }

  @Override
  @Nullable
  public PicatImportDeclaration getImportDeclaration() {
    return findChildByClass(PicatImportDeclaration.class);
  }

  @Override
  @Nullable
  public PicatIncludeDeclaration getIncludeDeclaration() {
    return findChildByClass(PicatIncludeDeclaration.class);
  }

  @Override
  @Nullable
  public PicatModuleDeclaration getModuleDeclaration() {
    return findChildByClass(PicatModuleDeclaration.class);
  }

  @Override
  @Nullable
  public PicatPredicateDefinition getPredicateDefinition() {
    return findChildByClass(PicatPredicateDefinition.class);
  }

  @Override
  @Nullable
  public PsiElement getComment() {
    return findChildByType(COMMENT);
  }

  @Override
  @Nullable
  public PsiElement getMultilineComment() {
    return findChildByType(MULTILINE_COMMENT);
  }

}
