package com.example.arquitecturaretrofit.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arquitecturaretrofit.R
import com.example.arquitecturaretrofit.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private lateinit var binding : FragmentLoginBinding
    private var listener : LoginFragmentInterface? = null

    interface LoginFragmentInterface{
        fun onLogin()
        fun onLoginRegister()
        fun onLoginRecovery()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? LoginFragment.LoginFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement LoginFragment")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        binding.btnRegister.setOnClickListener {
            onLoginRegister()
        }
        binding.recoveryPassword.setOnClickListener {
            onLoginRecovery()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val email = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            signIn(email, password)

        }
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply{
            title = resources.getString(R.string.login_fragment_title)
            setDisplayHomeAsUpEnabled(false)
        }
    }


    fun onLogin(){
        listener?.onLogin()
    }

    fun onLoginRegister(){
        listener?.onLoginRegister()
    }

    fun onLoginRecovery(){
        listener?.onLoginRecovery()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.usernameInput.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.usernameInput.error = "Required."
            valid = false
        } else {
            binding.usernameInput.error = null
        }

        val password = binding.passwordInput.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordInput.error = "Required."
            valid = false
        } else {
            binding.passwordInput.error = null
        }

        return valid
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        context,
                        "Success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    onLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}