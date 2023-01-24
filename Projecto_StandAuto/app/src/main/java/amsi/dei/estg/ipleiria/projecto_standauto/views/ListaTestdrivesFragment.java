package amsi.dei.estg.ipleiria.projecto_standauto.views;

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

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores.ListaTestdriveAdaptador;
import amsi.dei.estg.ipleiria.projecto_standauto.Listener.TestdrivesListener;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.SingletonTestdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.R;
import amsi.dei.estg.ipleiria.projecto_standauto.views.DetalhesTestdriveActivity;

public class ListaTestdrivesFragment extends Fragment implements TestdrivesListener {

    public static final int CODE_REQUEST_EDITAR = 2;
    private ListView lvTestdrives;
    private ListaTestdriveAdaptador adaptadorTD;
    public static final String TESTDRIVE = "TESTDRIVE";

    public ListaTestdrivesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_testdrives, container, false);

        SingletonTestdrive.getInstance(getContext()).getAllTestdrivesAPI(getContext()); // Retorna todos os testdrives do utilizador registado
        setHasOptionsMenu(true);

        lvTestdrives = view.findViewById(R.id.lvTestdrive);
        adaptadorTD = new ListaTestdriveAdaptador(getContext(), SingletonTestdrive.getInstance(getContext()).getTestdrivesBD());
        lvTestdrives.setAdapter(adaptadorTD);

        lvTestdrives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesTestdriveActivity.class);
                intent.putExtra(TESTDRIVE, id);
                startActivityForResult(intent, CODE_REQUEST_EDITAR);
            }
        });

        SingletonTestdrive.getInstance(getContext()).setTestdrivesListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //atualizar a lista
            SingletonTestdrive.getInstance(getContext()).getAllTestdrivesAPI(getContext());
            lvTestdrives.setAdapter(new ListaTestdriveAdaptador(getContext(), SingletonTestdrive.getInstance(getContext()).getTestdrivesBD()));
        }
    }

    @Override
    public void onRefreshlistaTestdrives(ArrayList<Testdrive> lista) {
        if (lista != null) {
            lvTestdrives.setAdapter(new ListaTestdriveAdaptador(getContext(), lista));
        }
    }
}