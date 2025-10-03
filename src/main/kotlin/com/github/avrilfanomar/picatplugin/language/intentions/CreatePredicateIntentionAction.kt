package com.github.avrilfanomar.picatplugin.language.intentions

import com.github.avrilfanomar.picatplugin.language.psi.PicatAtom
import com.github.avrilfanomar.picatplugin.language.psi.PicatAtomOrCallNoLambda
import com.github.avrilfanomar.picatplugin.language.psi.PicatPsiUtil
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException

/**
 * Quick-fix intention to create a missing predicate/function for unresolved references.
 */
class CreatePredicateIntentionAction(
    private val predicateName: String,
    private val arity: Int
) : IntentionAction {

    override fun getText(): String = "Create predicate '$predicateName/$arity'"

    override fun getFamilyName(): String = "Create predicate"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return file != null && predicateName.isNotBlank()
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (file == null || editor == null) return

        try {
            // Generate predicate template based on arity
            val params = if (arity > 0) {
                (1..arity).joinToString(", ") { "X$it" }
            } else {
                ""
            }
            
            val predicateTemplate = if (arity > 0) {
                "\n\n% TODO: Implement predicate\n$predicateName($params) =>\n    true."
            } else {
                "\n\n% TODO: Implement predicate\n$predicateName =>\n    true."
            }

            // Insert at the end of file
            val document = editor.document
            val endOffset = document.textLength
            document.insertString(endOffset, predicateTemplate)
            
            // Position cursor at the implementation line
            val newOffset = endOffset + predicateTemplate.indexOf("true")
            editor.caretModel.moveToOffset(newOffset)
        } catch (e: IncorrectOperationException) {
            // Log the error for debugging purposes
            com.intellij.openapi.diagnostic.Logger.getInstance(CreatePredicateIntentionAction::class.java)
                .warn("Failed to create predicate template", e)
        }
    }

    override fun startInWriteAction(): Boolean = true

    /**
     * Factory methods for creating predicate intention actions.
     */
    companion object {
        /**
         * Creates an intention action for the given element if it represents an unresolved reference.
         */
        fun createIfApplicable(element: PsiElement): CreatePredicateIntentionAction? {
            val atom = when (element) {
                is PicatAtom -> element
                is PicatAtomOrCallNoLambda -> PsiTreeUtil.findChildOfType(element, PicatAtom::class.java)
                else -> null
            }
            
            val name = atom?.let { PicatPsiUtil.getName(it) }
            
            return if (name != null) {
                // Default arity to 0 for simplicity - can be enhanced later
                CreatePredicateIntentionAction(name, 0)
            } else {
                null
            }
        }
    }
}
