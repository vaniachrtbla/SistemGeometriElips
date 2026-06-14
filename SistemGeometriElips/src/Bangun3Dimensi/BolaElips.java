package Bangun3Dimensi;

import Bangun2Dimensi.Elips;

/**
 * BolaElips (Ellipsoid): bangun 3D tiga sumbu.
 * Extends Elips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungLuas & hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class BolaElips extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double semiAxisC;
    public double hasilVolume;

    // ====== CONSTRUCTOR DENGAN PARAMETER ======
    public BolaElips(double semiMayor, double semiMinor,
                     double semiAxisC, int jumlahData) throws Exception {

        super(semiMayor, semiMinor, jumlahData); // memanggil contructor

        if (semiAxisC <= 0) {
            throw new Exception("[BolaElips] Semi Axis C harus positif!");
        }

        this.semiAxisC   = semiAxisC;
        this.hasilVolume = 0;
        this.namaThread  = "Thread-BolaElips";

        System.out.println("[LOG][BolaElips] Constructor dipanggil: a=" + semiMayor
                + ", b=" + semiMinor + ", c=" + semiAxisC);
    }


    // ====== OVERRIDE LUAS PERMUKAAN (tanpa parameter) – throw Exception ======
    @Override
    public double hitungLuas() throws ArithmeticException  {

        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0) {
            throw new ArithmeticException("[BolaElips] Dimensi tidak valid saat hitungLuas!");
        }
        double p  = 1.6075;
        double ap = Math.pow(semiMayor, p);
        double bp = Math.pow(semiMinor, p);
        double cp = Math.pow(semiAxisC, p);
        hasilLuas = 4 * Math.PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
        return hasilLuas;
    }

    // ====== OVERLOADING LUAS (dengan parameter) ======
    public double hitungLuas(double a, double b, double c) throws Exception {

        if (a <= 0 || b <= 0 || c <= 0) {
            throw new Exception("[BolaElips] Semua dimensi harus positif!");
        }
        double p  = 1.6075;
        double ap = Math.pow(a, p);
        double bp = Math.pow(b, p);
        double cp = Math.pow(c, p);
        return 4 * Math.PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
    }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    public double hitungVolume() {

        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0) {
            throw new IllegalArgumentException("[BolaElips] Dimensi tidak valid saat hitungVolume!");
        }
        hasilVolume = (4.0 / 3.0) * Math.PI * semiMayor * semiMinor * semiAxisC;
        return hasilVolume;
    }

    // ====== OVERLOADING VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double c) {

        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException ("[BolaElips] Dimensi harus positif!");
        }
        return (4.0 / 3.0) * Math.PI * a * b * c;
    }


    // ====== MULTITHREADING – RUNNABLE ======
    @Override
    public void run() {

        try {
            statusThread = "RUNNING";
            progress     = 0;

            System.out.println("[" + namaThread + "] START – hitung BolaElips, jumlahData=" + jumlahData);

            for (int i = 1; i <= jumlahData; i++) {

                hasilLuas   = hitungLuas();
                hasilVolume = hitungVolume();
                Thread.sleep(1);
                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0 && progress > 0 && (i * 100 % jumlahData == 0)) {
                    System.out.println("[" + namaThread + "] Progress: " + progress + "%");
                }
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE – Volume=" + String.format("%.4f", hasilVolume));

        } catch (InterruptedException ie) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
            System.out.println("[" + namaThread + "] INTERRUPTED");

        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}
