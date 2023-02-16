package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.SignupListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Reserva.Reserva;
import amsi.dei.estg.ipleiria.projecto_standauto.views.LoginActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Favorito.FavoritosDBHelper;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User.User;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonVeiculos {

    private static SingletonVeiculos instancia = null;

    private final String urlLoginAPI = "http://10.0.2.2:80/api/users/login";
    private final String urlVeiculosAPI = "http://10.0.2.2:80/api/vehicles";
    private final String urlSignupAPI = "http://10.0.2.2:80/api/users";
    private final String urlReservaAPI = "http://10.0.2.2:80/api/reserves";

    private ArrayList<Veiculo> veiculos;

    private static RequestQueue mRequestQueue;
    private FavoritosDBHelper favoritosDBHelper = null;

    private LoginListener loginListener;
    private SignupListener signupListener;
    private VeiculosListener veiculosListener;

    public static synchronized SingletonVeiculos getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonVeiculos(context);
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return instancia;
    }

    private SingletonVeiculos(Context context) {
        veiculos = new ArrayList<>();
        favoritosDBHelper = new FavoritosDBHelper(context);
    }

    //region Veículos
    public Veiculo getVeiculo(int idVeiculo) {
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getId() == idVeiculo) {
                return veiculo;
            }
        }
        return null;
    }

    public ArrayList<Veiculo> getAllVeiculos() {
        return veiculos;
    }

    //API
    public void getAllVeiculosAPI(final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlVeiculosAPI,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        veiculos = JsonParserHelper.parserJsonVeiculos(response);

                        if (veiculosListener != null) {
                            veiculosListener.onRefreshlista(veiculos);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }) {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
                String email = sharedPref.getString(LoginActivity.EMAIL_KEY, "");
                String password = sharedPref.getString(LoginActivity.PASSWORD_KEY, "");

                String credentials = email + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        mRequestQueue.add(jsonArrayRequest);
    }

    public void setVeiculosListener(VeiculosListener veiculosListener) {
        this.veiculosListener = veiculosListener;
    }

    public void adicionarVeiculoFavoritosBD(int idVeiculo, int idUser) {
        favoritosDBHelper.adicionarFavoritoDB(idVeiculo, idUser);
    }

    public void removerVeiculoFavoritosBD(int idVeiculo, int idUser) {
        favoritosDBHelper.removerFavoritoDB(idVeiculo, idUser);
    }
    //endregion

    //region User
    public void loginAPI(final String email, final String password, Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLoginAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = JsonParserHelper.parserJsonLogin(response);
                if (user != null) {
                    SharedPreferences sharedPrefUser = context.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedEditor = sharedPrefUser.edit();
                    sharedEditor.putString(LoginActivity.EMAIL_KEY, email);
                    sharedEditor.putString(LoginActivity.PASSWORD_KEY, password);
                    sharedEditor.putInt(LoginActivity.ID_KEY, user.getId());
                    sharedEditor.putString(LoginActivity.NOME_KEY, user.getName());
                    sharedEditor.apply();
                }

                if (loginListener != null) {
                    loginListener.onValidateLogin(user, context);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.loginInvalido, Toast.LENGTH_LONG).show();//display the response on screen
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

    public void signupAPI(final String nome, final String email, final String password, Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlSignupAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String resposta = JsonParserHelper.parserJsonSignup(response);

                        if (loginListener != null) {
                            boolean sucesso = resposta.equals("true");
                            signupListener.onValidateSignup(sucesso, resposta, context);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", nome);
                    jsonBody.put("password", password);
                    jsonBody.put("email", email);
                    String requestBody = jsonBody.toString();

                    return requestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException ex) {
                    Log.d("Error", ex.getMessage());
                    return null;
                }
            }
        };

        mRequestQueue.add(stringRequest);

    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
    public void setSignupListener(SignupListener signupListener) {
        this.signupListener = signupListener;
    }
    //endregion

    //region Veiculos Favoritos
    public ArrayList<Veiculo> getFavoritosBD(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
        int idUser = sharedPref.getInt(LoginActivity.ID_KEY, -1);

        if (veiculos == null) {
            veiculos = getAllVeiculos();
        }

        ArrayList<Integer> idListVeiculos = favoritosDBHelper.getAllFavoritosBD(idUser);

        ArrayList<Veiculo> newList = new ArrayList<>();

        for (int idVeiculo :
                idListVeiculos) {
            for (Veiculo veiculo : veiculos) {
                if (veiculo.getId() == idVeiculo) {
                    newList.add(veiculo);
                }
            }
        }

        return newList;
    }

    public boolean verificarVeiculoFavoritoBD(int idVeiculo, int idUser) {
        return favoritosDBHelper.verificarVeiculoFavortioDB(idVeiculo, idUser);
    }
    //endregion

    //region Reserva

    public void adicionarReserva(Reserva reserva, Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlReservaAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Reserva registada.", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    // request body goes here
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("idVehicle", reserva.getIdVehicle());
                    jsonBody.put("number", reserva.getNumero());
                    jsonBody.put("nif", reserva.getNif());
                    jsonBody.put("morada", reserva.getMorada());
                    jsonBody.put("image", reserva.getCc());

                    String requestBody = jsonBody.toString();

                    return requestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException ex) {
                    Log.d("Error", ex.getMessage());
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                SharedPreferences sharedPref = context.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
                String email = sharedPref.getString(LoginActivity.EMAIL_KEY, "");
                String password = sharedPref.getString(LoginActivity.PASSWORD_KEY, "");

                Map<String, String> headers = new HashMap<>();

                String credentials = email + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    //endregion
}
