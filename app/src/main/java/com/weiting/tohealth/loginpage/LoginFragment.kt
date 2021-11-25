package com.weiting.tohealth.loginpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentLoginBinding
import com.weiting.tohealth.factory.MainActivityViewModelFactory
import java.lang.Exception

class LoginFragment : Fragment() {

    companion object {
        const val RC_SIGN_IN = 9001
    }

    val viewModel by viewModels<LoginViewModel> { getVmLoginFactory() }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        val factory =
            MainActivityViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val mainActivityViewModel = ViewModelProvider(requireActivity(), factory).get(MainActivityViewModel::class.java)

        viewModel.userInfo.observe(viewLifecycleOwner) {
            UserManager.UserInfo = it
            mainActivityViewModel.loginSuccess()
            findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
        }

        binding.btLogin.setOnClickListener {
            getGoogleSignInPop()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Log.w("firebaseAuthWithGoogle:", e.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Log.d("firebaseAuthWithGoogle", "signInWithCredential:success")
                    val user = auth.currentUser
                    onGetUserFromFirebase(user)
                } else {
                    Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.exception)
                    onGetUserFromFirebase(null)
                }
            }
    }

    private fun getGoogleSignInPop() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun onGetUserFromFirebase(user: FirebaseUser?) {
        if (user != null) {
            viewModel.signInUserInfo(
                User(
                    name = user.displayName,
                    id = user.uid,
                    userPhoto = user.photoUrl.toString()
                )
            )
        }
    }
}
