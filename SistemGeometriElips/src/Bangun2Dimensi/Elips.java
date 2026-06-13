package Bangun2Dimensi;

import BangunGeometri.BangunGeometri;

/**
 * Class Elips sebagai base class untuk semua turunan bangun berbasis elips.
 * Implements BangunGeometri dan Runnable.
 * 
 * @author Swift
 */
public class Elips implements BangunGeometri, Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double semiMayor;
    public double semiMinor;
    public String namaThread;
    public int jumlahData;
    public double hasilLuas;
    public String statusThread;
    public int progress;

    // ====== CONSTRUCTOR ======
    public Elips(double semiMayor, double semiMinor, int jumlahData) throws Exception {

        if (semiMayor <= 0 || semiMinor <= 0) {
            throw new IllegalArgumentException(
                    "[Elips] Semi Mayor dan Semi Minor harus positif!"
            );
        }

        if (jumlahData <= 0) {
            throw new IllegalArgumentException(
                    "[Elips] Jumlah data harus lebih dari 0!"
            );
        }

        this.semiMayor = semiMayor;
        this.semiMinor = semiMinor;
        this.jumlahData = jumlahData;

        this.namaThread = "Thread-Elips";
        this.statusThread = "READY";
        this.progress = 0;
        this.hasilLuas = 0;

        System.out.println(
                "[LOG][Elips] Constructor dipanggil: a="
                + semiMayor
                + ", b="
                + semiMinor
        );
    }

    // ====== GETTER / SETTER ======
    public double getSemiMayor() {
        return semiMayor;
    }

    public void setSemiMayor(double semiMayor) {
        this.semiMayor = semiMayor;
    }

    public double getSemiMinor() {
        return semiMinor;
    }

    public void setSemiMinor(double semiMinor) {
        this.semiMinor = semiMinor;
    }

    public String getNamaThread() {
        return namaThread;
    }

    public void setNamaThread(String namaThread) {
        this.namaThread = namaThread;
    }

    public int getJumlahData() {
        return jumlahData;
    }

    public void setJumlahData(int jumlahData) {
        this.jumlahData = jumlahData;
    }

    public double getHasilLuas() {
        return hasilLuas;
    }

    public String getStatusThread() {
        return statusThread;
    }

    public int getProgress() {
        return progress;
    }

    // ====== HITUNG LUAS ======
    @Override
    public double hitungLuas() {

        if (semiMayor <= 0 || semiMinor <= 0) {
            throw new ArithmeticException(
                    "[Elips] Dimensi tidak valid saat hitungLuas!"
            );
        }

        hasilLuas = Math.PI * semiMayor * semiMinor;

        return hasilLuas;
    }

    // ====== OVERLOADING LUAS ======
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException(
                    "[Elips] Parameter hitungLuas harus positif!"
            );
        }

        return Math.PI * a * b;
    }

    // ====== HITUNG KELILING ======
    @Override
    public double hitungKeliling() {

        if (semiMayor <= 0 || semiMinor <= 0) {
            throw new ArithmeticException(
                    "[Elips] Dimensi tidak valid saat hitungKeliling!"
            );
        }

        double h =
                Math.pow(semiMayor - semiMinor, 2)
                / Math.pow(semiMayor + semiMinor, 2);

        return Math.PI * (semiMayor + semiMinor)
                * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    // ====== OVERLOADING KELILING ======
    @Override
    public double hitungKeliling(double a, double b) {

        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException(
                    "[Elips] Parameter hitungKeliling harus positif!"
            );
        }

        double h =
                Math.pow(a - b, 2)
                / Math.pow(a + b, 2);

        return Math.PI * (a + b)
                * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    // ====== TAMPIL INFO ======
    public void tampilInfo() {

        System.out.println("=== ELIPS ===");
        System.out.println("Semi Mayor (a) : " + semiMayor);
        System.out.println("Semi Minor (b) : " + semiMinor);

        try {
            System.out.printf("Luas      : %.4f%n", hitungLuas());
            System.out.printf("Keliling  : %.4f%n", hitungKeliling());

        } catch (Exception e) {

            System.out.println(
                    "[ERROR] tampilInfo: "
                    + e.getMessage()
            );
        }
    }

    // ====== THREAD ======
    @Override
    public void run() {

        try {

            statusThread = "RUNNING";
            progress = 0;

            System.out.println(
                    "[" + namaThread + "] START"
            );

            for (int i = 1; i <= jumlahData; i++) {

                hasilLuas = hitungLuas();

                hitungKeliling();

                Thread.sleep(1);

                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0) {

                    System.out.println(
                            "[" + namaThread + "] Progress: "
                            + progress + "%"
                    );
                }
            }

            progress = 100;

            statusThread = "DONE";

            System.out.println(
                    "[" + namaThread + "] DONE"
            );

        } catch (InterruptedException e) {

            statusThread = "INTERRUPTED";

            Thread.currentThread().interrupt();

            System.out.println(
                    "[" + namaThread + "] INTERRUPTED"
            );

        } catch (Exception e) {

            statusThread = "ERROR";

            System.out.println(
                    "[" + namaThread + "] ERROR: "
                    + e.getMessage()
            );
        }
    }
}