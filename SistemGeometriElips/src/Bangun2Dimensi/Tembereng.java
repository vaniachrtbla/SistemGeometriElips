package Bangun2Dimensi;

/**
 * Tembereng: daerah elips dipotong tali busur.
 * Konsep: juring elips - segitiga pusat.
 */
public class Tembereng extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double sudut;

    // ====== CONSTRUCTOR ======
    public Tembereng(
            double semiMayor,
            double semiMinor,
            double sudut,
            int jumlahData
    ) throws Exception {

        super(semiMayor, semiMinor, jumlahData);

        if (sudut <= 0 || sudut >= 360) {
            throw new IllegalArgumentException(
                "[Tembereng] Sudut harus 0 - 360"
            );
        }

        this.sudut = sudut;
        this.namaThread = "Thread-Tembereng";

        System.out.println(
            "[LOG][Bangun2Dimensi.Tembereng] Constructor dipanggil: a="
            + semiMayor + ", b=" + semiMinor + ", sudut=" + sudut
        );
    }

    // ====== OVERRIDE LUAS ======
    @Override
    public double hitungLuas() {

        if (sudut <= 0 || sudut >= 360) {
            throw new IllegalArgumentException("[Tembereng] Sudut tidak valid");
        }

        // luas elips penuh dari parent
        double luasElips = super.hitungLuas();

        // juring elips
        double juring = (sudut / 360.0) * luasElips;

        // segitiga pusat elips
        double rad = Math.toRadians(sudut);
        double segitiga = 0.5 * semiMayor * semiMinor * Math.sin(rad);

        hasilLuas = juring - segitiga;

        return hasilLuas;
    }

    // ====== OVERLOADING (sesuai interface) ======
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("[Tembereng] Parameter tidak valid");
        }

        return super.hitungLuas(a, b);
    }

    // ====== OVERLOADING KHUSUS (utility optional) ======
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

    // ====== KELILING ======
        @Override
    public double hitungKeliling() {

        if (sudut <= 0 || sudut >= 360) {
            throw new IllegalArgumentException(
                "[Tembereng] Sudut tidak valid"
            );
        }

        // keliling elips penuh dari parent
        double kelilingElips = super.hitungKeliling();

        // panjang busur
        double busur =
                (sudut / 360.0) * kelilingElips;

        // konversi sudut ke radian
        double rad = Math.toRadians(sudut);

        // pendekatan tali busur
        double taliBusur =
                2 * semiMayor * Math.sin(rad / 2);

        return busur + taliBusur;
    }

    @Override
    public double hitungKeliling(double a, double b) {
        return super.hitungKeliling(a, b);
    }

    // ====== TAMPIL INFO ======
    public void tampilInfo() {

        System.out.println("=== TEMBERENG ELIPS ===");
        System.out.println("a = " + semiMayor);
        System.out.println("b = " + semiMinor);
        System.out.println("sudut = " + sudut);

        try {
            System.out.printf("Luas = %.4f%n", hitungLuas());
            System.out.printf("Keliling = %.4f%n", hitungKeliling());
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // ====== THREAD ======
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
            System.out.println("[Bangun2Dimensi.Tembereng] " + e.getMessage());
        }
    }
}