package com.example.kotlinnnn


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
public fun StateFlowScreen(modifier: Modifier) {
    val counterStateFlow = remember { mutableStateOf(0) }
    val isAutoIncrementing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var autoIncrementJob by remember { mutableStateOf<Job?>(null) }

    fun increment() = run { counterStateFlow.value += 1 }
    fun decrement() = run { counterStateFlow.value -= 1 }
    fun reset() = run { counterStateFlow.value = 0 }
    fun incrementBy(value: Int) = run { counterStateFlow.value += value }

    fun toggleAutoIncrement() {
        if (isAutoIncrementing.value) {
            isAutoIncrementing.value = false
            autoIncrementJob?.cancel()
            autoIncrementJob = null
        } else {
            isAutoIncrementing.value = true
            autoIncrementJob = scope.launch {
                while (true) {
                    delay(1000)
                    counterStateFlow.value += 1
                }
            }
        }
    }

    // Отмена при выходе
    DisposableEffect(Unit) {
        onDispose {
            autoIncrementJob?.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = counterStateFlow.value.toString(),
            style = MaterialTheme.typography.displayLarge
        )

        if (isAutoIncrementing.value) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Автоинкремент активен")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = { decrement() }) { Text("-1") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { increment() }) { Text("+1") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { reset() }) { Text("Сброс") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { incrementBy(5) }) { Text("+5") }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { toggleAutoIncrement() }) {
            Text(if (isAutoIncrementing.value) "Остановить" else "Запустить")
        }
    }
}