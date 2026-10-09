package com.example.literacy.ui.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.literacy.data.DatabaseProvider
import com.example.literacy.data.LearningRecordEntity
import com.example.literacy.data.SampleContent
import kotlinx.coroutines.launch

@Composable
fun LearnScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val characters = SampleContent.characters
    var index by remember { mutableStateOf(0) }
    var showPinyin by remember { mutableStateOf(false) }

    val current = characters.getOrNull(index) ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("识字") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = current,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (showPinyin) {
                Text("拼音：待补充", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showPinyin = !showPinyin }) {
                Text(if (showPinyin) "隐藏拼音" else "显示拼音")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                scope.launch {
                    val db = DatabaseProvider.get(context)
                    db.dao().upsertRecord(
                        LearningRecordEntity(characterId = current)
                    )
                }
                if (index < characters.lastIndex) {
                    index++
                    showPinyin = false
                }
            }) {
                Text("下一个")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("${index + 1} / ${characters.size}")
        }
    }
}
