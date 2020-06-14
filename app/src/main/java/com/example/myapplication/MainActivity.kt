package com.example.myapplication

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.R
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {


    companion object{
        var response = """{"rates":{"CAD":1.5347,"HKD":8.7607,"ISK":152.1,"PHP":56.763,"DKK":7.4551,"HUF":346.0,"CZK":26.698,"AUD":1.6447,"RON":4.8343,"SEK":10.5103,"IDR":16058.24,"INR":85.7875,"BRL":5.7388,"RUB":78.7662,"HRK":7.566,"JPY":121.26,"THB":34.986,"CHF":1.0697,"SGD":1.5718,"PLN":4.4484,"BGN":1.9558,"TRY":7.7224,"CNY":7.999,"NOK":10.8475,"NZD":1.7553,"ZAR":19.2794,"USD":1.1304,"MXN":25.445,"ILS":3.9189,"GBP":0.89653,"KRW":1360.45,"MYR":4.824},"base":"EUR","date":"2020-06-12"}"""
    }

    var firstEdit = true

    data class Currency(val title: String, val value:Double)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(
            arrayOf(
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            ), 1
        )
        setContentView(R.layout.activity_main)

        val start_currency_field = findViewById<EditText>(R.id.start_currency_field)
        val final_currency_field = findViewById<EditText>(R.id.final_currency_field)
        var start_currency_spinner = findViewById<Spinner>(R.id.start_currency_spinner);
        var final_currency_spinner = findViewById<Spinner>(R.id.final_currency_spinner);

//        doAsync{
//            response = sendGet("https://api.exchangeratesapi.io/latest")
//                .split("{")[2].split("}")[0]
//            println("response-" + response)
//            writeFileOnInternalStorage("data.txt",response)
//            println("response-" + response)
//        }.execute()

//        var red = readFile("data.txt")
//        println(red)
//        Log.d("My app", response)


        var  currencies = HashMap<String, Double>()

        response = response.split("{")[2].split("}")[0]
        val pairs:List<String> = response.split(",")
        pairs.forEach { item ->
            val currency = item.split(":")[0].toString()
            val value = item.split(":")[1].toDouble()
            currencies[currency] = value
        }
        currencies["EUR"] = 1.0


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
                        val numb =  0 // chs.toString().toDouble()/ currencies[selected_start] !!* currencies[selected_final]!!
                        val res = String.format("%.2f", numb)
                        final_currency_field.setText(res)
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
                        val numb = 0 // chs.toString().toDouble()/ currencies[selected_final]!! * currencies[selected_start]!!
                        val res = String.format("%.2f", numb)
                        start_currency_field.setText(res)
                    }
                    firstEdit = true
                }
            }
        })
    }

    fun readFile(fileName: String):String{
        var text = ""
        File(fileName).forEachLine { text += it }
        return text
    }

    fun writeFileOnInternalStorage(sFileName: String, text: String?) {
        try {
            val writer = FileWriter(File(sFileName))
            writer.append(text)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    class doAsync(val handler: () -> Unit) : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg p0: Void?): Void? {
            handler()
            return null
        }

    }



}
