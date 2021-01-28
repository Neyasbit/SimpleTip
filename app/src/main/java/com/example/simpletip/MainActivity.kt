package com.example.simpletip

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
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

        //find and added listener for a button
        binding.calculateButton.setOnClickListener { calculateTip() }
        //set listener for the editText
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)}
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
    //fun for hide the keyboard
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            //hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
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



