package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Reserva;

import java.io.Serializable;

public class Reserva implements Serializable {
    private int id;
    private String numero;
    private String nif;
    private String morada;
    private String cc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }


    public int getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

    private int idVehicle;

    public Reserva (int id, int idVehicle, String numero, String nif, String morada, String cc){
        this.id = id;
        this.idVehicle = idVehicle;
        this.numero = numero;
        this.nif = nif;
        this.morada = morada;
        this.cc = cc;
    }
}
