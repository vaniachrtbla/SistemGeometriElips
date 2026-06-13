package Bangun3Dimensi;

/**
 * TemberengBolaElips: bagian bola elips dipotong bidang datar.
 * Extends BolaElips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class TemberengBolaElips extends BolaElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double tinggiTembereng;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public TemberengBolaElips(double semiMayor, double semiMinor,
                              double semiAxisC, double tinggiTembereng,
                              int jumlahData) throws Exception {

        super(semiMayor, semiMinor, semiAxisC, jumlahData); // polimorfisme eksplisit

        if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC) {
            throw new Exception("[TemberengBolaElips] Tinggi tembereng tidak valid! Harus antara 0 dan semiAxisC.");
        }

        this.tinggiTembereng = tinggiTembereng;
        this.namaThread      = "Thread-TemberengBola";

        System.out.println("[LOG][TemberengBolaElips] Constructor dipanggil: h=" + tinggiTembereng);
    }

    // ====== GETTER / SETTER ======
    public double getTinggiTembereng() { return tinggiTembereng; }
    public void   setTinggiTembereng(double v) { this.tinggiTembereng = v; }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    @Override
    public double hitungVolume() throws ArithmeticException {

        if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC) {
            throw new ArithmeticException("[TemberengBolaElips] Tinggi tembereng tidak valid saat hitungVolume!");
        }
        double h = tinggiTembereng;
        double c = semiAxisC;
        hasilVolume = Math.PI * h * h * (c - h / 3.0) * (semiMayor * semiMinor / (c * c));
        return hasilVolume;
    }

    // ====== OVERLOADING VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double c, double h) throws Exception {

        if (a <= 0 || b <= 0 || c <= 0 || h <= 0) {
            throw new Exception("[TemberengBolaElips] Semua dimensi harus positif!");
        }
        if (h > c) {
            throw new Exception("[TemberengBolaElips] Tinggi tembereng melebihi semi axis C!");
        }
        return Math.PI * h * h * (c - h / 3.0) * (a * b / (c * c));
    }

    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {

        System.out.println("=== TEMBERENG BOLA ELIPS ===");
        System.out.println("Semi Mayor (a)       : " + semiMayor);
        System.out.println("Semi Minor (b)       : " + semiMinor);
        System.out.println("Semi Axis C (c)      : " + semiAxisC);
        System.out.println("Tinggi Tembereng (h) : " + tinggiTembereng);
        try {
            System.out.printf("Luas Permukaan       : %.4f%n", hitungLuas());
            System.out.printf("Volume Tembereng     : %.4f%n", hitungVolume());
        } catch (Exception e) {
            System.out.println("[ERROR] tampilInfo: " + e.getMessage());
        }
    }

    // ====== MULTITHREADING – RUNNABLE ======
    @Override
    public void run() {

        try {
            statusThread = "RUNNING";
            progress     = 0;

            System.out.println("[" + namaThread + "] START – hitung TemberengBolaElips, jumlahData=" + jumlahData);

            for (int i = 1; i <= jumlahData; i++) {

                hasilLuas   = hitungLuas();
                hasilVolume = hitungVolume();
                Thread.sleep(1);
                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0 && progress > 0 && (i * 100 % jumlahData == 0)) {
                    System.out.println("[" + namaThread + "] Progress: " + progress + "%");
                }
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE – Volume=" + String.format("%.4f", hasilVolume));

        } catch (InterruptedException ie) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
            System.out.println("[" + namaThread + "] INTERRUPTED");

        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}
