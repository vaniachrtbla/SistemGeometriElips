package Bangun3Dimensi;

/**
 * LimasElipsTerpancung: limas elips dipotong bagian atasnya.
 * Extends LimasElips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class LimasElipsTerpancung extends LimasElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double semiMayorAtas;
    public double semiMinorAtas;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public LimasElipsTerpancung(double semiMayor, double semiMinor,
                                double tinggi,
                                double semiMayorAtas, double semiMinorAtas,
                                int jumlahData) throws Exception {

        super(semiMayor, semiMinor, tinggi, jumlahData); // polimorfisme eksplisit

        if (semiMayorAtas <= 0 || semiMinorAtas <= 0) {
            throw new Exception("[LimasElipsTerpancung] Dimensi alas atas harus positif!");
        }
        if (semiMayorAtas >= semiMayor || semiMinorAtas >= semiMinor) {
            throw new Exception("[LimasElipsTerpancung] Alas atas harus lebih kecil dari alas bawah!");
        }

        this.semiMayorAtas = semiMayorAtas;
        this.semiMinorAtas = semiMinorAtas;
        this.namaThread    = "Thread-LimasTerpancung";

        System.out.println("[LOG][LimasElipsTerpancung] Constructor dipanggil: a2=" + semiMayorAtas
                + ", b2=" + semiMinorAtas);
    }

    // ====== GETTER / SETTER ======
    public double getSemiMayorAtas() { return semiMayorAtas; }
    public void   setSemiMayorAtas(double v) { this.semiMayorAtas = v; }
    public double getSemiMinorAtas() { return semiMinorAtas; }
    public void   setSemiMinorAtas(double v) { this.semiMinorAtas = v; }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    @Override
    public double hitungVolume() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0 || tinggi <= 0
                || semiMayorAtas <= 0 || semiMinorAtas <= 0) {
            throw new ArithmeticException("[LimasElipsTerpancung] Dimensi tidak valid saat hitungVolume!");
        }
        double luasAlasBawah = Math.PI * semiMayor * semiMinor;
        double luasAlasAtas  = Math.PI * semiMayorAtas * semiMinorAtas;
        hasilVolume = (tinggi / 3.0)
                * (luasAlasBawah + luasAlasAtas + Math.sqrt(luasAlasBawah * luasAlasAtas));
        return hasilVolume;
    }

    // ====== OVERLOADING VOLUME (dengan parameter) ======
    public double hitungVolume(double a1, double b1,
                               double a2, double b2,
                               double t) throws Exception {

        if (a1 <= 0 || b1 <= 0 || a2 <= 0 || b2 <= 0 || t <= 0) {
            throw new Exception("[LimasElipsTerpancung] Semua dimensi harus positif!");
        }
        double A1 = Math.PI * a1 * b1;
        double A2 = Math.PI * a2 * b2;
        return (t / 3.0) * (A1 + A2 + Math.sqrt(A1 * A2));
    }

    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {

        System.out.println("=== LIMAS ELIPS TERPANCUNG ===");
        System.out.println("Semi Mayor Bawah (a) : " + semiMayor);
        System.out.println("Semi Minor Bawah (b) : " + semiMinor);
        System.out.println("Semi Mayor Atas (a2) : " + semiMayorAtas);
        System.out.println("Semi Minor Atas (b2) : " + semiMinorAtas);
        System.out.println("Tinggi               : " + tinggi);
        try {
            System.out.printf("Luas Permukaan       : %.4f%n", hitungLuas());
            System.out.printf("Volume               : %.4f%n", hitungVolume());
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

            System.out.println("[" + namaThread + "] START – hitung LimasTerpancung, jumlahData=" + jumlahData);

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
