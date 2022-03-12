package com.example.sqlite01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtCodigo, txtDescripcion, txtPrecio;
    Button btnBuscar, btnGuardar, btnEditar, btnBorrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCodigo=findViewById(R.id.txtCodigo);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        txtPrecio=findViewById(R.id.txtPrecio);

        btnBuscar=findViewById(R.id.btnBuscar);
        btnGuardar=findViewById(R.id.btnGuardar);
        btnEditar=findViewById(R.id.btnEditar);
        btnBorrar=findViewById(R.id.btnBorrar);

    }

    //creamos el metodo guardar onclick en el boton
    public void guardar(View view){
        //creamos el objeto de tipo conexionBD
        ConexionBD conexion=new ConexionBD(this,"administracion",null,1);
        //creamos un objeto de tipo sqlite database
        SQLiteDatabase bd=conexion.getWritableDatabase(); //con getWritable permitimos que escriba en la base de datos


        //incluir validación del null para que no guarde registros vacios
        String codigo=txtCodigo.getText().toString(); //no importa que sea string la BD lo lee igual y convierte
        String descripcion=txtDescripcion.getText().toString();
        String precio=txtPrecio.getText().toString();

        //debo crear otro objeto ContentValues que le pasa por parametros codigo, desc y precio a la BD
        ContentValues registro=new ContentValues();

        //aqui hacemos el par del atributo de la bd con la variable
        registro.put("codigo", codigo);
        registro.put("precio", precio);
        registro.put("descripcion", descripcion);

        //hacemos la union entre la conexion a la BD con el registro
        bd.insert("articulos",null,registro);

        //cerramos la conexion
        bd.close();

        //seteamos el campo para que quede vacio
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");

        //mostramos la verificacion del registro
        Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();

    }
    public void buscar(View view){
        ConexionBD conexion=new ConexionBD(this,"administracion",null,1);
        SQLiteDatabase bd=conexion.getWritableDatabase();

        String codigo=txtCodigo.getText().toString();

        //el metodo buscar necesita de un cursor

        Cursor fila = bd.rawQuery("SELECT descripcion, precio FROM articulos WHERE codigo='"+codigo+"'", null);

        //hacemos un if para saber si lo encontro fila move to first lo unica en la primera fila y entrega true
        if (fila.moveToFirst()){
            txtDescripcion.setText(fila.getString(0)); //el indice 0 del registro
            txtPrecio.setText(fila.getString(1));
            bd.close();
            Toast.makeText(this, "producto encontrado", Toast.LENGTH_SHORT).show();
        }
        else{
            bd.close();
            Toast.makeText(this, "no se econtró el producto", Toast.LENGTH_SHORT).show();
            txtCodigo.setText("");
            txtDescripcion.setText("");
            txtPrecio.setText("");

        }

        public void editar(View view){
            ConexionBD conexion=new ConexionBD(this,"administracion",null,1);
            SQLiteDatabase bd=conexion.getWritableDatabase();

            String codigo=txtCodigo.getText().toString(); //no importa que sea string la BD lo lee igual y convierte
            String descripcion=txtDescripcion.getText().toString();
            String precio=txtPrecio.getText().toString();

            ContentValues registro=new ContentValues();

            registro.put("codigo", codigo);
            registro.put("precio", precio);
            registro.put("descripcion", descripcion);

            int x = bd.update("articulos", registro,"codigo='"+codigo+"'",null);

            //el update y el insert retornan un numero 0 y 1

            if(x>0){
                Toast.makeText(this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(this, "No se pudo modificar", Toast.LENGTH_SHORT).show();
            }

        }
    }
}