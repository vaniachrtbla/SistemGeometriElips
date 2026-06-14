package Bangun2Dimensi;

import BangunGeometri.BangunGeometri;

public class Elips implements BangunGeometri, Runnable {

    // ====== ATRIBUT PUBLIC ======
    
    // semiMayor = sumbu terpanjang elips
    // semiMinor = sumbu terpendek elips
    public double semiMayor;
    public double semiMinor;

    // atribut thread & hasil
    public String namaThread;
    public int jumlahData;
    public double hasilLuas;
    public double hasilKeliling;
    public String statusThread;
    public int progress;

    // array data untuk tabel GUI
    public double[] dataSemiMayor;
    public double[] dataSemiMinor;
    public double[] dataHasilLuas;

    // ====== CONSTRUCTOR ======
    public Elips(double semiMayor, double semiMinor, int jumlahData) throws Exception {

        // validasi input
        if (semiMayor <= 0 || semiMinor <= 0)
            throw new IllegalArgumentException("[Elips] Semi Mayor dan Semi Minor harus positif!");
        if (jumlahData <= 0)
            throw new IllegalArgumentException("[Elips] Jumlah data harus lebih dari 0!");

        // inisialisasi atribut
        this.semiMayor = semiMayor;
        this.semiMinor = semiMinor;
        this.jumlahData = jumlahData;

        // inisialisasi thread
        this.namaThread = "Thread-Elips";
        this.statusThread = "READY";
        this.progress = 0;

        // inisialisasi hasil & array
        this.hasilLuas = 0;
        this.hasilKeliling = 0;
        this.dataSemiMayor = new double[jumlahData];
        this.dataSemiMinor = new double[jumlahData];
        this.dataHasilLuas = new double[jumlahData];

        System.out.println("[LOG][Elips] Constructor dipanggil: a=" + semiMayor + ", b=" + semiMinor);
    }

    // ====== VARIASI RANDOM ======
    
    // menghasilkan variasi 50%-150% dari nilai asli
    protected double variasi(double base) {
        return base * (0.5 + Math.random());
    }

    // ====== HITUNG LUAS ======
    // rumus luas elips = πab
    @Override
    public double hitungLuas() {

        if (semiMayor <= 0 || semiMinor <= 0)
            throw new ArithmeticException("[Elips] Dimensi tidak valid saat hitungLuas!");

        hasilLuas = Math.PI * semiMayor * semiMinor;

        return hasilLuas;
    }

    // overloading luas dengan parameter
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Elips] Parameter hitungLuas harus positif!");

        hasilLuas = Math.PI * a * b;

        return hasilLuas;
    }


    // ====== HITUNG KELILING ======

    // memakai pendekatan Ramanujan
    @Override
    public double hitungKeliling() {

        if (semiMayor <= 0 || semiMinor <= 0)
            throw new ArithmeticException("[Elips] Dimensi tidak valid saat hitungKeliling!");

        double h = Math.pow(semiMayor - semiMinor, 2)
                 / Math.pow(semiMayor + semiMinor, 2);

        hasilKeliling = Math.PI * (semiMayor + semiMinor)
                       * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));

        return hasilKeliling;
    }

    // overloading keliling dengan parameter
    @Override
    public double hitungKeliling(double a, double b) {

        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Elips] Parameter hitungKeliling harus positif!");

        double h = Math.pow(a - b, 2)
                 / Math.pow(a + b, 2);

        hasilKeliling = Math.PI * (a + b)
                       * (1 + (3 * h) / (10 + Math.sqrt(4 - 3 * h)));

        return hasilKeliling;
    }
    // ====== THREAD ======
    @Override
    public void run() {
        try {

            // thread mulai berjalan
            statusThread = "RUNNING";
            progress = 0;

            System.out.println("[" + namaThread + "] START");

            // looping simulasi data
            for (int i = 0; i < jumlahData; i++) {

                // data pertama pakai nilai asli
                // sisanya memakai variasi random
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));

                // simpan data ke array
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;

                // hitung & simpan luas
                dataHasilLuas[i] = hitungLuas(a, b);

                // cek interrupt thread
                if (Thread.interrupted())
                    throw new InterruptedException();

                // update progress
                progress = ((i + 1) * 100) / jumlahData;
            }

            // thread selesai
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