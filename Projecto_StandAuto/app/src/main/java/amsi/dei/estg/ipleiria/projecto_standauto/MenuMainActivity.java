package amsi.dei.estg.ipleiria.projecto_standauto;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nV;
    private DrawerLayout dL;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dL = findViewById(R.id.drawerLayout);
        nV = findViewById(R.id.navView);

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
    }
}