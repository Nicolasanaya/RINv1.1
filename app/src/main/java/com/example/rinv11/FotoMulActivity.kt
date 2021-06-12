package com.example.rinv11

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_fotomul.*

class FotoMulActivity : AppCompatActivity() {

    private val REQUEST_CAMARE = 1
    private val REQUEST_GALERY = 2



    var foto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotomul)
        abrirGaleria_Click()
        abrirCamara_Click()
        pruebavolley()

    }


    private fun pruebavolley(){
        val textoresultado = findViewById<TextView>(R.id.textTituloRegistrar)
        val url = "https://apir.apiupbateneo.xyz/Multas/Marcas"
       //val url = "https://apir.apiupbateneo.xyz/ping"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET,url,Response.Listener { response -> textTituloRegistrar.text = "La respuesta  es:${response}" },
            Response.ErrorListener { textTituloRegistrar.text = "algo salio mal" })

//        val stringRequest = StringRequest(Request.Method.GET,url,Response.Listener {  },
//            Response.ErrorListener {  })

        //val klaxon = Klaxon().parseArray<Marca>(stringRequest.toString())

        //textTituloRegistrar.text = klaxon.toString()

//
//        val stringRequest = StringRequest(Request.Method.GET,url,Response.Listener {  },
//            Response.ErrorListener {  })


//        val gson = Gson()
//        val json:String = stringRequest.toString()
//
//        val marca:Marca = gson.fromJson(json,Marca::class.java)
//        textoresultado.text = marca.toString()


        queue.add(stringRequest)
    }


    private fun muestragaleria(){
        val intentGaleria = Intent(Intent.ACTION_PICK)
        intentGaleria.type = "image/*"
        startActivityForResult(intentGaleria, REQUEST_GALERY)
       //Toast.makeText(applicationContext,"AQUI VAN LAS IMAGENES",Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            REQUEST_GALERY ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    muestragaleria()
                else
                    Toast.makeText(applicationContext, "NO PUEDE ACCEDER A TUS IMAGENES",Toast.LENGTH_SHORT).show()
            }
            REQUEST_CAMARE ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abrecamara()
                else
                    Toast.makeText(applicationContext, "No se pudo cargar la camara", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun abrirGaleria_Click(){
        btnGaleria.setOnClickListener(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permisoarchivos = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permisoarchivos,REQUEST_GALERY)
                }else{
                    muestragaleria()
                }
            }else{
                muestragaleria()
            }
        }

    }

    private fun abrirCamara_Click() {
        //
        btnCamara.setOnClickListener(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permisocamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permisocamara,REQUEST_CAMARE)
                }else
                    abrecamara()

            }else{
                abrecamara()
            }
        }


    }

    private fun abrecamara(){
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Nueva Imagen")
        foto = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, foto)
        startActivityForResult(camaraIntent,REQUEST_CAMARE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALERY){
            imgFoto.setImageURI(data?.data)
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMARE){
            imgFoto.setImageURI(foto!!)
        }
    }

}