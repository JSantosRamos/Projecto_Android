package amsi.dei.estg.ipleiria.projecto_standauto.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class ListaTestdriveAdaptador  extends BaseAdapter {
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

        //preenchimento do view
        ListaTestdriveAdaptador.ViewHolderLista viewHL = (ListaTestdriveAdaptador.ViewHolderLista) convertView.getTag();
        if (viewHL == null) {
            viewHL = new ListaTestdriveAdaptador.ViewHolderLista(convertView);
            convertView.setTag(viewHL);
        }

        viewHL.update(listaTD.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvDia, tvHora, tvEstado;
        private ImageView ivImage;

        public ViewHolderLista(View view) {

            tvDia = view.findViewById(R.id.tvDiaTestdriveLista);
            tvHora = view.findViewById(R.id.tvHoraTestdriveLista);
            tvEstado = view.findViewById(R.id.tvEstadoTestdriveLista);
            ivImage = view.findViewById(R.id.ivCapaTestdriveLista);
        }

        public void update(Testdrive test) {
            tvDia.setText(test.getData());
            tvHora.setText(test.getHora());
            tvEstado.setText(test.getEstado());
            ivImage.setImageResource(R.drawable.ic_testdrive_capa);
        }
    }
}
