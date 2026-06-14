package Bangun2Dimensi;

public class TemberengElips extends Elips implements Runnable {

    public double sudut;

    public TemberengElips(double semiMayor, double semiMinor, double sudut, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, jumlahData);
        if (sudut <= 0 || sudut >= 360)
            throw new IllegalArgumentException("[Tembereng] Sudut tidak valid");
        this.sudut = sudut;
        this.namaThread = "Thread-Tembereng";
        System.out.println("[LOG][Tembereng] Constructor dipanggil");
    }

    @Override
    public double hitungLuas() {
        double luasElips = super.hitungLuas();
        double juring = (sudut / 360.0) * luasElips;
        double segitiga = 0.5 * semiMayor * semiMinor * Math.sin(Math.toRadians(sudut));
        hasilLuas = juring - segitiga;
        return hasilLuas;
    }

    @Override
    public double hitungLuas(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Tembereng] Parameter tidak valid");
        return super.hitungLuas(a, b);
    }

    public double hitungLuas(double a, double b, double sudutDeg) {
        if (a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg >= 360)
            throw new IllegalArgumentException("[Tembereng] Input tidak valid");
        double luasElips = Math.PI * a * b;
        double juring = (sudutDeg / 360.0) * luasElips;
        double segitiga = 0.5 * a * b * Math.sin(Math.toRadians(sudutDeg));
        return juring - segitiga;
    }

    @Override
    public double hitungKeliling() { return super.hitungKeliling(); }

    @Override
    public double hitungKeliling(double a, double b) { return super.hitungKeliling(a, b); }

    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                double s = (i == 0) ? sudut : Math.min(359.99, Math.max(0.01, variasi(sudut)));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataHasilLuas[i] = hitungLuas(a, b, s);

                if (Thread.interrupted()) throw new InterruptedException();
                Thread.sleep(1);
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE");

        } catch (InterruptedException e) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}