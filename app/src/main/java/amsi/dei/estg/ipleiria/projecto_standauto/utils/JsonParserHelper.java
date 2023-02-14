package amsi.dei.estg.ipleiria.projecto_standauto.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User.User;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;

public class JsonParserHelper {
    public static User parserJsonLogin(String resposta) {
        try {
            JSONObject jsonLogin = new JSONObject(resposta);
            if (jsonLogin.getBoolean("success")) {
                int id = jsonLogin.getInt("id");
                String email = jsonLogin.getString("email");
                String name = jsonLogin.getString("name");

                return new User(id, name, email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parserJsonSignup(String resposta) {
        try {
            JSONObject jsonSignup = new JSONObject(resposta);
            boolean sucesso = jsonSignup.getBoolean("sucesso");
            String mensagem = jsonSignup.getString("message");

            if (sucesso) {
                return "true";
            }

            return mensagem;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Registo inválido";
    }

    public static Boolean isConnectedInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.
                        getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    //region Veiculos
    public static ArrayList<Veiculo> parserJsonVeiculos(JSONArray resposta) {
        ArrayList<Veiculo> veiculosLista = new ArrayList<>();

        try {
            for (int i = 0; i < resposta.length(); i++) {
                JSONObject jsonVeiculo = (JSONObject) resposta.get(i);
                int id = jsonVeiculo.getInt("id");
                String marca = jsonVeiculo.getString("brand");
                String modelo = jsonVeiculo.getString("model");
                String serie = jsonVeiculo.getString("serie");
                String segmento = jsonVeiculo.getString("type");
                String combustivel = jsonVeiculo.getString("fuel");
                String km = jsonVeiculo.getString("mileage");
                String motor = jsonVeiculo.getString("engine");
                String cor = jsonVeiculo.getString("color");
                int ano = jsonVeiculo.getInt("year");
                int nportas = jsonVeiculo.getInt("doorNumber");
                String caixa = jsonVeiculo.getString("transmission");
                int preco = jsonVeiculo.getInt("price");
                String imagem = jsonVeiculo.getString("image");
                String matricula = jsonVeiculo.getString("plate");
                String descricao = jsonVeiculo.getString("description");
                String titulo = jsonVeiculo.getString("title");
                int cv = jsonVeiculo.getInt("cv");
                JSONArray imagens = jsonVeiculo.getJSONArray("listImages");

                ArrayList<String> listaImagens = new ArrayList<>();
                for (int x = 0; x < imagens.length(); x++) {
                    listaImagens.add(imagens.getString(x));
                }

                Veiculo veiculoAPI = new Veiculo(id, marca, modelo, serie, segmento, combustivel, km, motor, cor, ano, nportas, caixa, matricula, imagem, preco, cv, titulo, descricao, listaImagens);
                veiculosLista.add(veiculoAPI);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return veiculosLista;
    }
    //endregion

    //region TestDrives
    public static ArrayList<Testdrive> parserJsonTestDrives(JSONArray resposta) {

        ArrayList<Testdrive> list = new ArrayList<Testdrive>();

        try {
            for (int i = 0; i < resposta.length(); i++) {

                JSONObject jsonTestdrive = (JSONObject) resposta.get(i);
                long id = jsonTestdrive.getLong("id");
                String data = jsonTestdrive.getString("date");
                String hora = jsonTestdrive.getString("time");
                String motivo = jsonTestdrive.getString("description");
                int idVeiculo = jsonTestdrive.getInt("idVehicle");
                String estado = jsonTestdrive.getString("status");

                Testdrive testdrive = new Testdrive(id, data, hora, motivo, estado, idVeiculo);
                list.add(testdrive);
            }
        } catch (
                JSONException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public static Testdrive parserJsonTestdrive(String resposta) {
        Testdrive testdrive = null;
        try {
            JSONObject jsonTestdrive = new JSONObject(resposta);
            long id = jsonTestdrive.getLong("id");
            String data = jsonTestdrive.getString("date");
            String hora = jsonTestdrive.getString("time");
            String motivo = jsonTestdrive.getString("description");
            String estado = jsonTestdrive.getString("status");
            int idVeiculo = jsonTestdrive.getInt("idVehicle");

            testdrive = new Testdrive(id, data, hora, motivo, estado, idVeiculo);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return testdrive;
    }
    //endregion

    //region Venda
    public static ArrayList<Venda> parserJsonVendas(JSONArray resposta) {
        ArrayList<Venda> vendasLista = new ArrayList<>();

        try {
            for (int i = 0; i < resposta.length(); i++) {
                JSONObject jsonVenda = (JSONObject) resposta.get(i);

                //Dados Venda
                JSONObject jsonDadosVenda = jsonVenda.getJSONObject("venda");
                int id = jsonDadosVenda.getInt("id");
                int idUserSeller = jsonDadosVenda.getInt("idUser_seller");
                int idUserBuyer = jsonDadosVenda.getInt("idUser_buyer");
                int idVehicle = jsonDadosVenda.getInt("idVehicle");
                double valor = jsonDadosVenda.getDouble("price");
                String comentario = jsonDadosVenda.getString("comment");
                String numero = jsonDadosVenda.getString("number");
                String nif = jsonDadosVenda.getString("nif");
                String morada = jsonDadosVenda.getString("address");
                String nome = jsonDadosVenda.getString("name");

                //Veiculo da Venda
                JSONObject jsonVeiculo = jsonVenda.getJSONObject("veiculo");
                int idVeiculo = jsonVeiculo.getInt("id");
                String marca = jsonVeiculo.getString("brand");
                String modelo = jsonVeiculo.getString("model");
                String serie = jsonVeiculo.getString("serie");
                String segmento = jsonVeiculo.getString("type");
                String combustivel = jsonVeiculo.getString("fuel");
                String km = jsonVeiculo.getString("mileage");
                String motor = jsonVeiculo.getString("engine");
                String cor = jsonVeiculo.getString("color");
                String descricao = jsonVeiculo.getString("description");
                int ano = jsonVeiculo.getInt("year");
                int nportas = jsonVeiculo.getInt("doorNumber");
                String caixa = jsonVeiculo.getString("transmission");
                int preco = jsonVeiculo.getInt("price");
                String imagem = jsonVeiculo.getString("image");
                int publicado = jsonVeiculo.getInt("isActive");
                String titulo = jsonVeiculo.getString("title");
                String matricula = jsonVeiculo.getString("plate");
                int cv = jsonVeiculo.getInt("cv");
                int idMarca = jsonVeiculo.getInt("idBrand");
                int idModel = jsonVeiculo.getInt("idModel");
                String estado = jsonVeiculo.getString("status");

                Veiculo meuVeiculo = new Veiculo(id, marca, modelo, serie, segmento, combustivel, km, motor, cor, ano, nportas, caixa, matricula, imagem, preco, cv, titulo, descricao, null);
                Venda venda = new Venda(id, valor, nif, morada, nome, meuVeiculo);
                vendasLista.add(venda);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return vendasLista;
    }
    //endregion
}
