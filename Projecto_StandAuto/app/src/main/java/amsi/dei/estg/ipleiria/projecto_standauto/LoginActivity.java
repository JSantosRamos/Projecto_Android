package amsi.dei.estg.ipleiria.projecto_standauto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.utils.JsonParserHelper;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private Button btnLogin, btnSignup;
    private EditText etEmail, etPwd;

    //private RequestQueue mRequestQueue;
    // private StringRequest mStringRequest;
    //private String url = "http://10.0.2.2:80/api/users/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        etEmail = findViewById(R.id.etEmailLogin);
        etPwd = findViewById(R.id.etPasswordLogin);

        etEmail.setText("admin@gmail.com");
        etPwd.setText("admin123");

        //Listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarLogin();
            }
        });

        SingletonVeiculos.getInstance(this).setLoginListener(this);
    }

    private void validarLogin() {
        String email = etEmail.getText().toString();
        String password = etPwd.getText().toString();

        if (!isEmailValido(email)) {
            etEmail.setError(getString(R.string.erroEmail));
        }

        if (!isPasswordValida(password)) {
            etPwd.setError(getString(R.string.erroPass));
        }

        SingletonVeiculos.getInstance(this).loginAPI(email, password, this);

        // RequestQueue initialized
        /*mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (JsonParserHelper.parserJsonLogin(response)) {
                    startActivity(menuMain);
                }
                // Toast.makeText(getApplicationContext(), "ResponseT:" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
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

        mRequestQueue.add(mStringRequest);*/
    }

    private boolean isEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        return password.length() >= 8;
    }

    @Override
    public void onValidateLogin(boolean success, String email, Context context) {
        if (success){
            Intent intent = new Intent(this, MenuMainActivity.class);
            intent.putExtra(email, email);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(context, "Login Inválido, Tente novamente", Toast.LENGTH_SHORT).show();
        }
    }
}