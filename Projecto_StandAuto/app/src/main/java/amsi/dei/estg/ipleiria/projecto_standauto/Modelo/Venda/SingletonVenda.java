package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VendasListener;
import amsi.dei.estg.ipleiria.projecto_standauto.views.LoginActivity;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonVenda {

    private static SingletonVenda instancia = null;

    private final String urlVendasAPI = "http://10.0.2.2:80/api/vendas/info";

    private ArrayList<Venda> vendas;
    private static RequestQueue mRequestQueue;
    private VendasListener vendasListener;
    private VendaDBHelper vendasDBHelper = null;


    public static synchronized SingletonVenda getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonVenda(context);
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return instancia;
    }

    private SingletonVenda(Context context) {
        vendas = new ArrayList<>();
        vendasDBHelper = new VendaDBHelper(context);
    }


    //region DB
    public void adicionarVendaBD(Venda venda) {

        vendasDBHelper.adicionarVendaBD(venda);
    }

    public void adicionarVendasBD(ArrayList<Venda> list) {
        vendasDBHelper.removeAllVendasDB();

        for (Venda venda : list) {
            adicionarVendaBD(venda);
        }
    }

    public ArrayList<Venda> getVendasBD() {

        return vendas = vendasDBHelper.getAllVendasBD();
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
                Toast.makeText(context, "Não possui veículos", Toast.LENGTH_LONG).show();
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

    public ArrayList<Venda> getMeusVeiculos() { //o getVendasAPI obteve todas as vendas realizadas ao utilizador

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
