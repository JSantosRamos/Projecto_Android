package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import amsi.dei.estg.ipleiria.projecto_standauto.Listener.LoginListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User.User;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String DADOS_USER = "DADOS_USER";
    public static final String EMAIL_KEY = "EMAIL_KEY";
    public static final String NOME_KEY = "NOME_KEY";
    public static final String ID_KEY = "ID_KEY";
    public static final String PASSWORD_KEY = "PASSWORD_KEY";

    private Button btnLogin, btnSignup;
    private EditText etEmail, etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        etEmail = findViewById(R.id.etEmailLogin);
        etPwd = findViewById(R.id.etPasswordLogin);

        SharedPreferences sharedPref = this.getSharedPreferences(DADOS_USER, Context.MODE_PRIVATE);
        String email = sharedPref.getString(LoginActivity.EMAIL_KEY, "");
        String password = sharedPref.getString(LoginActivity.PASSWORD_KEY, "");

        etEmail.setText(email);
        etPwd.setText(password);

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
    public void onValidateLogin(User user, Context context) {
        if (user != null) {
            Intent intent = new Intent(this, MenuMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(context, R.string.loginInvalido, Toast.LENGTH_SHORT).show();
        }
    }
}