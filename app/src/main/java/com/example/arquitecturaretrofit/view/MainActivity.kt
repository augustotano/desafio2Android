package com.example.arquitecturaretrofit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.arquitecturaretrofit.R
import com.example.arquitecturaretrofit.databinding.ActivityMainBinding
import com.example.arquitecturaretrofit.model.Character
import com.example.arquitecturaretrofit.model.Comic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

public lateinit var auth: FirebaseAuth
class MainActivity : AppCompatActivity(), CharacterListFragment.CharacterListFragmentInterface,
    CharacterInfoFragment.CharacterInfoFragmentInterface,
    ComicInfoFragment.ComicInfoFragmentInterface,
    ComicListFragment.ComicListFragmentInterface,
    LoginFragment.LoginFragmentInterface,
    RegisterFragment.RegisterFragmentInterface,
    RecoveryFragment.RecoveryFragmentInterface {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.apply {
            setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_arrow_back_black_24)
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onSupportNavigateUp(): Boolean {
        onGoBack()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if(auth.currentUser != null){
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.main_menu, menu)
        } else{
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.simple_menu, menu)
        }
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.profile -> {
                onGoToProfile()
                true
            }
            R.id.updateProfile -> {
                onGoToUpdateProfile()
                true
            }
            R.id.signOut -> {
                auth.signOut()
                onGoLogin()
                setSupportActionBar(findViewById(R.id.my_toolbar))
                supportActionBar?.apply {
                    setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_arrow_back_black_24)
                }
                true
            }
            R.id.favorites -> {
                onGoToFavorites()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onStart() {
        super.onStart()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val currentUser = auth.currentUser
        //si el usuario inicio sesion es no null
        if (currentUser != null) {
            fragmentTransaction.add(binding.fragmentContainer.id, CharacterListFragment());
        }
        else {
            fragmentTransaction.add(binding.fragmentContainer.id, LoginFragment());
        }
        fragmentTransaction.commit()
    }

    fun onGoBack(){
        supportFragmentManager.popBackStack()
        if(supportFragmentManager.backStackEntryCount == 1){
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onGoToFullCharacter(character : Character) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, CharacterInfoFragment(character))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onGoToFullComic(comic: Comic) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, ComicInfoFragment(comic))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    override fun onLogin() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, CharacterListFragment())
        fragmentTransaction.commit()
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.apply {
            setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_arrow_back_black_24)
        }
        toggleLoadingBar(true)
    }

    override fun onGoLogin() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, LoginFragment())
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onLoginRegister() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, RegisterFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onLoginRecovery() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, RecoveryFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun toggleLoadingBar(active : Boolean){
        if(active){
            binding.indeterminateBar.visibility = View.VISIBLE
        }else{
            binding.indeterminateBar.visibility = View.INVISIBLE
        }
    }

    override fun goToAllComics(comicId: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, ComicListFragment(comicId))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun goToAllCharacters(comicId: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, CharacterListFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }
    fun onGoToFavorites(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, FavoriteListFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    fun onGoToProfile(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, SeePerfileFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    fun onGoToUpdateProfile(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, UpdateProfileFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
