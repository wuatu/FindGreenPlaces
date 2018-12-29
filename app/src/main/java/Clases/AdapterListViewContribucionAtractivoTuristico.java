package Clases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.findgreenplaces.AgregarAtractivoTuristico;
import com.example.cristian.findgreenplaces.Login;
import com.example.cristian.findgreenplaces.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterListViewContribucionAtractivoTuristico extends BaseAdapter {
    Context context;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    public AdapterListViewContribucionAtractivoTuristico(Context context, ArrayList<AtractivoTuristico> atractivoTuristicos) {
        this.context=context;
        this.atractivoTuristicos = atractivoTuristicos;
    }

    @Override
    public int getCount() {
        return atractivoTuristicos.size();
    }

    @Override
    public Object getItem(int position) {
        return atractivoTuristicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.row_recycler_view_contribucion_atractivo_turistico,null);
        }
        TextView nombre=(TextView)convertView.findViewById(R.id.textViewTitulo);
        TextView ciudad=(TextView)convertView.findViewById(R.id.textViewCiudad);
        TextView comuna=(TextView)convertView.findViewById(R.id.textViewComuna);
        nombre.setText(atractivoTuristicos.get(position).getNombre());
        ciudad.setText(atractivoTuristicos.get(position).getCiudad());
        comuna.setText(atractivoTuristicos.get(position).getComuna());
        return convertView;
    }
}