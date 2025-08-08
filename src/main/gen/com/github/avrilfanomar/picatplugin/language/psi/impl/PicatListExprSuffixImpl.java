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

public class PicatListExprSuffixImpl extends ASTWrapperPsiElement implements PicatListExprSuffix {

  public PicatListExprSuffixImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitListExprSuffix(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatArgument getArgument() {
    return findChildByClass(PicatArgument.class);
  }

  @Override
  @Nullable
  public PicatIterator getIterator() {
    return findChildByClass(PicatIterator.class);
  }

  @Override
  @Nullable
  public PicatListComprehensionTail getListComprehensionTail() {
    return findChildByClass(PicatListComprehensionTail.class);
  }

  @Override
  @Nullable
  public PicatListItemsTail getListItemsTail() {
    return findChildByClass(PicatListItemsTail.class);
  }

}
