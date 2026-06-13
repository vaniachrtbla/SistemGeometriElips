package Bangun3Dimensi;

/**
 * JuringBolaElips: potongan bola elips berdasarkan sudut.
 * Extends BolaElips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class JuringBolaElips extends BolaElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double sudut;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public JuringBolaElips(double semiMayor, double semiMinor,
                           double semiAxisC, double sudut,
                           int jumlahData) throws Exception {

        super(semiMayor, semiMinor, semiAxisC, jumlahData); // polimorfisme eksplisit

        if (sudut <= 0 || sudut > 360) {
            throw new Exception("[JuringBolaElips] Sudut harus antara 0 dan 360 derajat!");
        }

        this.sudut      = sudut;
        this.namaThread = "Thread-JuringBola";

        System.out.println("[LOG][JuringBolaElips] Constructor dipanggil: sudut=" + sudut);
    }

    // ====== GETTER / SETTER ======
    public double getSudut() { return sudut; }
    public void   setSudut(double v) { this.sudut = v; }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    @Override
    public double hitungVolume() throws ArithmeticException {

        if (sudut <= 0 || sudut > 360) {
            throw new ArithmeticException("[JuringBolaElips] Sudut tidak valid saat hitungVolume!");
        }
        double volumePenuh = (4.0 / 3.0) * Math.PI * semiMayor * semiMinor * semiAxisC;
        hasilVolume = (sudut / 360.0) * volumePenuh;
        return hasilVolume;
    }

    // ====== OVERLOADING VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double c, double sudutDeg) throws Exception {

        if (a <= 0 || b <= 0 || c <= 0) {
            throw new Exception("[JuringBolaElips] Dimensi harus positif!");
        }
        if (sudutDeg <= 0 || sudutDeg > 360) {
            throw new Exception("[JuringBolaElips] Sudut tidak valid!");
        }
        return (sudutDeg / 360.0) * (4.0 / 3.0) * Math.PI * a * b * c;
    }

    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {

        System.out.println("=== JURING BOLA ELIPS ===");
        System.out.println("Semi Mayor (a)  : " + semiMayor);
        System.out.println("Semi Minor (b)  : " + semiMinor);
        System.out.println("Semi Axis C (c) : " + semiAxisC);
        System.out.println("Sudut           : " + sudut + " derajat");
        try {
            System.out.printf("Luas Permukaan  : %.4f%n", hitungLuas());
            System.out.printf("Volume Juring   : %.4f%n", hitungVolume());
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

            System.out.println("[" + namaThread + "] START – hitung JuringBolaElips, jumlahData=" + jumlahData);

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
