package Bangun3Dimensi;

import Bangun2Dimensi.Elips;

/**
 * LimasElips: limas dengan alas berbentuk elips.
 * Extends Elips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungLuas & hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class LimasElips extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double tinggi;
    public double hasilVolume;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public LimasElips(double semiMayor, double semiMinor,
                      double tinggi, int jumlahData) throws Exception {

        super(semiMayor, semiMinor, jumlahData); // polimorfisme eksplisit

        if (tinggi <= 0) {
            throw new Exception("[LimasElips] Tinggi limas harus positif!");
        }

        this.tinggi      = tinggi;
        this.hasilVolume = 0;
        this.namaThread  = "Thread-LimasElips";

        System.out.println("[LOG][LimasElips] Constructor dipanggil: a=" + semiMayor
                + ", b=" + semiMinor + ", t=" + tinggi);
    }

    // ====== GETTER / SETTER ======
    public double getTinggi()      { return tinggi; }
    public void   setTinggi(double v) { this.tinggi = v; }
    public double getHasilVolume() { return hasilVolume; }

    // ====== OVERRIDE HITUNG LUAS (tanpa parameter) – throw Exception ======
    @Override
    public double hitungLuas() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0) {
            throw new ArithmeticException("[LimasElips] Dimensi tidak valid saat hitungLuas!");
        }
        double luasAlas   = Math.PI * semiMayor * semiMinor;
        double garisA     = Math.sqrt(semiMayor * semiMayor + tinggi * tinggi);
        double garisB     = Math.sqrt(semiMinor * semiMinor + tinggi * tinggi);
        double luasSelimut = Math.PI * semiMayor * garisB / 2
                           + Math.PI * semiMinor * garisA / 2;
        hasilLuas = luasAlas + luasSelimut;
        return hasilLuas;
    }

    // ====== OVERLOADING HITUNG LUAS (dengan parameter) ======
    public double hitungLuas(double a, double b, double t) throws Exception {

        if (a <= 0 || b <= 0 || t <= 0) {
            throw new Exception("[LimasElips] Semua dimensi harus positif!");
        }
        double luasAlas   = Math.PI * a * b;
        double garisA     = Math.sqrt(a * a + t * t);
        double garisB     = Math.sqrt(b * b + t * t);
        double luasSelimut = Math.PI * a * garisB / 2 + Math.PI * b * garisA / 2;
        return luasAlas + luasSelimut;
    }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    public double hitungVolume() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0) {
            throw new ArithmeticException("[LimasElips] Dimensi tidak valid saat hitungVolume!");
        }
        hasilVolume = (1.0 / 3.0) * Math.PI * semiMayor * semiMinor * tinggi;
        return hasilVolume;
    }

    // ====== OVERLOADING HITUNG VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double t) throws Exception {

        if (a <= 0 || b <= 0 || t <= 0) {
            throw new Exception("[LimasElips] Dimensi harus positif!");
        }
        return (1.0 / 3.0) * Math.PI * a * b * t;
    }

    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {

        System.out.println("=== LIMAS ELIPS ===");
        System.out.println("Semi Mayor (a) : " + semiMayor);
        System.out.println("Semi Minor (b) : " + semiMinor);
        System.out.println("Tinggi         : " + tinggi);
        try {
            System.out.printf("Luas Permukaan : %.4f%n", hitungLuas());
            System.out.printf("Volume         : %.4f%n", hitungVolume());
        } catch (Exception e) {
            System.out.println("[ERROR] tampilInfo: " + e.getMessage());
        }
    }

    // ====== MULTITHREADING – RUNNABLE ======
    @Override
    public void run() {

        try {
            statusThread = "RUNNING";
            progress     = 0;

            System.out.println("[" + namaThread + "] START – hitung LimasElips, jumlahData=" + jumlahData);

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
