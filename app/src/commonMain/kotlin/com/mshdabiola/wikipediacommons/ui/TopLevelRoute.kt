package com.mshdabiola.wikipediacommons.ui

import androidx.compose.ui.graphics.vector.ImageVector
import com.mshdabiola.designsystem.icon.WcsIcons
import com.mshdabiola.main.navigation.Main
import com.mshdabiola.setting.navigation.Setting

data class TopLevelRoute<T : Any>(val route: T, val icon: ImageVector)

val TOP_LEVEL_ROUTES =
    listOf(
        TopLevelRoute(route = Main, icon = WcsIcons.Domain),
        TopLevelRoute(route = Unit, icon = WcsIcons.Person),
        TopLevelRoute(route = Unit, icon = WcsIcons.Stairs),
    )

val SETTING_LEVEL_ROUTES =
    listOf(
        TopLevelRoute(route = Setting, icon = WcsIcons.Settings),
        TopLevelRoute(route = Setting, icon = WcsIcons.Info),
    )
