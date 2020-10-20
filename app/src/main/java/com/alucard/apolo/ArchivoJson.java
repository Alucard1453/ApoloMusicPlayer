package com.alucard.apolo;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ArchivoJson
{
    Context context;
    String filename;
    FileInputStream in = null;

    public ArchivoJson(Context context, String filename){
        this.context = context;
        this.filename = filename;

    }

    public void crearArchivo(String filename){
        try{
            in = context.openFileInput(filename);
        }catch (FileNotFoundException e)
        {
            primerJSON();
        }
    }

    public void primerJSON(){
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject principal = new JSONObject();
        JSONArray canciones = new JSONArray();
        try{
            object.put("nombre", "Favoritos");
            object.put("numCanciones", "0");
            object.put("canciones", canciones);
            array.put(object);
            principal.put("lista",array);
        }catch (JSONException e){}


        WriteFile(context, filename, principal.toString());
    }

    public void WriteFile(Context context, String filename, String str){

        FileOutputStream out = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(str, 0, str.length());
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (out != null){
                try{
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
            InputStream bin = new BufferedInputStream(in);
            InputStreamReader reader = new InputStreamReader(bin, "UTF-8");
            BufferedReader buf = new BufferedReader(reader);
            int read = 0;
            while ((read = buf.read()) != -1){
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

    public void crearLista(String nombre){
        Toast.makeText(context, nombre, Toast.LENGTH_SHORT).show();
        String resultado = readJSON();
        try {
            JSONObject PRUEBA = new JSONObject(resultado);
            JSONArray canciones = new JSONArray();
            JSONObject object = new JSONObject();
            try{
                object.put("nombre", nombre);
                object.put("numCanciones", 0);
                object.put("canciones", canciones);

            }catch (JSONException e){}

            JSONArray old = PRUEBA.getJSONArray("lista");
            old.put(object);

            WriteFile(context, filename, PRUEBA.toString());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void crearListaCancion(String lista, String title, String artist,
                                  String time, String name, String foto){

        Toast.makeText(context, lista, Toast.LENGTH_SHORT).show();
        String resultado = readJSON();
        try {
            JSONObject PRUEBA = new JSONObject(resultado);
            JSONArray canciones = new JSONArray();
            JSONObject object = new JSONObject();
            JSONObject element = new JSONObject();
            try{
                object.put("nombre", lista);
                object.put("numCanciones", 1);
                object.put("canciones", canciones);
                element.put("nombreCancion",title);
                element.put("artista", artist);
                element.put("duracion", time);
                element.put("album", name);
                element.put("foto", foto);
                canciones.put(element);

            }catch (JSONException e){}

            JSONArray old = PRUEBA.getJSONArray("lista");
            old.put(object);

            WriteFile(context, filename, PRUEBA.toString());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void AgregarCancion(int lista, String cancion, String artist, String time, String name, String foto){

        Toast.makeText(context, "Cancion agregada", Toast.LENGTH_SHORT).show();
        String resultado = readJSON();
        String num;
        int nuevonum;
        try{
            JSONObject jsonObject = new JSONObject(resultado);
            JSONObject elementos = new JSONObject();
            try{
                elementos.put("nombreCancion", cancion);
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
}
