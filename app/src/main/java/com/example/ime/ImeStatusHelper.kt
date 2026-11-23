package com.example.ime

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager

data class ImeStatus(
    val isEnabled: Boolean,
    val isSelected: Boolean
)

object ImeStatusHelper {

    fun getStatus(context: Context): ImeStatus {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return ImeStatus(isEnabled = false, isSelected = false)

        val pkgName = context.packageName
        val enabled = imm.enabledInputMethodList.any { it.packageName == pkgName }

        val defaultIme = runCatching {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        }.getOrNull().orEmpty()

        val selected = defaultIme.startsWith("$pkgName/")
        return ImeStatus(isEnabled = enabled, isSelected = selected)
    }

    fun openInputMethodSettings(context: Context) {
        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }
    }

    fun showInputMethodPicker(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showInputMethodPicker()
    }
}
