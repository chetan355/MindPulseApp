package com.chets.mindpulseapp.screens

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chets.mindpulseapp.R
import com.chets.mindpulseapp.data.moodList
import com.chets.mindpulseapp.ui.theme.DustyBlue
import com.chets.mindpulseapp.ui.theme.LightBackground
import com.chets.mindpulseapp.ui.theme.LightOnSurface
import com.chets.mindpulseapp.ui.theme.LightSurface
import com.chets.mindpulseapp.ui.theme.SageGreen
import com.chets.mindpulseapp.ui.theme.SoftLavender
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val headline: String,
    val body: String,
    val illustrationType: OnboardingIllustrationType
)

enum class OnboardingIllustrationType {
    JOURNALING,
    INSIGHTS,
    PRIVACY
}

val onboardingPages = listOf(
    OnboardingPageData(
        headline = "Your thoughts deserve a little space.",
        body = "Capture your thoughts, reflect on your day, and record how you feel — without pressure to write perfectly.",
        illustrationType = OnboardingIllustrationType.JOURNALING
    ),
    OnboardingPageData(
        headline = "Small reflections. Meaningful patterns.",
        body = "Notice how your mood changes over time and discover patterns in your journal entries, one day at a time.",
        illustrationType = OnboardingIllustrationType.INSIGHTS
    ),
    OnboardingPageData(
        headline = "Your thoughts. Your space.",
        body = "Reflect privately, at your own pace.",
        illustrationType = OnboardingIllustrationType.PRIVACY
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })

    fun completeOnboarding() {
        val prefs = context.getSharedPreferences("mindpulse_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("onboarding_completed", true).apply()
        onFinishOnboarding()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = LightBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.mindpulse_logo),
                        contentDescription = "MindPulse Logo",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "MindPulse",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightOnSurface
                    )
                }

                if (pagerState.currentPage < onboardingPages.size - 1) {
                    TextButton(onClick = { completeOnboarding() }) {
                        Text(
                            text = "Skip",
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = SageGreen
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                OnboardingPageContent(pageData = onboardingPages[pageIndex])
            }

            // Bottom Navigation Controls (Pager Dots & Action Button)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Organic Pill Page Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingPages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 8.dp,
                            animationSpec = tween(300),
                            label = "indicator_width"
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) SageGreen else SageGreen.copy(alpha = 0.25f),
                            animationSpec = tween(300),
                            label = "indicator_color"
                        )

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                // Action Button (Continue / Get started)
                val isLastPage = pagerState.currentPage == onboardingPages.size - 1
                Button(
                    onClick = {
                        if (isLastPage) {
                            completeOnboarding()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SageGreen,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = if (isLastPage) "Get started" else "Continue",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Illustration Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (pageData.illustrationType) {
                    OnboardingIllustrationType.JOURNALING -> WindowJournalingIllustration()
                    OnboardingIllustrationType.INSIGHTS -> MoodInsightsIllustration()
                    OnboardingIllustrationType.PRIVACY -> PrivacySpaceIllustration()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Headline & Body Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = pageData.headline,
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LightOnSurface,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = pageData.body,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = LightOnSurface.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

// Custom Illustration 1: Person Peaceful Journaling Beside a Window
@Composable
fun WindowJournalingIllustration() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Sunlight window background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Window frame background with warm sunlight gradient
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF3E0),
                        Color(0xFFE8F5E9)
                    )
                ),
                topLeft = Offset(w * 0.15f, h * 0.08f),
                size = Size(w * 0.7f, h * 0.84f),
                cornerRadius = CornerRadius(24f, 24f)
            )

            // Window Pane Lines
            val windowLineColor = SageGreen.copy(alpha = 0.25f)
            drawLine(
                color = windowLineColor,
                start = Offset(w * 0.5f, h * 0.08f),
                end = Offset(w * 0.5f, h * 0.92f),
                strokeWidth = 3f
            )
            drawLine(
                color = windowLineColor,
                start = Offset(w * 0.15f, h * 0.45f),
                end = Offset(w * 0.85f, h * 0.45f),
                strokeWidth = 3f
            )
        }

        // Journal & Mug Overlay
        Card(
            modifier = Modifier
                .width(180.dp)
                .height(130.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Today",
                        fontFamily = FontFamily.Serif,
                        fontSize = 11.sp,
                        color = DustyBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Journal Lines
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(SageGreen.copy(alpha = 0.35f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(SageGreen.copy(alpha = 0.25f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(SageGreen.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

// Custom Illustration 2: Abstract Mood Symbols and Mood-Over-Days Line Chart
@Composable
fun MoodInsightsIllustration() {
    val primaryColor = SageGreen
    val secondaryColor = DustyBlue

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Floating Mood Emojis Badges Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            moodList.take(4).forEach { item ->
                Surface(
                    shape = CircleShape,
                    color = item.tint.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, item.tint.copy(alpha = 0.4f)),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(item.emoji),
                            contentDescription = item.mood,
                            tint = item.tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Smooth Line Chart Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(horizontal = 16.dp)
        ) {
            val w = size.width
            val h = size.height

            val path = Path()
            val fillPath = Path()

            val p1 = Offset(0f, h * 0.7f)
            val p2 = Offset(w * 0.3f, h * 0.35f)
            val p3 = Offset(w * 0.65f, h * 0.55f)
            val p4 = Offset(w, h * 0.2f)

            path.moveTo(p1.x, p1.y)
            path.cubicTo(w * 0.15f, h * 0.7f, w * 0.15f, h * 0.35f, p2.x, p2.y)
            path.cubicTo(w * 0.48f, h * 0.35f, w * 0.48f, h * 0.55f, p3.x, p3.y)
            path.cubicTo(w * 0.82f, h * 0.55f, w * 0.82f, h * 0.2f, p4.x, p4.y)

            fillPath.addPath(path)
            fillPath.lineTo(w, h)
            fillPath.lineTo(0f, h)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.25f),
                        Color.Transparent
                    )
                )
            )

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3.dp.toPx())
            )

            listOf(p1, p2, p3, p4).forEachIndexed { index, point ->
                val nodeColor = if (index % 2 == 0) primaryColor else secondaryColor
                drawCircle(
                    color = nodeColor,
                    radius = 6.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = point
                )
            }
        }
    }
}

// Custom Illustration 3: Journal with Subtle Shield / Lock Symbol
@Composable
fun PrivacySpaceIllustration() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Soft Privacy Aura Circle
        Surface(
            shape = CircleShape,
            color = SoftLavender.copy(alpha = 0.18f),
            modifier = Modifier.size(170.dp)
        ) {}

        Card(
            modifier = Modifier
                .width(160.dp)
                .height(150.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = SageGreen.copy(alpha = 0.15f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Private Lock",
                            tint = SageGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = "Private & Personal",
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightOnSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
