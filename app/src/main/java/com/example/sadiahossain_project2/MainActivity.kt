package com.example.sadiahossain_project2

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var tipSeekBar: SeekBar
    private lateinit var percentageTextView: TextView
    private lateinit var tipDescription: TextView
    private lateinit var tipAmount: TextView
    private lateinit var grandTotal: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        amountInput = findViewById(R.id.amountInput)
        tipSeekBar = findViewById(R.id.tipSeekBar)
        percentageTextView = findViewById(R.id.percentageTextView)
        tipDescription = findViewById(R.id.tipDescription)
        tipAmount = findViewById(R.id.tipAmount)
        grandTotal = findViewById(R.id.grandTotal)

        // Load saved tip percentage
        sharedPreferences = getSharedPreferences("TipCalculatorPrefs", MODE_PRIVATE)
        tipSeekBar.progress = sharedPreferences.getInt("TIP_PERCENTAGE", 20)

        // Set up listeners
        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedPreferences.edit().putInt("TIP_PERCENTAGE", progress).apply()
                updateUI()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        amountInput.addTextChangedListener { updateUI() }
        updateUI()
    }

    private fun updateUI() {
        val tipPercentage = tipSeekBar.progress
        val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
        val tip = calculateTip(amount, tipPercentage)
        val total = amount + tip

        percentageTextView.text = "$tipPercentage%"
        tipAmount.text = String.format("Tip: $%.2f", tip)
        grandTotal.text = String.format("Total: $%.2f", total)
        updateTipDescription(tipPercentage)
    }

    private fun calculateTip(amount: Double, percentage: Int): Double {
        return (amount * percentage) / 100
    }

    private fun updateTipDescription(tipPercentage: Int) {
        val (description, colorId) = when (tipPercentage) {
            in 0..9 -> "Poor" to R.color.red
            in 10..15 -> "OK" to R.color.orange
            in 16..20 -> "Good" to R.color.green
            in 21..25 -> "Great" to R.color.dark_green
            else -> "Awesome" to R.color.blue
        }
        tipDescription.text = description
        tipDescription.setTextColor(ContextCompat.getColor(this, colorId))
    }
}