package Bangun3Dimensi;

/**
 * Cincin3Dimensi: bola elips berlubang (torus elips).
 * Extends BolaElips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class Cincin3Dimensi extends BolaElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double radiusDalam;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public Cincin3Dimensi(double semiMayor, double semiMinor,
                          double semiAxisC, double radiusDalam,
                          int jumlahData) throws Exception {

        super(semiMayor, semiMinor, semiAxisC, jumlahData); // polimorfisme eksplisit

        if (radiusDalam <= 0) {
            throw new Exception("[Cincin3Dimensi] Radius dalam harus positif!");
        }
        if (radiusDalam >= semiMayor || radiusDalam >= semiMinor) {
            throw new Exception("[Cincin3Dimensi] Radius dalam harus lebih kecil dari semi mayor dan semi minor!");
        }

        this.radiusDalam = radiusDalam;
        this.namaThread  = "Thread-Cincin3D";

        System.out.println("[LOG][Cincin3Dimensi] Constructor dipanggil: rDalam=" + radiusDalam);
    }

    // ====== OVERRIDE HITUNG VOLUME (tanpa parameter) – throw Exception ======
    @Override
    public double hitungVolume() throws ArithmeticException {
        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0 || radiusDalam <= 0) {
            throw new ArithmeticException("[Cincin3Dimensi] Dimensi tidak valid saat hitungVolume!");
        }
        double volumeLuar = super.hitungVolume();
        double volumeDalam = Math.PI * radiusDalam * radiusDalam * (2 * semiAxisC);
        hasilVolume = volumeLuar - volumeDalam;
        if (hasilVolume < 0) hasilVolume = 0;
        return hasilVolume;
    }

    // ====== OVERLOADING VOLUME (dengan parameter) ======
    public double hitungVolume(double a, double b, double c, double rDalam) throws Exception {

        if (a <= 0 || b <= 0 || c <= 0 || rDalam <= 0) {
            throw new Exception("[Cincin3Dimensi] Semua dimensi harus positif!");
        }
        double vLuar = super.hitungVolume(a,b,c);
        double vDalam = Math.PI * rDalam * rDalam * (2 * c);
        double hasil  = vLuar - vDalam;
        if (hasil < 0) {throw new Exception("[Cincin3Dimensi] Radius dalam terlalu besar!");}
        return hasil;
    }

    // ====== TAMPIL INFO ======
    @Override
    public void tampilInfo() {
        System.out.println("=== CINCIN 3 DIMENSI ===");
        System.out.println("Semi Mayor (a)  : " + semiMayor);
        System.out.println("Semi Minor (b)  : " + semiMinor);
        System.out.println("Semi Axis C (c) : " + semiAxisC);
        System.out.println("Radius Dalam    : " + radiusDalam);
        try {
            System.out.printf("Luas Permukaan  : %.4f%n", hitungLuas());
            System.out.printf("Volume Cincin   : %.4f%n", hitungVolume());
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

            System.out.println("[" + namaThread + "] START – hitung Cincin3D, jumlahData=" + jumlahData);

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
