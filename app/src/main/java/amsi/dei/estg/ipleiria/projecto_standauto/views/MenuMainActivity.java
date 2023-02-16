package amsi.dei.estg.ipleiria.projecto_standauto.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nV;
    private DrawerLayout dL;
    private FragmentManager fragmentManager;
    private ImageView ivPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        boolean reserva = getIntent().getBooleanExtra(ReservaActivity.ADICIONADO, false);
        if(reserva){
            SingletonVeiculos.getInstance(this).getAllVeiculosAPI(this);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dL = findViewById(R.id.drawerLayout);
        nV = findViewById(R.id.navView);
        ivPerfil = findViewById(R.id.ivHeader);

        fragmentManager = getSupportFragmentManager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dL, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();

        dL.addDrawerListener(toggle);
        nV.setNavigationItemSelectedListener(this);

        carregarHeader();
        carregarFragmentoInicial();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int opcao = item.getItemId();
        Fragment fragment = null;
        switch (opcao) {
            case R.id.navLista:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Veículos");
                fragment = new ListaVeiculosFragment();
                break;
            case R.id.navFavoritos:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Favoritos");
                fragment = new ListaFavoritosFragment();
                break;

            case R.id.navTestdrive:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Test-Drives");
                fragment = new ListaTestdrivesFragment();
                break;

            case R.id.navMeusVeiculos:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Meus veículos");
                fragment = new ListaMeusVeiculosFragament();
                break;

            case R.id.navPerfil:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Perfil");
                fragment = new PerfilFragment();
                break;

            case R.id.navSair:
                SharedPreferences sharedPref = this.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
                sharedPref.edit().clear().apply();
                Intent intent = new Intent(this, LoginActivity.class );
                startActivity(intent);
                finish();
                break;
        }

        if(fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        }
        dL.closeDrawer(GravityCompat.START);
        return false;
    }

    private boolean carregarFragmentoInicial() {
        Menu menu = nV.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    private void carregarHeader() {

    }
}