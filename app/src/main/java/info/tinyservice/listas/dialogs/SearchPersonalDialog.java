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
package info.tinyservice.listas.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.tinyservice.listas.MainApplication;
import info.tinyservice.listas.R;
import info.tinyservice.listas.model.Personal;

public class SearchPersonalDialog extends DialogFragment{
    private MainApplication application;
    private DialogFragment dialog;
    public EmployeeAdapter adapter;
    private ArrayList<Personal> personalData = new ArrayList<>();
    private ArrayList<Personal> filteredData = new ArrayList<>();


    private OnAddPersonalListener callback;
    public interface OnAddPersonalListener {
        void onAddPersonalSubmit(Personal personal);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (MainApplication) getActivity().getApplication();
        adapter = new EmployeeAdapter(this.getContext(), new ArrayList<Personal>());
        personalData = application.getDataManager().getAllPersonal();
        filteredData = application.getDataManager().getAllPersonal();
        dialog = this;

        try {
            callback = (OnAddPersonalListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddPersonalListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_searchpersonal, container, false);

        EditText searchPersonal = (EditText) v.findViewById(R.id.empleadoSearch);
        adapter = new EmployeeAdapter(this.getContext(), filteredData);

        ListView employeeList = (ListView) v.findViewById(R.id.employeeListSearch);
        employeeList.setAdapter(adapter);
        employeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Personal personal = filteredData.get(i);
                callback.onAddPersonalSubmit(personal);
                dialog.dismiss();
            }
        });

        searchPersonal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchPersonalDialog.this.adapter.getFilter().filter(charSequence);
            }
        });
        return v;
    }

    private class EmployeeAdapter extends ArrayAdapter<Personal> implements Filterable {
        private List<Personal> data;
        private ItemFilter mFilter = new ItemFilter();

        public EmployeeAdapter(Context context, List<Personal> data) {
            super(context, R.layout.searhemployee_rowlist, data);
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.searhemployee_rowlist, null);

            TextView lblNombre = (TextView)item.findViewById(R.id.employee_searchlisttxt);
            lblNombre.setText(data.get(position).getNombre());

            TextView lblTipo = (TextView) item.findViewById(R.id.lblTipo);
            lblTipo.setText(data.get(position).getTipo_documento() + ": " + data.get(position).getDocumento());

            return(item);
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            ArrayList<Personal> allPersonal = new ArrayList<>();
            allPersonal.addAll(personalData);
            if(constraint == null || constraint.length() == 0){
                result.values = allPersonal;
                result.count = allPersonal.size();
            }else{
                String filterString = constraint.toString().toLowerCase();
                String filterableString ;
                ArrayList<Personal> filteredList = new ArrayList<>();
                for(Personal p : allPersonal){
                    filterableString = p.getNombre();
                    if(filterableString.toLowerCase().contains(filterString))
                        filteredList.add(p);
                }
                result.values = filteredList;
                result.count = filteredList.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData.clear();
            if (results.count != 0) {
                filteredData.addAll((ArrayList<Personal>) results.values);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
