package com.example.ime

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.data.KeyboardLayoutSettings
import com.example.data.KeyboardSettingsRepository
import com.example.data.OutputMode
import com.example.ui.keyboard.ScientificKeyboardView
import com.example.ui.theme.NablaKeysTheme
import kotlinx.coroutines.launch

class NablaInputMethodService :
    InputMethodService(),
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    private lateinit var settingsRepository: KeyboardSettingsRepository

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = store

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        settingsRepository = KeyboardSettingsRepository(applicationContext)
    }

    override fun onCreateInputView(): View {
        val decorView = window?.window?.decorView
        decorView?.setViewTreeLifecycleOwner(this)
        decorView?.setViewTreeViewModelStoreOwner(this)
        decorView?.setViewTreeSavedStateRegistryOwner(this)

        return ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@NablaInputMethodService)
            setViewTreeViewModelStoreOwner(this@NablaInputMethodService)
            setViewTreeSavedStateRegistryOwner(this@NablaInputMethodService)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val settings by settingsRepository.settingsFlow.collectAsState(
                    initial = KeyboardLayoutSettings()
                )
                var activeDeck by remember(settings.defaultDeck) {
                    mutableStateOf(settings.defaultDeck)
                }
                val scope = rememberCoroutineScope()

                NablaKeysTheme(darkTheme = true) {
                    ScientificKeyboardView(
                        settings = settings,
                        activeDeck = activeDeck,
                        onSelectDeck = { activeDeck = it },
                        onInsertText = { text ->
                            currentInputConnection?.commitText(text, 1)
                        },
                        onBackspace = { handleBackspace() },
                        onEnter = { handleEnter() },
                        onMoveCursor = { delta ->
                            val keyCode = if (delta < 0) KeyEvent.KEYCODE_DPAD_LEFT else KeyEvent.KEYCODE_DPAD_RIGHT
                            sendDownUpKeyEvents(keyCode)
                        },
                        onToggleOutputMode = {
                            scope.launch {
                                val next = if (settings.outputMode == OutputMode.UNICODE) {
                                    OutputMode.LATEX
                                } else {
                                    OutputMode.UNICODE
                                }
                                settingsRepository.setOutputMode(next)
                            }
                        },
                        showImeSwitchKey = true,
                        onSwitchIme = {
                            runCatching { switchToNextInputMethod(false) }
                        },
                        applyNavigationBarPadding = true
                    )
                }
            }
        }
    }

    override fun onWindowShown() {
        super.onWindowShown()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onWindowHidden() {
        super.onWindowHidden()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
        super.onDestroy()
    }

    private fun handleBackspace() {
        val ic = currentInputConnection ?: return
        val selectedText = ic.getSelectedText(0)
        if (!selectedText.isNullOrEmpty()) {
            ic.commitText("", 1)
            return
        }
        val before = ic.getTextBeforeCursor(2, 0)
        if (!before.isNullOrEmpty()) {
            val lastChar = before[before.length - 1]
            val deleteCount = if (Character.isLowSurrogate(lastChar) && before.length >= 2) 2 else 1
            ic.deleteSurroundingText(deleteCount, 0)
        } else {
            sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL)
        }
    }

    private fun handleEnter() {
        val ic = currentInputConnection ?: return
        val info = currentInputEditorInfo
        val actionId = info?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_NONE
        val isMultiLine = (info?.inputType?.and(android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE) ?: 0) != 0

        if (!isMultiLine && actionId != EditorInfo.IME_ACTION_NONE && actionId != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(actionId)
        } else {
            ic.commitText("\n", 1)
        }
    }
}
