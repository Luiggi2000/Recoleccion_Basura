package com.example.miprimerapractica

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.miprimerapractica.ui.fragments.DashboardFragment
import com.example.miprimerapractica.ui.fragments.ReportFragment
import com.example.miprimerapractica.ui.fragments.TaskManagementFragment
import com.google.android.gms.maps.model.Dash
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance() // Inicialización de Firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Configurar los elementos del diseño
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<MaterialButton>(R.id.loginbtn)
        val registerButton = view.findViewById<MaterialButton>(R.id.registerbtn)

        // Configurar las acciones de los botones
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(email, password)        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val db = FirebaseFirestore.getInstance()

                    if (user != null) {
                        // Utilizamos el UID del usuario autenticado para obtener los datos del usuario en Firestore
                        val userRef = db.collection("users").document(user.uid)

                        userRef.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // Obtenemos los datos del usuario
                                    val nombres = document.getString("nombres")
                                    val apellidos = document.getString("apellidos")
                                    val rol = document.getString("rol")

                                    Toast.makeText(requireContext(), "Bienvenido, $nombres $apellidos", Toast.LENGTH_SHORT).show()

                                    // Redirigir según el rol
                                    when (rol) {
                                        "admin" -> {
                                            replaceFragment(DashboardFragment())
                                        }
                                        "limpiador" -> {
                                            replaceFragment(TaskManagementFragment())
                                        }
                                        "common" -> {
                                            replaceFragment(ReportFragment())
                                        }
                                        else -> {
                                            replaceFragment(ReportFragment())
                                        }
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "No se encontraron detalles del usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(requireContext(), "Error al obtener datos del usuario: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_graph, fragment) // Asegúrate de que fragment_container es el ID del contenedor
        transaction.addToBackStack(null) // Opcional si deseas que se pueda volver al fragmento anterior
        transaction.commit()
    }



    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}