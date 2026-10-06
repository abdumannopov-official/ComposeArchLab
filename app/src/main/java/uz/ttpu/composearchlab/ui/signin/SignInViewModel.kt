package uz.ttpu.composearchlab.ui.signin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    // _uiState is private and mutable so only the ViewModel can modify it (encapsulation).
    // uiState is public and read-only (State), preserving Unidirectional Data Flow.
    private val _uiState = mutableStateOf<SignInUiState>(SignInUiState.SignedOut)
    val uiState: State<SignInUiState> get() = _uiState

    fun onSignIn(email: String, password: String) {
        // Protect against double-tapping while a request is already running
        if (_uiState.value is SignInUiState.InProgress) return

        _uiState.value = SignInUiState.InProgress

        viewModelScope.launch {
            delay(1500) // Simulate network delay
            if (password == "kotlin123") {
                _uiState.value = SignInUiState.SignedIn(email)
            } else {
                _uiState.value = SignInUiState.Error("Wrong email or password")
            }
        }
    }

    fun onErrorShown() {
        if (_uiState.value is SignInUiState.Error) {
            _uiState.value = SignInUiState.SignedOut
        }
    }
}