package info.tinyservice.listas.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.tinyservice.listas.MainApplication;
import info.tinyservice.listas.R;
import info.tinyservice.listas.WebService;
import info.tinyservice.listas.WebServiceCallback;
import info.tinyservice.listas.dialogs.SearchPersonalDialog;
import info.tinyservice.listas.dialogs.SearchVehicleDialog;
import info.tinyservice.listas.dialogs.SendListadoDialog;
import info.tinyservice.listas.helper.ListadoAdapter;
import info.tinyservice.listas.model.Listado;
import info.tinyservice.listas.model.ListadoLogger;
import info.tinyservice.listas.model.Personal;
import info.tinyservice.listas.model.Vehicle;
import retrofit2.Response;

public class CrearLista extends Fragment implements SearchPersonalDialog.OnAddPersonalListener, SearchVehicleDialog.OnAddVehicleListener, SendListadoDialog.OnAddSendListadoListener {

    private ArrayList<Listado> listadoResult;
    private ListView listView;
    private Button addButton, cancelButton, searchPersonal, searchVehicle;
    private EditText placa, remolque, observaciones;
    private TextView personalLbl;

    private static ListadoAdapter adapter;

    private boolean isEditing = false;
    private int editIndex = -1;
    private Personal currentPersonalSelected = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (container == null)  return null;

        View view = inflater.inflate(R.layout.crearlistas_layout, container, false);

        listView = (ListView) view.findViewById(R.id.listado);

        if(getArguments() != null && getArguments().containsKey("lista_data")){
            listadoResult = loadLista((ListadoLogger) getArguments().getSerializable("lista_data"));

        }else{
            listadoResult = new ArrayList<>();
        }

        adapter = new ListadoAdapter(listadoResult, this.getContext());

        personalLbl = (TextView) view.findViewById(R.id.personallbl);
        placa = (EditText) view.findViewById(R.id.placaInput);
        remolque = (EditText) view.findViewById(R.id.remolqueInput);
        observaciones = (EditText) view.findViewById(R.id.observacionesInput);

        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(placa.getText().length() > 0 || remolque.getText().length() > 0 || currentPersonalSelected != null || observaciones.getText().length() > 0){
                    Listado element = new Listado(placa.getText().toString(), remolque.getText().toString(), currentPersonalSelected, observaciones.getText().toString());
                    if(isEditing && editIndex != -1){
                        listadoResult.set(editIndex, element);
                        editIndex = -1;
                        isEditing = false;
                        addButton.setText("Agregar");
                    }else{
                        listadoResult.add(element);
                    }
                    clearInputs();
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "Ingrese un dato", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = false;
                editIndex = -1;
                addButton.setText("Agregar");
                clearInputs();
            }
        });

        searchPersonal = (Button) view.findViewById(R.id.searchPersonalButton);
        searchPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchPersonalDialog();
            }
        });

        searchVehicle = (Button) view.findViewById(R.id.searchVehicleButton);
        searchVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchVehicleDialog();
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isEditing = true;
                Listado element = listadoResult.get(position);
                onAddPersonalSubmit(element.getPersonal());
                placa.setText(element.getPlaca());
                remolque.setText(element.getRemolque());
                observaciones.setText(element.getObservaciones());
                editIndex = position;
                addButton.setText("Editar");
                Toast.makeText(getContext(), "editando seleccion", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                listadoResult.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildListado(view);
            }
        });

        return view;
    }

    public ArrayList<Listado> loadLista(ListadoLogger listado){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString =  listado.getRaw();
            return mapper.readValue(jsonInString, new TypeReference<List<Listado>>(){});
        } catch (IOException e) {
            Log.e("CrearLista", "Error al cargar listado, " + e.getMessage());
        }
        return new ArrayList<>();
    }



    private void showSearchPersonalDialog() {
        FragmentManager fm =  getActivity().getSupportFragmentManager();
        SearchPersonalDialog searchPersonalDialog = new SearchPersonalDialog();
        searchPersonalDialog.setTargetFragment(this, 0);
        searchPersonalDialog.show(fm, "fragmentSearchEmployee");
    }

    private void showSearchVehicleDialog() {
        FragmentManager fm =  getActivity().getSupportFragmentManager();
        SearchVehicleDialog searchVehicleDialog = new SearchVehicleDialog();
        searchVehicleDialog.setTargetFragment(this, 0);
        searchVehicleDialog.show(fm, "fragmentSearchVehicle");
    }

    private void showSendListadoDialog() {
        FragmentManager fm =  getActivity().getSupportFragmentManager();
        SendListadoDialog sendListadoDialog = new SendListadoDialog();
        sendListadoDialog.setTargetFragment(this, 0);
        sendListadoDialog.setCancelable(false);
        sendListadoDialog.show(fm, "fragmentSendListado");

    }


    public void clearInputs(){
        currentPersonalSelected = null;
        personalLbl.setText("");
        placa.setText("");
        remolque.setText("");
        placa.requestFocus();
        observaciones.setText("");
    }

    @Override
    public void onAddPersonalSubmit(Personal personal) {
        currentPersonalSelected = personal;
        personalLbl.setText(personal.getNombre());
    }

    @Override
    public void onAddVehicleSubmit(Vehicle vehicle) {
        if(vehicle != null){
            placa.setText(vehicle.getPlaca());
            remolque.setText(vehicle.getRemolque());
        }
    }

    public void buildListado(final View view) {
        showSendListadoDialog();
    }

    @Override
    public void onAddSendListadoSubmit(String[] emailTo) {
        Toast.makeText(getContext(), "enviado listado...", Toast.LENGTH_SHORT).show();
        MainApplication application = (MainApplication) this.getActivity().getApplication();
        WebService service = application.getService();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(listadoResult);
            service.sendListado(jsonInString, emailTo).enqueue(new WebServiceCallback<List<Listado>>(getContext()) {
                @Override
                public void onSuccess(Response response) {
                    Toast.makeText(getContext(), "Listado enviado", Toast.LENGTH_SHORT).show();
                }


            });
        } catch (JsonProcessingException e) {
            Log.e("CrearLista", e.getMessage());
        }
    }
}
