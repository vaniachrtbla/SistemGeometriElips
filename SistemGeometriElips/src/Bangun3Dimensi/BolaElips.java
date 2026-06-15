package Bangun3Dimensi;

import Bangun2Dimensi.Elips;

public class BolaElips extends Elips implements Runnable {

    public double semiAxisC;
    public double hasilVolume;

    // ====== ARRAY DATA PER BARIS ======
    public double[] dataSemiAxisC;
    public double[] dataHasilVolume;

    public BolaElips(double semiMayor, double semiMinor, double semiAxisC, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, jumlahData);
        if (semiAxisC <= 0)
            throw new Exception("[BolaElips] Semi Axis C harus positif!");
        this.semiAxisC = semiAxisC;
        this.hasilVolume = 0;
        this.namaThread = "Thread-BolaElips";
        this.dataSemiAxisC = new double[jumlahData];
        this.dataHasilVolume = new double[jumlahData];
        System.out.println("[LOG][BolaElips] Constructor dipanggil: a=" + semiMayor + ", b=" + semiMinor + ", c=" + semiAxisC);
    }

    // ====== HITUNG LUAS ======
    @Override
    public double hitungLuas() {

        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0)
            throw new IllegalArgumentException(
                    "[BolaElips] Dimensi tidak valid saat hitungLuas!");
        double p = 1.6075;
        hasilLuas = 4 * Math.PI * Math.pow(
                (
                    Math.pow(semiMayor, p) * Math.pow(semiMinor, p)
                  + Math.pow(semiMayor, p) * Math.pow(semiAxisC, p)
                  + Math.pow(semiMinor, p) * Math.pow(semiAxisC, p)
                ) / 3.0,
                1.0 / p
        );
        return hasilLuas;
    }

    // OVERLOADING
    public double hitungLuas(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0)
            throw new IllegalArgumentException(
                    "[BolaElips] Semua dimensi harus positif!");
        double p = 1.6075;
        hasilLuas = 4 * Math.PI * Math.pow(
                (
                    Math.pow(a, p) * Math.pow(b, p)
                  + Math.pow(a, p) * Math.pow(c, p)
                  + Math.pow(b, p) * Math.pow(c, p)
                ) / 3.0,
                1.0 / p
        );
        return hasilLuas;
    }

    // ====== HITUNG VOLUME ======
    public double hitungVolume() {
        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0)
            throw new IllegalArgumentException(
                    "[BolaElips] Dimensi tidak valid saat hitungVolume!");
        hasilVolume = (4.0 / 3.0)* Math.PI* semiMayor * semiMinor* semiAxisC;
        return hasilVolume;
    }

    // OVERLOADING
    public double hitungVolume(double a, double b, double c) {

        if (a <= 0 || b <= 0 || c <= 0)
            throw new IllegalArgumentException(
                    "[BolaElips] Dimensi harus positif!");
        hasilVolume =(4.0 / 3.0)* Math.PI* a* b* c;
        return hasilVolume;
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
                double c = (i == 0) ? semiAxisC : Math.max(0.01, variasi(semiAxisC));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataSemiAxisC[i] = c;
                dataHasilLuas[i] = hitungLuas(a, b, c);
                dataHasilVolume[i] = hitungVolume(a, b, c);

                if (Thread.interrupted()) throw new InterruptedException();
                
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