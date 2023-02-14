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

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.SingletonVeiculos;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaTestdriveAdaptador extends BaseAdapter {
    private ArrayList<Testdrive> listaTD;
    private Context context;
    private LayoutInflater lInflater;

    public ListaTestdriveAdaptador(Context context, ArrayList<Testdrive> lista) {
        this.context = context;
        this.listaTD = lista;
    }

    @Override
    public int getCount() {
        return listaTD.size();
    }

    @Override
    public Object getItem(int position) {
        return listaTD.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaTD.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (lInflater == null) {
            lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item_lista_testdrive, null);
        }

        ListaTestdriveAdaptador.ViewHolderLista viewHL = (ListaTestdriveAdaptador.ViewHolderLista) convertView.getTag();
        if (viewHL == null) {
            viewHL = new ListaTestdriveAdaptador.ViewHolderLista(convertView);
            convertView.setTag(viewHL);
        }

        viewHL.update(listaTD.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvDia, tvHora, tvEstado, tvVeiculo;
        private ImageView ivImage;

        public ViewHolderLista(View view) {

            tvDia = view.findViewById(R.id.tvDiaTestdriveLista);
            tvHora = view.findViewById(R.id.tvHoraTestdriveLista);
            tvEstado = view.findViewById(R.id.tvEstadoTestdriveLista);
            tvVeiculo = view.findViewById(R.id.tvVeiculoTestdriveLista);
            ivImage = view.findViewById(R.id.ivTestdriveLista);
        }

        public void update(Testdrive test) {
            tvDia.setText(test.getData());
            tvHora.setText(test.getHora());
            tvEstado.setText(test.getEstado());

            Veiculo veiculo = SingletonVeiculos.getInstance(context).getVeiculo(test.getIdVeiculo());
            if (veiculo != null) {
                String sVeiculo = veiculo.getMarca() + ", " + veiculo.getModelo() + " (" + veiculo.getMatricula() + ") ";
                tvVeiculo.setText(sVeiculo);

                String urlImage = veiculo.getImagem();
                urlImage = urlImage.replace("http://backendstand.test/", "http://10.0.2.2:80/");
                Glide.with(context).load(urlImage).placeholder(R.drawable.ic_testdrive_capa).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
            } else {
                tvVeiculo.setText("Veículo já não está dispovível");
            }
        }
    }
}
