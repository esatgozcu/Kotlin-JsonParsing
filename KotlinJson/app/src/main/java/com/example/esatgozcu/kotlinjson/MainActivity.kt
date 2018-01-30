package com.example.esatgozcu.kotlinjson

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // Getir butonuna tıklayınca..
    fun button(view: View)
    {
        val downloadData= Download()
        try{

            // Veriyi çekeceğimiz url.
            val url ="http://api.fixer.io/latest?base="
            // Kullanıcının sonuna gireceği değer.
            val chosenBase = editText.text.toString()
            // Kullanıcının verisini link ile birleştiriyoruz.
            downloadData.execute(url+chosenBase)

        }catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
    inner class Download : AsyncTask<String,Void,String>(){
        // Arka planda gerçekleşecek işlemler..
        override fun doInBackground(vararg params: String?): String {

            var result = ""
            val url: URL
            val httpURLConnection: HttpURLConnection

            try {

                // Kullanıcının editText'te veri girmesi ile elde ettiğimiz url'deki bütün
                // verileri karakter karakter çekiyoruz
                url = URL(params[0])
                // Url ile bağlantı kuruyoruz.
                httpURLConnection = url.openConnection() as HttpURLConnection
                // Url'deki verileri çekebilmek için InputStream oluşturuyoruz.
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character
                    // Karakteri bir ileriye taşıyoruz.
                    data = inputStreamReader.read()
                }
                return result
            } catch (e: Exception) {
                e.printStackTrace()
                return result
            }
        }
        // Arka plandaki işlemler bittikten sonra buraya geliyoruz.
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {

                // Elde ettiğimiz data'yı json formatına dönüştürüyoruz.
                val jsonObject = JSONObject(result)
                val rates = jsonObject.getString("rates")
                val jsonObject2 = JSONObject(rates)
                // Json formatına dönüştürdükten sonra verimizi çekiyoruz.
                val TL = jsonObject2.getString("TRY")

                textView.setText("TRY : $TL")
            }
            catch (e :Exception)
            {
                e.printStackTrace()
            }
        }
    }
}
