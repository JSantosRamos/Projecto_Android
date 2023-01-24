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

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaMeusVeiculosAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.VendasListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.SingletonVenda;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaMeusVeiculosFragament extends Fragment implements VendasListener {

    public static final int CODE_REQUEST_VER = 1;
    private ListView lvMeusVeiculos;
    private ListaMeusVeiculosAdaptador adaptador;
    public static final String VENDA = "VENDA";

    public ListaMeusVeiculosFragament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_meus_veiculos, container, false);
        SingletonVenda.getInstance(getContext()).getVendasAPI(getContext());
        setHasOptionsMenu(true);

        lvMeusVeiculos = view.findViewById(R.id.lvMeusVeiculos);
        adaptador = new ListaMeusVeiculosAdaptador(getContext(), SingletonVenda.getInstance(getContext()).getVendasBD());
        lvMeusVeiculos.setAdapter(adaptador);

        lvMeusVeiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), InfoVeiculoActivity.class);
                intent.putExtra(VENDA, id);
                startActivityForResult(intent, CODE_REQUEST_VER);
            }
        });

        SingletonVenda.getInstance(getContext()).setVendasListener(this);
        return view;
    }

    @Override
    public void onRefreshlistaVendas(ArrayList<Venda> lista) {
        if (lista != null) {
            lvMeusVeiculos.setAdapter(new ListaMeusVeiculosAdaptador(getContext(), lista));
        }
    }
}