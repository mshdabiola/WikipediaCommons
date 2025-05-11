package com.mshdabiola.designsystem.drawable

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import wikipediacommons.modules.designsystem.generated.resources.Res
import wikipediacommons.modules.designsystem.generated.resources.logo

val defaultAppIcon
    @Composable
    get() = painterResource(Res.drawable.logo)

// imageResource(Res.drawable.icon)
