package com.example.flashcardlab3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    lateinit var flashcardQuestion : TextView
    lateinit var flashcardAnswer   : TextView

    lateinit var flashcardDatabase: FlashcardDatabase

    //declare a variable to hold a list of Flashcards like so
    var allFlashcards = mutableListOf<Flashcard>()

   var  currentCardDisplayedIndex=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        flashcardQuestion=findViewById<TextView>(R.id.flashcard_question)
        flashcardAnswer=findViewById<TextView>(R.id.flashcard_answer)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }


        flashcardQuestion.setOnClickListener {

            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }



        flashcardAnswer.setOnClickListener {

            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
        }




        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            // Launch EndingActivity with the resultLauncher so we can execute more code
            // once we come back here from EndingActivity
            resultLauncher.launch(intent)
        }

        findViewById<View>(R.id.next_button).setOnClickListener {
            // don't try to go to next card if you have no cards to begin with
            if (allFlashcards.size == 0) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }

            // advance our pointer index so we can show the next card
            currentCardDisplayedIndex++

            // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.flashcard_question), // This should be the TextView for displaying your flashcard question
                    "Vous avez atteint la derniere carte, retournez.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }

            // set the question and answer TextViews with data from the database
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<TextView>(R.id.flashcard_answer).text = answer
            findViewById<TextView>(R.id.flashcard_question).text = question
        }

    }
    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // This code is executed in StartingActivity after we come back from EndingActivity

        // This extracts any data that was passed back from EndingActivity
        val data: Intent? = result.data
        val extras=data?.extras
        // ToDo: Execute more code here
        if (extras != null) { // Check that we have data returned
            val question = extras.getString("question")
            val answer = extras.getString("answer")

            // Log the value of the strings for easier debugging
            Log.i("MainActivity", "question: $question")
            Log.i("MainActivity", "answer: $answer")

            // Display newly created flashcard
            findViewById<TextView>(R.id.flashcard_question).text = question
            findViewById<TextView>(R.id.flashcard_answer).text = answer

            // Save newly created flashcard to database
            if (question != null && answer != null) {
                flashcardDatabase.insertCard(Flashcard(question, answer))
                // Update set of flashcards to include new card
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            } else {
                Log.e("TAG", "Missing question or answer to input into database. Question is $question and answer is $answer")
            }
        } else {
            Log.i("MainActivity", "Returned null data from AddCardActivity")
        }
    }




}