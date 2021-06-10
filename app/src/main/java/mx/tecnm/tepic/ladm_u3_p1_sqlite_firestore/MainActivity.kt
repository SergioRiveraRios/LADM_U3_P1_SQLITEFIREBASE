package mx.tecnm.tepic.ladm_u3_p1_sqlite_firestore

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var baseSQL = database(this, "Apartado", null, 1)
    var baseFirebase =FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()
    var mostrarFirebase = ArrayList<String>()
    var error = ArrayList<String>()
    var ahora = System.currentTimeMillis()
    var fecha: Date = Date(ahora)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mostrar()
        guardar.setOnClickListener {
            insertarDB()
        }
        sincronizar.setOnClickListener {
            subirFirebase()
        }
    }

    fun insertarDB (){
        try {
            var transaccion = baseSQL.writableDatabase
            var data= ContentValues()

            data.put("IdApartado", id.text.toString().toInt())
            data.put("NombreCliente", nombre.text.toString())
            data.put("Producto", producto.text.toString())
            data.put("Precio", precio.text.toString().toFloat())

            var respuesta = transaccion.insert("Apartado", null, data)
            if (respuesta.toInt()==-1){
                toast("No se pudo insertar")
            }else{
                toast("Insertado Correctamente")
            }
            transaccion.close()
        }catch (err: SQLiteException){
        }

    }
    fun subirFirebase(){

        try {
            var transaccion=baseSQL.writableDatabase

            var cursor = transaccion.query("Apartado", arrayOf("*"), null, null, null, null, null)
            if (cursor.moveToFirst()){
                mostrarFirebase.clear()
                do {
                    var datosInsertar = hashMapOf(
                        "IdApartado" to cursor.getString(0),
                        "nombreCliente" to cursor.getString(1),
                        "Producto" to cursor.getString(2),
                        "Precio" to cursor.getString(3)
                    )
                    mostrarFirebase.add(cursor.getInt(0).toString())

                    baseFirebase.collection("Apartado")
                        .add(datosInsertar)
                        .addOnSuccessListener { documentReference ->
                        }
                }while (cursor.moveToNext())
            }else{
                error.add("Null")
            }

            mostrar()
            eliminarSQL()
        }catch (err: SQLiteException){

        }
    }
    fun eliminarSQL(){
        try{
            var db=baseSQL.writableDatabase
            db.execSQL("delete from " + "Apartado");
            db.close()
            toast("Datos borrados")
        }catch (err: SQLiteException){
           toast("Datos no borrados")
        }
    }

    private fun mostrar(){
        baseFirebase.collection("Apartado")
            .addSnapshotListener { querySnapshot, error ->
                if(error != null)
                {
                    return@addSnapshotListener
                }
                datalista.clear()
                mostrarFirebase.clear()
                for (document in querySnapshot!!){
                    var doc = "${document.getString("Producto")} \n ${document.getString("nombreCliente")} \n ${document.get("Precio")}"
                    datalista.add(doc)
                    mostrarFirebase.add(document.id.toString())
                }
                datosFirebase.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, datalista)
                datosFirebase.setOnItemClickListener { parent, view, pos, i ->
                    datoEscogido(pos)
                }
            }
    }

    private fun datoEscogido(pos: Int){
        var dato = mostrarFirebase.get(pos)
            var intent = Intent(this, activity2::class.java)
            intent.putExtra("dato", dato)
            startActivity(intent)
    }
    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}