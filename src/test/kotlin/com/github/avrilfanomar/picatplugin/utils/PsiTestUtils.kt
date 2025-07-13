package com.github.avrilfanomar.picatplugin.utils

import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.psi.util.PsiTreeUtil
import org.junit.jupiter.api.Assertions

/**
 * Utility object for common PSI testing operations
 */
object PsiTestUtils {
    
    /**
     * Checks for PSI parsing errors in the given file and asserts that no errors exist.
     * Also provides debug logging of errors and PSI tree structure.
     * 
     * @param file The PSI file to check for errors
     * @param testContext A descriptive context for the test (e.g., "conjunctive goals", "simple function call")
     */
    fun assertNoPsiErrors(file: PsiFile, testContext: String) {
        // Check for PSI parsing errors
        val errorElements = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
        println("[DEBUG_LOG] Found ${errorElements.size} PSI parsing errors in $testContext")
        
        if (errorElements.isNotEmpty()) {
            errorElements.forEachIndexed { index, error ->
                println(
                    "[DEBUG_LOG] Error $index: '${error.errorDescription}' at text: '${error.text}' " +
                            "parent: '${error.parent?.javaClass?.simpleName}'"
                )
            }
        }

        // Print PSI tree for debugging
        println("[DEBUG_LOG] $testContext PSI Tree:\n" + DebugUtil.psiToString(file, true))

        Assertions.assertEquals(
            0,
            errorElements.size,
            "Expected zero PSI parsing errors in $testContext, but found ${errorElements.size}. " +
                    "First error: ${errorElements.firstOrNull()?.errorDescription}"
        )
    }
    
    /**
     * Checks for PSI parsing errors in the given file and asserts that no errors exist.
     * Also provides detailed debug logging with additional context information.
     * 
     * @param file The PSI file to check for errors
     * @param testContext A descriptive context for the test
     */
    fun assertNoPsiErrorsWithDetailedLogging(file: PsiFile, testContext: String) {
        // Debug output of the entire PSI tree
        println("[DEBUG_LOG] $testContext PSI Tree:\n" + DebugUtil.psiToString(file, true))

        // Check for PSI parsing errors
        val errorElements = PsiTreeUtil.findChildrenOfType(file, PsiErrorElement::class.java)
        println("[DEBUG_LOG] Found ${errorElements.size} PSI parsing errors")
        
        if (errorElements.isNotEmpty()) {
            errorElements.forEachIndexed { index, error ->
                println(
                    "[DEBUG_LOG] Error $index: '${error.errorDescription}' at text: '${error.text}' parent: '${error.parent?.javaClass?.simpleName}' context: '${
                        error.parent?.text?.take(100)
                    }'"
                )
            }
            // Print the first few errors in detail for debugging
            errorElements.take(3).forEach { error ->
                println("[DEBUG_LOG] Detailed error: ${error.errorDescription}")
                println("[DEBUG_LOG] Error text: '${error.text}'")
                println("[DEBUG_LOG] Error parent: ${error.parent}")
                println("[DEBUG_LOG] Error context: '${error.parent?.text?.take(200)}'")
                println("[DEBUG_LOG] ---")
            }
        }
        
        Assertions.assertEquals(
            0,
            errorElements.size,
            "Expected zero PSI parsing errors, but found ${errorElements.size}. " +
                    "First error: ${errorElements.firstOrNull()?.errorDescription}"
        )
    }
}
