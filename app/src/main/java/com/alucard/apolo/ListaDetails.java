package com.alucard.apolo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListaDetailsAdapter listaDetailsAdapter;
    ImageView listaFoto;
    String nombreLista;
    TextView nameLista;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_details);
        nombreLista = getIntent().getStringExtra("listName");

        recyclerView = findViewById(R.id.listaElements);
        listaFoto = findViewById(R.id.listaPhoto);
        nameLista = findViewById(R.id.nombreLista);

        nameLista.setText(nombreLista);

        List<MusicFiles> data = new ArrayList<>();
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
}
