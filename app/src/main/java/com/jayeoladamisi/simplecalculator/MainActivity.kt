package com.jayeoladamisi.simplecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var resultShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)

        // Number buttons
        val numberButtons = listOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to "."
        )
        for ((id, value) in numberButtons) {
            findViewById<Button>(id).setOnClickListener { appendInput(value) }
        }

        // Basic operator buttons
        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("/") }

        // Equals and Clear
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }

        // Scientific buttons
        findViewById<Button>(R.id.btnSin).setOnClickListener { applyScientific("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { applyScientific("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { applyScientific("tan") }
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { applyScientific("sqrt") }
        findViewById<Button>(R.id.btnLog).setOnClickListener { applyScientific("log") }
        findViewById<Button>(R.id.btnLn).setOnClickListener { applyScientific("ln") }

        // Two-number scientific buttons
        findViewById<Button>(R.id.btnPerm).setOnClickListener { applyTwoArg("nPr") }
        findViewById<Button>(R.id.btnComb).setOnClickListener { applyTwoArg("nCr") }
    }

    // ----------- BASIC FUNCTIONS -----------

    private fun appendInput(value: String) {
        if (resultShown) { currentInput = ""; resultShown = false }
        if (value == "." && currentInput.contains(".")) return
        currentInput += value
        tvDisplay.text = currentInput
    }

    private fun setOperator(op: String) {
        if (currentInput.isEmpty()) return
        firstNumber = currentInput.toDouble()
        operator = op
        currentInput = ""
    }

    // ⬇️ THIS IS THE calculate() FUNCTION YOU WERE LOOKING FOR
    private fun calculate() {
        if (currentInput.isEmpty() || operator.isEmpty()) return
        val secondNumber = currentInput.toDouble()
        val result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else {
                tvDisplay.text = "Error: ÷0"
                return
            }
            "nPr" -> {
                val n = firstNumber.toInt()
                val r = secondNumber.toInt()
                factorial(n).toDouble() / factorial(n - r).toDouble()
            }
            "nCr" -> {
                val n = firstNumber.toInt()
                val r = secondNumber.toInt()
                factorial(n).toDouble() / (factorial(r).toDouble() * factorial(n - r).toDouble())
            }
            else -> 0.0
        }
        val display = if (result == result.toLong().toDouble())
            result.toLong().toString() else result.toString()
        tvDisplay.text = display
        currentInput = display
        operator = ""
        resultShown = true
    }

    private fun clear() {
        currentInput = ""
        operator = ""
        firstNumber = 0.0
        resultShown = false
        tvDisplay.text = "0"
    }

    // ----------- SCIENTIFIC FUNCTIONS -----------

    private fun applyScientific(func: String) {
        val value = currentInput.toDoubleOrNull() ?: return
        val result = when (func) {
            "sin"  -> sin(Math.toRadians(value))
            "cos"  -> cos(Math.toRadians(value))
            "tan"  -> tan(Math.toRadians(value))
            "sqrt" -> if (value >= 0) sqrt(value) else { tvDisplay.text = "Error"; return }
            "log"  -> if (value > 0) log10(value) else { tvDisplay.text = "Error"; return }
            "ln"   -> if (value > 0) ln(value) else { tvDisplay.text = "Error"; return }
            else   -> return
        }
        val display = "%.6f".format(result).trimEnd('0').trimEnd('.')
        tvDisplay.text = display
        currentInput = display
        resultShown = true
    }

    private fun applyTwoArg(func: String) {
        if (currentInput.isNotEmpty() && operator.isEmpty()) {
            firstNumber = currentInput.toDouble()
            operator = func
            currentInput = ""
            tvDisplay.text = "$func("
        }
    }

    private fun factorial(n: Int): Long {
        if (n < 0) return 0
        var result = 1L
        for (i in 2..n) result *= i
        return result
    }
}