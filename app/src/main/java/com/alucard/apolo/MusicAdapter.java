package com.alucard.apolo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MusicFiles> mFiles;
    Dialog myDialog;

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles){
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_cancionesmenu);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String duracion="";
        holder.file_name.setText(mFiles.get(position).getTitle());
        holder.artist_name.setText(mFiles.get(position).getArtist());
        final int min = Integer.parseInt (mFiles.get(position).getDuration()) / 1000 / 60;
        int sec = Integer.parseInt (mFiles.get(position).getDuration()) / 1000 % 60;

        duracion = min+":";
        if (sec<10){
            duracion += "0";
        }
        duracion+=sec;

        holder.duration.setText(duracion);
        byte[] image = getAlbumArt(mFiles.get(position).getPath());
        if(image != null){
            Glide.with(mContext).asBitmap().load(image).into(holder.album_art);
        }else{
            Glide.with(mContext).asBitmap().load(R.drawable.no_cover).into(holder.album_art);
        }
        //Generamos el intento para iniciar el reproductor
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView file_name;
        TextView artist_name;
        ImageView album_art;
        TextView duration;
        ImageButton menu;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            artist_name = itemView.findViewById(R.id.music_file_artist);
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

            music_file_name_op.setText(file_name.getText().toString());
            music_file_artist_op.setText(artist_name.getText().toString());
            music_img_op.setImageDrawable(album_art.getDrawable());

            myDialog.show();

            Button prueba = (Button) myDialog.findViewById(R.id.view_album);
            prueba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("prueba");
                    myDialog.hide();
                }
            });
        }
    }

    // Anotacion por el nivel de la API 29 o superior, para obtener la imagen a traves de la uri
    // declare la propiedad
    // android:requestLegacyExternalStorage="true"
    // en el manifest, en la seccion aplication
}
