package amsi.dei.estg.ipleiria.projecto_standauto.Modelo.Veiculos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class VeiculosDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "DBStandv3";
    private final static String TABLE_VEICULOS = "veiculos";
    private final static String TABLE_FAVORITOS = "favoritos";
    private final static int DB_VERSION = 1;

    private static final String ID = "id";
    private static final String MARCA = "marca";
    private static final String MODELO = "modelo";
    private static final String SERIE = "serie";
    private static final String SEGMENTO = "segmento";
    private static final String COMBUSTIVEL = "combustivel";
    private static final String QUILOMETROS = "quilometros";
    private static final String MOTOR = "motor";
    private static final String COR = "cor";
    private static final String DESCRICAO = "descricao";
    private static final String NUMERO_PORTAS = "numero_portas";
    private static final String TIPO_CAIXA = "tipo_caixa";
    private static final String PRECO = "preco";
    private static final String TITULO = "titulo";
    private static final String IMAGEM = "imagem";
    private static final String MATRICULA = "matricula";
    private static final String CV = "cv";
    private static final String ANO = "ano";

    //Favoritos
    private static final String ID_VEICULO = "idVeiculo";


    private final SQLiteDatabase db;

    public VeiculosDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQLtableVeiculos = "CREATE TABLE IF NOT EXISTS " + TABLE_VEICULOS + "( " +
                ID + " INTEGER NOT NULL, " +
                MARCA + " TEXT NOT NULL, " +
                ANO + " INTEGER NOT NULL, " +
                IMAGEM + " TEXT); ";
        /* +
                MODELO + " TEXT NOT NULL, " +
                SERIE + " TEXT NOT NULL, " +
                SEGMENTO + " TEXT NOT NULL, " +
                COMBUSTIVEL + " TEXT NOT NULL, " +
                QUILOMETROS + " TEXT NOT NULL, " +
                MOTOR + " TEXT NOT NULL, " +
                COR + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                NUMERO_PORTAS + " TEXT NOT NULL, " +
                TIPO_CAIXA + " TEXT NOT NULL, " +
                PRECO + " TEXT NOT NULL, " +
                TITULO + " TEXT NOT NULL, " +
                MATRICULA + " TEXT NOT NULL, " +
                CV + " TEXT NOT NULL, " +
                ANO + " INTEGER NOT NULL, " +
                IMAGEM + " TEXT );";*/

        db.execSQL(SQLtableVeiculos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        this.onCreate(db);
    }

    public Veiculo adicionarVeiculoBD(Veiculo veiculo) {

        ContentValues values = new ContentValues();

        values.put(ID, veiculo.getId());
        values.put(MARCA, veiculo.getMarca());
        values.put(ANO, veiculo.getAno());
        values.put(IMAGEM, veiculo.getImagem());
       /* values.put(MODELO, veiculo.getModelo());
        values.put(SERIE, veiculo.getSerie());
        values.put(SEGMENTO, veiculo.getSegmento());
        values.put(COMBUSTIVEL, veiculo.getCombustivel());
        values.put(QUILOMETROS, veiculo.getQuilometros());
        values.put(MOTOR, veiculo.getMotor());
        values.put(COR, veiculo.getCor());
        values.put(DESCRICAO, veiculo.getDescricao());
        values.put(NUMERO_PORTAS, veiculo.getNumeroPortas());
        values.put(TIPO_CAIXA, veiculo.getTipoCaixa());
        values.put(PRECO, veiculo.getPreco());
        values.put(TITULO, veiculo.getTitulo());
        values.put(MATRICULA, veiculo.getMatricula());
        values.put(CV, veiculo.getCv());
        values.put(ANO, veiculo.getAno());
        values.put(IMAGEM, veiculo.getImagem());*/

      /*  long id = this.db.insert(TABLE_NAME, null, values);
        if (id > -1) {
            veiculo.setId(id);
            return veiculo;
        }*/
        long id = this.db.insert(TABLE_VEICULOS, null, values);

        if (id == -1) {
            return null;
        }

        return veiculo;
    }


    public ArrayList<Integer> getAllFavoritosBD() {

        ArrayList<Integer> lista = new ArrayList<>();
        Cursor cursor = this.db.query(
                TABLE_FAVORITOS,
                new String[]{ID_VEICULO},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return lista;

    }

    public ArrayList<Veiculo> getAllVeiculosBD() {
        ArrayList<Veiculo> lista = new ArrayList<>();
        Cursor cursor = this.db.query(
                TABLE_VEICULOS,
                new String[]{ID, MARCA, ANO, IMAGEM, COMBUSTIVEL},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Veiculo vei = new Veiculo(
                        cursor.getInt(0),
                        cursor.getString(0),
                        cursor.getInt(0),
                        cursor.getString(0),
                        cursor.getString(0)
                );
                lista.add(vei);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    /*public boolean removerVeiculo(int id) {
        int nDelete = db.delete(TABLE_NAME, ID + " = ?", new String[]{"" + id});

        return nDelete > 0;
    }*/

    public void removerAllVeiculosBD() {
        db.delete(TABLE_VEICULOS, null, null);
    }

    //Favoritos
    public boolean adicionarVeiculoFavoritosDB(Veiculo veiculo) {

        removerVeiculoFavoritos(veiculo.getId()); //need fix...

        ContentValues values = new ContentValues();
        values.put(ID_VEICULO, veiculo.getId());

        long id = this.db.insert(TABLE_FAVORITOS, null, values);

        return id != -1;
    }

    public boolean removerVeiculoFavoritos(int id) {
        int nDelete = db.delete(TABLE_FAVORITOS, ID_VEICULO + " = ?", new String[]{"" + id});

        return nDelete > 0;
    }
 /*   private boolean verificarVeiculoFavortiosDB(int idVeiculo) {

        String query = "SELECT idVeiculo FROM favoritos WHERE idVeiculo =" + idVeiculo;

        Cursor cursor = db.rawQuery(query, new String[]{ID_VEICULO});

        if (cursor.moveToFirst()){
           return true;
        }
        return false;
    }*/
}
