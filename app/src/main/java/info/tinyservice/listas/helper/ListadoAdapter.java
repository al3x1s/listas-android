package info.tinyservice.listas.helper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import info.tinyservice.listas.R;

/**
 * Created by alexis on 6/12/16.
 */

public class ListadoAdapter extends ArrayAdapter<ListadoModel> implements View.OnClickListener{
    private ArrayList<ListadoModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtUnidad;
        TextView txtRemolque;
        TextView txtEmpleado;
        TextView txtObservaciones;
        LinearLayout row_content;
    }

    public ListadoAdapter(ArrayList<ListadoModel> data, Context context) {
        super(context, R.layout.row_item_lista, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        ListadoModel dataModel = (ListadoModel) object;
//
//        switch (v.getId()){
//            case R.id.content_row:
//                Snackbar.make(v, "Release date " +dataModel.getEmpleado(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//
//                break;
//        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListadoModel dataModel = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_lista, parent, false);
            viewHolder.txtUnidad = (TextView) convertView.findViewById(R.id.unidad);
            viewHolder.txtRemolque = (TextView) convertView.findViewById(R.id.remolque);
            viewHolder.txtEmpleado = (TextView) convertView.findViewById(R.id.empleado);
            viewHolder.txtObservaciones = (TextView) convertView.findViewById(R.id.observaciones);
//            viewHolder.row_content = (LinearLayout) convertView.findViewById(R.id.content_row);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtUnidad.setText(dataModel.getPlaca());
        viewHolder.txtRemolque.setText(dataModel.getRemolque());
        viewHolder.txtEmpleado.setText(dataModel.getEmpleado());
        viewHolder.txtObservaciones.setText(dataModel.getObservaciones());
//        viewHolder.row_content.setOnClickListener(this);
//        viewHolder.row_content.setTag(position);

        return convertView;
    }
}