package cz.sic.ds.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object Ds {
    object Color {
        object Theme {
            val itemBackground: androidx.compose.ui.graphics.Color =
                Grey.g30
            val itemBorder: androidx.compose.ui.graphics.Color =
                Grey.g80
        }

        object LocadingContent {
            val background: androidx.compose.ui.graphics.Color
                @Composable get() = if (isSystemInDarkTheme()) Transparent.t50d else Transparent.t50l
        }
        object Badge {
            val contentColorLocal: androidx.compose.ui.graphics.Color =
                androidx.compose.ui.graphics.Color.Green
            val contentColorRemote: androidx.compose.ui.graphics.Color =
                androidx.compose.ui.graphics.Color.Cyan

            val textColor: androidx.compose.ui.graphics.Color =
                DsColorsLight.Gray.g90
        }

        object Transparent {
            val t50l: androidx.compose.ui.graphics.Color = Color(0x80000000)
            val t50d: androidx.compose.ui.graphics.Color = Color(0x80FFFFFF)
        }

        object Grey {
            val g30: androidx.compose.ui.graphics.Color = Color(0xFFDADEE0)
            val g80: androidx.compose.ui.graphics.Color = Color(0xFF374045)
        }
    }
}