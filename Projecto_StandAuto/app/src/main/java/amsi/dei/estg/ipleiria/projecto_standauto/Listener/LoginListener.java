package amsi.dei.estg.ipleiria.projecto_standauto.Listener;

import android.content.Context;

public interface LoginListener {
    void onValidateLogin(final boolean success, final String email, final String password, final Context context);
}
