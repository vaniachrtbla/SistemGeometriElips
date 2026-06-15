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

    // ====== HITUNG LUAS ======

    @Override
    public double hitungLuas() {

        if (sudut <= 0 || sudut > 360)
            throw new ArithmeticException("[Juring] Sudut tidak valid");

        // luas juring = sudut/360 × luas elips
        hasilLuas = (sudut / 360.0) * super.hitungLuas();

        return hasilLuas;
    }

    // OVERLOADING
    public double hitungLuas(double a, double b, double sudutDeg) {

        if (a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg > 360)
            throw new IllegalArgumentException("[Juring] Input tidak valid");
        hasilLuas = (sudutDeg / 360.0) * super.hitungLuas(a, b);
        return hasilLuas;
    }


    // ====== HITUNG KELILING ======

    @Override
    public double hitungKeliling() {
        if (sudut <= 0 || sudut > 360)
            throw new ArithmeticException("[Juring] Sudut tidak valid");
        // keliling elips penuh
        double kelilingElips = super.hitungKeliling();
        // panjang busur juring
        double panjangBusur = (sudut / 360.0) * kelilingElips;
        // pendekatan 2 sisi radius
        double radiusRata = (semiMayor + semiMinor) / 2.0;
        hasilKeliling = panjangBusur + (2 * radiusRata);
        return hasilKeliling;
    }


    // OVERLOADING
    @Override
    public double hitungKeliling(double a, double b) {

        if (a <= 0 || b <= 0)
            throw new IllegalArgumentException("[Juring] Parameter tidak valid");
        double kelilingElips = super.hitungKeliling(a, b);
        double panjangBusur = (sudut / 360.0) * kelilingElips;
        double radiusRata = (a + b) / 2.0;
        hasilKeliling = panjangBusur + (2 * radiusRata);
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
                double s = (i == 0) ? sudut : Math.min(360, Math.max(0.01, variasi(sudut)));
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