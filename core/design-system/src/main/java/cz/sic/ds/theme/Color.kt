package cz.sic.ds.theme

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

        object Badge {
            val contentColorLocal: androidx.compose.ui.graphics.Color =
                androidx.compose.ui.graphics.Color.Green
            val contentColorRemote: androidx.compose.ui.graphics.Color =
                androidx.compose.ui.graphics.Color.Cyan
        }

        object Transparent {
            val t50: androidx.compose.ui.graphics.Color = Color(0x80000000)
        }

        object Grey {
            val g30: androidx.compose.ui.graphics.Color = Color(0xFFDADEE0)
            val g80: androidx.compose.ui.graphics.Color = Color(0xFF374045)
        }
    }
}