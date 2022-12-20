package amsi.dei.estg.ipleiria.projecto_standauto.Modelo;

import java.io.Serializable;

public class Testdrive implements Serializable {
    private static int autoincrement;
    private long id;
    private String data;
    private String hora;
    private String estado;
    private String motivo;

    public Testdrive(long id, String data, String hora, String estado, String motivo) {
        this.id = 0;
        this.data = data;
        this.hora = hora;
        this.estado = estado;
        this.motivo = motivo;
    }

    public Testdrive(String data, String hora, String estado, String motivo) {
        this.id = ++autoincrement;
        this.data = data;
        this.hora = hora;
        this.estado = estado;
        this.motivo = motivo;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    @Override
    public String toString() {
        return this.data + ", " + this.hora;
    }
}
