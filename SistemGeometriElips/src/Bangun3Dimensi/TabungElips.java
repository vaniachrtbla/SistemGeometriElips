package Bangun3Dimensi;

import Bangun2Dimensi.Elips;
import GUI.MainFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class TabungElips extends Elips implements Bangun3Dimensi {

    protected double tinggiTabung;
    protected double volume;
    protected double luasPermukaan;

    public TabungElips(
            double semiMajor,
            double semiMinor,
            double tinggiTabung,
            String namaThread,
            int dataAwal,
            int dataAkhir,
            JTextArea area,
            MainFrame vGui,
            JProgressBar pb) {
        super(semiMajor, semiMinor, namaThread, dataAwal, dataAkhir, area, vGui, pb);
        this.tinggiTabung = tinggiTabung;
    }

    @Override
    public double hitungVolume() {
        volume = Math.PI * semiMajor * semiMinor * tinggiTabung;
        return volume;
    }

    @Override
    public double hitungLuasPermukaan() {
        // 2 × luas alas elips + keliling elips × tinggi
        luasPermukaan = (2 * luas) + (keliling * tinggiTabung);
        return luasPermukaan;
    }

    @Override
    public void run() {
        try {
            double aMajorAwal  = semiMajor;
            double bMinorAwal  = semiMinor;
            double tTabungAwal = tinggiTabung;
            int totalDataThread = dataAkhir - dataAwal + 1;

            for (int i = dataAwal; i <= dataAkhir; i++) {
                if (i % 100 == 0 || i == dataAkhir) {
                    int progress = (int) (((double) (i - dataAwal + 1) / totalDataThread) * 100);
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    Thread.sleep(150);
                }

                semiMajor    = aMajorAwal  * i;
                semiMinor    = bMinorAwal  * i;
                tinggiTabung = tTabungAwal * i;

                hitungLuas();
                hitungKeliling();
                hitungVolume();
                hitungLuasPermukaan();

                logArea.append(namaThread + " memproses data ke-" + i + "\n");

                gui.tambahBarisTabel(new Object[]{
                    namaThread, "Tabung Elips", i,
                    luas, keliling, volume, luasPermukaan
                });
            }
            logArea.append(namaThread + " selesai.\n");

        } catch (Exception e) {
            logArea.append(namaThread + " gagal : " + e.getMessage() + "\n");
        }
    }
}
