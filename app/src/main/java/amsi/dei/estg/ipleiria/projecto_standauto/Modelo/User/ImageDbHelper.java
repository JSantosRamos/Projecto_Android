package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.User;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageDbHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "STAND";
    private static final int DB_VERSION = 1;
    private static final String TABLE_IMAGE = "images";
    private static final String ID_USER = "id";
    private static final String IMAGE = "image";
    private SQLiteDatabase bd;

    public ImageDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.bd = this.getWritableDatabase();
        this.onCreate(bd);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTable = "CREATE TABLE IF NOT EXISTS " + TABLE_IMAGE + "("
                + ID_USER + " INTEGER NOT NULL,"
                + IMAGE + " BLOB"
                + ")";
        db.execSQL(sqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);

        this.onCreate(db);
    }

    public void addImage(Bitmap image, int idUser) {

        if (verificarImagem(idUser)) {
            removerImage(idUser);
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGE, getBitmapAsByteArray(image));
        values.put(ID_USER, idUser);

        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public Bitmap getImage(int idUser) {

        if (!verificarImagem(idUser)) {
            return null;
        }

        Cursor cursor = this.bd.query(TABLE_IMAGE,
                new String[]{IMAGE},
                ID_USER + "=?",
                new String[]{String.valueOf(idUser)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(IMAGE));
        cursor.close();

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void removerImage(int id) {
        bd.delete(TABLE_IMAGE, ID_USER + " = ?", new String[]{"" + id});
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private boolean verificarImagem(int idUser) {

        Cursor cursor = null;
        String sql = "SELECT image FROM " + TABLE_IMAGE + " WHERE id=" + idUser;
        cursor = bd.rawQuery(sql, null);

        return cursor.getCount() > 0;
    }
}
