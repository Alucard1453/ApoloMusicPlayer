package com.alucard.apolo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DialogNombreLista extends DialogFragment {
    private static ListasDeReproduccion lista = new ListasDeReproduccion("Nombre", "0");
    private String filename = "lista.txt";
    EditText nombre;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View MyView = inflater.inflate(R.layout.dialog_nombre_lista, null);
        nombre = (EditText)MyView.findViewById(R.id.name);
        builder.setView(MyView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecuperarValor(nombre.getText().toString());
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ListasFragment ln = new ListasFragment();
                        ft.replace(R.id.framelistas, ln);
                        ft.commit();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }



    public void RecuperarValor(String valor)
    {
        Toast.makeText(getActivity(), valor, Toast.LENGTH_SHORT).show();
        String resultado = ReadFile();
        try {
            JSONObject PRUEBA = new JSONObject(resultado);
            JSONObject object = new JSONObject();
            try{
                object.put("nombre", valor);
                object.put("numCanciones", 0);

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


}
