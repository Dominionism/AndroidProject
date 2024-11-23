package edu.uw.ischool.kmuret.procrastinationpal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    lateinit var phoneText: TextView
    lateinit var intervalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        phoneText = findViewById(R.id.editTextPhone2)
        phoneText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        intervalText = findViewById(R.id.editTextNumber)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if(validate()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun validate () : Boolean {
        var valid = true
        if(phoneText.text.length != 14) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
            valid = false
        }
        if(intervalText.text.isEmpty()) {
            Toast.makeText(this, "Please Enter Time", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }
}