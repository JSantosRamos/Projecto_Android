package amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos.Veiculo;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaVeiculoAdaptador extends BaseAdapter {

    private ArrayList<Veiculo> listaVeiculos;
    private Context context;
    private LayoutInflater lInflater;

    public ListaVeiculoAdaptador(Context context, ArrayList<Veiculo> lista) {
        this.context = context;
        this.listaVeiculos = lista;
    }

    @Override
    public int getCount() {
        return listaVeiculos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaVeiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaVeiculos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (lInflater == null) {
            lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item_lista_veiculo, null);
        }

        //preenchimento do view
        ViewHolderLista viewHL = (ViewHolderLista) convertView.getTag();
        if (viewHL == null) {
            viewHL = new ViewHolderLista(convertView);
            convertView.setTag(viewHL);
        }

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvMarca;

        public ViewHolderLista(View view) {
            tvMarca = view.findViewById(R.id.tvMarcaLista);
        }
    }
}
