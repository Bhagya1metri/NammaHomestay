package com.example.nammahomestay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class AiActivity : AppCompatActivity() {

    // Your Gemini API Key
    private val apiKey = "AIzaSyAPxPxm71Sb9URdEUSHcP5Cw-oW8KCCTtc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)

        val questionEt = findViewById<EditText>(R.id.questionEt)
        val askBtn = findViewById<Button>(R.id.askBtn)
        val answerTv = findViewById<TextView>(R.id.answerTv)

        // Gemini AI Model
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey,
        )

        askBtn.setOnClickListener {
            val question = questionEt.text.toString().trim()

            if (question.isBlank()) {
                questionEt.error = getString(R.string.enter_question)
                return@setOnClickListener
            }

            // Show loading state
            answerTv.setText(R.string.ai_thinking)
            askBtn.isEnabled = false

            lifecycleScope.launch {
                try {
                    val prompt =
                        "You are a helpful travel assistant for the Namma HomeStay app. " +
                                "Answer this query clearly and concisely: $question"

                    val response = generativeModel.generateContent(prompt)
                    
                    runOnUiThread {
                        answerTv.text = response.text ?: getString(R.string.empty_response)
                        askBtn.isEnabled = true
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        answerTv.text = getString(R.string.ai_error, e.message)
                        askBtn.isEnabled = true
                    }
                }
            }
        }
    }
}