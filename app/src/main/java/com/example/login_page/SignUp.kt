package com.example.login_page

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor =
            getColor(R.color.light_pink)

        setContentView(
            R.layout.activity_sign_up
        )

        auth = FirebaseAuth.getInstance()




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




        val name =
            findViewById<TextInputEditText>(
                R.id.tex_name
            )

        val email =
            findViewById<TextInputEditText>(
                R.id.tex_email
            )

        val password =
            findViewById<TextInputEditText>(
                R.id.tex_password
            )

        val confirmPassword =
            findViewById<TextInputEditText>(
                R.id.tex_comfrimPassword
            )


        val nameLayout =
            findViewById<TextInputLayout>(
                R.id.name_layout
            )

        val emailLayout =
            findViewById<TextInputLayout>(
                R.id.email_layout
            )

        val passwordLayout =
            findViewById<TextInputLayout>(
                R.id.password_layout
            )

        val confirmPasswordLayout =
            findViewById<TextInputLayout>(
                R.id.comfrimPassword_layout
            )


        val signUpButton =
            findViewById<Button>(
                R.id.btn_signUp
            )

        val goLogin =
            findViewById<LinearLayout>(
                R.id.go_login_screen
            )

        val backArrow =
            findViewById<ImageView>(
                R.id.back_arrow
            )




        name.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                nameLayout.error = null
            }
        }

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

        confirmPassword.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                confirmPasswordLayout.error = null
            }
        }






        signUpButton.setOnClickListener {

            val nameText =
                name.text.toString().trim()

            val emailText =
                email.text.toString().trim()

            val passwordText =
                password.text.toString()

            val confirmPasswordText =
                confirmPassword.text.toString()


            if (nameText.isEmpty()) {

                nameLayout.error =
                    "Enter your name"

                return@setOnClickListener
            }

            nameLayout.error = null


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


            if (passwordText.length < 6) {

                passwordLayout.error =
                    "Password must be 6 digits"

                return@setOnClickListener
            }

            passwordLayout.error = null


            if (confirmPasswordText.isEmpty()) {

                confirmPasswordLayout.error =
                    "Confirm your password"

                return@setOnClickListener
            }


            if (
                passwordText !=
                confirmPasswordText
            ) {

                confirmPasswordLayout.error =
                    "Password does not match"

                return@setOnClickListener
            }

            confirmPasswordLayout.error = null




            auth.createUserWithEmailAndPassword(
                emailText,
                passwordText
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    auth.signOut()

                    Toast.makeText(
                        this,
                        "Create your account successful",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(
                            this,
                            SignIn::class.java
                        )
                    )

                    finish()

                } else {

                    emailLayout.error =
                        task.exception?.message
                            ?: "Registration failed"
                }
            }
        }




        goLogin.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SignIn::class.java
                )
            )

            finish()
        }



        backArrow.setOnClickListener {

            finish()
        }
    }
}