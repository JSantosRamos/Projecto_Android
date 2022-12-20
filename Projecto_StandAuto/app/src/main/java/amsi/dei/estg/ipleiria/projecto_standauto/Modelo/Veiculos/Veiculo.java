package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos;

import java.io.Serializable;
public class Veiculo{

    private int id;
    private String marca;
    private String modelo;
    private String segmento;
    private String combustivel;
    private String quilometros;
    private String motor;
    private String cor;
    private int ano;
    private int numeroPortas;
    private String tipoCaixa;
    private String matricula;
    private String imagem;
    private double preco;
    private int cv;
    private String titulo;
    private String descricao;


    public Veiculo(int id, String marca){
        this.id = id;
        this.marca = marca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
