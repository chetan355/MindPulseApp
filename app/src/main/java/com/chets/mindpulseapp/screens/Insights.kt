package com.chets.mindpulseapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chets.mindpulseapp.data.AiAnalysisManager
import com.chets.mindpulseapp.data.DailyMoodPoint
import com.chets.mindpulseapp.data.InsightsHelper
import com.chets.mindpulseapp.data.InsightsSummary
import com.chets.mindpulseapp.viewmodel.JournalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Insights(
    journalViewModel: JournalViewModel? = if (LocalInspectionMode.current) null else viewModel()
) {
    var isWeekly by remember { mutableStateOf(true) }

    val allEntries by journalViewModel?.getAllJournalEntries?.collectAsState(initial = emptyList())
        ?: remember { mutableStateOf(emptyList()) }

    val filteredEntries = remember(allEntries, isWeekly) {
        InsightsHelper.getFilteredEntries(allEntries, isWeekly)
    }

    val dailyPoints = remember(filteredEntries, isWeekly) {
        InsightsHelper.getDailyMoodPoints(filteredEntries, isWeekly)
    }

    val summary = remember(filteredEntries) {
        InsightsHelper.getInsightsSummary(filteredEntries)
    }

    var aiMessage by remember { mutableStateOf("Generating personalized reflection insights...") }
    var isLoadingAi by remember { mutableStateOf(true) }

    LaunchedEffect(summary, isWeekly) {
        isLoadingAi = true
        val excerpts = filteredEntries.map { it.content }
        aiMessage = AiAnalysisManager.generateInsight(summary, isWeekly, excerpts)
        isLoadingAi = false
    }

    val backgroundColor = MaterialTheme.colorScheme.background

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Insights",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Small patterns, gentle progress.",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Time Range Toggle Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = isWeekly,
                    onClick = { isWeekly = true },
                    label = {
                        Text(
                            text = "Weekly (Last 7 Days)",
                            fontFamily = FontFamily.Serif,
                            fontWeight = if (isWeekly) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                FilterChip(
                    selected = !isWeekly,
                    onClick = { isWeekly = false },
                    label = {
                        Text(
                            text = "Monthly (Last 30 Days)",
                            fontFamily = FontFamily.Serif,
                            fontWeight = if (!isWeekly) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            // Mood Line Graph Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = "Trend Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isWeekly) "Weekly Mood Trend" else "Monthly Mood Trend",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    MoodGraphCanvas(
                        dailyPoints = dailyPoints,
                        isWeekly = isWeekly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            // Summary Metrics Card
            SummaryMetricsSection(summary = summary)

            // AI Reflection Analysis Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Sparkle",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "AI Reflection Analysis",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    AnimatedVisibility(
                        visible = !isLoadingAi,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = aiMessage,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    if (isLoadingAi) {
                        Text(
                            text = "Analyzing your emotional journey...",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoodGraphCanvas(
    dailyPoints: List<DailyMoodPoint>,
    isWeekly: Boolean,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val guideColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)

    if (dailyPoints.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No mood records logged yet.",
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val width = size.width
            val height = size.height
            val paddingX = 20.dp.toPx()
            val paddingY = 16.dp.toPx()
            val usableWidth = width - (paddingX * 2)
            val usableHeight = height - (paddingY * 2)

            // Draw horizontal reference guide lines for scores 1..5
            for (level in 1..5) {
                val y = paddingY + usableHeight - ((level - 1) / 4f * usableHeight)
                drawLine(
                    color = guideColor,
                    start = Offset(paddingX, y),
                    end = Offset(width - paddingX, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                )
            }

            val validPointsWithIndex = dailyPoints.mapIndexedNotNull { index, point ->
                if (point.score > 0f) Pair(index, point) else null
            }

            val stepX = if (dailyPoints.size > 1) usableWidth / (dailyPoints.size - 1) else usableWidth / 2f

            if (validPointsWithIndex.size == 1) {
                val (dayIndex, point) = validPointsWithIndex.first()
                val x = paddingX + (dayIndex * stepX)
                val scoreFraction = (point.score - 1f) / 4f
                val y = paddingY + usableHeight - (scoreFraction * usableHeight)
                val pointColor = point.moodObject?.tint ?: lineColor

                drawLine(
                    color = pointColor.copy(alpha = 0.5f),
                    start = Offset(paddingX, y),
                    end = Offset(width - paddingX, y),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )

                drawCircle(
                    color = pointColor,
                    radius = 8.dp.toPx(),
                    center = Offset(x, y)
                )
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            } else if (validPointsWithIndex.size >= 2) {
                val path = Path()
                val fillPath = Path()

                validPointsWithIndex.forEachIndexed { i, (dayIndex, point) ->
                    val x = paddingX + (dayIndex * stepX)
                    val scoreFraction = (point.score - 1f) / 4f
                    val y = paddingY + usableHeight - (scoreFraction * usableHeight)

                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height - paddingY)
                        fillPath.lineTo(x, y)
                    } else {
                        val (prevDayIndex, prevPoint) = validPointsWithIndex[i - 1]
                        val prevX = paddingX + (prevDayIndex * stepX)
                        val prevScoreFraction = (prevPoint.score - 1f) / 4f
                        val prevY = paddingY + usableHeight - (prevScoreFraction * usableHeight)

                        val controlX1 = prevX + (x - prevX) / 2f
                        val controlY1 = prevY
                        val controlX2 = prevX + (x - prevX) / 2f
                        val controlY2 = y

                        path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                        fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                    }

                    if (i == validPointsWithIndex.lastIndex) {
                        fillPath.lineTo(x, height - paddingY)
                        fillPath.close()
                    }
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw nodes at each valid point
                validPointsWithIndex.forEach { (dayIndex, point) ->
                    val x = paddingX + (dayIndex * stepX)
                    val scoreFraction = (point.score - 1f) / 4f
                    val y = paddingY + usableHeight - (scoreFraction * usableHeight)
                    val pointColor = point.moodObject?.tint ?: lineColor

                    drawCircle(
                        color = pointColor,
                        radius = 7.dp.toPx(),
                        center = Offset(x, y)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 3.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }

        // X-Axis Date Labels Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = if (isWeekly) 1 else 5
            dailyPoints.forEachIndexed { index, point ->
                if (index % step == 0 || index == dailyPoints.size - 1) {
                    Text(
                        text = point.dateLabel,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryMetricsSection(summary: InsightsSummary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Average Mood Metric Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Average Mood",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (summary.averageMood != null) {
                    Icon(
                        painter = painterResource(summary.averageMood.emoji),
                        contentDescription = summary.averageMood.mood,
                        tint = summary.averageMood.tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = summary.averageMood.mood,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                } else {
                    Text(
                        text = "—",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Dominant Mood Metric Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dominant Mood",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (summary.predominantMood != null) {
                    Icon(
                        painter = painterResource(summary.predominantMood.emoji),
                        contentDescription = summary.predominantMood.mood,
                        tint = summary.predominantMood.tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = summary.predominantMood.mood,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                } else {
                    Text(
                        text = "—",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Reflections Logged Count Metric Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reflections",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${summary.totalEntries}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "logged",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsightsPreview() {
    Insights()
}
