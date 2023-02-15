package amsi.dei.estg.ipleiria.projecto_standauto.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaVeiculoAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VeiculosListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaFavoritosFragment extends Fragment implements VeiculosListener {

    public static final int CODE_REQUEST_VER = 1;
    private ListView lvVeiculos;
    private ListaVeiculoAdaptador adaptador;
    public static final String VEICULO = "VEICULO";

    public ListaFavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_veiculos, container, false);

        setHasOptionsMenu(true);

        //obter instancia da ListView
        lvVeiculos = view.findViewById(R.id.lvVeiculos);

        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getFavoritosBD(getContext()));

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
    public void onResume() {
        super.onResume();
        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getFavoritosBD(getContext()));
        lvVeiculos.setAdapter(adaptador);
    }

    @Override
    public void onRefreshlista(ArrayList<Veiculo> list) {
        if (list != null) {
            lvVeiculos.setAdapter(new ListaVeiculoAdaptador(getContext(), list));
        }
    }
}