package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SoftGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Infinite slow-moving flow for gradient offsets to feel alive but calm
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val animOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "xOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF161E36), // slightly lighter midnight navy
                        DeepMidnight
                    ),
                    radius = 1200f + animOffset1 / 10f
                )
            ),
        content = content
    )
}

@Composable
fun BreathingOrb(
    modifier: Modifier = Modifier,
    scale: Float,          // Controlled from screen timer
    glowIntensity: Float,   // Pulsing glow factor
    phaseColor: Color       // Indigo, Mint or Pink based on inhale / hold / exhale
) {
    Box(
        modifier = modifier
            .size(240.dp)
            .semantics { contentDescription = "이완을 돕는 심호흡 구체" },
        contentAlignment = Alignment.Center
    ) {
        // Glowing Background blur circle
        Box(
            modifier = Modifier
                .size(180.dp)
                .blur(50.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            phaseColor.copy(alpha = 0.45f * glowIntensity),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(90.dp)
                )
        )

        // Concentric Canvas layers mimicking Apple Breathe styles
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val centerOffset = this.center
            val baseRadius = size.minDimension / 3.2f
            val animRadius = baseRadius * scale

            // Draw outer atmospheric ring
            drawCircle(
                color = phaseColor.copy(alpha = 0.08f),
                radius = animRadius * 1.35f,
                center = centerOffset
            )

            // Draw glassmorphic translucent layers
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        phaseColor.copy(alpha = 0.18f * glowIntensity),
                        phaseColor.copy(alpha = 0.03f)
                    ),
                    center = centerOffset,
                    radius = animRadius
                ),
                radius = animRadius,
                center = centerOffset
            )

            // Dynamic sharp accent ring
            drawCircle(
                color = phaseColor.copy(alpha = 0.35f),
                radius = animRadius,
                center = centerOffset,
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner core bulb
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        WarmIvory.copy(alpha = 0.8f),
                        phaseColor.copy(alpha = 0.4f)
                    )
                ),
                radius = animRadius * 0.45f,
                center = centerOffset
            )
        }
    }
}

@Composable
fun BreathingPhaseText(
    phaseText: String,
    comfortText: String? = null,
    phaseColor: Color
) {
    val finalComfortText = comfortText ?: BreathStrings.PHASE_COMFORT
    val animatedColor by animateColorAsState(
        targetValue = phaseColor,
        animationSpec = tween(1000),
        label = "color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = phaseText,
            color = animatedColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.testTag("breathing_phase_title")
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = finalComfortText,
            color = MutedSlate,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.testTag("breathing_phase_subtitle")
        )
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color = BorderMidnight,
    backgroundColor: Color = CardMidnight.copy(alpha = 0.85f),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape)
            .padding(24.dp),
        content = content
    )
}

@Composable
fun MoodChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) SoftPurple.copy(alpha = 0.15f) else CardMidnight,
        animationSpec = tween(250),
        label = "bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) SoftPurple else WarmIvory,
        animationSpec = tween(250),
        label = "text"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SoftPurple else BorderMidnight,
        animationSpec = tween(250),
        label = "border"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("mood_chip_$text"),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(if (isSelected) SoftPurple else MutedSlate)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun CalmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SoftPurple,
    contentColor: Color = DeepMidnight,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = CardMidnight,
            disabledContentColor = MutedSlate
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .height(56.dp)
            .minimumInteractiveComponentSize()
            .testTag("calm_button_${text.replace(" ", "_").lowercase()}"),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SessionDurationSelector(
    selectedDurationSeconds: Int,
    onDurationSelected: (Int) -> Unit
) {
    val durations = listOf(
        60 to BreathStrings.COMP_DURATION_1,
        180 to BreathStrings.COMP_DURATION_3,
        300 to BreathStrings.COMP_DURATION_5
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session_duration_selector"),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        durations.forEach { (seconds, label) ->
            val isSelected = selectedDurationSeconds == seconds
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) SoftPurple.copy(alpha = 0.15f) else CardMidnight,
                label = "bg"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) SoftPurple else BorderMidnight,
                label = "border"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) SoftPurple else WarmIvory,
                label = "text"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable { onDurationSelected(seconds) }
                    .testTag("duration_option_$seconds"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun QuietStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardMidnight)
            .border(1.dp, BorderMidnight, RoundedCornerShape(16.dp))
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BorderMidnight),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                color = MutedSlate,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = WarmIvory,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
