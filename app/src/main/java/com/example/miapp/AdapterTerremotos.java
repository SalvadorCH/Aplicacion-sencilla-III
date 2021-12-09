package com.example.miapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterTerremotos extends ArrayAdapter<Terremoto> {

    public AdapterTerremotos(Activity context, ArrayList<Terremoto> terremotos){
        super(context,0,terremotos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Terremoto terremoto = getItem(position);

        TextView textoMagnitud = (TextView) view.findViewById(R.id.magnitud);
        textoMagnitud.setText(Double.toString(terremoto.getMagnitud()));
        TextView textoCiudad = (TextView) view.findViewById(R.id.ciudad);
        textoCiudad.setText(terremoto.getCiudad());
        TextView textoFecha = (TextView) view.findViewById(R.id.fecha);
        textoFecha.setText(terremoto.getFecha());

        return view;
    }
}
