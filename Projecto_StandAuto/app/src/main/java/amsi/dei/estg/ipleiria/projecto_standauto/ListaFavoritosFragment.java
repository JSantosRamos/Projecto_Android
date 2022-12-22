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

public class ListaFavoritosFragment extends Fragment implements VeiculosListener {
/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*
    public ListaFavoritosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaFavoritosFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static ListaFavoritosFragment newInstance(String param1, String param2) {
        ListaFavoritosFragment fragment = new ListaFavoritosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_favoritos, container, false);
    }
    */

    public static final int CODE_REQUEST_VER = 1;
    private ListView lvVeiculos;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private ListaVeiculoAdaptador adaptador;
    public static final String VEICULO = "amsi.dei.estg.ipleiria.projecto_standauto.veiculo";

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

        adaptador = new ListaVeiculoAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getFavoritosBD());

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