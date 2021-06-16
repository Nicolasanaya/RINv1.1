package com.example.rinv11

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Cambiar vista para iniciar sesion

//        val boton1=findViewById<ImageButton>(R.id.imageButton)
//        boton1.setOnClickListener {
//            val intento1 = Intent(this, ActivityIniciar::class.java)
//            startActivity(intento1)
//        }

        //Cambiar vista para fotomultas
        val boton2=findViewById<ImageButton>(R.id.btnRegistrate)
        boton2.setOnClickListener {
            val intento2 = Intent(this, FotoMulActivity::class.java)
            startActivity(intento2)
        }

        //Toast.makeText(applicationContext, "onCreate", Toast.LENGTH_SHORT).show()

        //Cambiar vista para fotomultas
//        val boton2=findViewById<ImageButton>(R.id.imageButton2)
//        boton2.setOnClickListener {
//            val intento2 = Intent(this, ActivityFotomultas::class.java)
//            startActivity(intento2)
//        }

    }

//    override fun onStart() {
//        super.onStart()
//        Toast.makeText(applicationContext, "onStart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(applicationContext, "onResume", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Toast.makeText(applicationContext, "onPause", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Toast.makeText(applicationContext, "onStop", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Toast.makeText(applicationContext, "onRestart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
//
//    }

    public fun ImageButton_Click(view: View) {

            val intento1 = Intent(this, IniciarActivity::class.java)
            startActivity(intento1)

    }



}