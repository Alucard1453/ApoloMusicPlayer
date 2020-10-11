package com.alucard.apolo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.alucard.apolo.Controlador.AlbumesFragment;
import com.alucard.apolo.Controlador.ArtistasFragment;
import com.alucard.apolo.Controlador.CancionesFragment;
import com.alucard.apolo.Controlador.ListasFragment;
import com.alucard.apolo.Controlador.ViewPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BibliotecaActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem canciones, listas, albumes, artistas;
    ViewPagerAdapter pagerAdapter;
    private List<Integer> fragmentsIcons = new ArrayList<>(Arrays.asList(R.drawable.canciones, R.drawable.listas, R.drawable.album, R.drawable.artist));
    public static int REQUEST_CODE = 1;
    ArrayList<MusicFiles> musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        permission();
        setupView();
        setUpViewPagerAdapter();
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BibliotecaActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            , REQUEST_CODE);
        }else{
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            Log.e("Prueba","Hola");
            musicFiles = getAllAudio(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                musicFiles = getAllAudio(this);
            }else{
                ActivityCompat.requestPermissions(BibliotecaActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
    }

    private void setupView(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    private void setUpViewPagerAdapter(){
        pagerAdapter.addFragment(new CancionesFragment(), "Canciones");
        pagerAdapter.addFragment(new ListasFragment(), "Listas");
        pagerAdapter.addFragment(new AlbumesFragment(), "Albumes");
        pagerAdapter.addFragment(new ArtistasFragment(), "Artistas");
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.d("TAG1","Posicion: "+tabLayout.getSelectedTabPosition()+" Titulo: "+ pagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                    case 1:
                        Log.d("TAG1","Posicion: "+tabLayout.getSelectedTabPosition()+" Titulo: "+ pagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                    case 2:
                        Log.d("TAG1","Posicion: "+tabLayout.getSelectedTabPosition()+" Titulo: "+ pagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                    case 3:
                        Log.d("TAG1","Posicion: "+tabLayout.getSelectedTabPosition()+" Titulo: "+ pagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        //Iconos
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(fragmentsIcons.get(i));
        }
    }

    private static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, //Para el path (ruta)
            MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,
                null, null, null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);

                //Revisamos
                Log.e("Path: "+path, "Album: "+ album);

                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }

}
