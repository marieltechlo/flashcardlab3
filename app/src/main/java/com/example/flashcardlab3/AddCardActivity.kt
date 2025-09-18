package com.example.flashcardlab3


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.flashcard_app)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.cancel).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.save).setOnClickListener {

            val question=findViewById<EditText>(R.id.editTextQuestion).text.toString()
            val answer=findViewById<EditText>(R.id.editTextAnswer).text.toString()

            val data=Intent()
            data.putExtra("question",question)
            data.putExtra("answer", answer)

            setResult(RESULT_OK, data)

            finish()
        }
    }
}