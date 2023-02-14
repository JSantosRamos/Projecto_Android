package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class SingletonUtilizador {
    private final static String mUrlApiLogin = "http://amsi.dei.estg.ipleiria.pt/api";
    private static SingletonUtilizador instancia = null;
    private static RequestQueue volleyQueue = null;
    private LoginListener loginListener;

    public SingletonUtilizador(Context context) {

    }

    public static synchronized SingletonUtilizador getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonUtilizador(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instancia;
    }

    public void loginAPI(final String email, final String password, Context context) {
        if (!JsonParserHelper.isConnectedInternet(context)) {
            Toast.makeText(context, R.string.semInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                mUrlApiLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // String id = JsonParserHelper.parserJsonLogin(response);
                        //ativar o listener
                       // if (loginListener != null) {
                        //    loginListener.onValidateLogin(id, email, context);
                      //  }

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        volleyQueue.add(stringRequest);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
