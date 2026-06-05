/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bangun2Dimensi;

import GUI.MainFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Elips implements Runnable, Bangun2Dimensi {

    // Atribut semi-sumbu elips
    protected double semiMajor; // sumbu a (semi-mayor)
    protected double semiMinor; // sumbu b (semi-minor)
    protected double luas;
    protected double keliling;

    // Atribut thread & GUI
    protected Thread t;
    protected String namaThread;
    protected int dataAwal;
    protected int dataAkhir;
    protected JTextArea logArea;
    protected MainFrame gui;
    protected JProgressBar progressBar;

    // Constructor utama (dengan thread & GUI)
    public Elips(
            double semiMajor,
            double semiMinor,
            String namaThread,
            int dataAwal,
            int dataAkhir,
            JTextArea area,
            MainFrame vGui,
            JProgressBar pb) {
        this.semiMajor   = semiMajor;
        this.semiMinor   = semiMinor;
        this.namaThread  = namaThread;
        this.dataAwal    = dataAwal;
        this.dataAkhir   = dataAkhir;
        this.logArea     = area;
        this.gui         = vGui;
        this.progressBar = pb;
        this.t           = new Thread(this, namaThread);
    }

    // Constructor default (tanpa parameter)
    public Elips() {}

    // Constructor sederhana (hanya dimensi)
    public Elips(double semiMajor, double semiMinor) {
        this.semiMajor = semiMajor;
        this.semiMinor = semiMinor;
    }

    // Getter
    public Thread getThread()      { return t; }
    public String getNamaThread()  { return namaThread; }
    public double getLuas()        { return luas; }
    public double getKeliling()    { return keliling; }

    // --- Implementasi Bangun2Dimensi ---

    @Override
    public double hitungLuas() {
        luas = Math.PI * semiMajor * semiMinor;
        return luas;
    }

    /**
     * Pendekatan Ramanujan untuk keliling elips:
     * K ≈ π × [3(a+b) - √((3a+b)(a+3b))]
     */
    @Override
    public double hitungKeliling() {
        double a = semiMajor, b = semiMinor;
        keliling = Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
        return keliling;
    }

    // --- Implementasi Runnable ---

    @Override
    public void run() {
        try {
            double aMajorAwal = semiMajor;
            double bMinorAwal = semiMinor;
            int totalDataThread = dataAkhir - dataAwal + 1;

            for (int i = dataAwal; i <= dataAkhir; i++) {
                if (i % 100 == 0 || i == dataAkhir) {
                    int progress = (int) (((double) (i - dataAwal + 1) / totalDataThread) * 100);
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    Thread.sleep(150);
                }

                semiMajor = aMajorAwal * i;
                semiMinor = bMinorAwal * i;

                hitungLuas();
                hitungKeliling();

                logArea.append(namaThread + " memproses data ke-" + i + "\n");

                gui.tambahBarisTabel(new Object[]{
                    namaThread, "Elips", i,
                    luas, keliling, "-", "-"
                });
            }
            logArea.append(namaThread + " selesai.\n");

        } catch (Exception e) {
            logArea.append(namaThread + " gagal : " + e.getMessage() + "\n");
        }
    }
}
