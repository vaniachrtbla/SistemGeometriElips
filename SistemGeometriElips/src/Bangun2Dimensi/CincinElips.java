package Bangun2Dimensi;

public class CincinElips extends Elips implements Runnable {

    public double semiMayorDalam;
    public double semiMinorDalam;

    public CincinElips(double semiMayorLuar, double semiMinorLuar,
                       double semiMayorDalam, double semiMinorDalam,
                       int jumlahData) throws Exception {
        super(semiMayorLuar, semiMinorLuar, jumlahData);
        if (semiMayorDalam <= 0 || semiMinorDalam <= 0)
            throw new IllegalArgumentException("[Cincin] Dimensi dalam harus positif!");
        if (semiMayorDalam >= semiMayorLuar || semiMinorDalam >= semiMinorLuar)
            throw new IllegalArgumentException("[Cincin] Elips dalam harus lebih kecil dari elips luar!");
        this.semiMayorDalam = semiMayorDalam;
        this.semiMinorDalam = semiMinorDalam;
        this.namaThread = "Thread-Cincin";
        System.out.println("[LOG][Cincin] Constructor dipanggil: aLuar=" + semiMayorLuar + ", aDalam=" + semiMayorDalam);
    }

    @Override
    public double hitungLuas() {
        if (semiMayor <= 0 || semiMinor <= 0 || semiMayorDalam <= 0 || semiMinorDalam <= 0)
            throw new ArithmeticException("[Cincin] Dimensi tidak valid!");
        hasilLuas = super.hitungLuas() - super.hitungLuas(semiMayorDalam, semiMinorDalam);
        return hasilLuas;
    }

    @Override
    public double hitungLuas(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Cincin] Parameter harus positif!");
        return super.hitungLuas(a, b);
    }

    public double hitungLuas(double aLuar, double bLuar, double aDalam, double bDalam) {
        if (aLuar <= 0 || bLuar <= 0 || aDalam <= 0 || bDalam <= 0)
            throw new IllegalArgumentException("[Cincin] Semua dimensi harus positif!");
        return super.hitungLuas(aLuar, bLuar) - super.hitungLuas(aDalam, bDalam);
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
                double aL = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double bL = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                // Pastikan dimensi dalam tetap lebih kecil
                double aD = Math.min(semiMayorDalam, aL * 0.9);
                double bD = Math.min(semiMinorDalam, bL * 0.9);
                if (aD <= 0) aD = aL * 0.3;
                if (bD <= 0) bD = bL * 0.3;
                dataSemiMayor[i] = aL;
                dataSemiMinor[i] = bL;
                dataHasilLuas[i] = hitungLuas(aL, bL, aD, bD);

                if (Thread.interrupted()) throw new InterruptedException();
                
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