package info.tinyservice.listas.model;

public class Personal {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo_personal_id() {
        return tipo_personal_id;
    }

    public void setTipo_personal_id(int tipo_personal_id) {
        this.tipo_personal_id = tipo_personal_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    private int id, tipo_personal_id;
    private String nombre, documento, tipo_documento;



}
