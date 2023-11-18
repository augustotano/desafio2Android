package com.example.arquitecturaretrofit

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arquitecturaretrofit.databinding.FragmentRecoveryBinding
import com.example.arquitecturaretrofit.databinding.FragmentRegisterBinding


class RecoveryFragment : Fragment() {
    private lateinit var binding : FragmentRecoveryBinding
    private var listener : RecoveryFragmentInterface? = null
    private var viewModel = CharacterViewModel()

    interface RecoveryFragmentInterface{
        fun onRecovery()
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
            onRecovery()
        }
        return binding.root
    }

    fun onRecovery(){
        listener?.onRecovery()
    }

}