package Bangun3Dimensi;

import Bangun2Dimensi.Elips;

public class PrismaElips extends Elips implements Runnable {

    public double tinggi;
    public double hasilVolume;

    public double[] dataTinggi;
    public double[] dataHasilVolume;

    public PrismaElips(double semiMayor, double semiMinor, double tinggi, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, jumlahData);
        if (tinggi <= 0)
            throw new Exception("[PrismaElips] Tinggi prisma harus positif!");
        this.tinggi = tinggi;
        this.hasilVolume = 0;
        this.namaThread = "Thread-PrismaElips";
        this.dataTinggi = new double[jumlahData];
        this.dataHasilVolume = new double[jumlahData];
        System.out.println("[LOG][PrismaElips] Constructor dipanggil: a=" + semiMayor + ", b=" + semiMinor + ", t=" + tinggi);
    }

    @Override
    public double hitungLuas() {
        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0)
            throw new ArithmeticException("[PrismaElips] Dimensi tidak valid saat hitungLuas!");
        hasilLuas = (2 * super.hitungLuas()) + (super.hitungKeliling() * tinggi);
        return hasilLuas;
    }

    public double hitungLuas(double a, double b, double t) throws Exception {
        if (a <= 0 || b <= 0 || t <= 0)
            throw new Exception("[PrismaElips] Semua dimensi harus positif!");
        return (2 * super.hitungLuas(a, b)) + (super.hitungKeliling(a, b) * t);
    }

    public double hitungVolume() {
        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0)
            throw new ArithmeticException("[PrismaElips] Dimensi tidak valid saat hitungVolume!");
        hasilVolume = super.hitungLuas() * tinggi;
        return hasilVolume;
    }

    public double hitungVolume(double a, double b, double t) throws Exception {
        if (a <= 0 || b <= 0 || t <= 0)
            throw new Exception("[PrismaElips] Dimensi harus positif!");
        return super.hitungLuas(a, b) * t;
    }

    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                double t = (i == 0) ? tinggi : Math.max(0.01, variasi(tinggi));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataTinggi[i] = t;
                dataHasilLuas[i] = hitungLuas(a, b, t);
                dataHasilVolume[i] = hitungVolume(a, b, t);

                if (Thread.interrupted()) throw new InterruptedException();
                Thread.sleep(1);
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE");

        } catch (InterruptedException ie) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}