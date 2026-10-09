package com.example.literacy.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.literacy.data.SampleContent
import com.example.literacy.data.ArticleEntity
import com.example.literacy.ui.learn.LearnScreen
import com.example.literacy.ui.reading.ReadingListScreen
import com.example.literacy.ui.reading.ArticleScreen
import com.example.literacy.ui.parent.ParentScreen

@Composable
fun HomeScreen() {
    var tab by remember { mutableStateOf("home") }
    var selectedArticle by remember { mutableStateOf<ArticleEntity?>(null) }

    when (tab) {
        "home" -> HomeTab(
            onLearnClick = { tab = "learn" },
            onReadingClick = { tab = "reading" },
            onParentClick = { tab = "parent" }
        )
        "learn" -> LearnScreen(onBack = { tab = "home" })
        "reading" -> ReadingListScreen(
            onBack = { tab = "home" },
            onArticleClick = {
                selectedArticle = it
                tab = "article"
            }
        )
        "article" -> selectedArticle?.let {
            ArticleScreen(
                article = it,
                onBack = { tab = "reading" }
            )
        }
        "parent" -> ParentScreen(onBack = { tab = "home" })
    }
}

@Composable
private fun HomeTab(
    onLearnClick: () -> Unit,
    onReadingClick: () -> Unit,
    onParentClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("识字启蒙") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("欢迎来到识字启蒙", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onLearnClick, modifier = Modifier.fillMaxWidth()) {
                Text("开始识字")
            }

            Button(onClick = onReadingClick, modifier = Modifier.fillMaxWidth()) {
                Text("分级阅读")
            }

            Button(onClick = onParentClick, modifier = Modifier.fillMaxWidth()) {
                Text("家长中心")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("已准备 ${SampleContent.characters.size} 个汉字", style = MaterialTheme.typography.bodySmall)
            Text("已准备 ${SampleContent.articles.size} 篇短文", style = MaterialTheme.typography.bodySmall)
        }
    }
}
