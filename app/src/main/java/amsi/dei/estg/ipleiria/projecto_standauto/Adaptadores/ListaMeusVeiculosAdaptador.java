package amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaMeusVeiculosAdaptador extends BaseAdapter {
    private final ArrayList<Venda> listaMeusVeiculos;
    private final Context context;
    private LayoutInflater lInflater;

    public ListaMeusVeiculosAdaptador(Context context, ArrayList<Venda> lista) {
        this.context = context;
        this.listaMeusVeiculos = lista;
    }

    @Override
    public int getCount() {
        return listaMeusVeiculos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMeusVeiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaMeusVeiculos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (lInflater == null) {
            lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item_lista_meus_veiculos, null);
        }

        ListaMeusVeiculosAdaptador.ViewHolderLista viewHL = (ListaMeusVeiculosAdaptador.ViewHolderLista) convertView.getTag();
        if (viewHL == null) {
            viewHL = new ListaMeusVeiculosAdaptador.ViewHolderLista(convertView);
            convertView.setTag(viewHL);
        }

        viewHL.update(listaMeusVeiculos.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvTitulo, tvMatricula;
        private ImageView ivImage;

        public ViewHolderLista(View view) {
            tvTitulo = view.findViewById(R.id.tvTituloMeusVeiculosLista);
            tvMatricula = view.findViewById(R.id.tvMatriculaMeusVeiculosLista);
            ivImage = view.findViewById(R.id.ivImageMeuVeiculoLista);
        }

        public void update(Venda venda) {
            tvTitulo.setText(venda.toString());
            tvMatricula.setText(venda.getVeiculo().getMatricula());

            if (!venda.getVeiculo().getImagem().isEmpty()) {
                String url = venda.getVeiculo().getImagem();
                url = url.replace("http://backendstand.test/", "http://10.0.2.2:80/");
                Glide.with(context).load(url).placeholder(R.drawable.ic_car_holo_light).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            } else {
                ivImage.setImageResource(R.drawable.ic_car_holo_light);
            }
        }
    }
}
