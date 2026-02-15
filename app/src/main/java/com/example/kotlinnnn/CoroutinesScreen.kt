package com.example.kotlinnnn


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

// 1. Долгая операция
public suspend fun simulateLongOperation(duration: Long): String {
    delay(duration)
    return "Операция завершена за $duration мс"
}

// 2. Сумма чисел
suspend fun calculateSum(numbers: List<Int>): Int {
    return withContext(Dispatchers.Default) {
        delay(1000)
        numbers.sum()
    }
}

// 3. Экран
@Composable
public fun CoroutinesScreen(modifier: Modifier) {
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
        result?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isLoading = true
                result = null
                scope.launch {
                    val res = simulateLongOperation(2000)
                    result = res
                    isLoading = false
                }
            },
            enabled = !isLoading
        ) {
            Text("Запустить долгую операцию")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                result = null
                scope.launch {
                    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    val sum = calculateSum(numbers)
                    result = "Сумма чисел: $sum"
                    isLoading = false
                }
            },
            enabled = !isLoading
        ) {
            Text("Вычислить сумму")
        }
    }
}