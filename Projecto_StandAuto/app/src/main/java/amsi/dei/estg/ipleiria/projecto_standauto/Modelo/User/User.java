package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User;

public class User {
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String name;
    private String email;
}
