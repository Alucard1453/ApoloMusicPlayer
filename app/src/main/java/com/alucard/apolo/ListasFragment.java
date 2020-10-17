package com.alucard.apolo;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListasFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private String filename = "lista.txt";
    Dialog listas;
    Button crear;
    EditText nombre;
    FileInputStream in = null;


    public ListasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_listas, container, false);
        View view = inflater.inflate(R.layout.fragment_listas, container, false);
        listas = new Dialog(getContext());
        listas.setContentView(R.layout.dialog_nombre_lista);
        //Recuperar de Json
        List<ListasDeReproduccion> data = new ArrayList<>();

        try {
            String jsonString = readJSON();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray arreglo = jsonObject.getJSONArray("lista");

            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject itemObj = arreglo.getJSONObject(i);
                String name = itemObj.getString("nombre");
                String canciones = itemObj.getString("numCanciones");
                ListasDeReproduccion listasDeReproduccion = new ListasDeReproduccion(name, canciones);
                data.add(listasDeReproduccion);
            }

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerlistas);
            listAdapter = new ListAdapter(getContext(), data);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }catch (JSONException e) {
            System.out.println(e);
        }

        crear = (Button)view.findViewById(R.id.crear);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listas.show();
                nombre = (EditText)listas.findViewById(R.id.name);

                Button cancelar = (Button)listas.findViewById(R.id.cancelar);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listas.hide();
                    }
                });

                Button aceptar = (Button)listas.findViewById(R.id.aceptar);
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecuperarValor(nombre.getText().toString());
                        listas.hide();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ListasFragment ln = new ListasFragment();
                        ft.replace(R.id.framelistas, ln);
                        ft.commit();
                    }
                });
            }
        });

        return view;
    }

    public void RecuperarValor(String valor)
    {
        Toast.makeText(getActivity(), valor, Toast.LENGTH_SHORT).show();
        String resultado = ReadFile();
        try {
            JSONObject PRUEBA = new JSONObject(resultado);
            JSONArray canciones = new JSONArray();
            JSONObject object = new JSONObject();
            try{
                object.put("nombre", valor);
                object.put("numCanciones", 0);
                object.put("canciones", canciones);

            }catch (JSONException e){}

            JSONArray old = PRUEBA.getJSONArray("lista");
            old.put(object);

            WriteFile(getActivity(), filename, PRUEBA.toString());
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String ReadFile()
    {
        FileInputStream in = null;
        StringBuilder sb = new StringBuilder();
        String resultado = "";

        try{
            in = getContext().openFileInput("lista.txt");
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
            in = getContext().openFileInput("lista.txt");
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
