package uz.ttpu.composearchlab.ui.signin

// A sealed interface is better than an enum because each state can carry different data
// (e.g. Error holds a message String, SignedIn holds an email String), which enums cannot do.
sealed interface SignInUiState {
    data object SignedOut : SignInUiState
    data object InProgress : SignInUiState
    data class Error(val message: String) : SignInUiState
    data class SignedIn(val email: String) : SignInUiState
}