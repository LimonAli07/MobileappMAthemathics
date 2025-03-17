package com.example.androidquestiongame.model

data class Question(
    val firstNumber: Int,
    val secondNumber: Int,
    val operation: Operation,
    val answer: Int
) {
    val text: String
        get() = "$firstNumber ${operation.symbol} $secondNumber = ?"
}

enum class Operation(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷")
}

fun generateQuestion(): Question {
    val operation = Operation.values().random()
    
    return when (operation) {
        Operation.ADD -> {
            val num1 = (1..12).random()
            val num2 = (1..12).random()
            Question(num1, num2, operation, num1 + num2)
        }
        Operation.SUBTRACT -> {
            val num2 = (1..12).random()
            val num1 = (num2..24).random() // Ensures positive result
            Question(num1, num2, operation, num1 - num2)
        }
        Operation.MULTIPLY -> {
            val num1 = (1..12).random()
            val num2 = (1..12).random()
            Question(num1, num2, operation, num1 * num2)
        }
        Operation.DIVIDE -> {
            val num2 = (1..12).random()
            val answer = (1..12).random()
            val num1 = num2 * answer // Ensures whole number division
            Question(num1, num2, operation, answer)
        }
    }
} 