package com.example.arquitecturaretrofit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arquitecturaretrofit.databinding.FragmentRecoveryBinding
import com.example.arquitecturaretrofit.databinding.FragmentRegisterBinding
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await


class RecoveryFragment : Fragment() {
    private lateinit var binding : FragmentRecoveryBinding
    private var listener : RecoveryFragmentInterface? = null

    interface RecoveryFragmentInterface{
        fun onGoLogin()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? RecoveryFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement RecoveryFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecoveryBinding.inflate(layoutInflater, container, false)
        binding.btnRecovery.setOnClickListener {
            val email = binding.usernameInput.text.toString()
            resetPassword(email)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.recovery_fragment_title)
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#407bb9"))
        )
    }

    fun onRecovery(){
        listener?.onGoLogin()
    }

    private fun resetPassword(email: String){

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Email sent",
                        Toast.LENGTH_SHORT,
                    ).show()
                    onRecovery()
                }
                else{
                    Toast.makeText(
                        context,
                        "Reset failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


    companion object {
        private const val TAG = "RecoveryFragment"
    }

}