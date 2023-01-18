package amsi.dei.estg.ipleiria.projecto_standauto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaTestdriveAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdrivesListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.SingletonVeiculos;

public class ListaTestdrivesFragment extends Fragment implements TestdrivesListener {

    public static final int CODE_REQUEST_ADICIONAR = 1;
    public static final int CODE_REQUEST_EDITAR = 2;

    private ListView lvTestdrives;
    private ListaTestdriveAdaptador adaptadorTD;
    public static final String TESTDRIVE = "amsi.dei.estg.ipleiria.projecto_stand.testdrive";

    public ListaTestdrivesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_lista_testdrives, container, false);

        SingletonVeiculos.getInstance(getContext()).getAllTestdrivesAPI(getContext()); // Retorna todos os testdrives do utilizador registado
        setHasOptionsMenu(true);

        lvTestdrives = view.findViewById(R.id.lvTestdrive);
        adaptadorTD = new ListaTestdriveAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getAllTestdrives());
        lvTestdrives.setAdapter(adaptadorTD);

        lvTestdrives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesTestdriveActivity.class);
                intent.putExtra(TESTDRIVE, id);
                startActivityForResult(intent, CODE_REQUEST_EDITAR);
            }
        });

        SingletonVeiculos.getInstance(getContext()).setTestdrivesListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            //atualizar a lista
            lvTestdrives.setAdapter(new ListaTestdriveAdaptador(getContext(), SingletonVeiculos.getInstance(getContext()).getAllTestdrives()));
            switch (requestCode) {
                case CODE_REQUEST_ADICIONAR:
                    Snackbar.make(getView(), "Adicionado com sucesso", Snackbar.LENGTH_SHORT).show();
                    break;
                case CODE_REQUEST_EDITAR:
                    int op = data.getIntExtra(DetalhesTestdriveActivity.OP_CODE, 0);
                    if (op == DetalhesTestdriveActivity.OP_CODE_EDITAR) {
                        Snackbar.make(getView(), "Alterado com sucesso", Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onRefreshlistaTestdrives(ArrayList<Testdrive> lista) {
        if (lista != null) {
            lvTestdrives.setAdapter(new ListaTestdriveAdaptador(getContext(), lista));
        }
    }
}