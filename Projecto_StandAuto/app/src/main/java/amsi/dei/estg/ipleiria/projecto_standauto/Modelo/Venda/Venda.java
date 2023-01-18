package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class Venda {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    private int id;
    private double valor;
    private String nif;
    private String morada;
    private String nome;
    private Veiculo veiculo;

    public Venda(int id, double valor, String nif, String morada, String nome, Veiculo veiculo) {
        this.id = id;
        this.nif = nif;
        this.valor = valor;
        this.morada = morada;
        this.nome = nome;
        this.veiculo = veiculo;
    }

    @Override
    public String toString() {
        return this.veiculo.getMarca() + "(" + this.veiculo.getModelo() + ")";
    }


}
