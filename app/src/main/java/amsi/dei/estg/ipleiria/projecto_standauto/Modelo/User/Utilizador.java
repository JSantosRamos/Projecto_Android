package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User;

public class Utilizador {
    private long id;
    private String nome;
    private String email;
    private String password;
    private String numero;
    private String nif;

    public Utilizador(long id, String nome, String email, String password, String numero, String nif){
        this.id = 0;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.numero = numero;
        this.nif = nif;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
