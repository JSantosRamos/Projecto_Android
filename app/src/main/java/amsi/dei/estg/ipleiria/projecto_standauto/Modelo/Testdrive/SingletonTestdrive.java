package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive;

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

import amsi.dei.estg.ipleiria.projecto_standauto.views.LoginActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.views.DetalhesTestdriveActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdriveListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdrivesListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonTestdrive {
    private static SingletonTestdrive instancia = null;

    private final String urlTestdriveAPI = "http://10.0.2.2:80/api/testdrives";
    private ArrayList<Testdrive> testdrives;

    private TestdriveDBHelper testdriveDBHelper = null;

    private TestdriveListener testdriveListener;
    private TestdrivesListener testdrivesListener;

    private static RequestQueue mRequestQueue;


    public static synchronized SingletonTestdrive getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonTestdrive(context);
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return instancia;
    }

    public SingletonTestdrive(Context context) {
        testdrives = new ArrayList<>();
        testdriveDBHelper = new TestdriveDBHelper(context);
    }

    public Testdrive getTestdrive(long idTestdrive) {
        for (Testdrive testdrive : testdrives) {
            if (testdrive.getId() == idTestdrive) {
                return testdrive;
            }
        }
        return null;
    }

    //region DB
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

    public void setTestdrives(ArrayList<Testdrive> list) {
        this.testdrives = list;
    }
    //endregion

    //region API
    public void getAllTestdrivesAPI(final Context context) {

        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlTestdriveAPI,
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
                        /*if (testdrive != null) {
                            testdriveListener.onRefreshDetalhes(DetalhesTestdriveActivity.OP_CODE_ADICIONAR);
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

    public void setTestdriveListener(TestdriveListener testdriveListener) {
        this.testdriveListener = testdriveListener;
    }
    //endregion
}