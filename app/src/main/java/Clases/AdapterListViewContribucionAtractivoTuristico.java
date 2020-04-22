package Clases;

import android.content.Context;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.cristian.findgreenplaces.R;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterListViewContribucionAtractivoTuristico extends BaseAdapter implements Serializable {
    Context context;
    ArrayList<AtractivoTuristico> atractivoTuristicos;
    public AdapterListViewContribucionAtractivoTuristico(Context context, ArrayList<AtractivoTuristico> atractivoTuristicos) {
        this.context=context;
        this.atractivoTuristicos = atractivoTuristicos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<AtractivoTuristico> getAtractivoTuristicos() {
        return atractivoTuristicos;
    }

    public void setAtractivoTuristicos(ArrayList<AtractivoTuristico> atractivoTuristicos) {
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
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(AdapterListViewContribucionAtractivoTuristico.this.context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();
    }
}
