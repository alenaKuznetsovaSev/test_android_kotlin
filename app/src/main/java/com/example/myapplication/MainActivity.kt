package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.round
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    companion object{
        var response = """aa{"rates":{"CAD":1.5347,"HKD":8.7607,"ISK":152.1,"PHP":56.763,"DKK":7.4551,"HUF":346.0,"CZK":26.698,"AUD":1.6447,"RON":4.8343,"SEK":10.5103,"IDR":16058.24,"INR":85.7875,"BRL":5.7388,"RUB":78.7662,"HRK":7.566,"JPY":121.26,"THB":34.986,"CHF":1.0697,"SGD":1.5718,"PLN":4.4484,"BGN":1.9558,"TRY":7.7224,"CNY":7.999,"NOK":10.8475,"NZD":1.7553,"ZAR":19.2794,"USD":1.1304,"MXN":25.445,"ILS":3.9189,"GBP":0.89653,"KRW":1360.45,"MYR":4.824},"base":"EUR","date":"2020-06-12"}"""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var firstEdit = true

        val start_currency_field = findViewById<EditText>(R.id.start_currency_field)
        val final_currency_field = findViewById<EditText>(R.id.final_currency_field)

        val start_currency_spinner = findViewById<Spinner>(R.id.start_currency_spinner)
        val final_currency_spinner = findViewById<Spinner>(R.id.final_currency_spinner)

//        val handler = Handler()
//
//        val r: Runnable = object : Runnable {
//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun run() {
//                response = sendGet("https://api.exchangeratesapi.io/latest")
//                println("response in run-" + response)
//            }
//        }
//        r.run()
//        println("main response-" + response)

        val  currencies = HashMap<String, Double>()
        val listOfKeys = object : ArrayList<String>(){}
        response = response.split("{")[2].split("}")[0]
        val pairs:List<String> = response.split(",")
        pairs.forEach { item ->
            val currency = item.split(":")[0].substring(1, 4)
            val value = item.split(":")[1].toDouble()
            currencies[currency] = value
            listOfKeys.add(currency)
        }
        currencies["EUR"] = 1.0



        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, listOfKeys
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        start_currency_spinner.setAdapter(adapter)
        final_currency_spinner.setAdapter(adapter)

        start_currency_spinner.setSelection(listOfKeys.indexOf("RUB"))
        final_currency_spinner.setSelection(listOfKeys.indexOf("USD"))

        start_currency_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
               start_currency_field.setText(start_currency_field.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        final_currency_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                final_currency_field.setText(final_currency_field.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        start_currency_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(chs: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(chs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(firstEdit) {
                    firstEdit = false
                    if (chs.toString() == "")
                        final_currency_field.setText("")
                    else {
                        val selected_start: String = start_currency_spinner.getSelectedItem().toString()
                        val selected_final: String = final_currency_spinner.getSelectedItem().toString()
                        val numb = chs.toString().toDouble()/ currencies[selected_start] !!* currencies[selected_final]!!
                        val res = round(numb*100)/100.0
                        final_currency_field.setText(res.toString())
                    }
                    firstEdit = true
                }
            }
        })
        final_currency_field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(chs: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(chs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(firstEdit) {
                    firstEdit = false
                    if (chs.toString() == "") {
                        start_currency_field.setText("")
                    } else {
                        val selected_start: String = start_currency_spinner.getSelectedItem().toString()
                        val selected_final: String = final_currency_spinner.getSelectedItem().toString()
                        val numb = chs.toString().toDouble()/ currencies[selected_final]!! * currencies[selected_start]!!
                        val res = round(numb*100)/100.0
                        start_currency_field.setText(res.toString())
                    }
                    firstEdit = true
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGet(link: String): String {
        val url = URL(link)
        var res: String = ""
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
//                    Toast.makeText(applicationContext, line, Toast.LENGTH_LONG).show()
                    res += line
                }
            }
        }
        return res
    }

}
