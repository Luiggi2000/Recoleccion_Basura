package com.example.miprimerapractica

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de FirebaseApp solo si no está ya inicializado
            FirebaseApp.initializeApp(this)  // Debe ser llamado solo una vez


        setContentView(R.layout.activity_main)
        enableEdgeToEdge()


        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val loginFragment = LoginFragment()

            // Reemplazar el fragmento en el contenedor de FrameLayout
            fragmentTransaction.replace(R.id.fragment_container, loginFragment)
            fragmentTransaction.commit()
        }}

}