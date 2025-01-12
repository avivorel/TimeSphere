package com.example.timesphere.ui.theme


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter


@Composable
fun RoundedCornerCardTop(
    content : @Composable () -> Unit,
    modifier: Modifier,
    onClick: () -> Unit,
    onDismiss : () -> Unit
    ){
    Card(modifier = modifier,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        onClick = { onClick() },
    ){
        content()
    }
}

@Composable
fun ClockInButton(text :String,onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .size(100.dp) // Circular size
            .clip(CircleShape), // Ensures the button is circular
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp), // Ensure text is centered
        elevation = ButtonDefaults.elevatedButtonElevation() // Optional: Adds a shadow effect
    ) {
        Text(text)
    }
}


@Composable
fun MonthSlider(
    modifier : Modifier = Modifier, // 0-based: 0 = January, 11 = December
    startMonthIndex: Int = 0,
    onClickPrev: () -> Unit,
    onClickNext: () -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    // Coerce the starting index into [0..11]
    var currentMonthIndex by remember {
        mutableIntStateOf(startMonthIndex.coerceIn(0, months.size - 1))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                onClickPrev()
                // Shift + months.size to avoid negative remainder
                currentMonthIndex = (currentMonthIndex - 1 + months.size) % months.size
            }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = "Previous Month"
            )
        }

        Text(
            text = months[currentMonthIndex],
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(
            onClick = {
                onClickNext()
                // For incrementing, (x + 1) % months.size stays >= 0 so it's okay
                currentMonthIndex = (currentMonthIndex + 1) % months.size
            }
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_next),
                contentDescription = "Next Month"
            )
        }
    }
}



@Composable
fun RoundedImage(
    modifier: Modifier = Modifier,
    imageUrl: String?, // URL to the user image
    contentDescription: String? = null,
    cornerRadius: Int = 16, // Default corner radius in dp
    placeholderColor: Color = Color.Gray // Default placeholder color
) {
    Box(
        modifier = modifier
            .size(120.dp) // Adjust the size as needed
            .clip(RoundedCornerShape(cornerRadius.dp)) // Clip with rounded corners
    ) {
        // Use Coil's AsyncImage for loading images from a URL
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            placeholder = rememberAsyncImagePainter(placeholderColor) // Optional placeholder
        )
    }
}


@Preview
@Composable
fun DebugRoundedImagePreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hardcoded URL for debugging
        RoundedImage(
            imageUrl = "https://placekitten.com/200/200", // Example placeholder URL
            contentDescription = "Debugging Profile Picture",
            cornerRadius = 16
        )
    }
}
