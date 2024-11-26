package edu.uw.ischool.kmuret.procrastinationpal

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class Motivation : AppCompatActivity() {
    private lateinit var quoteTextView: TextView
    private lateinit var quoteAuthorView: TextView
    private lateinit var newQuoteButton: Button


    private val motivationalQuotes = listOf(
        Quote(
            "Procrastination is like a credit card: it's a lot of fun until you get the bill.",
            "Anonymous Procrastinator"
        ),
        Quote(
            "You don't have to see the whole staircase, just take the first step.",
            "Martin Luther King Jr."
        ),
        Quote(
            "Studying is not the time to Netflix and chill. It's time to Netflix and skill!",
            "Productivity Guru"
        ),
        Quote(
            "Your future self will thank you for the work you do today.",
            "Wise Mentor"
        ),
        Quote(
            "Homework won't do itself. (But wouldn't that be nice?)",
            "Realistic Student"
        ),
        Quote(
            "Success is not final, failure is not fatal: it is the courage to continue that counts.",
            "Winston Churchill"
        ),
        Quote("Education is the passport to the future, for tomorrow belongs to those who prepare for it today.", "Malcolm X"),
        Quote("The future depends on what you do today.", "Mahatma Gandhi"),
        Quote("Study hard what interests you the most in the most undisciplined, irreverent, and original manner possible.", "Richard Feynman"),
        Quote("Live as if you were to die tomorrow. Learn as if you were to live forever.", "Mahatma Gandhi"),
        Quote("The capacity to learn is a gift; the ability to learn is a skill; the willingness to learn is a choice.", "Brian Herbert"),
        Quote("Intelligence is the ability to adapt to change.", "Stephen Hawking"),
        Quote("Your education is a dress rehearsal for a life that is yours to lead.", "Nora Ephron"),
        Quote("The learning process continues until the day you die.", "Kirk Douglas"),
        Quote("An investment in knowledge pays the best interest.", "Benjamin Franklin"),
        Quote("The beautiful thing about learning is that no one can take it away from you.", "B.B. King"),
        Quote("Procrastination is the art of keeping up with yesterday.", "Don Marquis"),
        Quote("Procrastination is the thief of time.", "Edward Young"),
        Quote("You don't have to see the whole staircase, just take the first step.", "Martin Luther King Jr."),
        Quote("Productivity is never an accident. It is always a result of a commitment to excellence, intelligent planning, and focused effort.", "Paul J. Meyer"),
        Quote("The best way to get started is to quit talking and begin doing.", "Walt Disney"),
        Quote("Someday is not a day of the week.", "Janet Dailey"),
        Quote("Procrastination makes easy things hard and hard things harder.", "Mason Cooley"),
        Quote("You may delay, but time will not.", "Benjamin Franklin"),
        Quote("Procrastination is the grave in which opportunity is buried.", "Unknown"),
        Quote("The next best time to plant a tree was 20 years ago. The next best time is now.", "Chinese Proverb"),
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill"),
        Quote("It always seems impossible until it's done.", "Nelson Mandela"),
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote("Don't watch the clock; do what it does. Keep going.", "Sam Levenson"),
        Quote("Success is walking from failure to failure with no loss of enthusiasm.", "Winston Churchill"),
        Quote("Your attitude, not your aptitude, will determine your altitude.", "Zig Ziglar"),
        Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt"),
        Quote("Hard work beats talent when talent doesn't work hard.", "Tim Notke"),
        Quote("The only limit to our realization of tomorrow will be our doubts of today.", "Franklin D. Roosevelt"),
        Quote("Smart people learn from everything and everyone, average people from their experiences, stupid people already have all the answers.", "Socrates"),
        Quote("I have never let my schooling interfere with my education.", "Mark Twain"),
        Quote("The mind is not a vessel to be filled, but a fire to be kindled.", "Plutarch"),
        Quote("Education is not the filling of a pot but the lighting of a fire.", "W.B. Yeats"),
        Quote("Learning never exhausts the mind.", "Leonardo da Vinci"),
        Quote("Knowledge is power. Information is liberating.", "Kofi Annan"),
        Quote("The more that you read, the more things you will know. The more that you learn, the more places you'll go.", "Dr. Seuss"),
        Quote("Anyone who stops learning is old, whether at twenty or eighty.", "Henry Ford"),
        Quote("Learning is a treasure that will follow its owner everywhere.", "Chinese Proverb"),
        Quote("Education is the most powerful weapon which you can use to change the world.", "Nelson Mandela"),
        Quote("I'm not lazy, I'm just on energy-saving mode.", "Unknown"),
        Quote("I put the 'pro' in procrastination.", "Unknown"),
        Quote("My bed is a magical place where I suddenly remember everything I was supposed to do.", "Unknown"),
        Quote("Studying is not the time to Netflix and chill. It's time to Netflix and skill!", "Productivity Guru"),
        Quote("Homework won't do itself. (But wouldn't that be nice?)", "Realistic Student"),
        Quote("Success is not how high you have climbed, but how you make a positive difference to the world.", "Roy T. Bennett"),
        Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius"),
        Quote("Our greatest glory is not in never falling, but in rising every time we fall.", "Confucius"),
        Quote("The only way to achieve the impossible is to believe it is possible.", "Charles Kingsleigh"),
        Quote("Time is what we want most, but what we use worst.", "William Penn"),
        Quote("You can have it all. Just not all at once.", "Oprah Winfrey"),
        Quote("Lost time is never found again.", "Benjamin Franklin"),
        Quote("The best revenge is massive success.", "Frank Sinatra"),
        Quote("Your time is limited, don't waste it living someone else's life.", "Steve Jobs"),
        Quote("The only person you are destined to become is the person you decide to be.", "Ralph Waldo Emerson"),
        Quote("Creativity is intelligence having fun.", "Albert Einstein"),
        Quote("Logic will get you from A to B. Imagination will take you everywhere.", "Albert Einstein"),
        Quote("Fall seven times and stand up eight.", "Japanese Proverb"),
        Quote("When you feel like quitting, remember why you started.", "Unknown"),
        Quote("Technology is best when it brings people together.", "Matt Mullenweg"),
        Quote("The most important skill in the future will be the ability to learn and relearn.", "Alvin Toffler"),
        Quote("You are never too old to set another goal or to dream a new dream.", "C.S. Lewis"),
        Quote("Strive not to be a success, but rather to be of value.", "Albert Einstein")
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motivation)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        quoteTextView = findViewById(R.id.motivationQuote)
        quoteAuthorView = findViewById(R.id.quoteAuthor)
        newQuoteButton = findViewById(R.id.newQuoteButton)

        setRandomQuote()

        newQuoteButton.setOnClickListener {
            setRandomQuote()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRandomQuote() {
        val randomQuote = motivationalQuotes.random()
        quoteTextView.text = "\"${randomQuote.text}\""
        quoteAuthorView.text = "- ${randomQuote.author}"
    }
}