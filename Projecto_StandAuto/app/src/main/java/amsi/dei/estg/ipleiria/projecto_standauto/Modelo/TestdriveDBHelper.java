package amsi.dei.estg.ipleiria.projecto_standauto.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TestdriveDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "DBTestedrive";
    private final static String TABLE_NAME = "testdrives";
    private final static int DB_VERSION = 1;
    private static final String ID = "id";
    private static final String DATA = "data";
    private static final String HORA = "hora";
    private static final String ESTADO = "estado";
    private static final String MOTIVO = "motivo";

    private SQLiteDatabase bd;

    public TestdriveDBHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);

        this.bd = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLtable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATA + " TEXT NOT NULL, " +
                HORA + " TEXT NOT NULL, " +
                ESTADO + " TEXT NOT NULL, " +
                MOTIVO + " TEXT NOT NULL);";

        db.execSQL(SQLtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);

        this.onCreate(db);
    }

    public Testdrive adicionarTestDriveBD(Testdrive test) {

        ContentValues valores = new ContentValues();

        valores.put(DATA, test.getData());
        valores.put(HORA, test.getHora());
        valores.put(ESTADO, test.getEstado());
        valores.put(MOTIVO, test.getMotivo());

        long id = this.bd.insert(TABLE_NAME, null, valores);
        if (id > -1) {
            test.setId(id);
            return test;
        }
        return null;
    }

    public boolean editarTestdriveBD(Testdrive testdrive) {

        ContentValues valores = new ContentValues();

        valores.put(DATA, testdrive.getData());
        valores.put(HORA, testdrive.getHora());
        valores.put(ESTADO, testdrive.getEstado());
        valores.put(MOTIVO, testdrive.getMotivo());

        int registos = this.bd.update(TABLE_NAME,
                valores,
                ID + " = ?",
                new String[]{"" + testdrive.getId()});

        return registos > 0;
    }


    public boolean removerTestdrive(long id) {
        int nDelete = bd.delete(TABLE_NAME, ID + " = ?", new String[]{"" + id});

        return nDelete > 0;
    }

    public ArrayList<Testdrive> getAllTestdrivesBD() {

        ArrayList<Testdrive> listaTestDrives = new ArrayList<>();

        Cursor cursor = this.bd.query(TABLE_NAME,
                new String[]{ID, DATA, HORA, ESTADO, MOTIVO},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Testdrive newTestdrive = new Testdrive(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );

                listaTestDrives.add(newTestdrive);

            } while (cursor.moveToNext());
        }

        return listaTestDrives;
    }
}
