package Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cristian.findgreenplaces.R;

import java.util.ArrayList;

public class AdapterListViewComentarioAT extends BaseAdapter {
    Context context;
    ArrayList<Comentario> comentarios;
    public AdapterListViewComentarioAT(Context context, ArrayList<Comentario> comentarios){
        this.context=context;
        this.comentarios=comentarios;
    }
    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int position) {
        return comentarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.row_list_view_comentarios_at,null);

        }
        TextView nombre=(TextView) convertView.findViewById(R.id.textViewNombre);
        TextView apellido=(TextView)convertView.findViewById(R.id.textViewApellido);
        TextView comentario=(TextView)convertView.findViewById(R.id.textViewComentario);
        nombre.setText(comentarios.get(position).getNombreUsuario());
        apellido.setText(comentarios.get(position).getApellidoUsuario());
        comentario.setText(comentarios.get(position).getComentario());
        return convertView;
    }
}
