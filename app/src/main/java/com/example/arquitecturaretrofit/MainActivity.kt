package com.example.arquitecturaretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.arquitecturaretrofit.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

public lateinit var auth: FirebaseAuth
class MainActivity : AppCompatActivity(), CharacterListFragment.CharacterListFragmentInterface,
    CharacterInfoFragment.CharacterInfoFragmentInterface,
    ComicListFragment.ComicListFragmentInterface,
    LoginFragment.LoginFragmentInterface, 
    RegisterFragment.RegisterFragmentInterface, 
    RecoveryFragment.RecoveryFragmentInterface{

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener {
            onGoBack()
        }
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }


    public override fun onStart() {
        super.onStart()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val currentUser = auth.currentUser
        //si el usuario inicio sesion es no null
        if (currentUser != null) {
            fragmentTransaction.add(binding.fragmentContainer.id, CharacterListFragment());
        }
        else
        {
            fragmentTransaction.add(binding.fragmentContainer.id, LoginFragment());
        }
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

    override fun onGoToFullComic(comic: Comic) {
        TODO("Not yet implemented")
    }
    
    override fun onLogin() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, CharacterListFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        toggleLoadingBar()
    }

    override fun onGoLogin() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, LoginFragment())
        fragmentTransaction.commit()
        toggleLoadingBar()
    }

    override fun onLoginRegister() {
        binding.backButton.visibility = View.VISIBLE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, RegisterFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        toggleLoadingBar()
    }

    override fun onLoginRecovery() {
        binding.backButton.visibility = View.VISIBLE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, RecoveryFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        toggleLoadingBar()
    }

    override fun toggleLoadingBar(){
        if(binding.indeterminateBar.isVisible){
            binding.indeterminateBar.visibility = View.INVISIBLE
        }else{
            binding.indeterminateBar.visibility = View.VISIBLE
        }
    }

    override fun goToAllComics(comicId: Int) {
        binding.backButton.visibility = View.VISIBLE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, ComicListFragment(comicId))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
