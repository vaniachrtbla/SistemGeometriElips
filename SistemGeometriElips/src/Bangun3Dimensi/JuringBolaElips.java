package Bangun3Dimensi;

public class JuringBolaElips extends BolaElips implements Runnable {

    public double sudut;

    public JuringBolaElips(double semiMayor, double semiMinor, double semiAxisC,
                            double sudut, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, semiAxisC, jumlahData);
        if (sudut <= 0 || sudut > 360)
            throw new IllegalArgumentException("[JuringBolaElips] Sudut harus 0 < sudut ≤ 360");
        this.sudut = sudut;
        this.namaThread = "Thread-JuringBolaElips";
        System.out.println("[LOG] JuringBolaElips dibuat: sudut=" + sudut);
    }

    // ====== HITUNG LUAS ======
    @Override
    public double hitungLuas() {
        if (sudut <= 0 || sudut > 360)
            throw new ArithmeticException(
                    "[JuringBolaElips] Sudut tidak valid!");
        hasilLuas =
                (sudut / 360.0)* super.hitungLuas();
        return hasilLuas;
    }
    // OVERLOADING
    public double hitungLuas(
            double a,
            double b,
            double c,
            double sudutDeg) {
        if (a <= 0 || b <= 0 || c <= 0
                || sudutDeg <= 0 || sudutDeg > 360)
            throw new IllegalArgumentException(
                    "[JuringBolaElips] Parameter tidak valid!");
        hasilLuas =
                (sudutDeg / 360.0)
                * super.hitungLuas(a, b, c);
        return hasilLuas;
    }


    // ====== HITUNG VOLUME ======
    @Override
    public double hitungVolume() {

        if (sudut <= 0 || sudut > 360)
            throw new ArithmeticException(
                    "[JuringBolaElips] Sudut tidak valid!");

        hasilVolume =
                (sudut / 360.0)
                * super.hitungVolume();

        return hasilVolume;
    }
    // OVERLOADING
    public double hitungVolume(double a,double b,double c,double sudutDeg) {
        if (a <= 0 || b <= 0 || c <= 0
                || sudutDeg <= 0 || sudutDeg > 360)
            throw new IllegalArgumentException(
                    "[JuringBolaElips] Parameter tidak valid!");
        hasilVolume =
                (sudutDeg / 360.0)
                * super.hitungVolume(a, b, c);
        return hasilVolume;
    }

    @Override
    public void run() {
        try {
            waktuMulai = System.nanoTime();
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                double c = (i == 0) ? semiAxisC : Math.max(0.01, variasi(semiAxisC));
                double s = (i == 0) ? sudut : Math.min(360, Math.max(0.01, variasi(sudut)));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataSemiAxisC[i] = c;
                dataHasilLuas[i] = hitungLuas(a, b, c, s);
                dataHasilVolume[i] = hitungVolume(a, b, c, s);

                if (Thread.interrupted()) throw new InterruptedException();
                
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            waktuSelesai = System.nanoTime();
            durasiDetik = (waktuSelesai - waktuMulai) / 1_000_000_000.0;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE dalam "
                + String.format("%.4f", durasiDetik) + " detik"
            );

        } catch (InterruptedException e) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}