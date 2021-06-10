package mx.tecnm.tepic.ladm_u3_p1_sqlite_firestore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout2.*

class activity2 : AppCompatActivity(){
    var baseFirebase = FirebaseFirestore.getInstance()
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout2)
        var ids=intent.extras
        id=ids!!.getString("dato")!!
        obtenerDocumento(id)

        Actualizar.setOnClickListener {
            confirmacionAccion("Estas seguro de actualizar este documento?",id,1)
        }

        Eliminar.setOnClickListener {
           confirmacionAccion("Estas seguro de eliminar este documento?",id,0)

        }
        Regresar.setOnClickListener {
            finish()
        }
    }

    private fun obtenerDocumento(idDato:String){
        baseFirebase.collection("Apartado")
            .document(idDato)
            .get()
            .addOnSuccessListener {
                nombreCliente.setText(it.getString("nombreCliente"))
                Producto.setText(it.getString("Producto"))
                Precio.setText(it.getString("Precio"))
            }
    }
    private fun actualizarDocumento(idDato:String){
        baseFirebase.collection("Apartado")
            .document(idDato)
            .update("nombreCliente",nombreCliente.text.toString(),"Producto",Producto.text.toString(),"Precio",Precio.text.toString().toFloat())
            .addOnSuccessListener {
                System.out.println("Actualizado")
            }
    }
    private fun eliminarDocumento(idDato:String){
        baseFirebase.collection("Apartado")
            .document(idDato)
            .delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    private fun confirmacionAccion(mensaje:String,idDato:String,accion:Int) {
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(mensaje)
            .setPositiveButton("Si"){d, i->
                if(accion==0){
                eliminarDocumento(idDato)
                    toast("Eliminado")
                    limpiarCampos()
                }
                else{
                    actualizarDocumento(idDato)
                    toast("Editado")
                }
            }
            .setNegativeButton("No"){d,i->}
            .show()
    }
    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun limpiarCampos(){
        nombreCliente.setText("")
        Producto.setText("")
        Precio.setText("")
        id=""
    }
}