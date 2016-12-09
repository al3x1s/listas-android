package info.tinyservice.listas.model;

/**
 * Created by alexis on 6/12/16.
 */

public class Listado {
    private String placa;
    private String remolque;
    private Personal personal;

    private String observaciones;

    public Listado(String placa, String remolque, Personal personal, String observaciones){
        this.placa = placa;
        this.remolque = remolque;
        this.personal = personal;
        this.observaciones = observaciones;
    }

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

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }


}
