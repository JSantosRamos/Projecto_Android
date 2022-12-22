package amsi.dei.estg.ipleiria.projecto_standauto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaVeiculoAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;

public class ListaVeiculosFragment extends Fragment implements VeiculosListener {

    public static final int CODE_REQUEST_VER = 1;
    private ListView lvVeiculos;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
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

        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getVeiculosBD());

        //associar o adaptador à lista
        lvVeiculos.setAdapter(adaptador);

        lvVeiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentMail = new Intent(getContext(), DetalhesVeiculoActivity.class);
                intentMail.putExtra(VEICULO, id);
                startActivityForResult(intentMail, CODE_REQUEST_VER);
            }
        });

        SingletonVeiculos.getInstance(getContext()).setVeiculosListener(this);

        return view;
    }

    @Override
    public void onRefreshlista(ArrayList<Veiculo> list) {
        if (list != null) {
            lvVeiculos.setAdapter(new ListaVeiculoAdaptador(getContext(), list));
        }
    }
}