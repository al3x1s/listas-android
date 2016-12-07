package info.tinyservice.listas.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import info.tinyservice.listas.R;
import info.tinyservice.listas.helper.ListadoAdapter;
import info.tinyservice.listas.helper.ListadoModel;

public class CrearLista extends Fragment {

    ArrayList<ListadoModel> dataModels;
    ListView listView;
    Button addButton;
    Button cancelButton;
    EditText placa;
    EditText remolque;

    private static ListadoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (container == null)  return null;

        View view = inflater.inflate(R.layout.crearlistas_layout, container, false);

        listView = (ListView) view.findViewById(R.id.listado);
        dataModels= new ArrayList<>();
        adapter = new ListadoAdapter(dataModels, this.getContext());

        placa = (EditText) view.findViewById(R.id.placaInput);
        remolque = (EditText) view.findViewById(R.id.remolqueInput);

        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.add(new ListadoModel(placa.getText().toString(), remolque.getText().toString(), "Irving Alexis Gonzalez", ""));
                adapter.notifyDataSetChanged();
            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placa.setText("");
                remolque.setText("");
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListadoModel dataModel= dataModels.get(position);
                Snackbar.make(view, dataModel.getPlaca()+"\n"+dataModel.getRemolque(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        return view;
    }
}
