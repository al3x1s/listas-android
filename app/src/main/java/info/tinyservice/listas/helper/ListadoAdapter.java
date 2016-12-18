package info.tinyservice.listas.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import info.tinyservice.listas.R;
import info.tinyservice.listas.model.Listado;
import info.tinyservice.listas.model.Personal;

public class ListadoAdapter extends ArrayAdapter<Listado>{
    private ArrayList<Listado> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtUnidad;
        TextView txtRemolque;
        TextView txtEmpleado;
        TextView txtObservaciones;
    }

    public ListadoAdapter(ArrayList<Listado> data, Context context) {
        super(context, R.layout.row_item_lista, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listado dataModel = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_lista, parent, false);
            viewHolder.txtUnidad = (TextView) convertView.findViewById(R.id.unidad);
            viewHolder.txtRemolque = (TextView) convertView.findViewById(R.id.remolque);
            viewHolder.txtEmpleado = (TextView) convertView.findViewById(R.id.empleado);
            viewHolder.txtObservaciones = (TextView) convertView.findViewById(R.id.observaciones);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Personal personal = dataModel.getPersonal();
        String nombre = personal != null ? personal.getNombre() : "";
        viewHolder.txtUnidad.setText(dataModel.getPlaca());
        viewHolder.txtRemolque.setText(dataModel.getRemolque());
        viewHolder.txtEmpleado.setText(nombre);
        viewHolder.txtObservaciones.setText(dataModel.getObservaciones());
        return convertView;
    }
}