package Bangun3Dimensi;

import Bangun2Dimensi.Elips;
import GUI.MainFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class KerucutTerpancungElips extends Elips implements Bangun3Dimensi {

    protected double semiMajor2;     // semi-mayor alas atas
    protected double semiMinor2;     // semi-minor alas atas
    protected double tinggi;
    protected double volume;
    protected double luasPermukaan;

    // Luas & keliling alas atas (dihitung secara internal)
    private double luasAtas;
    private double kelilingAtas;

    public KerucutTerpancungElips(
            double semiMajor1,
            double semiMinor1,
            double semiMajor2,
            double semiMinor2,
            double tinggi,
            String namaThread,
            int dataAwal,
            int dataAkhir,
            JTextArea area,
            MainFrame vGui,
            JProgressBar pb) {
        super(semiMajor1, semiMinor1, namaThread, dataAwal, dataAkhir, area, vGui, pb);
        this.semiMajor2 = semiMajor2;
        this.semiMinor2 = semiMinor2;
        this.tinggi     = tinggi;
    }

    /** Hitung luas alas atas */
    private double hitungLuasAtas() {
        luasAtas = Math.PI * semiMajor2 * semiMinor2;
        return luasAtas;
    }

    /** Hitung keliling alas atas (Ramanujan) */
    private double hitungKelilingAtas() {
        double a2 = semiMajor2, b2 = semiMinor2;
        kelilingAtas = Math.PI * (3 * (a2 + b2) - Math.sqrt((3 * a2 + b2) * (a2 + 3 * b2)));
        return kelilingAtas;
    }

    @Override
    public double hitungVolume() {
        double A1 = luas;                            // luas alas bawah
        double A2 = Math.PI * semiMajor2 * semiMinor2; // luas alas atas
        volume = (tinggi / 3.0) * (A1 + A2 + Math.sqrt(A1 * A2));
        return volume;
    }

    @Override
    public double hitungLuasPermukaan() {
        hitungLuasAtas();
        hitungKelilingAtas();

        // Garis miring frustum (aproksimasi eliptik)
        double deltaA   = (semiMajor - semiMajor2) / 2.0;
        double deltaB   = (semiMinor - semiMinor2) / 2.0;
        double garisMiring = Math.sqrt(tinggi * tinggi + deltaA * deltaA + deltaB * deltaB);

        // Luas selimut: rata-rata keliling × garis miring
        double luasSelimut = ((keliling + kelilingAtas) / 2.0) * garisMiring;

        luasPermukaan = luas + luasAtas + luasSelimut;
        return luasPermukaan;
    }

    @Override
    public void run() {
        try {
            double a1Awal   = semiMajor;
            double b1Awal   = semiMinor;
            double a2Awal   = semiMajor2;
            double b2Awal   = semiMinor2;
            double tAwal    = tinggi;
            int totalDataThread = dataAkhir - dataAwal + 1;

            for (int i = dataAwal; i <= dataAkhir; i++) {
                if (i % 100 == 0 || i == dataAkhir) {
                    int progress = (int) (((double) (i - dataAwal + 1) / totalDataThread) * 100);
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    Thread.sleep(150);
                }

                semiMajor  = a1Awal  * i;
                semiMinor  = b1Awal  * i;
                semiMajor2 = a2Awal  * i;
                semiMinor2 = b2Awal  * i;
                tinggi     = tAwal   * i;

                hitungLuas();
                hitungKeliling();
                hitungVolume();
                hitungLuasPermukaan();

                logArea.append(namaThread + " memproses data ke-" + i + "\n");

                gui.tambahBarisTabel(new Object[]{
                    namaThread, "Kerucut Terpancung Elips", i,
                    luas, keliling, volume, luasPermukaan
                });
            }
            logArea.append(namaThread + " selesai.\n");

        } catch (Exception e) {
            logArea.append(namaThread + " gagal : " + e.getMessage() + "\n");
        }
    }
}
