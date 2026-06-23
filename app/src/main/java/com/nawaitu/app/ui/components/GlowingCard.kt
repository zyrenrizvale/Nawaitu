package com.nawaitu.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nawaitu.app.ui.theme.NeonGreen
import com.nawaitu.app.ui.theme.SurfaceBorder
import com.nawaitu.app.ui.theme.SurfaceCard

@Composable
fun GlowingCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonGreen,
    cornerRadius: Dp = 16.dp,
    isGlowing: Boolean = false,
    backgroundColor: Color = SurfaceCard,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .then(
                if (isGlowing) {
                    Modifier.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(cornerRadius),
                        ambientColor = glowColor.copy(alpha = 0.25f),
                        spotColor = glowColor.copy(alpha = 0.25f)
                    )
                } else Modifier
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = if (isGlowing) 1.dp else 0.5.dp,
                color = if (isGlowing) glowColor.copy(alpha = 0.6f) else SurfaceBorder,
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}
