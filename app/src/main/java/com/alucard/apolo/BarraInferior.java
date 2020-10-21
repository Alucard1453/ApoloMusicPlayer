package com.alucard.apolo;


import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.alucard.apolo.AlbumDetailsAdapter.albumFiles;
import static com.alucard.apolo.BibliotecaActivity.musicFiles;
import static com.alucard.apolo.BibliotecaActivity.pista;
import static com.alucard.apolo.BibliotecaActivity.reproduccion;
import static com.alucard.apolo.ListaDetailsAdapter.listFiles;
import static com.alucard.apolo.MusicPlayActivity.sender;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarraInferior extends Fragment implements View.OnClickListener {
    ArrayList<MusicFiles> canciones = new ArrayList<>();

    public BarraInferior() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barra_inferior, container, false);
        view.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sender != null && sender.equals("albumDetails")){
            canciones = albumFiles;
        }else if (sender != null && sender.equals("listDetails")){
            canciones = listFiles;
        }
        else{
            canciones = musicFiles;
        }
        Log.e("Numpista",pista+"");
        if (pista != -1){
            ImageView imgrepro = getView().findViewById(R.id.imgReproduccion);
            TextView nomcanc = getView().findViewById(R.id.nameCancionReprod);
            nomcanc.setText(canciones.get(pista).getTitle());
            TextView nomart = getView().findViewById(R.id.nameArtistaReprod);
            nomart.setText(canciones.get(pista).getArtist());
            byte[] image = getAlbumArt(canciones.get(pista).getPath());
            if(image != null){
                Glide.with(this).asBitmap().load(image).into(imgrepro);
            }else{
                Glide.with(this).asBitmap().load(R.drawable.no_cover).into(imgrepro);
            }
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    public void onClick(View v) {
        if (reproduccion) {
            Log.e("Prueba", "Prueba");
            Intent reproduccion = new Intent(getContext(), MusicPlayActivity.class);
            reproduccion.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(reproduccion, 1);
        }
    }
}
