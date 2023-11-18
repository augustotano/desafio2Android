package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arquitecturaretrofit.databinding.FragmentCharacterListBinding
import com.example.arquitecturaretrofit.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private var listener : LoginFragmentInterface? = null
    private var viewModel = CharacterViewModel()

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
        binding.btnLogin.setOnClickListener {
            onLogin()
        }
        binding.btnRegister.setOnClickListener {
            onLoginRegister()
        }
        binding.recoveryPassword.setOnClickListener {
            onLoginRecovery()
        }
        return binding.root
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



}