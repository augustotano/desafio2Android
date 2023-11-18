package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arquitecturaretrofit.databinding.FragmentLoginBinding
import com.example.arquitecturaretrofit.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private var listener : RegisterFragmentInterface? = null
    private var viewModel = CharacterViewModel()

    interface RegisterFragmentInterface{
        fun onRegister()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? RegisterFragmentInterface
        if(listener == null){
            throw ClassCastException("Listener needs to implement RegisterFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.btnRegister.setOnClickListener {
            onRegister()
        }
        return binding.root
    }

    fun onRegister(){
        listener?.onRegister()
    }
}