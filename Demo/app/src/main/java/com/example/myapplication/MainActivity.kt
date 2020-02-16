package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.MalformedURLException
import java.net.URL
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<Messages>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ReadJson().execute("https://api.androidhive.info/mail/inbox.json")
    }

    private inner class ReadJson : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg strings: String): String {
            val content = StringBuilder()
            try {
                val url = URL(strings[0])
                val inputStreamReader = InputStreamReader(url.openConnection().getInputStream())
                val bufferedReader = BufferedReader(inputStreamReader)
                var line: String? = ""
                while ({ line = bufferedReader.readLine(); line }() != null) {
                    content.append(line)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return content.toString()
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            val books = java.util.ArrayList<Messages>()
            try {
                val json = JSONObject(s)
                val jsonArray = json.getJSONArray("messages")
                for (i in 0..jsonArray.length() - 1) {
                    val JSONObject = jsonArray.getJSONObject(i)
                    var id: String = JSONObject.getString("id")
                    var from: String = JSONObject.getString("from")
                    var email: String = JSONObject.getString("email")
                    var subject: String = JSONObject.getString("subject")
                    var message: String = JSONObject.getString("message")
                    var date: String = JSONObject.getString("date")
                    books.add(Messages(id,from,email,subject,message,date))
                }
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = Adapter(this@MainActivity,books)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerview.setLayoutManager(LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
