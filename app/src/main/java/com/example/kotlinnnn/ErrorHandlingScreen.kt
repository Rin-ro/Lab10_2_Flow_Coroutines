package com.example.kotlinnnn


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

// 1. Опасная операция
public suspend fun riskyOperation(success: Boolean): String {
    delay(1000)
    if (!success) {
        throw IllegalStateException("Операция не удалась")
    }
    return "Операция выполнена успешно"
}

// 2. Опасный Flow
fun riskyFlow(): kotlinx.coroutines.flow.Flow<String> = flow {
    emit("Шаг 1")
    delay(500)
    emit("Шаг 2")
    delay(500)
    throw RuntimeException("Ошибка на шаге 3!")
    emit("Шаг 3")
}.catch { exception ->
    emit("Ошибка обработана: ${exception.message}")
}

// 3. Безопасная операция
suspend fun safeOperation(success: Boolean): Result<String> {
    return try {
        delay(1000)
        if (!success) {
            Result.failure(IllegalStateException("Операция не удалась"))
        } else {
            Result.success("Операция выполнена успешно")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// 4. Экран
@Composable
fun ErrorHandlingScreen(modifier: Modifier) {
    var result by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        result?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(text = it, modifier = Modifier.padding(16.dp))
            }
        }

        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ошибка: $it", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            result = null
            errorMessage = null
            scope.launch {
                try {
                    val res = riskyOperation(true)
                    result = res
                } catch (e: Exception) {
                    errorMessage = e.message
                }
            }
        }) {
            Text("Успешная операция")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            result = null
            errorMessage = null
            scope.launch {
                try {
                    val res = riskyOperation(false)
                    result = res
                } catch (e: Exception) {
                    errorMessage = e.message
                }
            }
        }) {
            Text("Операция с ошибкой")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            result = null
            errorMessage = null
            scope.launch {
                riskyFlow().collect { value ->
                    result = value
                }
            }
        }) {
            Text("Flow с обработкой ошибок")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            result = null
            errorMessage = null
            scope.launch {
                val safeResult = safeOperation(false)
                safeResult.fold(
                    onSuccess = { result = it },
                    onFailure = { errorMessage = it.message ?: "Неизвестная ошибка" }
                )
            }
        }) {
            Text("Безопасная операция")
        }
    }
}