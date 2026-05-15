package com.example.nammahomestay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class AiActivity : AppCompatActivity() {

    private val apiKey = "AIzaSyAPxPxm71Sb9URdEUSHcP5Cw-oW8KCCTtc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)

        val questionEt = findViewById<EditText>(R.id.questionEt)
        val askBtn = findViewById<Button>(R.id.askBtn)
        val answerTv = findViewById<TextView>(R.id.answerTv)

        askBtn.setOnClickListener {

            val question = questionEt.text.toString()

            val client = OkHttpClient()

            val json = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "You are a travel assistant for homestay users. Answer this clearly: $question"
                        }
                      ]
                    }
                  ]
                }
            """.trimIndent()

            val body = RequestBody.create(
                "application/json".toMediaType(),
                json
            )

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=$apiKey")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                    runOnUiThread {
                        answerTv.text = e.message
                    }
                }

                override fun onResponse(call: Call, response: Response) {

                    val responseBody = response.body?.string()

                    try {

                        val jsonObject = JSONObject(responseBody)

                        val text = jsonObject
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        runOnUiThread {
                            answerTv.text = text
                        }

                    } catch (e: Exception) {

                        runOnUiThread {
                            answerTv.text = responseBody
                        }
                    }
                }
            })
        }
    }
}