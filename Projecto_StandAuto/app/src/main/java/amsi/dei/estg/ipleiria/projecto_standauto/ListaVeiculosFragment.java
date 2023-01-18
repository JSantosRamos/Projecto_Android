package amsi.dei.estg.ipleiria.projecto_standauto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaVeiculoAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class ListaVeiculosFragment extends Fragment implements VeiculosListener {

    public static final int CODE_REQUEST_VER = 1;
    private ListView lvVeiculos;
    private ListaVeiculoAdaptador adaptador;
    public static final String VEICULO = "amsi.dei.estg.ipleiria.projecto_standauto.veiculo";

    public ListaVeiculosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_veiculos, container, false);
        SingletonVeiculos.getInstance(getContext()).getAllVeiculosAPI(getContext());

        setHasOptionsMenu(true);

        //obter instancia da ListView
        lvVeiculos = view.findViewById(R.id.lvVeiculos);

        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getAllVeiculos());

        //associar o adaptador à lista
        lvVeiculos.setAdapter(adaptador);

        lvVeiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesVeiculoActivity.class);
                intent.putExtra(VEICULO, id);
                startActivityForResult(intent, CODE_REQUEST_VER);
            }
        });

        SingletonVeiculos.getInstance(getContext()).setVeiculosListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);

        MenuItem itemPequisa = menu.findItem(R.id.itemPesquisa);
        MenuItem itemFiltrar = menu.findItem(R.id.itemFiltrar);

        SearchView searchView = (SearchView) itemPequisa.getActionView();
        Button btnFilter = (Button) itemFiltrar.getActionView();
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Veiculo> newlist = new ArrayList<>();
                for (Veiculo veiculo : SingletonVeiculos.getInstance(getContext()).getAllVeiculos()) {
                    if (veiculo.getMarca().toUpperCase().contains(newText.toUpperCase())) {
                        newlist.add(veiculo);
                    }
                }
                lvVeiculos.setAdapter(new ListaVeiculoAdaptador(getContext(), newlist));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onRefreshlista(ArrayList<Veiculo> list) {
        if (list != null) {
            lvVeiculos.setAdapter(new ListaVeiculoAdaptador(getContext(), list));
        }
    }
}