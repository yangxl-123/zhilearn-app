package com.example.literacy.ui.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ParentScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("家长中心") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("家长中心", style = MaterialTheme.typography.titleLarge)

            Text("这里可以查看孩子的学习进度、已学汉字、阅读记录等。")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("查看学习记录")
            }

            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("设置难度")
            }

            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("导出进度")
            }
        }
    }
}
