package amsi.dei.estg.ipleiria.projecto_standauto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaVeiculoAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;

public class ListaVeiculosFragment extends Fragment {
    private ListView lvVeiculos;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private ListaVeiculoAdaptador adaptador;

    public ListaVeiculosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_veiculos, container, false);

        setHasOptionsMenu(true);

        //obter instancia da ListView
        lvVeiculos = view.findViewById(R.id.lvVeiculos);
        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getVeiculos());

        //associar o adaptador à lista
        lvVeiculos.setAdapter(adaptador);

        return view;
    }
}