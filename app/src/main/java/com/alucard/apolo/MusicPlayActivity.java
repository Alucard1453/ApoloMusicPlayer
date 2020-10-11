package com.alucard.apolo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvTime, tvDuration, nameArtist, titleSong;
    SeekBar seekBarTime;
    Button btn_back, btn_play_pause, btn_next, btn_add_playlist, btn_favorite, btn_loop, btn_suffle;

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //oculta la action bar
        //getSupportActionBar().hide();

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

        player = MediaPlayer.create(this, R.raw.blondie_maria);
        player.setLooping(true);
        player.seekTo(0);

        String duration = milisecondsToString(player.getDuration());
        tvDuration.setText(duration);

        btn_play_pause.setOnClickListener(this);


        seekBarTime.setMax(player.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean isFromUser) {
                if (isFromUser){
                    player.seekTo(progreso);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null){
                    if (player.isPlaying()){
                        try{
                            final double current = player.getCurrentPosition();
                            final String elapsedTime = milisecondsToString((int)current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(elapsedTime);
                                    seekBarTime.setProgress((int) current);
                                }
                            });

                            Thread.sleep(1000);
                        }catch (InterruptedException e){

                        }
                    }
                }
            }
        }).start();
    }

    public String milisecondsToString(int time){
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

    @Override
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
    }
}
