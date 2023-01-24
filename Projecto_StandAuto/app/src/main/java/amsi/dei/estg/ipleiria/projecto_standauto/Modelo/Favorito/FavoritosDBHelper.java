package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Favorito;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FavoritosDBHelper extends SQLiteOpenHelper {

    private final static String TABLE_FAVORITOS = "favoritos";
    private final static String ID = "id";
    private final static String ID_VEICULO = "idVeiculo";
    private final static String ID_USER = "idUser";
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "STAND";

    private final SQLiteDatabase db;

    public FavoritosDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
        this.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLtableFavoritos = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITOS + "( " +
                ID + " INTEGER PRIMARY KEY, " +
                ID_VEICULO + " INTEGER NOT NULL, " +
                ID_USER + " INTEGER NOT NULL); ";

        db.execSQL(SQLtableFavoritos);
    }

    public ArrayList<Integer> getAllFavoritosBD(int idUser) {

        ArrayList<Integer> lista = new ArrayList<>();
        Cursor cursor = this.db.query(
                TABLE_FAVORITOS,
                new String[]{ID_VEICULO, ID_USER},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                if (idUser == cursor.getInt(1)) {
                    lista.add(cursor.getInt(0));
                }
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public boolean adicionarFavoritoDB(int idVeiculo, int idUser) {

        ContentValues values = new ContentValues();
        values.put(ID_VEICULO, idVeiculo);
        values.put(ID_USER, idUser);

        long id = this.db.insert(TABLE_FAVORITOS, null, values);

        return id != -1;
    }

    public void removerFavoritoDB(int idVeiculo, int idUser) {
        int nDelete = db.delete(TABLE_FAVORITOS, ID_VEICULO + " = ? and " + ID_USER + " = ?", new String[]{"" + idVeiculo, "" + idUser});
    }

    public boolean verificarVeiculoFavortioDB(int idVeiculo, int idUser) {

        Cursor cursor = null;
        String sql = "SELECT idVeiculo FROM " + TABLE_FAVORITOS + " WHERE idVeiculo=" + idVeiculo + " AND idUser=" + idUser;
        cursor = db.rawQuery(sql, null);

        return cursor.getCount() > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS);
    }
}
