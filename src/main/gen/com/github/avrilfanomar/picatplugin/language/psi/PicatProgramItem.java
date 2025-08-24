// This is a generated file. Not intended for manual editing.
package com.github.avrilfanomar.picatplugin.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PicatProgramItem extends PsiElement {

  @Nullable
  PicatActorDefinition getActorDefinition();

  @Nullable
  PicatFunctionDefinition getFunctionDefinition();

  @Nullable
  PicatImportDeclaration getImportDeclaration();

  @Nullable
  PicatIncludeDeclaration getIncludeDeclaration();

  @Nullable
  PicatModuleDeclaration getModuleDeclaration();

  @Nullable
  PicatPredicateDefinition getPredicateDefinition();

  @Nullable
  PsiElement getComment();

  @Nullable
  PsiElement getMultilineComment();

}
