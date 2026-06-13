package Bangun2Dimensi;

/**
 * Tembereng: daerah elips dipotong tali busur.
 * Extends Elips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungLuas,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class Tembereng extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double sudut;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public Tembereng(double semiMayor, double semiMinor,
                     double sudut, int jumlahData) throws Exception {

        super(semiMayor, semiMinor, jumlahData); // polimorfisme eksplisit

        if (sudut <= 0 || sudut >= 360) {
            throw new Exception("[Tembereng] Sudut tembereng harus antara 0 dan 360 derajat!");
        }

        this.sudut      = sudut;
        this.namaThread = "Thread-Tembereng";

        System.out.println("[LOG][Tembereng] Constructor dipanggil: a=" + semiMayor
                + ", b=" + semiMinor + ", sudut=" + sudut);
    }

    // ====== GETTER / SETTER ======
    public double getSudut() { return sudut; }
    public void   setSudut(double v) { this.sudut = v; }

    // ====== OVERRIDE HITUNG LUAS (tanpa parameter) – throw Exception ======
    @Override
    public double hitungLuas() throws ArithmeticException {

        if (sudut <= 0 || sudut >= 360) {
            throw new ArithmeticException("[Tembereng] Sudut tidak valid saat hitungLuas!");
        }
        double sudutRad    = Math.toRadians(sudut);
        double luasJuring  = (sudut / 360.0) * Math.PI * semiMayor * semiMinor;
        double luasSegitiga = 0.5 * semiMayor * semiMinor * Math.sin(sudutRad);
        hasilLuas = luasJuring - luasSegitiga;
        return hasilLuas;
    }

    // ====== OVERLOADING HITUNG LUAS (dengan parameter) ======
    public double hitungLuas(double a, double b, double sudutDeg) throws Exception {

        if (a <= 0 || b <= 0) {
            throw new Exception("[Tembereng] Dimensi harus positif!");
        }
        if (sudutDeg <= 0 || sudutDeg >= 360) {
            throw new Exception("[Tembereng] Sudut tidak valid!");
        }
        double rad     = Math.toRadians(sudutDeg);
        double juring  = (sudutDeg / 360.0) * Math.PI * a * b;
        double segitiga = 0.5 * a * b * Math.sin(rad);
        return juring - segitiga;
    }


    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {

        System.out.println("=== TEMBERENG ELIPS ===");
        System.out.println("Semi Mayor (a) : " + semiMayor);
        System.out.println("Semi Minor (b) : " + semiMinor);
        System.out.println("Sudut          : " + sudut + " derajat");
        try {
            System.out.printf("Luas Tembereng : %.4f%n", hitungLuas());
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

            System.out.println("[" + namaThread + "] START – hitung Tembereng, jumlahData=" + jumlahData);

            for (int i = 1; i <= jumlahData; i++) {

                hasilLuas = hitungLuas();
                Thread.sleep(1);
                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0 && progress > 0 && (i * 100 % jumlahData == 0)) {
                    System.out.println("[" + namaThread + "] Progress: " + progress + "%");
                }
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE – Luas Tembereng=" + String.format("%.4f", hasilLuas));

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
