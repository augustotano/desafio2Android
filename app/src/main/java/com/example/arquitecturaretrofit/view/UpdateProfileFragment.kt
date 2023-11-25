package com.example.arquitecturaretrofit.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.arquitecturaretrofit.databinding.FragmentUpdateProfileBinding
import com.example.arquitecturaretrofit.view.auth
import com.google.firebase.auth.userProfileChangeRequest


class UpdateProfileFragment : Fragment() {
    private lateinit var binding : FragmentUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater, container, false)
        binding.btnUpdate.setOnClickListener {
            val name = binding.dataName.text.toString()
            val photo = binding.dataphoto.text.toString()
            val user = auth.currentUser

            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = Uri.parse(photo)
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Update Sucess",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
        return binding.root
    }

    companion object {
        private const val TAG = "UpdateProfileFragment"
    }

}