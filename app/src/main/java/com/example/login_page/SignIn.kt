package com.example.login_page

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        window.statusBarColor =
            getColor(R.color.light_pink)



        auth = FirebaseAuth.getInstance()



        val sharedPreferences =
            getSharedPreferences(
                "LoginPrefs",
                MODE_PRIVATE
            )

        val isLoggedIn =
            sharedPreferences.getBoolean(
                "isLoggedIn",
                false
            )


        if (isLoggedIn) {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finish()

            return
        }



        setContentView(
            R.layout.activity_sign_in
        )



        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }



        val email =
            findViewById<TextInputEditText>(
                R.id.login_email
            )



        val password =
            findViewById<TextInputEditText>(
                R.id.login_password
            )



        val emailLayout =
            findViewById<TextInputLayout>(
                R.id.login_email_layout
            )


        val passwordLayout =
            findViewById<TextInputLayout>(
                R.id.login_password_layout
            )



        val loginButton =
            findViewById<Button>(
                R.id.btn_login
            )



        val goSignup =
            findViewById<LinearLayout>(
                R.id.go_signup_screen
            )



        val forgotPassword =
            findViewById<TextView>(
                R.id.forgot_password
            )



        email.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                emailLayout.error = null
            }
        }



        password.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                passwordLayout.error = null
            }
        }




        loginButton.setOnClickListener {

            val emailText =
                email.text
                    .toString()
                    .trim()

            val passwordText =
                password.text
                    .toString()



            if (emailText.isEmpty()) {

                emailLayout.error =
                    "Enter your email"

                return@setOnClickListener
            }



            if (
                !Patterns.EMAIL_ADDRESS
                    .matcher(emailText)
                    .matches()
            ) {

                emailLayout.error =
                    "Email address is badly formatted"

                return@setOnClickListener
            }

            emailLayout.error = null



            if (passwordText.isEmpty()) {

                passwordLayout.error =
                    "Enter your password"

                return@setOnClickListener
            }


            if (passwordText.length != 6) {

                passwordLayout.error =
                    "Password must be 6 digits"

                return@setOnClickListener
            }

            passwordLayout.error = null



            loginButton.isEnabled = false

            auth.signInWithEmailAndPassword(
                emailText,
                passwordText
            ).addOnCompleteListener { task ->

                loginButton.isEnabled = true

                if (task.isSuccessful) {



                    sharedPreferences.edit {
                        putBoolean(
                            "isLoggedIn",
                            true
                        )
                    }

                    Toast.makeText(
                        this,
                        "Successfully logged in",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )

                    finish()

                } else {

                    emailLayout.error =
                        "Invalid email or password"
                }
            }
        }



        goSignup.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignUp::class.java
                )
            )
        }



        forgotPassword.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ForgotPassword::class.java
                )
            )
        }
    }
}