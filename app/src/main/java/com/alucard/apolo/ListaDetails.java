package com.alucard.apolo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListaDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListaDetailsAdapter listaDetailsAdapter;
    private String filename = "lista.txt";
    ImageView listaFoto;
    String nombreLista;
    TextView nameLista;
    int posicion;
    ArchivoJson archivoJson;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.lista_details);
        nombreLista = getIntent().getStringExtra("listName");
        posicion = getIntent().getIntExtra("posicion", 0);
        recyclerView = findViewById(R.id.listaElements);
        listaFoto = findViewById(R.id.listaPhoto);
        nameLista = findViewById(R.id.nombreLista);
        nameLista.setText(nombreLista);

        ArrayList<MusicFiles> data = new ArrayList<>();
        archivoJson = new ArchivoJson(this, filename);

        try {
            String jsonString = archivoJson.readJSON();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray canciones = jsonObject.getJSONArray("lista").getJSONObject(posicion).getJSONArray("canciones");

            for (int i=0; i<canciones.length(); i++){
                JSONObject item = canciones.getJSONObject(i);
                String nombre = item.getString("nombreCancion");
                String artista = item.getString("artista");
                String duracion = item.getString("duracion");
                String album = item.getString("album");
                String foto = item.getString("foto");
                MusicFiles musicFiles = new MusicFiles(foto, nombre, artista, album, duracion);
                data.add(musicFiles);
            }

            recyclerView = (RecyclerView)findViewById(R.id.listaElements);
            listaDetailsAdapter = new ListaDetailsAdapter(this, data);
            recyclerView.setAdapter(listaDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }catch (JSONException e){
        }
    }


}
