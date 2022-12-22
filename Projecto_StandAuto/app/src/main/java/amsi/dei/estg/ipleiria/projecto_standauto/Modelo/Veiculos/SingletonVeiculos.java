package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonVeiculos {

    private static SingletonVeiculos instancia = null;
    private ArrayList<Veiculo> veiculos;
    private LoginListener loginListener;
    private VeiculosListener veiculosListener;

    private static RequestQueue mRequestQueue;
    private final String urlLoginAPI = "http://10.0.2.2:80/api/users/login";
    private final String urlVeiculosAPI = "http://10.0.2.2:80/api/vehicles";

    private VeiculosDBHelper veiculosDBHelper = null;

    public static synchronized SingletonVeiculos getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonVeiculos(context);
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return instancia;
    }

    private SingletonVeiculos(Context context) {
        veiculos = new ArrayList<>();
        veiculosDBHelper = new VeiculosDBHelper(context);
    }

    public Veiculo getVeiculo(int idVeiculo) {
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getId() == idVeiculo) {
                return veiculo;
            }
        }
        return null;
    }

    public void adicionarVeiculosBD(ArrayList<Veiculo> list) {
        veiculosDBHelper.removerAllVeiculosBD();

        for (Veiculo vei : list) {
            adicionarVeiculoBD(vei);
        }
    }

    public void adicionarVeiculoBD(Veiculo veiculo) {
        veiculosDBHelper.adicionarVeiculoBD(veiculo);
    }

    public ArrayList<Veiculo> getVeiculosBD() {
        veiculos = veiculosDBHelper.getAllVeiculosBD();
        return veiculos;
    }

    public ArrayList<Veiculo> getFavoritosBD() {

        if (veiculos == null) {
            veiculos = getVeiculosBD();
        }

        ArrayList<Integer> idList = veiculosDBHelper.getAllFavoritosBD();

        ArrayList<Veiculo> newList = new ArrayList<>();

        for (int id :
                idList) {
            for (Veiculo veiculo : veiculos) {
                if (veiculo.getId() == id) {
                    newList.add(veiculo);
                }
            }
        }

        return newList;
    }

    //API
    public void getAllVeiculosAPI(final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
        }

        // String Request initialized
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlVeiculosAPI,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        veiculos = JsonParserHelper.parserJsonVeiculos(response);
                        adicionarVeiculosBD(veiculos);

                        if (veiculosListener != null) {
                            veiculosListener.onRefreshlista(veiculos);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ResponseF:" + error.getMessage(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                String credentials = "admin@gmail.com" + ":" + "admin123";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        mRequestQueue.add(jsonArrayRequest);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setVeiculosListener(VeiculosListener veiculosListener) {
        this.veiculosListener = veiculosListener;
    }

    public void loginAPI(final String email, final String password, Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLoginAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean sucesso = JsonParserHelper.parserJsonLogin(response); //resposta é "true" ou "false" depende se o user existe ou não

                if (loginListener != null) {
                    loginListener.onValidateLogin(sucesso, email, context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response:" + error.getMessage(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                String credentials = email + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    public boolean adicionarVeiculoFavoritosBD(Veiculo veiculo) {
        return veiculosDBHelper.adicionarVeiculoFavoritosDB(veiculo);
    }
}
