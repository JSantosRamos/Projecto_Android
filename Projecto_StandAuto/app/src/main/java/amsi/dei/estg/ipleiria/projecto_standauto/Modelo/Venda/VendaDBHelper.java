package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Venda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Testdrive.Testdrive;
import amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculo.Veiculo;

public class VendaDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "STAND";
    private final static String TABLE_NAME = "venda";
    private final static int DB_VERSION = 1;
    private static final String ID = "id";
    private static final String VALOR = "valor";
    private static final String NIF = "nif";
    private static final String MORADA = "morada";
    private static final String NOME = "nome";
    private static final String VEICULO = "veiculo";

    private SQLiteDatabase bd;

    public VendaDBHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        this.bd = this.getWritableDatabase();
        this.onCreate(bd);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLtable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                ID + " INTEGER NOT NULL, " +
                VALOR + " REAL NOT NULL, " +
                NIF + " TEXT NOT NULL, " +
                MORADA + " TEXT NOT NULL, " +
                NOME + " TEXT NOT NULL, " +
                VEICULO + " BLOB NOT NULL);";
        db.execSQL(SQLtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        this.onCreate(db);
    }

    public Venda adicionarVendaBD(Venda venda) {

        ContentValues valores = new ContentValues();
        valores.put(ID, venda.getId());
        valores.put(VALOR, venda.getValor());
        valores.put(NIF, venda.getNif());
        valores.put(MORADA, venda.getMorada());
        valores.put(NOME, venda.getNome());
        valores.put(VEICULO, ConverterVeiculo(venda.getVeiculo()));

        long id = this.bd.insert(TABLE_NAME, null, valores);
        if (id > -1) {
            return venda;
        }
        return null;
    }

    public ArrayList<Venda> getAllVendasBD() {

        ArrayList<Venda> listaVendas = new ArrayList<>();

        Cursor cursor = this.bd.query(TABLE_NAME,
                new String[]{ID, VALOR, NIF, MORADA, NOME, VEICULO},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {

                byte[] bytes = cursor.getBlob(5);
                Veiculo veiculo = ConverterByteArray(bytes);
                Venda venda = new Venda(
                        cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        veiculo
                );

                listaVendas.add(venda);

            } while (cursor.moveToNext());
        }

        return listaVendas;
    }

    public void removeAllVendasDB() {
        bd.delete(TABLE_NAME, null, null);
    }

    private byte[] ConverterVeiculo(Veiculo veiculo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(veiculo);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Veiculo ConverterByteArray(byte[] byteArray) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            if (obj instanceof Veiculo) {
                Veiculo veiculo = (Veiculo) obj;
                return veiculo;
            }
            return null;
        } catch (IOException | ClassNotFoundException e) {
            // Handle the exception
            e.printStackTrace();
            return null;
        }
    }
}



