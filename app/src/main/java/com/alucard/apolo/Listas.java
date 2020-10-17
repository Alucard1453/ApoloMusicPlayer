package com.alucard.apolo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Listas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private String filename = "lista.txt";
    Dialog listas;
    EditText nombre;
    Button nueva;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog_listas);

        getSupportActionBar().hide();
        listas = new Dialog(this);
        listas.setContentView(R.layout.dialog_nombre_lista);

        nueva = (Button)findViewById(R.id.nueva_lista_app);

        List<ListasDeReproduccion> data = new ArrayList<>();

        try {
            String jsonString = readJSON();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray arreglo = jsonObject.getJSONArray("lista");

            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject itemObj = arreglo.getJSONObject(i);
                String name = itemObj.getString("nombre");
                String canciones = itemObj.getString("numCanciones");
                ListasDeReproduccion listasDeReproduccion = new ListasDeReproduccion(name, canciones);
                data.add(listasDeReproduccion);
            }

            recyclerView = (RecyclerView)findViewById(R.id.recyclernew);
            listAdapter = new ListAdapter(this, data);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }catch (JSONException e) {
            System.out.println(e);
        }

        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listas.show();
                nombre = (EditText)listas.findViewById(R.id.name);

                Button cancelar = (Button)listas.findViewById(R.id.cancelar);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listas.hide();
                    }
                });

                Button aceptar = (Button)listas.findViewById(R.id.aceptar);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecuperarValor(nombre.getText().toString());
                        listas.hide();
                        finish();
                        Intent intent = new Intent(Listas.this, BibliotecaActivity.class);
                        overridePendingTransition( 0, 0);
                        Listas.this.startActivity(intent);
                        overridePendingTransition( 0, 0);
                    }
                });
            }
        });
    }

    public String readJSON(){
        FileInputStream in = null;
        StringBuilder sb = new StringBuilder();
        String resultado = "";

        try{
            in = this.openFileInput("lista.txt");
            int read = 0;
            while ((read = in.read()) != -1){
                resultado = (sb.append((char) read)).toString();
            }
            System.out.println(sb.toString());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return  resultado;
    }

    public void RecuperarValor(String valor)
    {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
        String resultado = ReadFile();
        try {
            JSONObject PRUEBA = new JSONObject(resultado);
            JSONArray canciones = new JSONArray();
            JSONObject object = new JSONObject();
            try{
                object.put("nombre", valor);
                object.put("numCanciones", 0);
                object.put("canciones", canciones);

            }catch (JSONException e){}

            JSONArray old = PRUEBA.getJSONArray("lista");
            old.put(object);

            WriteFile(this, filename, PRUEBA.toString());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String ReadFile()
    {
        FileInputStream in = null;
        StringBuilder sb = new StringBuilder();
        String resultado = "";

        try{
            in = this.openFileInput("lista.txt");
            int read = 0;
            while ((read = in.read()) != -1){
                resultado = (sb.append((char) read)).toString();
            }
            System.out.println(sb.toString());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return  resultado;
    }

    public void WriteFile(Context context, String filename, String str)
    {
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            out.write(str.getBytes(), 0, str.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(out != null){
                try {
                    out.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
