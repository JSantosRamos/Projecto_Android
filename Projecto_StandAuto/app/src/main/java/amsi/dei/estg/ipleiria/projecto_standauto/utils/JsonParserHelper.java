package amsi.dei.estg.ipleiria.projecto_standauto.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class JsonParserHelper {
    public static boolean parserJsonLogin(String resposta) {
        try {
            JSONObject jsonLogin = new JSONObject(resposta);
            if (jsonLogin.getBoolean("success")) {

                return true;
                //success = jsonLogin.getBoolean("success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isConnectedInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.
                        getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

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
                double preco = jsonVeiculo.getDouble("price");
                String imagem = jsonVeiculo.getString("image");
                String matricula = jsonVeiculo.getString("plate");
                String descricao = jsonVeiculo.getString("description");
                String titulo = jsonVeiculo.getString("title");
                int cv = jsonVeiculo.getInt("cv");

                //Veiculo veiculoAPI = new Veiculo(id, marca, modelo, serie, segmento, combustivel, km, motor, cor, ano, nportas, caixa, matricula, imagem, preco, cv, titulo, descricao);
                Veiculo veiculoAPI = new Veiculo(id, marca, ano, imagem, combustivel);
                veiculosLista.add(veiculoAPI);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return veiculosLista;
    }
}
