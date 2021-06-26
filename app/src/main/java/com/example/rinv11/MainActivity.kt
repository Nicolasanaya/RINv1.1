package com.example.rinv11

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        //Cambiar vista para fotomultas
//        val boton2=findViewById<Button>(R.id.btnRegistrate)
//        boton2.setOnClickListener {
//            val intento2 = Intent(this, FotoMulActivity::class.java)
//            startActivity(intento2)
//        }

        //Toast.makeText(applicationContext, "onCreate", Toast.LENGTH_SHORT).show()

        //Cambiar vista para fotomultas
//        val boton2=findViewById<ImageButton>(R.id.imageButton2)
//        boton2.setOnClickListener {
//            val intento2 = Intent(this, ActivityFotomultas::class.java)
//            startActivity(intento2)
//        }

    }

//cambiar a iniciar sesion
        public fun ImageButton_Click(view: View) {

            val intento1 = Intent(this, IniciarActivity::class.java)
            startActivity(intento1)

    }

    //cambiar a Fotomul
    public fun ButtonFotomul_Click(view: View) {

        val intento2 = Intent(this, FotoMulActivity::class.java)
        startActivity(intento2)

    }



}