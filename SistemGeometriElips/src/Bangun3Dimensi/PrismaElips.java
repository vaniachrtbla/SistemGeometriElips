package Bangun3Dimensi;

import Bangun2Dimensi.Elips;

/**
 * PrismaElips: prisma dengan alas berbentuk elips.
 * Extends Elips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungLuas & hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class PrismaElips extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double tinggi;
    public double hasilVolume;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public PrismaElips(double semiMayor, double semiMinor,
                       double tinggi, int jumlahData) throws Exception {

        super(semiMayor, semiMinor, jumlahData); // polimorfisme eksplisit

        if (tinggi <= 0) {
            throw new Exception("[PrismaElips] Tinggi prisma harus positif!");
        }

        this.tinggi      = tinggi;
        this.hasilVolume = 0;
        this.namaThread  = "Thread-PrismaElips";

        System.out.println("[LOG][PrismaElips] Constructor dipanggil: a=" + semiMayor
                + ", b=" + semiMinor + ", t=" + tinggi);
    }

    // ====== OVERRIDE HITUNG LUAS (tanpa parameter) – throw Exception ======
    @Override
    public double hitungLuas() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0) {
            throw new ArithmeticException("[PrismaElips] Dimensi tidak valid saat hitungLuas!");
        }
        double luasAlas = super.hitungLuas();
        double keliling = super.hitungKeliling();
        hasilLuas = (2 * luasAlas) + (keliling * tinggi);
        return hasilLuas;
    }

    // ====== OVERLOADING HITUNG LUAS (dengan parameter) ======
    public double hitungLuas(double a, double b, double t) throws Exception {

        if (a <= 0 || b <= 0 || t <= 0) {
            throw new Exception("[PrismaElips] Semua dimensi harus positif!");
        }
        double luasAlas =super.hitungLuas(a,b);
        double keliling = super.hitungKeliling(a,b);
        return (2 * luasAlas) + (keliling * t);
    }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    public double hitungVolume() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0) {
            throw new ArithmeticException("[PrismaElips] Dimensi tidak valid saat hitungVolume!");
        }
        hasilVolume = super.hitungLuas() * tinggi;
        return hasilVolume;
    }

    // ====== OVERLOADING HITUNG VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double t) throws Exception {

        if (a <= 0 || b <= 0 || t <= 0) {
            throw new Exception("[PrismaElips] Dimensi harus positif!");
        }
        return super.hitungLuas(a,b) * t;
    }

    // ====== MULTITHREADING – RUNNABLE ======
    @Override
    public void run() {

        try {
            statusThread = "RUNNING";
            progress     = 0;

            System.out.println("[" + namaThread + "] START – hitung PrismaElips, jumlahData=" + jumlahData);

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
