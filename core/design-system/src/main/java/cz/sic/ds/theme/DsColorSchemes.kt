package cz.sic.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import cz.sic.ds.theme.DsColorsLight

internal val DsLightColorScheme = lightColorScheme(
    primary = DsColorsLight.Green.g60,
    onPrimary = DsColorsLight.Gray.g00,
    surface = DsColorsLight.Gray.g00,
    onSurface = DsColorsLight.Gray.g90,
    onSurfaceVariant = DsColorsLight.Gray.g70,
    surfaceVariant = DsColorsLight.Gray.g10,
    background = DsColorsLight.Gray.g00,
    onBackground = DsColorsLight.Gray.g90,
    secondaryContainer = DsColorsLight.Green.g30,
    tertiaryContainer = DsColorsLight.Green.g40,

    error = DsColorsLight.Red.r60,
    onError = DsColorsLight.Gray.g00,

    secondary = DsColorsLight.Green.g20,
    tertiary = DsColorsLight.Green.g40
)

internal val DsDarkColorScheme = darkColorScheme(
    primary = DsColorsDark.Green.g60,
    onPrimary = DsColorsDark.Gray.g00,
    surface = DsColorsDark.Gray.g00,
    onSurface = DsColorsDark.Gray.g90,
    onSurfaceVariant = DsColorsDark.Gray.g70,
    surfaceVariant = DsColorsDark.Gray.g10,
    background = DsColorsDark.Gray.g00,
    onBackground = DsColorsDark.Gray.g90,
    secondaryContainer = DsColorsLight.Green.g80,

    error = DsColorsDark.Red.r60,
    onError = DsColorsDark.Gray.g00,

    secondary = DsColorsDark.Green.g60,
    tertiary = DsColorsDark.Green.g80
)