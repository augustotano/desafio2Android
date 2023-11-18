package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.util.Log
import com.example.arquitecturaretrofit.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private var listener: RegisterFragmentInterface? = null
    private lateinit var auth: FirebaseAuth

    interface RegisterFragmentInterface {
        fun onRegister()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? RegisterFragmentInterface
        if (listener == null) {
            throw ClassCastException("Listener needs to implement RegisterFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            Log.d(TAG, "createAccount:$email")
            Log.d(TAG, "createAccount:$password")
            createAccount(email, password)

        }

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

        fun onRegister() {
        listener?.onRegister()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
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
    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        context,
                        "Sucess",
                        Toast.LENGTH_SHORT,
                    ).show()
                    onRegister()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Revise el formato del mail o si la contraseña cumple medidas de seguridad",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }

            }
    }

    //verifica que el usuario haya accedido
    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    companion object {
        private const val TAG = "RegisterFragment"
    }
}