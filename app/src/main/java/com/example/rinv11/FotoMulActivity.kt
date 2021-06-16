package com.example.rinv11

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.activity_fotomul.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


class FotoMulActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    private val REQUEST_CAMARE = 1
    private val REQUEST_GALERY = 2
    var foto: Uri? = null
    var listDescripcion: MutableList<String> = mutableListOf<String>()
    var listCodigo: MutableList<String> = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotomul)
        abrirGaleria_Click()
        abrirCamara_Click()
        LlamadoMarcas()
        LlamadosCodigos()
        Enviopost()

    }


    private fun Enviopost(){

        val url = "https://apir.apiupbateneo.xyz/Multas/Multa"
        val texto = findViewById<TextView>(R.id.textTituloRegistrar)

        val placa = findViewById<EditText>(R.id.editTextPlaca)
        val nplaca = placa.text

        val marca = findViewById<Spinner>(R.id.spnMarca)


        val multa = findViewById<Spinner>(R.id.spnCodigoMulta)
        multa.onItemSelectedListener. toString()

        val descripcioncodigo = findViewById<TextView>(R.id.textDescripcioncodigo)
        val ndescripcion = descripcioncodigo.text

        val observacion = findViewById<EditText>(R.id.editTextObservaciones)
        val nobservacion = observacion.text

        val jsonObject = JSONObject()
        jsonObject.put("placa",nplaca)
        jsonObject.put("marca",marca)
        jsonObject.put("codigo",multa)
        jsonObject.put("Descripcion",ndescripcion)
        jsonObject.put("observacion",nobservacion)


        buttonprueba.setOnClickListener(){
            texto.text = (jsonObject.toString())

        }

        //texto.text = (jsonObject.toString())




//        val queue = Volley.newRequestQueue(this)
//
//        val stringRequest = StringRequest(Request.Method.POST,url,Response.Listener {
//                response ->
//            val jsonObject = JSONObject()
//            jsonObject.put("multa",placa)
//        }, { Toast.makeText(applicationContext, "algo salio mal", Toast.LENGTH_SHORT).show() })
    }

    private fun LlamadosCodigos(){
        val spiner1 = findViewById<Spinner>(R.id.spnCodigoMulta)
        spiner1.onItemSelectedListener = this

        val url1 = "https://apir.apiupbateneo.xyz/Multas/Codigos"

        //val txtDescripcion = findViewById<EditText>(R.id.editTextDescripcion)


        val queue = Volley.newRequestQueue(this)

        val stringRequest1 = StringRequest(Request.Method.GET,url1, {
                response ->  val klaxonC = Klaxon().parseArray<codigoMulta>(response)

            //var listDescripcion: MutableList<String> = mutableListOf<String>()

            if (klaxonC != null) {
                listCodigo.add("Seleccione esta mierda")
                listDescripcion.add("")
                for (element in klaxonC){
                    listCodigo.add(element.codigo)
                    listDescripcion.add(element.descripcion)
                }
                spiner1.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,listCodigo)
            }

        }, { Toast.makeText(applicationContext, "algo salio mal", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest1)

    }

    private fun LlamadoMarcas(){
        val spiner = findViewById<Spinner>(R.id.spnMarca)
        val url = "https://apir.apiupbateneo.xyz/Multas/Marcas"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET,url, {
                response ->  val klaxon = Klaxon().parseArray<Marca>(response)
            var list: MutableList<String> = mutableListOf<String>()
            if (klaxon != null) {
                list.add("Seleccione esta mierda")
                for (element in klaxon){
                    list.add(element.nombre)
                }
                spiner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,list)
            }
        }, { Toast.makeText(applicationContext, "algo salio mal", Toast.LENGTH_SHORT).show()})

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != null){
            textDescripcioncodigo.text = listDescripcion.set(position,"")
        }
        //Toast.makeText(applicationContext, "${onItemSelected()}", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}