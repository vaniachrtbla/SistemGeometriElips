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
    // ====== HITUNG LUAS ======

    @Override
    public double hitungLuas() {
        if (sudut <= 0 || sudut >= 360)
            throw new ArithmeticException("[Tembereng] Sudut tidak valid");
        double luasElips = super.hitungLuas();
        double juring = (sudut / 360.0) * luasElips;
        double segitiga =
                0.5 * semiMayor * semiMinor
                * Math.sin(Math.toRadians(sudut));
        hasilLuas = juring - segitiga;
        return hasilLuas;
    }


    // OVERLOADING
    public double hitungLuas(double a, double b, double sudutDeg) {
        if (a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg >= 360)
            throw new IllegalArgumentException("[Tembereng] Input tidak valid");
        double luasElips = super.hitungLuas(a, b);
        double juring = (sudutDeg / 360.0) * luasElips;
        double segitiga =
                0.5 * a * b
                * Math.sin(Math.toRadians(sudutDeg));
        hasilLuas = juring - segitiga;
        return hasilLuas;
    }

    // ====== HITUNG KELILING ======
    @Override
    public double hitungKeliling() {
        if (sudut <= 0 || sudut >= 360)
            throw new ArithmeticException("[Tembereng] Sudut tidak valid");
        // keliling elips penuh
        double kelilingElips = super.hitungKeliling();
        // panjang busur
        double panjangBusur =
                (sudut / 360.0) * kelilingElips;
        // panjang tali busur
        double taliBusur =
                2 * Math.sqrt(
                    Math.pow(semiMayor * Math.sin(Math.toRadians(sudut / 2)), 2)
                  + Math.pow(semiMinor * Math.cos(Math.toRadians(sudut / 2)), 2)
                );
        hasilKeliling = panjangBusur + taliBusur;
        return hasilKeliling;
    }

    // OVERLOADING
    @Override
    public double hitungKeliling(double a, double b) {
        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Tembereng] Parameter tidak valid");
        double kelilingElips = super.hitungKeliling(a, b);
        double panjangBusur =
                (sudut / 360.0) * kelilingElips;
        double taliBusur =
                2 * Math.sqrt(
                    Math.pow(a * Math.sin(Math.toRadians(sudut / 2)), 2)
                  + Math.pow(b * Math.cos(Math.toRadians(sudut / 2)), 2)
                );
        hasilKeliling = panjangBusur + taliBusur;
        return hasilKeliling;
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
                double s = (i == 0) ? sudut : Math.min(359.99, Math.max(0.01, variasi(sudut)));
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataHasilLuas[i] = hitungLuas(a, b, s);
                dataHasilKeliling[i] = hitungKeliling(a, b);

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
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}