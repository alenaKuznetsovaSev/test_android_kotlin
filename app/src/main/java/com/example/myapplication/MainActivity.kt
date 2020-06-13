package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    var firstEdit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val start_currency_field = findViewById<EditText>(R.id.start_currency_field)
        val final_currency_field = findViewById<EditText>(R.id.final_currency_field)

        val coef:Int = 74

        start_currency_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(chs: Editable?) {

                println("rub after")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("rub before ")
            }

            override fun onTextChanged(chs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(firstEdit) {
                    firstEdit = false
                    if (chs.toString() == "")
                        final_currency_field.setText("")
                    else {
                        println(chs.toString())
                        val res = String.format("%.2f", (chs.toString().toDouble() / coef))
                        println(res)
                        final_currency_field.setText(res)
                    }
                    firstEdit = true
                }
            }
        })

        final_currency_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(chs: Editable?) {
                println("usd after")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("usd before")
            }

            override fun onTextChanged(chs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println(chs)
                if(firstEdit) {
                    println("second")
                    firstEdit = false
                    if (chs.toString() == "") {
                        start_currency_field.setText("")
                    } else {
                        val rub = (chs.toString().toDouble() * coef)
                        start_currency_field.setText(String.format("%.2f", rub))
                    }
                    firstEdit = true
                }
            }
        })
    }
}
