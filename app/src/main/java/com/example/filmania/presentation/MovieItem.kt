package com.example.filmania.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.filmania.domain.Movies

@Composable
fun MovieItem(movie: Movies) {
    Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterImage}",
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .height(170.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(Modifier.weight(3f), horizontalAlignment = Alignment.Start) {
            Text(text = movie.title, fontSize = 12.sp, color = Color.Black)
            Text(text = movie.page.toString(), fontSize = 12.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.releaseData, fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = movie.voteAverage, fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(2.dp))
                Icon(Icons.Default.Star, contentDescription = "", tint = Color.Yellow)
            }
        }
    }
}