package com.alucard.apolo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.alucard.apolo.BibliotecaActivity.musicFiles;

public class MusicPlayActivity extends AppCompatActivity{

    TextView tvTime, tvDuration, nameArtist, titleSong;
    SeekBar seekBarTime;
    Button btn_back, btn_play_pause, btn_next, btn_add_playlist, btn_favorite, btn_loop, btn_suffle;
    ImageView cover;
    int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Ocultamos barra
        getSupportActionBar().hide();
        
        initViews();
        getIntentMethod();

        titleSong.setText(listSongs.get(position).getTitle());
        nameArtist.setText(listSongs.get(position).getArtist());

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean isFromUser) {
                if (mediaPlayer != null && isFromUser){
                    mediaPlayer.seekTo(progreso);
                    seekBar.setProgress(progreso);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    double current = mediaPlayer.getCurrentPosition();
                    String elapsedTime = milisecondsToString((int)current);
                    tvTime.setText(elapsedTime);
                    seekBarTime.setProgress((int) current);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        listSongs = musicFiles;
        if (listSongs != null){
            btn_play_pause.setBackgroundResource(R.drawable.pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBarTime.setMax(mediaPlayer.getDuration());
        //Indicamos cuanto dura la cancion
        String duration = milisecondsToString(mediaPlayer.getDuration());
        tvDuration.setText(duration);
        //Cargamos portada
        metaData(uri.toString());
    }

    private void initViews(){
        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        nameArtist = findViewById(R.id.nameArtist);
        titleSong = findViewById(R.id.titleSong);

        seekBarTime = findViewById(R.id.seekBarTime);

        btn_back = findViewById(R.id.btn_back);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        btn_next = findViewById(R.id.btn_next);
        btn_add_playlist = findViewById(R.id.btn_add_playlist);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_loop = findViewById(R.id.btn_loop);
        btn_suffle = findViewById(R.id.btn_suffle);

        cover = findViewById(R.id.coverArt);
    }

    private String milisecondsToString(int time){
        String elapsedTime="";
        int minutos = time / 1000 / 60;
        int segundos = time / 1000 % 60;
        elapsedTime = minutos+":";
        if (segundos<10){
            elapsedTime += "0";
        }
        elapsedTime+=segundos;

        return elapsedTime;
    }

    private void metaData(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null){
            Glide.with(this).asBitmap().load(art).into(cover);
        }else{
            Glide.with(this).asBitmap().load(R.drawable.no_cover).into(cover);
        }
        retriever.release();
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_play_pause:
                if (player.isPlaying()){
                    player.pause();
                    btn_play_pause.setBackgroundResource(R.drawable.play);
                }else{
                    player.start();
                    btn_play_pause.setBackgroundResource(R.drawable.pause);
                }
        }
    }*/
}
