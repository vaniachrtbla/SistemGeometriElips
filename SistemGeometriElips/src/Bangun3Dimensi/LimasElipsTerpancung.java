package Bangun3Dimensi;

public class LimasElipsTerpancung extends LimasElips implements Runnable {

    public double semiMayorAtas;
    public double semiMinorAtas;

    public LimasElipsTerpancung(double semiMayor, double semiMinor, double tinggi,
                                 double semiMayorAtas, double semiMinorAtas,
                                 int jumlahData) throws Exception {
        super(semiMayor, semiMinor, tinggi, jumlahData);
        if (semiMayorAtas <= 0 || semiMinorAtas <= 0)
            throw new Exception("[LimasElipsTerpancung] Dimensi alas atas harus positif!");
        if (semiMayorAtas >= semiMayor || semiMinorAtas >= semiMinor)
            throw new Exception("[LimasElipsTerpancung] Alas atas harus lebih kecil dari alas bawah!");
        this.semiMayorAtas = semiMayorAtas;
        this.semiMinorAtas = semiMinorAtas;
        this.namaThread = "Thread-LimasTerpancung";
        System.out.println("[LOG][LimasElipsTerpancung] Constructor dipanggil: a2=" + semiMayorAtas + ", b2=" + semiMinorAtas);
    }

    @Override
    public double hitungVolume() {
        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0 || semiMayorAtas <= 0 || semiMinorAtas <= 0)
            throw new ArithmeticException("[LimasElipsTerpancung] Dimensi tidak valid saat hitungVolume!");
        double A1 = Math.PI * semiMayor * semiMinor;
        double A2 = Math.PI * semiMayorAtas * semiMinorAtas;
        hasilVolume = (tinggi / 3.0) * (A1 + A2 + Math.sqrt(A1 * A2));
        return hasilVolume;
    }

    public double hitungVolume(double a1, double b1, double a2, double b2, double t) throws Exception {
        if (a1 <= 0 || b1 <= 0 || a2 <= 0 || b2 <= 0 || t <= 0)
            throw new Exception("[LimasElipsTerpancung] Semua dimensi harus positif!");
        double A1 = Math.PI * a1 * b1;
        double A2 = Math.PI * a2 * b2;
        return (t / 3.0) * (A1 + A2 + Math.sqrt(A1 * A2));
    }

    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a1 = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b1 = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                double t  = (i == 0) ? tinggi : Math.max(0.01, variasi(tinggi));
                // Pastikan alas atas tetap < alas bawah
                double a2 = Math.min(semiMayorAtas, a1 * 0.9);
                double b2 = Math.min(semiMinorAtas, b1 * 0.9);
                if (a2 <= 0) a2 = a1 * 0.3;
                if (b2 <= 0) b2 = b1 * 0.3;
                dataSemiMayor[i] = a1;
                dataSemiMinor[i] = b1;
                dataTinggi[i] = t;
                dataHasilLuas[i] = hitungLuas(a1, b1, t);
                dataHasilVolume[i] = hitungVolume(a1, b1, a2, b2, t);

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