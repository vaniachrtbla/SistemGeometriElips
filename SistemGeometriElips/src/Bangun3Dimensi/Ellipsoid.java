package Bangun3Dimensi;

import Bangun2Dimensi.Elips;
import GUI.MainFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Ellipsoid extends Elips implements Bangun3Dimensi {

    protected double semiAxisC;   // sumbu c (kedalaman)
    protected double volume;
    protected double luasPermukaan;

    public Ellipsoid(
            double semiMajor,
            double semiMinor,
            double semiAxisC,
            String namaThread,
            int dataAwal,
            int dataAkhir,
            JTextArea area,
            MainFrame vGui,
            JProgressBar pb) {
        super(semiMajor, semiMinor, namaThread, dataAwal, dataAkhir, area, vGui, pb);
        this.semiAxisC = semiAxisC;
    }

    @Override
    public double hitungVolume() {
        volume = (4.0 / 3.0) * Math.PI * semiMajor * semiMinor * semiAxisC;
        return volume;
    }

    @Override
    public double hitungLuasPermukaan() {
        // Pendekatan Knud Thomsen: p = 1.6075
        double p  = 1.6075;
        double a  = semiMajor;
        double b  = semiMinor;
        double c  = semiAxisC;
        double ap = Math.pow(a, p);
        double bp = Math.pow(b, p);
        double cp = Math.pow(c, p);
        luasPermukaan = 4 * Math.PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
        return luasPermukaan;
    }

    @Override
    public void run() {
        try {
            double aMajorAwal  = semiMajor;
            double bMinorAwal  = semiMinor;
            double cAxisAwal   = semiAxisC;
            int totalDataThread = dataAkhir - dataAwal + 1;

            for (int i = dataAwal; i <= dataAkhir; i++) {
                if (i % 100 == 0 || i == dataAkhir) {
                    int progress = (int) (((double) (i - dataAwal + 1) / totalDataThread) * 100);
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    Thread.sleep(150);
                }

                semiMajor  = aMajorAwal * i;
                semiMinor  = bMinorAwal * i;
                semiAxisC  = cAxisAwal  * i;

                hitungLuas();
                hitungKeliling();
                hitungVolume();
                hitungLuasPermukaan();

                logArea.append(namaThread + " memproses data ke-" + i + "\n");

                gui.tambahBarisTabel(new Object[]{
                    namaThread, "Ellipsoid", i,
                    luas, keliling, volume, luasPermukaan
                });
            }
            logArea.append(namaThread + " selesai.\n");

        } catch (Exception e) {
            logArea.append(namaThread + " gagal : " + e.getMessage() + "\n");
        }
    }
}
