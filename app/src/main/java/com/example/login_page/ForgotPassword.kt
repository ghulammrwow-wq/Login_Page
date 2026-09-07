package com.example.login_page

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor =
            getColor(R.color.light_pink)

        setContentView(
            R.layout.activity_forgot_password
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

        val email =
            findViewById<TextInputEditText>(
                R.id.forgot_email
            )

        val emailLayout =
            findViewById<TextInputLayout>(
                R.id.forgot_email_layout
            )

        val sendOtp =
            findViewById<MaterialButton>(
                R.id.btn_send_otp
            )

        val backArrow =
            findViewById<ImageView>(
                R.id.back_arrow
            )

        email.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                emailLayout.error = null
            }
        }





        backArrow.setOnClickListener {

            finish()
        }


        sendOtp.setOnClickListener {

            val emailText =
                email.text.toString().trim()


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

            checkEmailAndShowOtp(
                emailText,
                emailLayout,
                sendOtp
            )
        }
    }


    private fun checkEmailAndShowOtp(
        email: String,
        emailLayout: TextInputLayout,
        sendOtp: MaterialButton
    ) {

        sendOtp.isEnabled = false

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->

                sendOtp.isEnabled = true

                if (task.isSuccessful) {

                    val signInMethods =
                        task.result?.signInMethods


                    if (!signInMethods.isNullOrEmpty()) {

                        emailLayout.error = null

                        showOtpDialog(email)

                    } else {


                        emailLayout.error =
                            "Your email is not found. Please correct email."
                    }

                } else {

                    emailLayout.error =
                        "Your email is not found. Please correct email."
                }
            }
    }


    private fun showOtpDialog(
        email: String
    ) {

        val view =
            layoutInflater.inflate(
                R.layout.dialog_otp,
                null
            )

        val otp1 =
            view.findViewById<EditText>(
                R.id.otp_1
            )

        val otp2 =
            view.findViewById<EditText>(
                R.id.otp_2
            )

        val otp3 =
            view.findViewById<EditText>(
                R.id.otp_3
            )

        val otp4 =
            view.findViewById<EditText>(
                R.id.otp_4
            )

        val otp5 =
            view.findViewById<EditText>(
                R.id.otp_5
            )

        val otp6 =
            view.findViewById<EditText>(
                R.id.otp_6
            )

        val otpFields =
            arrayOf(
                otp1,
                otp2,
                otp3,
                otp4,
                otp5,
                otp6
            )

        val dialog =
            AlertDialog.Builder(this)
                .setTitle("OTP Verification")
                .setMessage(
                    "Enter the 6 digit OTP"
                )
                .setView(view)
                .setCancelable(false)
                .create()

        dialog.show()


        for (i in otpFields.indices) {

            otpFields[i].doAfterTextChanged {

                if (
                    otpFields[i].text.length == 1
                ) {


                    otp6.error = null


                    if (i < otpFields.lastIndex) {

                        otpFields[i + 1]
                            .requestFocus()

                    } else {


                        val otp =
                            otpFields.joinToString("") {
                                it.text.toString()
                            }


                        if (otp == "786786") {

                            Toast.makeText(
                                this,
                                "OTP correct",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()


                            sendPasswordResetEmail(
                                email
                            )

                        } else {


                            otp6.error =
                                "Please enter correct OTP"
                        }
                    }
                }
            }
        }


        for (i in otpFields.indices) {

            otpFields[i].setOnKeyListener {
                    _, keyCode, event ->

                if (
                    keyCode ==
                    KeyEvent.KEYCODE_DEL &&
                    event.action ==
                    KeyEvent.ACTION_DOWN &&
                    otpFields[i].text.isEmpty() &&
                    i > 0
                ) {

                    otpFields[i - 1]
                        .requestFocus()

                    otpFields[i - 1]
                        .setSelection(
                            otpFields[i - 1]
                                .text.length
                        )
                }

                false
            }
        }


        otp1.requestFocus()

        dialog.window?.setSoftInputMode(
            android.view.WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
    }


    private fun sendPasswordResetEmail(
        email: String
    ) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Password reset link sent to your email",
                        Toast.LENGTH_LONG
                    ).show()


                    val intent =
                        Intent(
                            this,
                            SignIn::class.java
                        )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Unable to send password reset email",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}