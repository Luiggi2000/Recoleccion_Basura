package com.example.miprimerapractica

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar FirebaseAuth y Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setContentView(R.layout.activity_main)
/*
        // Configurar NavController para manejar navegación desde nav_graph
        val navController =
            findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()

 */
        enableEdgeToEdge()


        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val loginFragment = LoginFragment()

            // Reemplazar el fragmento en el contenedor de FrameLayout
            fragmentTransaction.replace(R.id.nav_graph, loginFragment)
            fragmentTransaction.commit()
        }}

    }



}
