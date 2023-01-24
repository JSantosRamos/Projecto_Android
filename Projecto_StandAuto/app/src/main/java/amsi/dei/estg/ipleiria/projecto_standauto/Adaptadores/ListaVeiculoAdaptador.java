package amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores;

import android.annotation.SuppressLint;
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

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;
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

        ViewHolderLista viewHL = (ViewHolderLista) convertView.getTag();
        if (viewHL == null) {
            viewHL = new ViewHolderLista(convertView);
            convertView.setTag(viewHL);
        }

        viewHL.update(listaVeiculos.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvMarca, tvAno, tvCombustivel, tvKm, tvPreco;
        private ImageView ivImage;

        public ViewHolderLista(View view) {

            tvMarca = view.findViewById(R.id.tvMarcaLista);
            tvAno = view.findViewById(R.id.tvAnoLista);
            tvCombustivel = view.findViewById(R.id.tvCombustivelLista);
            tvKm = view.findViewById(R.id.tvKmLista);
            tvPreco = view.findViewById(R.id.tvPrecoLista);
            ivImage = view.findViewById(R.id.ivImageMeuVeiculoLista);
        }

        @SuppressLint("SetTextI18n")
        public void update(Veiculo veiculo) {
            tvMarca.setText(" " + veiculo.getMarca());
            tvAno.setText(" " + veiculo.getAno());
            tvCombustivel.setText(" " + veiculo.getCombustivel());
            tvPreco.setText(" " + veiculo.getPreco() + " €");
            tvKm.setText(" " + veiculo.getQuilometros());

            String urlImage = veiculo.getImagem();
            urlImage = urlImage.replace("http://backendstand.test/", "http://10.0.2.2:80/");

            if (!veiculo.getImagem().isEmpty()) {
                Glide.with(context).load(urlImage).placeholder(R.drawable.ic_car).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            } else {
                ivImage.setImageResource(R.drawable.ic_car);
            }
        }
    }
}