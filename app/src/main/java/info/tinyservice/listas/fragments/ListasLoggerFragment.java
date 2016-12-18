package info.tinyservice.listas.fragments;
/*
 * Copyright 2016 Irving Gonzalez (ialexis93@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import info.tinyservice.listas.MainActivity;
import info.tinyservice.listas.MainApplication;
import info.tinyservice.listas.R;
import info.tinyservice.listas.model.ListadoLogger;

public class ListasLoggerFragment extends Fragment {
    private MainApplication application;
    private ListView listasLogger;
    private ListasLoggerAdapter adapter;
    private ArrayList<ListadoLogger> listados;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (MainApplication) getActivity().getApplication();
        listados = application.getDataManager().getAllListados();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (container == null)  return null;

        View view = inflater.inflate(R.layout.listaslogger_layout, container, false);

        listasLogger = (ListView) view.findViewById(R.id.listalogger_listview);
        adapter = new ListasLoggerAdapter(listados, this.getContext());
        listasLogger.setAdapter(adapter);
        listasLogger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity ma = (MainActivity) getActivity();
                ma.changeToCrearLista(listados.get(position));
            }
        });

        return view;
    }

    private class ListasLoggerAdapter extends ArrayAdapter<ListadoLogger> {
        private ArrayList<ListadoLogger> dataSet;
        private Context mContext;

        public ListasLoggerAdapter(ArrayList<ListadoLogger> data, Context context) {
            super(context, R.layout.listalogger_rowlist, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listalogger_rowlist, null);
            ListadoLogger ll = dataSet.get(position);

            TextView lblTime = (TextView) item.findViewById(R.id.fecha_ll);
            long time = ll.getFecha();
            lblTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(time * 1000)));

            TextView lblEnviadoA = (TextView) item.findViewById(R.id.sendedTo_ll);
            lblEnviadoA.setText(ll.getEmailTo());
            return(item);
        }
    }

}
