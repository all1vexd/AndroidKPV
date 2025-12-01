package ru.itis.hw4.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.itis.hw4.CoroutineTracker
import ru.itis.hw4.R
import ru.itis.hw4.components.MySwitch
import ru.itis.hw4.utils.getDispatcherName
import ru.itis.hw4.utils.parallelCoroutines
import ru.itis.hw4.utils.sequentialCoroutines
import kotlin.math.roundToInt

@Composable
fun MainPage(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val tracker = remember { CoroutineTracker() }

    var sliderPosition by remember { mutableFloatStateOf(10f) }
    var expanded by remember { mutableStateOf(false) }
    var dispatcher by remember { mutableStateOf<CoroutineDispatcher>(Dispatchers.Default) }
    var inCreateTime by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isParallel by remember { mutableStateOf(false) }
    var currentJob by remember { mutableStateOf<Job?>(null) }


    val onError: (Exception, Int) -> Unit = { exception, index ->
        when (exception) {
            is RuntimeException -> {
                coroutineScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, context.getString(R.string.error_timeout, index), Toast.LENGTH_SHORT).show()
                }
            }
            is IllegalArgumentException -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.error_invalid_argument, index),
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is IllegalStateException -> {
                coroutineScope.launch(Dispatchers.Main) {
                    sliderPosition = 10f
                    dispatcher = Dispatchers.Default
                    isParallel = true
                    inCreateTime = false
                    Toast.makeText(context,  context.getString(R.string.settings_reset), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val dispatchersList = listOf(
        stringResource(R.string.dispatchers_main) to Dispatchers.Main,
        stringResource(R.string.dispatchers_io) to Dispatchers.IO,
        stringResource(R.string.dispatchers_default) to Dispatchers.Default,
        stringResource(R.string.dispatchers_unconfined) to Dispatchers.Unconfined
    )

    DisposableEffect(Unit) {
        onDispose {
            currentJob?.cancel()
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.coroutines_count, sliderPosition.toInt()),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )

            Slider (
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = (it / 5).roundToInt() * 5f
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                steps = (100 - 10) / 5 - 1,
                valueRange = 10f..100f
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = stringResource(
                    R.string.selected_dispatcher,
                    getDispatcherName(dispatcher, context)
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Box {

                Button(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Text(
                        text = stringResource(R.string.select_dispatcher)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    dispatchersList.forEach { (name, dispatcherType) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = name
                                )
                            },
                            onClick = {
                                dispatcher = dispatcherType
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MySwitch(
                checked = isParallel,
                onCheckedChange = {
                    isParallel = it
                },
                label = stringResource(R.string.parallel)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MySwitch(
                checked = !isParallel,
                onCheckedChange = {
                    isParallel = !it
                },
                label = stringResource(R.string.sequential)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MySwitch(
                checked = inCreateTime,
                onCheckedChange = {
                    inCreateTime = it
                },
                label = stringResource(R.string.delayed_launch)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (isLoading) {
                Button(
                    onClick = {
                        currentJob?.cancel()
                        currentJob = null

                        coroutineScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                context.getString(
                                R.string.cancelled,
                                sliderPosition.toInt() - tracker.totalCount
                                ),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        isLoading = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(
                        text = stringResource(R.string.stop)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = stringResource(
                        R.string.completed,
                        tracker.totalCount,
                        sliderPosition.toInt()
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            } else {
                Button(
                    onClick = {
                        tracker.reset()
                        isLoading = true

                        currentJob = coroutineScope.launch {
                            try {
                                if (isParallel) {
                                    parallelCoroutines(
                                        count = sliderPosition.toInt(),
                                        selectedDispatcher = dispatcher,
                                        onError = onError,
                                        inCreateTime = inCreateTime,
                                        tracker = tracker,
                                        context = context
                                    )
                                } else {
                                    sequentialCoroutines(
                                        count = sliderPosition.toInt(),
                                        selectedDispatcher = dispatcher,
                                        onError = onError,
                                        inCreateTime = inCreateTime,
                                        tracker = tracker,
                                        context = context
                                    )
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.start))
                }
            }
        }
    }
}



