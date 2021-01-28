package com.example.simpletip

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.simpletip.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //find and added listener for button
        binding.calculateButton.setOnClickListener { calculateTip() }
        //check bundle values
        if (savedInstanceState != null) {
            //check save visibility a value
            if (savedInstanceState.getBoolean("tip_amount_visible")) {
                binding.tipResult.visibility = View.VISIBLE
                binding.tipResult.text = savedInstanceState.getString("tip_amount")
            }
        }
    }

    private fun calculateTip() {

        //get value of a editText and cast toSting, cuz view return Editable
        val stringInTextField = binding.costOfServiceEditText?.text.toString()
        //get cost a value with null, cuz user can will enter empty a value
        val cost = stringInTextField.toDoubleOrNull()
        //return percent value and checked radiobutton
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.2
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        //if the cost is null or 0, then display 0 tip and exit this function early
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }
        //check round value
        val roundUp = binding.roundUpSwitch.isChecked
        var tip = tipPercentage * cost
        //set visibility of textView
        binding.tipResult.visibility = View.VISIBLE
        if (roundUp)
            tip = ceil(tip)
        displayTip(tip)
    }

    private fun displayTip(tip: Double) {
        //get formated value
        val formatedTip = NumberFormat.getCurrencyInstance().format(tip)
        //set result value
        binding.tipResult.text = getString(R.string.tip_amount, formatedTip)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //check value for save, if textView is a visibility, means can save it
        if (binding.tipResult.visibility == View.VISIBLE) {
            outState.putBoolean("tip_amount_visible", true)
            outState.putString("tip_amount", binding.tipResult.text.toString())
        }
    }
}



