package Bangun2Dimensi;

import BangunGeometri.BangunGeometri;

public class Elips implements BangunGeometri, Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double semiMayor;
    public double semiMinor;
    public String namaThread;
    public int jumlahData;
    public double hasilLuas;
    public String statusThread;
    public int progress;

    // ====== ARRAY DATA PER BARIS ======
    public double[] dataSemiMayor;
    public double[] dataSemiMinor;
    public double[] dataHasilLuas;

    // ====== CONSTRUCTOR ======
    public Elips(double semiMayor, double semiMinor, int jumlahData) throws Exception {
        if (semiMayor <= 0 || semiMinor <= 0)
            throw new IllegalArgumentException("[Elips] Semi Mayor dan Semi Minor harus positif!");
        if (jumlahData <= 0)
            throw new IllegalArgumentException("[Elips] Jumlah data harus lebih dari 0!");
        this.semiMayor = semiMayor;
        this.semiMinor = semiMinor;
        this.jumlahData = jumlahData;
        this.namaThread = "Thread-Elips";
        this.statusThread = "READY";
        this.progress = 0;
        this.hasilLuas = 0;
        this.dataSemiMayor = new double[jumlahData];
        this.dataSemiMinor = new double[jumlahData];
        this.dataHasilLuas = new double[jumlahData];
        System.out.println("[LOG][Elips] Constructor dipanggil: a=" + semiMayor + ", b=" + semiMinor);
    }

    // ====== GETTER/SETTER ======
    public String getNamaThread() { return namaThread; }
    public void setNamaThread(String nama) { this.namaThread = nama; }
    public int getProgress() { return progress; }
    public String getStatusThread() { return statusThread; }

    // ====== VARIASI RANDOM (50%-150% dari nilai asli) ======
    protected double variasi(double base) {
        return base * (0.5 + Math.random());
    }

    // ====== HITUNG LUAS ======
    @Override
    public double hitungLuas() {
        if (semiMayor <= 0 || semiMinor <= 0)
            throw new ArithmeticException("[Elips] Dimensi tidak valid saat hitungLuas!");
        hasilLuas = Math.PI * semiMayor * semiMinor;
        return hasilLuas;
    }

    @Override
    public double hitungLuas(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Elips] Parameter hitungLuas harus positif!");
        return Math.PI * a * b;
    }

    // ====== HITUNG KELILING ======
    @Override
    public double hitungKeliling() {
        if (semiMayor <= 0 || semiMinor <= 0)
            throw new ArithmeticException("[Elips] Dimensi tidak valid saat hitungKeliling!");
        double h = Math.pow(semiMayor - semiMinor, 2) / Math.pow(semiMayor + semiMinor, 2);
        return Math.PI * (semiMayor + semiMinor) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    @Override
    public double hitungKeliling(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Elips] Parameter hitungKeliling harus positif!");
        double h = Math.pow(a - b, 2) / Math.pow(a + b, 2);
        return Math.PI * (a + b) * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));
    }

    // ====== THREAD ======
    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataHasilLuas[i] = hitungLuas(a, b);

                if (Thread.interrupted()) throw new InterruptedException();
                
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE");

        } catch (InterruptedException e) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
            System.out.println("[" + namaThread + "] INTERRUPTED");
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}