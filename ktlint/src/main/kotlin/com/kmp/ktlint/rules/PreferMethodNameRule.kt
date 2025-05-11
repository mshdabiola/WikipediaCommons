package com.kmp.ktlint.rules

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtCallExpression

class PreferMethodNameRule : Rule(
    RuleId("wikipediacommons:prefer-method-name"),
    about =
        Rule.About(
            maintainer = "Your Name",
            repositoryUrl = "https://github.com/mshdabiola/wikipediacommons",
            issueTrackerUrl = "https://github.com/mshdabiola/wikipediacommons",
        ),
) {
    // Define the mapping of deprecated method names to preferred method names
    private val methodNameReplacements =
        mapOf(
            "MaterialTheme" to "WcsTheme",
            "Button" to "WcsButton",
            "OutlinedButton" to "WcsOutlinedButton",
            "TextButton" to "WcsTextButton",
            "FilterChip" to "WcsFilterChip",
            "ElevatedFilterChip" to "WcsFilterChip",
            "NavigationBar" to "WcsNavigationBar",
            "NavigationBarItem" to "WcsNavigationBarItem",
            "NavigationRail" to "WcsNavigationRail",
            "NavigationRailItem" to "WcsNavigationRailItem",
            "TabRow" to "WcsTabRow",
            "Tab" to "WcsTab",
            "IconToggleButton" to "WcsIconToggleButton",
            "FilledIconToggleButton" to "WcsIconToggleButton",
            "FilledTonalIconToggleButton" to "WcsIconToggleButton",
            "OutlinedIconToggleButton" to "WcsIconToggleButton",
            "CenterAlignedTopAppBar" to "WcsTopAppBar",
            "SmallTopAppBar" to "WcsTopAppBar",
            "MediumTopAppBar" to "WcsTopAppBar",
            "LargeTopAppBar" to "WcsTopAppBar",
        )

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeFixed: Boolean) -> Unit,
    ) {
        // Check if the current node is a method call expression
        if (node.psi is KtCallExpression) {
            val callExpression = node.psi as KtCallExpression
            val methodName = callExpression.calleeExpression?.text

            // Check if the method name is in our mapping
            if (methodName != null && methodNameReplacements.containsKey(methodName)) {
                val preferredName = methodNameReplacements[methodName]
                val errorMessage =
                    "Using '$methodName' instead of '$preferredName'. Consider using '$preferredName'."

                // Report the violation
                emit(
                    node.startOffset,
                    errorMessage,
                    false,
                )
            }
        }
    }
}
