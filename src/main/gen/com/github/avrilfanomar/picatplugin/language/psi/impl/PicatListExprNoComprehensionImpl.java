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

public class PicatListExprNoComprehensionImpl extends ASTWrapperPsiElement implements PicatListExprNoComprehension {

  public PicatListExprNoComprehensionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitListExprNoComprehension(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PicatTerm> getTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PicatTerm.class);
  }

  @Override
  @Nullable
  public PicatTermListTail getTermListTail() {
    return findChildByClass(PicatTermListTail.class);
  }

}
