package info.tinyservice.listas.dialogs;
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
import info.tinyservice.listas.model.Vehicle;

public class SearchVehicleDialog extends DialogFragment {
    private MainApplication application;
    private DialogFragment dialog;
    public VehicleAdapter adapter;
    private ArrayList<Vehicle> vehicleData = new ArrayList<>();
    private ArrayList<Vehicle> filteredData = new ArrayList<>();


    private OnAddVehicleListener callback;
    public interface OnAddVehicleListener {
        void onAddVehicleSubmit(Vehicle vehicle);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (MainApplication) getActivity().getApplication();
        adapter = new VehicleAdapter(this.getContext(), new ArrayList<Vehicle>());
        vehicleData = application.getDataManager().getAllVehicle();
        filteredData = application.getDataManager().getAllVehicle();
        dialog = this;

        try {
            callback = (OnAddVehicleListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddPersonalListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_searchvehicle, container, false);

        EditText searchVehicle = (EditText) v.findViewById(R.id.vehicleToSearch);
        adapter = new VehicleAdapter(this.getContext(), filteredData);

        ListView vehicleList = (ListView) v.findViewById(R.id.vehicleListSearch);
        vehicleList.setAdapter(adapter);
        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vehicle vehicle = filteredData.get(i);
                callback.onAddVehicleSubmit(vehicle);
                dialog.dismiss();
            }
        });

        searchVehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchVehicleDialog.this.adapter.getFilter().filter(charSequence);
            }
        });
        return v;
    }

    private class VehicleAdapter extends ArrayAdapter<Vehicle> implements Filterable {
        private List<Vehicle> data;
        private ItemFilter mFilter = new ItemFilter();

        public VehicleAdapter(Context context, List<Vehicle> data) {
            super(context, R.layout.searhemployee_rowlist, data);
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.searchvehicle_rowlist, null);

            TextView lblPlaca = (TextView)item.findViewById(R.id.placalbl);
            lblPlaca.setText(data.get(position).getPlaca());

            TextView lblRemolque = (TextView) item.findViewById(R.id.remolquelbl);
            lblRemolque.setText(data.get(position).getRemolque());

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
            ArrayList<Vehicle> allVehicle = new ArrayList<>();
            allVehicle.addAll(vehicleData);
            if(constraint == null || constraint.length() == 0){
                result.values = allVehicle;
                result.count = allVehicle.size();
            }else{
                String filterString = constraint.toString().toLowerCase();
                String filterableString ;
                ArrayList<Vehicle> filteredList = new ArrayList<>();
                for(Vehicle p : allVehicle){
                    filterableString = p.getPlaca();
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
                filteredData.addAll((ArrayList<Vehicle>) results.values);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
