package amsi.dei.estg.ipleiria.projecto_standauto.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User.ImageDbHelper;
import amsi.dei.estg.ipleiria.projecto_standauto.R;

public class PerfilFragment extends Fragment {

    private TextView etNome, etEmail;
    private ImageView ivImage;
    private SharedPreferences sharedPref;
    private ImageDbHelper imageDbHelper = null;
    private int idUser;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        etNome = view.findViewById(R.id.tvNomePerfil);
        etEmail = view.findViewById(R.id.tvEmailPerfil);
        ivImage = view.findViewById(R.id.ivImagePerfil);

        sharedPref = getContext().getSharedPreferences(LoginActivity.DADOS_USER, Context.MODE_PRIVATE);
        String email = sharedPref.getString(LoginActivity.EMAIL_KEY, "");
        String nome = sharedPref.getString(LoginActivity.NOME_KEY, "");
        idUser = sharedPref.getInt(LoginActivity.ID_KEY, -1);

        etEmail.setText(email);
        etNome.setText(nome);

        Bitmap bitmap = getImage();
        if (bitmap != null) {
            ivImage.setImageBitmap(bitmap);
        }

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                int PICK_IMAGE_REQUEST = 1;
                startActivityForResult(Intent.createChooser(intent, "Escolher imagem"), PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                ivImage.setImageBitmap(bitmap);
                guardarImagem(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getImage() {

        imageDbHelper = new ImageDbHelper(getContext());
        return imageDbHelper.getImage(idUser); //pode devolver null
    }

    private void guardarImagem(Bitmap bitmap) {
        if (imageDbHelper != null) {
            imageDbHelper = new ImageDbHelper(getContext());
            imageDbHelper.addImage(bitmap, idUser);
        }
    }
}