package mx.tecnm.tepic.ladm_u3_p1_sqlite_firestore

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class database(
    context: Context?,
    name:String?,
    factory: SQLiteDatabase.CursorFactory?,
    version:Int): SQLiteOpenHelper(context,name,factory,version){

        override fun onCreate(creacion: SQLiteDatabase) {
            creacion.execSQL("CREATE TABLE Apartado (IdApartado  INTEGER PRIMARY KEY  , nombreCliente VARCHAR(200),Producto VARCHAR(200),Precio FLOAT)")
        }

    override fun onUpgrade(creac: SQLiteDatabase, versionAnterior: Int, versionNueva: Int) {
        //update y upgrade= update=actualizacion menor Upgrade= cambio mayor
        //actualizacion
        // p1=version anterior


        // p2= version nueva
        //se invoca solo,cuando tu cambias el numero de version
    }

}