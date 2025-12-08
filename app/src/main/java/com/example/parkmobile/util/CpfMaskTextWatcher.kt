package com.example.parkmobile.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CpfMaskTextWatcher(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "###.###.###-##"

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) {
            return
        }

        isUpdating = true

        val unmasked = s.toString().filter { it.isDigit() }
        var masked = ""
        var maskIndex = 0
        var unmaskedIndex = 0

        while (maskIndex < mask.length && unmaskedIndex < unmasked.length) {
            if (mask[maskIndex] == '#') {
                masked += unmasked[unmaskedIndex]
                unmaskedIndex++
            } else {
                masked += mask[maskIndex]
            }
            maskIndex++
        }

        editText.setText(masked)
        editText.setSelection(masked.length)

        isUpdating = false
    }
}
