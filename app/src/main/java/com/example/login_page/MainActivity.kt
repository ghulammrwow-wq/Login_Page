package com.example.login_page

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = getColor(R.color.light_pink)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences =
            getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        val isLoggedIn =
            sharedPreferences.getBoolean("isLoggedIn", false)


        if (!isLoggedIn || auth.currentUser == null) {

            sharedPreferences.edit {
                putBoolean("isLoggedIn", false)
            }

            startActivity(
                Intent(this, SignIn::class.java)
            )

            finish()
            return
        }


        setContentView(R.layout.activity_main)

        val logoutIcon =
            findViewById<ImageView>(R.id.logout_icon)

        logoutIcon.setOnClickListener {

            auth.signOut()

            sharedPreferences.edit {
                putBoolean("isLoggedIn", false)
            }

            Toast.makeText(this, "Logout Your Account", Toast.LENGTH_LONG).show()

            val intent =
                Intent(this, SignIn::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }
}