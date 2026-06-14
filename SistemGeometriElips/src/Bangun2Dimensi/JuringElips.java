package Bangun2Dimensi;

public class JuringElips extends Elips implements Runnable {

    public double sudut;

    public JuringElips(double semiMayor, double semiMinor, double sudut, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, jumlahData);
        if (sudut <= 0 || sudut > 360)
            throw new IllegalArgumentException("[Juring] Sudut harus 0-360");
        this.sudut = sudut;
        this.namaThread = "Thread-Juring";
        System.out.println("[LOG][Juring] Constructor dipanggil");
    }

    @Override
    public double hitungLuas() {
        if (sudut <= 0 || sudut > 360)
            throw new ArithmeticException("[Juring] Sudut tidak valid");
        hasilLuas = (sudut / 360.0) * super.hitungLuas();
        return hasilLuas;
    }

    @Override
    public double hitungLuas(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Juring] Parameter tidak valid");
        return super.hitungLuas(a, b);
    }

    public double hitungLuas(double a, double b, double sudutDeg) {
        if (a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg > 360)
            throw new IllegalArgumentException("[Juring] Input tidak valid");
        return (sudutDeg / 360.0) * super.hitungLuas(a, b);
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
                double s = (i == 0) ? sudut : Math.min(360, Math.max(0.01, variasi(sudut)));
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