package com.example.rinv11

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.activity_fotomul.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


class FotoMulActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    private val REQUEST_CAMARE = 1
    private val REQUEST_GALERY = 2
    var fotoformulario: Uri? = null
    var listDescripcion: MutableList<String> = mutableListOf<String>()
    var listCodigo: MutableList<String> = mutableListOf<String>()
//    var list: MutableList<String> = mutableListOf<String>()
    //val Prueba = findViewById<TextView>(R.id.textDescripcioncodigo)
    val jsonObject:Multa = Multa("","","","","")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotomul)

        abrirGaleria_Click()
        abrirCamara_Click()
//        LlamadoMarcas()
        LlamadosCodigos()
        Enviopost()

    }

    //Metodo para mostrar la galeria y tomar foto
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
        fotoformulario = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoformulario)
        startActivityForResult(camaraIntent,REQUEST_CAMARE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALERY){
            fotoformulario = data?.data
            imgFoto.setImageURI(fotoformulario!!)
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMARE){
            imgFoto.setImageURI(fotoformulario!!)
        }
    }

    // llamados para los spinner
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
                listCodigo.add("Seleccione una opcion")
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

//    private fun LlamadoMarcas(){
//        val spiner = findViewById<Spinner>(R.id.spnMarca)
//        val url = "https://apir.apiupbateneo.xyz/Multas/Marcas"
//
//        val queue = Volley.newRequestQueue(this)
//
//        val stringRequest = StringRequest(Request.Method.GET,url, {
//                response ->  val klaxon = Klaxon().parseArray<Marca>(response)
////            var list: MutableList<String> = mutableListOf<String>()
//            if (klaxon != null) {
//                list.add("Seleccione una opcion")
//                for (element in klaxon){
//                    list.add(element.nombre)
//                }
//                spiner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,list)
//            }
//        }, { Toast.makeText(applicationContext, "algo salio mal", Toast.LENGTH_SHORT).show()})
//
//        queue.add(stringRequest)
//
//    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if (position != null){
//            textDescripcioncodigo.text = listDescripcion.set(position,"")
//        }
        //Toast.makeText(applicationContext, "${onItemSelected()}", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    // Armado de json
    private fun Enviopost(){

        val texto = findViewById<TextView>(R.id.textTituloRegistrar)

        val placas = findViewById<EditText>(R.id.editTextPlaca)
        val nplaca = placas.text
        jsonObject.placa = nplaca.toString()

//        val marca = findViewById<Spinner>(R.id.spnMarca)
//        //val marcaa:String = marca.onItemClickListener.toString()
//        marca.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//                val item = parent.getItemAtPosition(pos)
//                jsonObject.marca = item.toString()
//
//                //Toast.makeText(applicationContext, "$item", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }

        val multa = findViewById<Spinner>(R.id.spnCodigoMulta)
        multa.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                jsonObject.codigo = item.toString()
                if (pos != null){
                    textDescripcioncodigo.text = listDescripcion.set(pos,"")
                }
                jsonObject.estado = textDescripcionCodigo.toString()

                //Toast.makeText(applicationContext, "$item", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val descripcioncodigo = findViewById<TextView>(R.id.textDescripcioncodigo)
        val ndescripcion = descripcioncodigo.text

        val observacion = findViewById<EditText>(R.id.editTextObservaciones)
        val nobservacion = observacion.text

        jsonObject.observacion = nobservacion.toString()


//        buttonprueba.setOnClickListener(){
//            texto.text = (jsonObject.toString())
//
//        }

//        val queue = Volley.newRequestQueue(this)
//
//        val stringRequest = StringRequest(Request.Method.POST,url,Response.Listener {
//                response ->
//            val jsonObject = JSONObject()
//            jsonObject.put("multa",placa)
//        }, { Toast.makeText(applicationContext, "algo salio mal", Toast.LENGTH_SHORT).show() })
    }
    //Post
//    fun btnPrueba_ClickUri(view: View?){
//        val foto: URI = URI(fotoformulario?.scheme,fotoformulario?.schemeSpecificPart,fotoformulario?.fragment)
//        Toast.makeText(applicationContext,foto.toString(), Toast.LENGTH_SHORT).show()
//
////    }
    fun btnPrueba_Click(view: View?){
        //val foto: URI = URI("file:/" + fotoformulario?.scheme,fotoformulario?.schemeSpecificPart,fotoformulario?.fragment)
        //val foto: URI = URI.create("file:/" + fotoformulario?.path)
        val client = OkHttpClient()
        val direccionfoto = getRealPathFromURI(applicationContext,fotoformulario!!)
        jsonObject.placa = editTextPlaca.text.toString()
//        jsonObject.estado = textDescripcionCodigo.text.toString()
        jsonObject.observacion = editTextObservaciones.text.toString()
//        jsonObject.marca = spnMarca.toString()

        val requesBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("placa",jsonObject.placa)
//            .addFormDataPart("marca",jsonObject.marca)
            .addFormDataPart("codigo",jsonObject.codigo)
//            .addFormDataPart("estado",jsonObject.estado)
            .addFormDataPart("observacion",jsonObject.observacion)
            .addFormDataPart("foto","fotomulta.jpg", File(direccionfoto).asRequestBody("image/jpg".toMediaType()))
//            .addFormDataPart("foto","fotomulta",  )
            .build()


        val request = okhttp3.Request.Builder()
            .url("https://apir.apiupbateneo.xyz/Multas/Multa")
            .post(requesBody)
            .build()

        val llamado:Call = client.newCall(request)
        llamado.enqueue ( object : Callback{
            override fun onResponse(call: Call, response: okhttp3.Response) {
                runOnUiThread {
                    Toast.makeText(applicationContext,response.code.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext,e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
//        client.newCall(request).execute().use { response ->
//
//            Toast.makeText(applicationContext,response.body.toString(), Toast.LENGTH_SHORT).show()
//        }
    }
    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            contentResolver.query(contentUri, projection, null, null, null) ?: return null
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s: String = cursor.getString(column_index)
        cursor.close()
        return s


    }
}


