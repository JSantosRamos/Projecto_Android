package amsi.dei.estg.ipleiria.projecto_standauto.Listener;

import android.content.Context;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User.User;

public interface LoginListener {
    void onValidateLogin(final User user, final Context context);
}
