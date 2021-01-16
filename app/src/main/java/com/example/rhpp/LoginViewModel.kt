package com.example.rhpp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel : ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState : StateFlow<LoginUiState> = _loginUiState
    private val db = FirebaseFirestore.getInstance()
    init {
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
           }

    fun login(username : String, jbtn : String, password : String) = viewModelScope.launch {
        _loginUiState.value = LoginUiState.Loading
           delay(2000L)



        db.collection("users")
                .whereEqualTo("nama",username)
                .whereEqualTo("jabatan",jbtn)
                .whereEqualTo("password",password)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        _loginUiState.value = LoginUiState.Error("wrong crudential")
                        return@addSnapshotListener
                    }
                    querySnapshot?.let {
                        _loginUiState.value = LoginUiState.Success
                    }
                }
    }
    sealed class LoginUiState{
        object Success : LoginUiState()
        data class Error (val message:String): LoginUiState()
        object Loading : LoginUiState()
        object Empty : LoginUiState()
    }
}