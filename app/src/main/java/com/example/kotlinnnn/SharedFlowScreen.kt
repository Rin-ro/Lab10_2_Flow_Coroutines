package com.example.kotlinnnn


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
public fun SharedFlowScreen(modifier: Modifier) {
    val eventsSharedFlow = remember { MutableSharedFlow<String>(replay = 3) }
    val eventsFlow: SharedFlow<String> = eventsSharedFlow.asSharedFlow()

    var events by remember { mutableStateOf<List<String>>(emptyList()) }
    var eventCount by remember { mutableStateOf(0) }
    var eventCounter by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var autoGenerationJob by remember { mutableStateOf<Job?>(null) }
    var isAutoGenerating by remember { mutableStateOf(false) }

    // Сбор событий
    LaunchedEffect(Unit) {
        eventsFlow.collect { event ->
            events = (events + event).takeLast(10)
            eventCount++
        }
    }

    fun emitEvent(message: String) {
        scope.launch {
            eventsSharedFlow.emit(message)
        }
    }

    fun startAutoGeneration() {
        if (autoGenerationJob?.isActive == true) return
        isAutoGenerating = true
        autoGenerationJob = scope.launch {
            while (true) {
                delay(2000)
                eventCounter++
                val randomNumber = Random.nextInt(1, 101)
                emitEvent("Событие#$eventCounter: $randomNumber")
            }
        }
    }

    fun stopAutoGeneration() {
        isAutoGenerating = false
        autoGenerationJob?.cancel()
        autoGenerationJob = null
    }

    DisposableEffect(Unit) {
        onDispose {
            autoGenerationJob?.cancel()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Всего событий: $eventCount")

        LazyColumn {
            items(events.reversed()) { event ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Text(text = event, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            emitEvent("Ручное событие #${eventCount + 1}")
        }) {
            Text("Сгенерировать событие")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (isAutoGenerating) {
                stopAutoGeneration()
            } else {
                startAutoGeneration()
            }
        }) {
            Text(if (isAutoGenerating) "Остановить" else "Запустить")
        }
    }
}