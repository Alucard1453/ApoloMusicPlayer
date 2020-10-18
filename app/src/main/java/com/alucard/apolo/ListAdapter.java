package com.alucard.apolo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.alucard.apolo.BibliotecaActivity.tipoVista;

public class ListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<ListasDeReproduccion> data = Collections.emptyList();
    ListasDeReproduccion current;
    int currentPos = 0;
    private String filename = "lista.txt";
    String nombreCancion, nombreArtista, tiempoDuracion, nombreAlbum;
    byte[] fotoAlbum;

    public ListAdapter(Context context, List<ListasDeReproduccion> data){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public ListAdapter(Context context, List<ListasDeReproduccion> data, String nombreCancion,
                       String nombreArtista, String tiempoDuracion, String nombreAlbum, byte[] fotoAlbum){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.nombreCancion = nombreCancion;
        this.nombreArtista = nombreArtista;
        this.tiempoDuracion = tiempoDuracion;
        this.nombreAlbum = nombreAlbum;
        this.fotoAlbum = fotoAlbum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.list_items, parent, false);
        MyHolder holder = new MyHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, final int position){

        MyHolder myHolder = (MyHolder)holder;
        final ListasDeReproduccion current = data.get(position);
        myHolder.name.setText(current.getNombre());
        myHolder.num.setText(current.getNumCanciones() + " canciones");

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipoVista == 0)
                {
                RecuperarValor(position, nombreCancion, nombreArtista, tiempoDuracion, nombreAlbum, fotoAlbum);
                Intent intent = new Intent(context, BibliotecaActivity.class);
                    context.startActivity(intent);
                }else if (tipoVista == 1)
                {
                    System.out.println("Abrir");
                    Intent intent = new Intent(context, ListaDetails.class);
                    intent.putExtra("listName", current.getNombre());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView num;

        public MyHolder(View itemView){
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.list_file_name);
            num = (TextView)itemView.findViewById(R.id.num_songs);
        }
    }

    public String readJSON(){
        FileInputStream in = null;
        StringBuilder sb = new StringBuilder();
        String resultado = "";

        try{
            in = context.openFileInput("lista.txt");
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

    public void RecuperarValor(int lista, String valor, String artist, String time, String name, byte[] foto)
    {
        Toast.makeText(context, "Canción agregada", Toast.LENGTH_SHORT).show();
        String resultado = readJSON();
        String num;
        int nuevonum;
        try{
            JSONObject jsonObject = new JSONObject(resultado);
            JSONObject elementos = new JSONObject();
            try{
                elementos.put("nombreCancion", valor);
                elementos.put("artista", artist);
                elementos.put("duracion", time);
                elementos.put("album", name);
                elementos.put("foto", foto);
            }catch (JSONException e){}

            JSONArray old = jsonObject.getJSONArray("lista").getJSONObject(lista).getJSONArray("canciones");
            old.put(elementos);

            JSONObject obj = jsonObject.getJSONArray("lista").getJSONObject(lista);
            obj.has("numCanciones");
            num = obj.get("numCanciones").toString();
            nuevonum = Integer.parseInt(num);
            nuevonum = nuevonum+1;
            obj.putOpt("numCanciones", nuevonum);

            WriteFile(context, filename, jsonObject.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
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
