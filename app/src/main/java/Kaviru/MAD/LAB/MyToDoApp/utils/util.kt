package Kaviru.MAD.LAB.MyToDoApp.utils

import com.google.android.material.textfield.TextInputLayout
import android.app.Dialog
import android.widget.Toast
import android.content.Context
import android.widget.LinearLayout
import android.view.View
import android.widget.EditText
import android.view.inputmethod.InputMethodManager

enum class StatusResult{
    Added,
    Updated,
    Deleted
}
enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}



fun Context.longToastShow(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun Context.hideKeyBoard(view : View){
    try {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }catch (e: Exception){
        e.printStackTrace()
    }
}

fun Dialog.setupDialog(layoutResId: Int){
    setContentView(layoutResId)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    setCancelable(false)
}


fun clearEditText(editText: EditText, textTextInputLayout: TextInputLayout) {
    editText.text = null
    textTextInputLayout.error = null
}

fun validateEditText(editText: EditText, textTextInputLayout: TextInputLayout): Boolean {
    return when {
        editText.text.toString().trim().isEmpty() -> {
            textTextInputLayout.error = "Required"
            false
        }
        else -> {
            textTextInputLayout.error = null
            true
        }
    }
}