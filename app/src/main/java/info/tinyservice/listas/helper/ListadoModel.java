package info.tinyservice.listas.helper;

/**
 * Created by alexis on 6/12/16.
 */

public class ListadoModel {
    private String placa;
    private String remolque;
    private String empleado;
    private String documento;
    private String tipoDocumento;


    private String observaciones;

    public ListadoModel(String placa, String remolque, String empleado, String observaciones){
        this.placa = placa;
        this.remolque = remolque;
        this.empleado = empleado;
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

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }




}
