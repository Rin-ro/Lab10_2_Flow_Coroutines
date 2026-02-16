package com.example.kotlinnnn


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

public fun numberFlow(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(500)
        emit(i)
    }
}
fun transformedFlow(flow: Flow<Int>): Flow<Int> = flow
    .map { it * it }
    .filter { it % 2 == 0 }
fun errorFlow(): Flow<String> = flow {
    emit("Первое значение")
    delay(500)
    emit("Второе значение")
    delay(500)
    throw RuntimeException("Произошла ошибка!")
}.catch { exception ->
    emit("Ошибка обработана: ${exception.message}")
}
@Composable
fun FlowScreen(modifier: Modifier) {
    var flowValues by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(flowValues) { value ->
                Text(text = value, modifier = Modifier.padding(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            flowValues = emptyList()
            scope.launch {
                numberFlow().collect { value ->
                    flowValues = flowValues + "Число: $value"
                }
            }
        }) {
            Text("Запустить Flow")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            flowValues = emptyList()
            scope.launch {
                transformedFlow(numberFlow()).collect { value ->
                    flowValues = flowValues + "Квадрат четного: $value"
                }
            }
        }) {
            Text("Запустить преобразованный Flow")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            flowValues = emptyList()
            scope.launch {
                errorFlow().collect { value ->
                    flowValues = flowValues + value
                }
            }
        }) {
            Text("Запустить Flow с ошибкой")
        }
    }
}