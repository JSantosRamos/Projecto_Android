package amsi.dei.estg.ipleiria.projecto_standauto.Listener;

import android.content.Context;

public interface SignupListener {
    void onValidateSignup(final boolean sucesso, final String message, final Context context);
}
