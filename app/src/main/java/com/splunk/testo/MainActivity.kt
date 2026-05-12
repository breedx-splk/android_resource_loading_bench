package com.splunk.testo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.splunk.testo.ui.theme.AndroidResourceBenchTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidResourceBenchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var resourcesText by remember { mutableStateOf("") }
    var classesText by remember { mutableStateOf("") }
    var isResourcesLoading by remember { mutableStateOf(false) }
    var isClassesLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Button(
            enabled = !isResourcesLoading,
            onClick = {
                coroutineScope.launch {
                    isResourcesLoading = true
                    val result = withContext(Dispatchers.Default) {
                        ResourcesLoaderBench().load()
                    }
                    resourcesText = result.toString()
                    isResourcesLoading = false
                }
            },
            modifier = Modifier.sizeIn(minWidth = 204.dp, minHeight = 82.dp)
        ) {
            Text("load resources", fontSize = 27.sp)
        }
        OutlinedTextField(
            value = resourcesText,
            onValueChange = { resourcesText = it },
            modifier = Modifier.sizeIn(minWidth = 204.dp)
        )
        Button(
            enabled = !isClassesLoading,
            onClick = {
                coroutineScope.launch {
                    isClassesLoading = true
                    val result = withContext(Dispatchers.Default) {
                        ClassesLoaderBench().load()
                    }
                    classesText = result.toString()
                    isClassesLoading = false
                }
            },
            modifier = Modifier.sizeIn(minWidth = 204.dp, minHeight = 82.dp)
        ) {
            Text("load classes", fontSize = 27.sp)
        }
        OutlinedTextField(
            value = classesText,
            onValueChange = { classesText = it },
            modifier = Modifier.sizeIn(minWidth = 204.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidResourceBenchTheme {
        MainScreen()
    }
}
