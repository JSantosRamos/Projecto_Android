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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

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
                fragment = new ListaVeiculosFragment();
                break;
            case R.id.navFavoritos:
                fragment = new ListaFavoritosFragment();
                break;

            case R.id.navTestdrive:
                fragment = new ListaTestdrivesFragment();
                break;

            case R.id.navMeusVeiculos:
                fragment = new ListaMeusVeiculosFragament();
                break;

            case R.id.navPerfil:
                fragment = new PerfilFragment();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
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

       /* SharedPreferences sharedPref = this.getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
        int idUser = sharedPref.getInt(LoginActivity.ID_KEY, -1);

        ImageDbHelper imageDbHelper = new ImageDbHelper(this);
        Bitmap bitmap = imageDbHelper.getImage(idUser);
        if (bitmap != null) {
            ivPerfil.setImageBitmap(bitmap);
        }*/
    }
}