package com.example.margajaya.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EmailCustom : AppCompatEditText {

    constructor(context: Context) : super(context) {

        init()

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        init()

    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailText = s.toString()

                when {
                    emailText.isEmpty() -> setError("email tidak boleh kosong")

                    !Patterns.EMAIL_ADDRESS.matcher(emailText).matches() -> {
                        setError("email harus aktif dan valid")
                    }

                    else -> error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


}