package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos;

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

import amsi.dei.estg.ipleiria.projecto_standauto.DetalhesTestdriveActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdriveListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdrivesListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VendasListener;
import amsi.dei.estg.ipleiria.projecto_standauto.LoginActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.TestdriveDBHelper;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonVeiculos {

    private static SingletonVeiculos instancia = null;

    private final String urlLoginAPI = "http://10.0.2.2:80/api/users/login";
    private final String urlVeiculosAPI = "http://10.0.2.2:80/api/vehicles";
    private final String urlTestdriveAPI = "http://10.0.2.2:80/api/testdrives";
    private final String urlTestdrivesUserAPI = "http://10.0.2.2:80/api/testdrives";
    private final String urlSignupAPI = "http://10.0.2.2:80/api/users";
    private final String urlVendasAPI = "http://10.0.2.2:80/api/vendas/info";

    private ArrayList<Veiculo> veiculos;
    private ArrayList<Testdrive> testdrives;
    private ArrayList<Venda> vendas;

    private static RequestQueue mRequestQueue;

    private VeiculosDBHelper veiculosDBHelper = null;
    private TestdriveDBHelper testdriveDBHelper = null;

    private LoginListener loginListener;
    private VeiculosListener veiculosListener;
    private TestdriveListener testdriveListener;
    private TestdrivesListener testdrivesListener;
    private VendasListener vendasListener;


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

        testdrives = new ArrayList<>();
        testdriveDBHelper = new TestdriveDBHelper(context);
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

    public void adicionarVeiculosBD(ArrayList<Veiculo> list) {
        veiculosDBHelper.removerAllVeiculosBD();

        for (Veiculo vei : list) {
            adicionarVeiculoBD(vei);
        }
    }

    public void adicionarVeiculoBD(Veiculo veiculo) {
        veiculosDBHelper.adicionarVeiculoBD(veiculo);
    }

    public ArrayList<Veiculo> getAllVeiculos() {
        return veiculos;
    }

    public ArrayList<Veiculo> getFavoritosBD() {

        if (veiculos == null) {
            veiculos = getAllVeiculos();
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

    public boolean verificarVeiculoFavoritoBD(int idVeiculo) {
        return veiculosDBHelper.verificarVeiculoFavortiosDB(idVeiculo);
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
                boolean sucesso = JsonParserHelper.parserJsonLogin(response);
                if (sucesso) {
                    SharedPreferences sharedPrefUser = context.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedEditor = sharedPrefUser.edit();
                    sharedEditor.putString(LoginActivity.EMAIL_KEY, email);
                    sharedEditor.putString(LoginActivity.PASSWORD_KEY, password);
                    sharedEditor.apply();
                }

                if (loginListener != null) {
                    loginListener.onValidateLogin(sucesso, email, password, context);
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
                        if (resposta.equals("true")) {
                            Toast.makeText(context, "A sua conta foi registada!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
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
                    // request body goes here
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

    public void adicionarVeiculoFavoritosBD(Veiculo veiculo) {
        veiculosDBHelper.adicionarVeiculoFavoritosDB(veiculo);
    }

    public void removerVeiculoFavoritosBD(Veiculo veiculo) {
        veiculosDBHelper.removerVeiculoFavoritos(veiculo.getId());
    }
    //endregion

    //region TestDrives
    public Testdrive getTestdrive(long idTestdrive) {
        for (Testdrive testdrive : testdrives) {
            if (testdrive.getId() == idTestdrive) {
                return testdrive;
            }
        }
        return null;
    }

    public void adicionarTestdriveBD(Testdrive testdrive) {

        testdriveDBHelper.adicionarTestdriveBD(testdrive);
    }

    public void adicionarTestdrivesBD(ArrayList<Testdrive> list) {
        testdriveDBHelper.removeAllTestdrivesDB();

        for (Testdrive td : list) {
            adicionarTestdriveBD(td);
        }
    }

    public void removerTestdriveDB(long id) {

        Testdrive testdrive = getTestdrive(id);
        if (testdrive != null) {
            if (testdriveDBHelper.removerTestdrive(testdrive.getId())) {
                testdrives.remove(testdrive);
            }
        }
    }

    public void editarTestdriveBD(Testdrive dadosTestdrive) {
        Testdrive testdrive = getTestdrive(dadosTestdrive.getId());
        if (testdrive != null) {
            testdriveDBHelper.editarTestdriveBD(dadosTestdrive);
        }
    }

    public ArrayList<Testdrive> getTestdrivesBD() {

        return testdrives = testdriveDBHelper.getAllTestdrivesBD();
    }

    public ArrayList<Testdrive> getAllTestdrives() {

        return testdrives;
    }

    public void setTestdrives(ArrayList<Testdrive> list) {

        this.testdrives = list;
    }


    //API
    public void getAllTestdrivesAPI(final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlTestdrivesUserAPI,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        testdrives = JsonParserHelper.parserJsonTestDrives(response);
                        adicionarTestdrivesBD(testdrives);

                        if (testdrivesListener != null) {
                            testdrivesListener.onRefreshlistaTestdrives(testdrives);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
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

        mRequestQueue.add(jsonArrayRequest);
    }

    public void adicionarTestdriveAPI(final Testdrive testdrive, final Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlTestdriveAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        adicionarTestdriveBD(JsonParserHelper.parserJsonTestdrive(response));
                       /* if (testdrive != null) {
                            listenerTestdrive.onRefreshDetalhes(DetalhesTestdriveActivity.OP_CODE_ADICIONAR);
                        }*/
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
                    jsonBody.put("date", testdrive.getData());
                    jsonBody.put("time", testdrive.getHora());
                    jsonBody.put("description", testdrive.getMotivo());
                    jsonBody.put("idVehicle", testdrive.getIdVeiculo()); //
                    // o estado é sempre criado default "por ver" e o id do utilizador é obtido pela autencicação "auth" do header.
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

    public void editarTestdriveAPI(final Testdrive testdrive, final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                urlTestdriveAPI + "/" + testdrive.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editarTestdriveBD(testdrive);

                        if (testdriveListener != null) {
                            testdriveListener.onRefreshDetalhes(DetalhesTestdriveActivity.OP_CODE_EDITAR);
                        }
                        Toast.makeText(context, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
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
                    jsonBody.put("date", testdrive.getData());
                    jsonBody.put("time", testdrive.getHora());
                    jsonBody.put("description", testdrive.getMotivo());

                    String requestBody = jsonBody.toString();

                    return requestBody.getBytes(StandardCharsets.UTF_8);
                } catch (JSONException ex) {
                    return null;
                }
            }

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

        mRequestQueue.add(stringRequest);
    }

    public void removerTestdriveAPI(final Testdrive testdrive, final Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                urlTestdriveAPI + "/" + testdrive.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        removerTestdriveDB(testdrive.getId());
                        if (testdriveListener != null) {
                            testdriveListener.onRefreshDetalhes(DetalhesTestdriveActivity.OP_CODE_APAGAR);
                        }
                        Toast.makeText(context, "Removido com sucesso", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
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

    public void setTestdrivesListener(TestdrivesListener testdrivesListener) {
        this.testdrivesListener = testdrivesListener;
    }
    //endregion

    public void getVendasAPI(final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlVendasAPI,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        vendas = JsonParserHelper.parserJsonVendas(response);

                        if (vendasListener != null) {
                            vendasListener.onRefreshlistaVendas(vendas);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
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

        mRequestQueue.add(jsonArrayRequest);
    }


    public ArrayList<Venda> getMeusVeiculos() {

        ArrayList<Venda> v = new ArrayList<>();
        if (vendas != null) {
            v = vendas;
        }
        return v;
    }

    public Venda getVenda(int idVenda) {
        for (Venda venda : vendas) {
            if (venda.getId() == idVenda) {
                return venda;
            }
        }
        return null;
    }

    public void setVendasListener(VendasListener vendasListener) {
        this.vendasListener = vendasListener;
    }
}
