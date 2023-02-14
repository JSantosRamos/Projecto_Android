package amsi.dei.estg.ipleiria.projecto_standauto.Listener;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda.Venda;

public interface VendasListener {
    void onRefreshlistaVendas(ArrayList<Venda> lista);
}
