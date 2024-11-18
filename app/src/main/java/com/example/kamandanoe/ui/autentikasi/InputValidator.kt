package com.example.kamandanoe.ui.autentikasi

import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class InputValidator {

    fun validateUsername(edUsername : EditText, layUser: TextInputLayout): Boolean{
        return if (edUsername.text.toString().trim().isEmpty()){
            layUser.error = "Username tidak boleh kosong"
            false
        }else {
            layUser.error = null
            true
        }
    }
    fun validateEmail(edEmail: EditText, layEmail: TextInputLayout): Boolean {
        return when {
            edEmail.text.toString().trim().isEmpty() -> {
                layEmail.error = "Email tidak boleh kosong"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(edEmail.text.toString().trim()).matches() -> {
                layEmail.error = "Email harus unik dan valid"
                false
            }
            else -> {
                layEmail.error = null
                true
            }
        }
    }
    fun validatePassword(edPass: EditText, layPass: TextInputLayout): Boolean {
        return when {
            edPass.text.toString().trim().isEmpty() -> {
                layPass.error = "Password tidak boleh kosong"
                false
            }
            edPass.text.toString().trim().length < 8 -> {
                layPass.error = "Password harus lebih dari 8 karakter"
                false
            }
            else -> {
                layPass.error = null
                true
            }
        }
    }
    fun validateTel(edTel: EditText, layTel: TextInputLayout): Boolean {
        return if (edTel.text.toString().trim().isEmpty()) {
            layTel.error = "Nomor telephone tidak boleh kosong"
            false
        } else {
            layTel.error = null
            true
        }
    }
    fun validateConfirm(edCon: EditText, layCon: TextInputLayout, edPass: EditText): Boolean {
        return when {
            edCon.text.toString().trim().isEmpty() -> {
                layCon.error = "Password tidak boleh kosong"
                false
            }
            edCon.text.toString().trim().length < 8 -> {
                layCon.error = "Password harus lebih dari 8 karakter"
                false
            }
            edCon.text.toString().trim() != edPass.text.toString().trim() -> {
                layCon.error = "Password tidak sama"
                false
            }
            else -> {
                layCon.error = null
                true
            }
        }
    }
}