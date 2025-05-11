package com.mshdabiola.designsystem.drawable

import androidx.compose.runtime.Composable
import wikipediacommons.modules.designsystem.generated.resources.Res
import wikipediacommons.modules.designsystem.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

val defaultAppIcon
    @Composable
    get() = painterResource(Res.drawable.logo)

// imageResource(Res.drawable.icon)
