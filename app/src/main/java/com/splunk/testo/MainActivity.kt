package com.splunk.testo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val logoBitmap = remember(context) {
        context.assets.open("logo.png").use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    }
    var resourcesText by remember { mutableStateOf("") }
    var classesText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isResourcesLoading by remember { mutableStateOf(false) }
    var isClassesLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            confirmButton = {
                Button(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            },
            title = { Text("Benchmark Failed") },
            text = { Text(errorMessage!!) }
        )

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (logoBitmap != null) {
            Image(
                bitmap = logoBitmap,
                contentDescription = "App logo",
                modifier = Modifier.sizeIn(maxWidth = 160.dp, maxHeight = 160.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        Button(
            enabled = !isResourcesLoading,
            onClick = {
                coroutineScope.launch {
                    isResourcesLoading = true
                    try {
                        val result = withContext(Dispatchers.Default) {
                            ResourcesLoaderBench(num = 500).load()
                        }
                        resourcesText = result.toString()
                    } catch (t: Throwable) {
                        errorMessage = t.message ?: t.toString()
                    } finally {
                        isResourcesLoading = false
                    }
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
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            enabled = !isClassesLoading,
            onClick = {
                coroutineScope.launch {
                    isClassesLoading = true
                    try {
                        val result = withContext(Dispatchers.Default) {
                            ClassesLoaderBench(
                                packageName = "com.splunk.testo.versions",
                                num = 500
                            ).load()
                        }
                        classesText = result.toString()
                    } catch (t: Throwable) {
                        errorMessage = t.message ?: t.toString()
                    } finally {
                        isClassesLoading = false
                    }
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
