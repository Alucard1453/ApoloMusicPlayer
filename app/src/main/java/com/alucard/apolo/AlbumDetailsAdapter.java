package com.alucard.apolo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
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

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {
    private Context mContext;
    static ArrayList<MusicFiles> albumFiles;
    View view;
    Dialog myDialog;

    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFiles> albumFiles){
        this.mContext=mContext;
        this.albumFiles=albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_cancionesmenu);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        String duracion="";
        holder.file_name.setText(albumFiles.get(position).getTitle());
        holder.artist_name.setText(albumFiles.get(position).getArtist());
        final int min = Integer.parseInt (albumFiles.get(position).getDuration()) / 1000 / 60;
        int sec = Integer.parseInt (albumFiles.get(position).getDuration()) / 1000 % 60;

        duracion = min+":";
        if (sec<10){
            duracion += "0";
        }
        duracion+=sec;

        holder.duration.setText(duracion);
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
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
                intent.putExtra("sender", "albumDetails");
                mContext.startActivity(intent);
            }
        });

        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView file_name;
        TextView artist_name;
        ImageView album_art;
        TextView duration;
        ImageButton menu;
        public MyHolder(@NonNull View itemView) {
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

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}

