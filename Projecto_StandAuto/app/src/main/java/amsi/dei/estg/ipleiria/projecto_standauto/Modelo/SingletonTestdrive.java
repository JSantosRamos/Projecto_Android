package amsi.dei.estg.ipleiria.projecto_standauto.Modelo;

import android.content.Context;

import java.util.ArrayList;

public class SingletonTestdrive {

    private static SingletonTestdrive instancia = null;
    private ArrayList<Testdrive> testdrives;

    private TestdriveDBHelper testdriveBD = null;

    public static synchronized SingletonTestdrive getInstance(Context context) {
        if (instancia == null) {
            instancia = new SingletonTestdrive(context);
        }
        return instancia;
    }

    private SingletonTestdrive(Context context) {
        testdrives = new ArrayList<>();
        testdriveBD = new TestdriveDBHelper(context);
    }

    public Testdrive getTestdrive(long idTestdrive) {
        for (Testdrive testdrive : testdrives) {
            if (testdrive.getId() == idTestdrive) {
                return testdrive;
            }
        }
        return null;
    }

    public void adicionarTestDriveBD(Testdrive testdrive) {

        Testdrive helperBD = testdriveBD.adicionarTestDriveBD(testdrive);

        if (helperBD != null) {
            testdrives.add(helperBD);
        }
    }

    public void removerTestDrive(long id) {

        Testdrive testdrive = getTestdrive(id);
        if (testdrive != null) {
            if (testdriveBD.removerTestdrive(testdrive.getId())) {
                testdrives.remove(testdrive);
            }
        }
    }

    public void editarTestDriveBD(Testdrive dadosTestdrive) {
        Testdrive testdrive = getTestdrive(dadosTestdrive.getId());
        if (testdrive != null) {
            if (testdriveBD.editarTestdriveBD(dadosTestdrive)) {

                testdrive.setData(dadosTestdrive.getData());
                testdrive.setHora(dadosTestdrive.getHora());
                testdrive.setEstado(dadosTestdrive.getEstado());
                testdrive.setMotivo(dadosTestdrive.getMotivo());
            }
        }
    }
    public ArrayList<Testdrive> getTestdrives() {

        return testdrives = testdriveBD.getAllTestdrivesBD();
    }

    public void setTestdrives(ArrayList<Testdrive> list) {

        this.testdrives = list;
    }
}
