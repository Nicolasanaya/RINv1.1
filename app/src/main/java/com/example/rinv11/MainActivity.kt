package com.example.rinv11

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Cambiar vista para iniciar sesion
        val boton1=findViewById<ImageButton>(R.id.imageButton)
        boton1.setOnClickListener {
            val intento1 = Intent(this, ActivityIniciar::class.java)
            startActivity(intento1)
        }
        //Cambiar vista para fotomultas
        val boton2=findViewById<ImageButton>(R.id.imageButton2)
        boton2.setOnClickListener {
            val intento2 = Intent(this, ActivityFotoMul::class.java)
            startActivity(intento2)
        }

        //Cambiar vista para fotomultas
//        val boton2=findViewById<ImageButton>(R.id.imageButton2)
//        boton2.setOnClickListener {
//            val intento2 = Intent(this, ActivityFotomultas::class.java)
//            startActivity(intento2)
//        }



    }


}