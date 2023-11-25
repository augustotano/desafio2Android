package com.example.arquitecturaretrofit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.arquitecturaretrofit.databinding.FragmentFavoriteListBinding
import com.example.arquitecturaretrofit.databinding.FragmentSeePerfileBinding


class SeePerfileFragment : Fragment() {
    private lateinit var binding : FragmentSeePerfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeePerfileBinding.inflate(layoutInflater, container, false)

        val user = auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            binding.dataEmail.text = email
            binding.dataName.text = name
            binding.photo.load("${photoUrl}")
        }

        return binding.root
    }



}