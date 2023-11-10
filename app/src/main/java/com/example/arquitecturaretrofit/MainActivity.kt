package com.example.arquitecturaretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.arquitecturaretrofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CharacterListFragment.CharacterListFragmentInterface {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener {
            onGoBack()
        }
        setContentView(binding.root)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.add(binding.fragmentContainer.id, CharacterListFragment());
        fragmentTransaction.commit()
    }

    fun onGoBack(){
        supportFragmentManager.popBackStack()
        binding.backButton.visibility = View.INVISIBLE
    }

    override fun onGoToFullCharacter(character : Character) {
        binding.backButton.visibility = View.VISIBLE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, CharacterInfoFragment(character))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun toggleLoadingBar(){
        if(binding.indeterminateBar.isVisible){
            binding.indeterminateBar.visibility = View.INVISIBLE
        }else{
            binding.indeterminateBar.visibility = View.VISIBLE
        }
    }
}