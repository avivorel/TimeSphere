package com.example.timesphere.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
fun ClockInButton(text :String,) {
    Button(
        onClick = { /* Handle click action here */ },
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
@Preview
fun Preview(){
    ClockInButton("Clock In")
}