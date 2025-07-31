package com.example.easyshop.viewModels

import androidx.lifecycle.ViewModel
import com.example.easyshop.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun signup(
        email: String,
        password: String,
        name: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var userId = it.result.user?.uid
                    val userModel = UserModel(name, email, userId.toString())
                    if (userId != null) {
                        firestore.collection("users").document(userId).set(userModel)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    onResult(true, null)
                                } else {
                                    onResult(false, dbTask.exception?.localizedMessage)
                                }
                            }
                    }
                } else {
                    onResult(false, it.exception?.localizedMessage)

                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if(it.isSuccessful){
                onResult(true,null)
            }else{
                onResult(false,it.exception?.localizedMessage)
            }
        }
    }

}
