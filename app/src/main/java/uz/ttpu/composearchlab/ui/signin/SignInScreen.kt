package uz.ttpu.composearchlab.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SignInRoute(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState = viewModel.uiState.value
    val snackbarHostState = remember { SnackbarHostState() }

    // Task 8 & 9: Showing error effect and consuming it via onErrorShown()
    LaunchedEffect(uiState) {
        if (uiState is SignInUiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
            // Fix: Notify ViewModel that the snackbar was shown so state resets to SignedOut.
            // Without this call, rotating the device re-triggers the Snackbar because
            // the ViewModel retains the Error state across screen rotations.
            viewModel.onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        SignInScreen(
            uiState = uiState,
            onSignIn = viewModel::onSignIn,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onSignIn: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is SignInUiState.SignedIn -> {
                Text("Welcome, ${uiState.email}")
            }
            else -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSignIn(email, password) },
                    enabled = uiState !is SignInUiState.InProgress,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign in")
                }

                if (uiState is SignInUiState.InProgress) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}

// Previews for all 4 UI states
@Preview(showBackground = true)
@Composable
fun PreviewSignedOut() {
    SignInScreen(uiState = SignInUiState.SignedOut, onSignIn = { _, _ -> })
}

@Preview(showBackground = true)
@Composable
fun PreviewInProgress() {
    SignInScreen(uiState = SignInUiState.InProgress, onSignIn = { _, _ -> })
}

@Preview(showBackground = true)
@Composable
fun PreviewError() {
    SignInScreen(uiState = SignInUiState.Error("Wrong email or password"), onSignIn = { _, _ -> })
}

@Preview(showBackground = true)
@Composable
fun PreviewSignedIn() {
    SignInScreen(uiState = SignInUiState.SignedIn("test@example.com"), onSignIn = { _, _ -> })
}