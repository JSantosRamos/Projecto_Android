package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive;

import java.io.Serializable;

public class Testdrive implements Serializable {
    private static int autoincrement;
    private long id;
    private int idVeiculo;
    private String data;
    private String hora;
    private String motivo;
    private String estado;

    public final static String POR_VER = "Por ver";


    public Testdrive(long id, String data, String hora, String motivo, String estado, int idVeiculo) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.idVeiculo = idVeiculo;
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

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }
}
