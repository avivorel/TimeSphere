package com.example.timesphere.ui.theme


import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.material3.*
import coil.compose.rememberAsyncImagePainter
import com.example.timesphere.model.Utils
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import com.example.timesphere.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

val utils = Utils()


@Composable
fun RoundedCornerCardTop(
    content: @Composable () -> Unit,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = { onClick() }
    ) {
        content()
    }
}

@Composable
fun ClockInButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State to track if the button is in "clock in" or "clock out" mode
    var isClockedIn by remember { mutableStateOf(text.lowercase().contains("out")) }

    // Scale animation for press effect
    val pressScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200), label = ""
    )

    // Pulse animation for vibrancy (only for clock-in state)
    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(isClockedIn) {
        if (!isClockedIn) {
            while (true) {
                pulseScale.animateTo(1.1f, animationSpec = tween(600))
                pulseScale.animateTo(1f, animationSpec = tween(600))
            }
        } else {
            pulseScale.animateTo(1f, animationSpec = tween(200))
        }
    }

    // Glow animation (rotating subtle border effect)
    val glowRotation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            glowRotation.animateTo(360f, animationSpec = tween(3000, delayMillis = 0))
            glowRotation.snapTo(0f)
        }
    }

    // Gradient background based on clock state
    // Solid background color based on clock state
    val buttonColor: Color = if (isClockedIn) {
        MaterialTheme.colorScheme.error // Red for clock out
    } else {
        MaterialTheme.colorScheme.primary // Green/Primary for clock in
    }

    // Glow gradient for the outer ring
    val glowGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    )

    Box(
        modifier = modifier
            .size(220.dp) // Slightly bigger size overall
            .shadow(24.dp, CircleShape, clip = false) // Increase shadow, no clipping
    ) {
        // Outer Glow Ring (rotating)
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(rotationZ = glowRotation.value) // Only glow rotates
                .background(glowGradient, CircleShape)
        )

        // Inner Button (static, slightly smaller)
        Box(
            modifier = Modifier
                .size(180.dp) // Make inner button smaller to expose glow
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(buttonColor)
                .graphicsLayer(
                    scaleX = pressScale * pulseScale.value,
                    scaleY = pressScale * pulseScale.value
                )
                .clickable {
                    onClick()
                    isClockedIn = !isClockedIn
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = if (isClockedIn) android.R.drawable.ic_menu_save else android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
fun MonthSlider(
    modifier: Modifier = Modifier,
    startMonthIndex: Int = 0,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var currentMonthIndex by remember { mutableIntStateOf(startMonthIndex.coerceIn(0, months.size - 1)) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        IconButton(onClick = { onClickPrev(); currentMonthIndex = (currentMonthIndex - 1 + months.size) % months.size }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = "Previous Month",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = months[currentMonthIndex],
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { onClickNext(); currentMonthIndex = (currentMonthIndex + 1) % months.size }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_next),
                contentDescription = "Next Month",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
fun RoundedImageWithLocalUpdate(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentDescription: String? = null,
    cornerRadius: Int = 16,
    onImageSelected: (String) -> Unit
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var croppedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                croppedImageUri = uri
                utils.uploadImageToFirebase(uri, context) { uploadedImageUrl ->
                    isUploading = false
                    onImageSelected(uploadedImageUrl)
                }
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val cropIntent = Intent("com.android.camera.action.CROP").apply {
                setDataAndType(uri, "image/*")
                putExtra("crop", "true")
                putExtra("aspectX", 1)
                putExtra("aspectY", 1)
                putExtra("outputX", 300)
                putExtra("outputY", 300)
                putExtra("return-data", false)
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cropImageLauncher.launch(cropIntent)
        }
    }

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .clickable { showConfirmationDialog = true }
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)) // Subtle background for default state
    ) {
        if (imageUrl.isNullOrEmpty() && croppedImageUri == null) {
            // Default placeholder when no image is available
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Click here to upload image",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // Display the image if available
            AsyncImage(
                model = croppedImageUri?.toString() ?: imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
                placeholder = rememberAsyncImagePainter(MaterialTheme.colorScheme.surface)
            )
        }

        if (isUploading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Change Image", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Do you want to change the image?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                    imagePickerLauncher.launch("image/*")
                }) {
                    Text("Yes", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("No", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun BackgroundContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.clocks_gpt),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f // Fade the image to make it subtle
        )
        content()
    }
}