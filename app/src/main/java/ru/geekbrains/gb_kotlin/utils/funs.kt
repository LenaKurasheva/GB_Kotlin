package ru.geekbrains.gb_kotlin.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

// функция - расширение класса EditText: после изменения текста вызывается afterTextChanged()
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable.toString())
        }
    })
}