package Bangun3Dimensi;

import Bangun2Dimensi.Elips;
import GUI.MainFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class KerucutElips extends Elips implements Bangun3Dimensi {

    protected double tinggiKerucut;
    protected double volume;
    protected double luasPermukaan;

    public KerucutElips(
            double semiMajor,
            double semiMinor,
            double tinggiKerucut,
            String namaThread,
            int dataAwal,
            int dataAkhir,
            JTextArea area,
            MainFrame vGui,
            JProgressBar pb) {
        super(semiMajor, semiMinor, namaThread, dataAwal, dataAkhir, area, vGui, pb);
        this.tinggiKerucut = tinggiKerucut;
    }

    @Override
    public double hitungVolume() {
        volume = (1.0 / 3.0) * Math.PI * semiMajor * semiMinor * tinggiKerucut;
        return volume;
    }

    @Override
    public double hitungLuasPermukaan() {
        // Garis pelukis: s = √(t² + r²), di mana r = (a+b)/2 (rata-rata semi-sumbu)
        double rRataRata    = (semiMajor + semiMinor) / 2.0;
        double garisPelukis = Math.sqrt(tinggiKerucut * tinggiKerucut + rRataRata * rRataRata);
        // Luas alas elips + luas selimut kerucut (aproksimasi)
        luasPermukaan = luas + (Math.PI * rRataRata * garisPelukis);
        return luasPermukaan;
    }

    @Override
    public void run() {
        try {
            double aMajorAwal    = semiMajor;
            double bMinorAwal    = semiMinor;
            double tKerucutAwal  = tinggiKerucut;
            int totalDataThread  = dataAkhir - dataAwal + 1;

            for (int i = dataAwal; i <= dataAkhir; i++) {
                if (i % 100 == 0 || i == dataAkhir) {
                    int progress = (int) (((double) (i - dataAwal + 1) / totalDataThread) * 100);
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    Thread.sleep(150);
                }

                semiMajor     = aMajorAwal   * i;
                semiMinor     = bMinorAwal   * i;
                tinggiKerucut = tKerucutAwal * i;

                hitungLuas();
                hitungKeliling();
                hitungVolume();
                hitungLuasPermukaan();

                logArea.append(namaThread + " memproses data ke-" + i + "\n");

                gui.tambahBarisTabel(new Object[]{
                    namaThread, "Kerucut Elips", i,
                    luas, keliling, volume, luasPermukaan
                });
            }
            logArea.append(namaThread + " selesai.\n");

        } catch (Exception e) {
            logArea.append(namaThread + " gagal : " + e.getMessage() + "\n");
        }
    }
}
