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
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonVeiculos extends AppCompatActivity {

    private static SingletonVeiculos instancia = null;
    private ArrayList<Veiculo> veiculos;
    private LoginListener loginListener;

    private static RequestQueue mRequestQueue;
    private String urlLoginAPI = "http://10.0.2.2:80/api/users/login";


    public static synchronized SingletonVeiculos getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonVeiculos(context);
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return instancia;
    }

    private SingletonVeiculos(Context context) {
        veiculos = new ArrayList<>();
    }

    public Veiculo getVeiculo(int idVeiculo) {
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getId() == idVeiculo) {
                return veiculo;
            }
        }
        return null;
    }


    public ArrayList<Veiculo> getVeiculos() {
        return veiculos = getFromAPI();
    }


    private ArrayList<Veiculo> getFromAPI() {

        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        String url = "http://10.0.2.2:80/api/vehicles";

        ArrayList<Veiculo> veiculosAPI = new ArrayList<Veiculo>();

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        veiculos = JsonParserHelper.parserJsonVeiculos(response);
                        String texto = "";

                        if (veiculos.size() > 0) {
                            Veiculo veiculo = veiculos.get(0);
                            texto = "Id: " + veiculo.getId() + "Marca: " + veiculo.getMarca();
                        } else {
                            texto = "Noooo";
                        }

                        //Toast.makeText(getApplicationContext(), "ResponseT:" + texto, Toast.LENGTH_LONG).show();//display the response on screen
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ResponseF:" + error.getMessage(), Toast.LENGTH_LONG).show();//display the response on screen
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

        return veiculosAPI;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
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
                Toast.makeText(getApplicationContext(), "ResponseF:" + error.getMessage(), Toast.LENGTH_LONG).show();//display the response on screen
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
}
