package com.alucard.apolo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<ListasDeReproduccion> data = Collections.emptyList();
    ListasDeReproduccion current;
    int currentPos = 0;

    public ListAdapter(Context context, List<ListasDeReproduccion> data){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.list_items, parent, false);
        MyHolder holder = new MyHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position){

        MyHolder myHolder = (MyHolder)holder;
        ListasDeReproduccion current = data.get(position);
        myHolder.name.setText(current.getNombre());
        myHolder.num.setText(current.getNumCanciones() + " canciones");
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
