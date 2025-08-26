package com.github.avrilfanomar.picatplugin.language

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PicatFoldingBuilderTest : BasePlatformTestCase() {

    private fun getFilePath(name: String): String {
        return javaClass.getResource("/folding/$name")!!.path
    }

    fun testFoldLongRuleBody() {
        myFixture.testFolding(getFilePath("long_rule_body.pi"))
    }

    fun testFoldFunctionRuleBodyWithArrow() {
        myFixture.testFolding(getFilePath("function_rule_body_arrow.pi"))
    }

    fun testFoldNonbacktrackableRuleBodyQArrow() {
        myFixture.testFolding(getFilePath("nonbacktrack_rule_body_qarrow.pi"))
    }

    fun testFoldMultiClauseGroup() {
        myFixture.testFolding(getFilePath("multi_clause_group.pi"))
    }

    fun testFoldMultiClauseGroupTriple() {
        myFixture.testFolding(getFilePath("multi_clause_group_triple.pi"))
    }
}
