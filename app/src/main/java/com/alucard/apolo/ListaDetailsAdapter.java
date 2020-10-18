package com.alucard.apolo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

public class ListaDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    Dialog myDialog;
    List<MusicFiles> data = Collections.emptyList();
    MusicFiles current;
    int currentPos = 0;
    private String filename = "lista.txt";

    public ListaDetailsAdapter(Context context, List<MusicFiles> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.music_items, parent, false);
        MyHolder holder = new MyHolder(view);
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dialog_cancionesmenu);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        final MusicFiles current = data.get(position);
        myHolder.file_name.setText(current.getTitle());
        myHolder.artist_name.setText(current.getArtist());
        myHolder.album_name.setText(current.getAlbum());
        myHolder.duration.setText(current.getDuration());

        myHolder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView file_name;
        TextView artist_name;
        ImageView album_art;
        TextView album_name;
        TextView duration;
        ImageButton menu;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            artist_name = itemView.findViewById(R.id.music_file_artist);
            album_name = itemView.findViewById(R.id.music_file_album);
            duration = itemView.findViewById(R.id.music_file_duration);
            album_art = itemView.findViewById(R.id.music_img);
            menu = itemView.findViewById(R.id.menu);
        }

        void setOnClickListeners() {
            menu.setOnClickListener(this);
        }

        public void onClick(View v) {
            ImageView music_img_op = (ImageView) myDialog.findViewById(R.id.music_img_op);
            TextView music_file_name_op = (TextView) myDialog.findViewById(R.id.music_file_name_op);
            TextView music_file_artist_op = (TextView) myDialog.findViewById(R.id.music_file_artist_op);
            TextView music_file_album_op = (TextView) myDialog.findViewById(R.id.music_file_album_op);

            music_file_name_op.setText(file_name.getText().toString());
            music_file_artist_op.setText(artist_name.getText().toString());
            music_img_op.setImageDrawable(album_art.getDrawable());
            music_file_album_op.setText(album_name.getText().toString());

            myDialog.show();

            Button album = (Button) myDialog.findViewById(R.id.botonalbum);
            /*album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                    String name = album_name.getText().toString();
                    Intent intento = new Intent(mContext, AlbumDetails.class);
                    intento.putExtra("albumName", name);
                    intento.putExtra("tipo", 2);
                    mContext.startActivity(intento);
                    //((BibliotecaActivity)mContext).getViewPager().setCurrentItem(2);

                }
            });*/

            Button artista = (Button) myDialog.findViewById(R.id.botonartista);
            /*artista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                    //((BibliotecaActivity)mContext).getViewPager().setCurrentItem(3);
                    String artista = artist_name.getText().toString();
                    Intent intento = new Intent(mContext, AlbumDetails.class);
                    intento.putExtra("albumName", artista);
                    intento.putExtra("tipo", 3);
                    mContext.startActivity(intento);
                    Log.i("Ver", artista);
                }
            });*/

            Button agregarlista = (Button) myDialog.findViewById(R.id.add_playlist);
            /*agregarlista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                    Intent intent = new Intent(mContext, Listas.class);
                    mContext.startActivity(intent);
                }
            });*/
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
}
