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

public class PicatHeadImpl extends ASTWrapperPsiElement implements PicatHead {

  public PicatHeadImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PicatVisitor visitor) {
    visitor.visitHead(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PicatVisitor) accept((PicatVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PicatAtomNoArgs getAtomNoArgs() {
    return findChildByClass(PicatAtomNoArgs.class);
  }

  @Override
  @Nullable
  public PicatHeadArgs getHeadArgs() {
    return findChildByClass(PicatHeadArgs.class);
  }

  @Override
  @Nullable
  public PicatQualifiedAtom getQualifiedAtom() {
    return findChildByClass(PicatQualifiedAtom.class);
  }

  @Override
  @Nullable
  public PicatStructure getStructure() {
    return findChildByClass(PicatStructure.class);
  }

}
