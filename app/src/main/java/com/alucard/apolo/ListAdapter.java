package com.alucard.apolo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import static com.alucard.apolo.BibliotecaActivity.tipoVista;
import static com.alucard.apolo.MusicPlayActivity.Vista;

public class ListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<ListasDeReproduccion> data = Collections.emptyList();
    ListasDeReproduccion current;
    int currentPos = 0;
    private String filename = "lista.txt";
    String nombreCancion, nombreArtista, tiempoDuracion, nombreAlbum, prueba;
    ArchivoJson archivoJson;

    public ListAdapter(Context context, List<ListasDeReproduccion> data){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public ListAdapter(Context context, List<ListasDeReproduccion> data, String nombreCancion,
                       String nombreArtista, String tiempoDuracion, String nombreAlbum, String prueba){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.nombreCancion = nombreCancion;
        this.nombreArtista = nombreArtista;
        this.tiempoDuracion = tiempoDuracion;
        this.nombreAlbum = nombreAlbum;
        this.prueba = prueba;
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
        archivoJson = new ArchivoJson(context, filename);

        System.out.println("TIPOOOO: " + tipoVista);
        System.out.println("VISTAAAA: " + Vista);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipoVista == 0 || tipoVista == 2 || tipoVista == 3)
                {
                    archivoJson.AgregarCancion(position, nombreCancion, nombreArtista, tiempoDuracion, nombreAlbum, prueba);
                    Intent intent = new Intent(context, BibliotecaActivity.class);
                    context.startActivity(intent);

                }
                if (tipoVista == 1)
                {
                    System.out.println("Abrir");
                    Intent intent = new Intent(context, ListaDetails.class);
                    intent.putExtra("listName", current.getNombre());
                    intent.putExtra("posicion", position);
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
}
