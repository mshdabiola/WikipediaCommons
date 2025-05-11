package com.mshdabiola.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Licence: ImageVector
    get() {
        if (licence1 != null) {
            return licence1!!
        }
        licence1 =
            ImageVector.Builder(
                name = "License",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(480f, 520f)
                    quadToRelative(-50f, 0f, -85f, -35f)
                    reflectiveQuadToRelative(-35f, -85f)
                    reflectiveQuadToRelative(35f, -85f)
                    reflectiveQuadToRelative(85f, -35f)
                    reflectiveQuadToRelative(85f, 35f)
                    reflectiveQuadToRelative(35f, 85f)
                    reflectiveQuadToRelative(-35f, 85f)
                    reflectiveQuadToRelative(-85f, 35f)
                    moveTo(240f, 920f)
                    verticalLineToRelative(-309f)
                    quadToRelative(-38f, -42f, -59f, -96f)
                    reflectiveQuadToRelative(-21f, -115f)
                    quadToRelative(0f, -134f, 93f, -227f)
                    reflectiveQuadToRelative(227f, -93f)
                    reflectiveQuadToRelative(227f, 93f)
                    reflectiveQuadToRelative(93f, 227f)
                    quadToRelative(0f, 61f, -21f, 115f)
                    reflectiveQuadToRelative(-59f, 96f)
                    verticalLineToRelative(309f)
                    lineToRelative(-240f, -80f)
                    close()
                    moveToRelative(240f, -280f)
                    quadToRelative(100f, 0f, 170f, -70f)
                    reflectiveQuadToRelative(70f, -170f)
                    reflectiveQuadToRelative(-70f, -170f)
                    reflectiveQuadToRelative(-170f, -70f)
                    reflectiveQuadToRelative(-170f, 70f)
                    reflectiveQuadToRelative(-70f, 170f)
                    reflectiveQuadToRelative(70f, 170f)
                    reflectiveQuadToRelative(170f, 70f)
                    moveTo(320f, 801f)
                    lineToRelative(160f, -41f)
                    lineToRelative(160f, 41f)
                    verticalLineToRelative(-124f)
                    quadToRelative(-35f, 20f, -75.5f, 31.5f)
                    reflectiveQuadTo(480f, 720f)
                    reflectiveQuadToRelative(-84.5f, -11.5f)
                    reflectiveQuadTo(320f, 677f)
                    close()
                    moveToRelative(160f, -62f)
                }
            }.build()
        return licence1!!
    }

private var licence1: ImageVector? = null
