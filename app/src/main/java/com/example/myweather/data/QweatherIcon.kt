package com.example.myweather.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myweather.R

@Composable
fun QweatherIcon(id: Int) {
    val context = LocalContext.current
    val drawableName = "qweather$id"
    val drawableResId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

    if (drawableResId != 0) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    } else {
        // 处理资源ID未找到的情况
        Image(
            painter = painterResource(id = R.drawable.qweatherqweather),
            contentDescription = "Default icon",
            modifier = Modifier.size(48.dp)
        )
    }
}