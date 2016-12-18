package info.tinyservice.listas.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by alexis on 6/12/16.
 */

public class Listado {
    private String placa;
    private String remolque;

    @JsonProperty("personal")
    private Personal personal;

    private String observaciones;



    public Listado(String placa, String remolque, Personal personal, String observaciones){
        this.placa = placa;
        this.remolque = remolque;
        this.personal = personal;
        this.observaciones = observaciones;
    }

    public Listado(){}

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRemolque() {
        return remolque;
    }

    public void setRemolque(String remolque) {
        this.remolque = remolque;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Personal getPersonal() {
        return personal;
    }

    @JsonProperty("personal")
    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

}
