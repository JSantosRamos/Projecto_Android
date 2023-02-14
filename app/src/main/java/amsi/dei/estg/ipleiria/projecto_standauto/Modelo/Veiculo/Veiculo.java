package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo;

import java.util.ArrayList;

public class Veiculo {

    private int id;
    private String marca;
    private String modelo;
    private String serie;
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
    private int preco; //é sempre um int
    private int cv;
    private String titulo;
    private String descricao;
    private ArrayList<String> imagensLista;


    public Veiculo(int id, String marca, String modelo, String serie, String segmento, String combustivel, String quilometros, String motor,
                   String cor, int ano, int numeroPortas, String tipoCaixa, String matricula, String imagem, int preco, int cv, String titulo, String descricao, ArrayList<String> imagensLista) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.serie = serie;
        this.segmento = segmento;
        this.combustivel = combustivel;
        this.quilometros = quilometros;
        this.motor = motor;
        this.cor = cor;
        this.ano = ano;
        this.numeroPortas = numeroPortas;
        this.tipoCaixa = tipoCaixa;
        this.matricula = matricula;
        this.imagem = imagem;
        this.preco = preco;
        this.cv = cv;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagensLista = imagensLista;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public String getQuilometros() {
        return quilometros;
    }

    public void setQuilometros(String quilometros) {
        this.quilometros = quilometros;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    public String getTipoCaixa() {
        return tipoCaixa;
    }

    public void setTipoCaixa(String tipoCaixa) {
        this.tipoCaixa = tipoCaixa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public int getCv() {
        return cv;
    }

    public void setCv(int cv) {
        this.cv = cv;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public ArrayList<String> getImagensLista() {
        return imagensLista;
    }

    public void setImagensLista(ArrayList<String> imagensLista) {
        this.imagensLista = imagensLista;
    }

    @Override
    public String toString() {
        return this.getMarca() + "(" + this.getModelo() + ")" + "ID: " + this.id;
    }

}
