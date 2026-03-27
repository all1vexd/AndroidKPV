package ru.itis.hw6.data

object ErrorSimulator {
    private var requestCounter = 0

    fun shouldSimulateError(): Boolean {
        requestCounter++
        return requestCounter % 3 == 0
    }

    fun reset() {
        requestCounter = 0
    }
}