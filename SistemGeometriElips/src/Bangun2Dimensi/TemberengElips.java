package Bangun2Dimensi;

/**
 * Tembereng: bagian elips (juring - segitiga).
 * Konsep: fraksi elips dikurangi segitiga pusat.
 */
public class TemberengElips extends Elips implements Runnable {

    // ===== ATRIBUT PUBLIC =====
    public double sudut;

    // ===== CONSTRUCTOR =====
    public TemberengElips(double semiMayor, double semiMinor, double sudut, int jumlahData) throws Exception {

        super(semiMayor, semiMinor, jumlahData);

        if (sudut <= 0 || sudut >= 360) {
            throw new IllegalArgumentException("[Tembereng] Sudut tidak valid");
        }

        this.sudut = sudut;
        this.namaThread = "Thread-Tembereng";

        System.out.println("[LOG][Tembereng] Constructor dipanggil");
    }

    // ===== OVERRIDE LUAS (STATE BASED) =====
    @Override
    public double hitungLuas() {

        double luasElips = super.hitungLuas();

        double juring = (sudut / 360.0) * luasElips;

        double rad = Math.toRadians(sudut);
        double segitiga = 0.5 * semiMayor * semiMinor * Math.sin(rad);

        hasilLuas = juring - segitiga;
        return hasilLuas;
    }

    // ===== OVERLOADING WAJIB INTERFACE =====
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("[Tembereng] Parameter tidak valid");
        }

        return super.hitungLuas(a, b);
    }

    // ===== OVERLOADING TAMBAHAN (LEVEL JURING STYLE) =====
    public double hitungLuas(double a, double b, double sudutDeg) {

        if (a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg >= 360) {
            throw new IllegalArgumentException("[Tembereng] Input tidak valid");
        }

        double luasElips = Math.PI * a * b;

        double juring = (sudutDeg / 360.0) * luasElips;

        double rad = Math.toRadians(sudutDeg);
        double segitiga = 0.5 * a * b * Math.sin(rad);

        return juring - segitiga;
    }

    // ===== KELILING =====
    @Override
    public double hitungKeliling() {
        return super.hitungKeliling();
    }

    @Override
    public double hitungKeliling(double a, double b) {
        return super.hitungKeliling(a, b);
    }

    // ===== THREAD (SAMA STYLE JURING & CINCIN) =====
    @Override
    public void run() {

        try {
            statusThread = "RUNNING";
            progress = 0;

            System.out.println("[" + namaThread + "] START");

            for (int i = 1; i <= jumlahData; i++) {

                hitungLuas();
                hitungKeliling();

                Thread.sleep(1);

                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0 && progress > 0) {
                    System.out.println("[" + namaThread + "] " + progress + "%");
                }
            }

            statusThread = "DONE";
            progress = 100;

            System.out.println("[" + namaThread + "] FINISH");

        } catch (InterruptedException e) {

            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();

        } catch (Exception e) {

            statusThread = "ERROR";
            System.out.println("[Tembereng] " + e.getMessage());
        }
    }
}