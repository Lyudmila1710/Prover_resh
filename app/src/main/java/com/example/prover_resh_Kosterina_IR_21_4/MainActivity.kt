package com.example.prover_resh_Kosterina_IR_21_4

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var num1: TextView? = null
    private var oper: TextView? = null
    private var num2: TextView? = null
    private var result: TextView? = null
    private var procent: TextView? = null
    private var allCount: TextView? = null
    private var rightCount: TextView? = null
    private var wrongCount: TextView? = null
    private var time: TextView? = null
    private var maxTime: TextView? = null
    private var minTime: TextView? = null
    private var avgTime: TextView? = null

    private var startButton: Button? = null
    private var rightButton: Button? = null
    private var wrongButton: Button? = null

    var startTime: Long = 0
    var endTime: Long = 0
    var correctAnswer: Double = 0.0
    var isCorrect: Boolean = false

    var correctCount: Int = 0
    var wrCount: Int = 0
    var totalTime: Long = 0
    var maxSelectionTime: Long = Long.MIN_VALUE
    var minSelectionTime: Long = Long.MAX_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        num1 = findViewById(R.id.one_number)
        oper = findViewById(R.id.oper)
        num2 = findViewById(R.id.two_number)
        result = findViewById(R.id.result)
        procent = findViewById(R.id.procent)
        allCount = findViewById(R.id.all_count)
        rightCount = findViewById(R.id.right)
        wrongCount = findViewById(R.id.wrong)
        time = findViewById(R.id.time)
        maxTime = findViewById(R.id.max)
        minTime = findViewById(R.id.min)
        avgTime = findViewById(R.id.sred)

        startButton = findViewById(R.id.start)
        rightButton = findViewById(R.id.right_Btn)
        wrongButton = findViewById(R.id.wrong_Btn)
        rightButton?.isEnabled = false
        wrongButton?.isEnabled = false
        startButton?.setOnClickListener {
            generateQuestion()
            startButton?.isEnabled = false
            rightButton?.isEnabled = true
            wrongButton?.isEnabled = true
            startTime = SystemClock.elapsedRealtime()
        }

        rightButton?.setOnClickListener {
            endTime = SystemClock.elapsedRealtime()
            calculateSelectionTime()
            checkAnswer(true)
            updateStats()
            resetButtons()
        }

        wrongButton?.setOnClickListener {
            endTime = SystemClock.elapsedRealtime()
            calculateSelectionTime()
            checkAnswer(false)
            updateStats()
            resetButtons()
        }
    }

    private fun generateQuestion() {
        val operand1 = Random.nextInt(10, 100)
        val operand2 = Random.nextInt(10, 100)
        val operator = listOf("*", "/", "-", "+").random()

        num1?.text = operand1.toString()
        oper?.text = operator
        num2?.text = operand2.toString()

        correctAnswer = calculateResult(operand1, operand2, operator)
        isCorrect = Random.nextBoolean()

        if (isCorrect) {
            result?.text = correctAnswer.toString()
        } else {
            val randomResult = (0..99).filter { it.toDouble() != correctAnswer }.random()
            result?.text = randomResult.toString()

        }
    }

    private fun calculateResult(operand1: Int, operand2: Int, operator: String): Double {
        return when (operator) {
            "*" -> (operand1 * operand2).toDouble()
            "/" -> (operand1.toDouble() / operand2 * 100.0).roundToInt() / 100.0
            "-" -> (operand1 - operand2).toDouble()
            "+" -> (operand1 + operand2).toDouble()
            else -> 0.0
        }
    }



    private fun checkAnswer(userAnswer: Boolean) {
        if (userAnswer == isCorrect) {

            result?.text = "ПРАВИЛЬНО"
            correctCount++
        } else {

            result?.text = "НЕ ПРАВИЛЬНО"
            wrCount++
        }
    }

    private fun calculateSelectionTime() {

        val selectionTime = endTime - startTime
        totalTime += selectionTime

        if (selectionTime > maxSelectionTime) {
            maxSelectionTime = selectionTime
        }

        if (selectionTime < minSelectionTime) {
            minSelectionTime = selectionTime
        }

        time?.text = "${selectionTime} мс"
    }

    private fun updateStats() {
        val totalQuestions = correctCount + wrCount
        allCount?.text = totalQuestions.toString()
        rightCount?.text = correctCount.toString()
        wrongCount?.text = wrCount.toString()

        val percentageCorrect = String.format("%.2f",correctCount.toDouble() / totalQuestions * 100)

        procent?.text = "$percentageCorrect%"

        val maxTimeSeconds = maxSelectionTime
        maxTime?.text = "${maxTimeSeconds} mс"

        val minTimeSeconds = minSelectionTime
        minTime?.text = "${minTimeSeconds} mс"

        val avgSelectionTime =
            if (totalQuestions > 0) (totalTime.toDouble() / totalQuestions).toLong() else 0
        avgTime?.text = "${avgSelectionTime} mс"
    }
    private fun resetButtons() {
        startButton?.isEnabled = true
        rightButton?.isEnabled = false
        wrongButton?.isEnabled = false
    }
}
