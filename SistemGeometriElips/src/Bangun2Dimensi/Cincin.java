package Bangun2Dimensi;

/**
 * Cincin elips: daerah antara dua elips konsentris.
 * Extends Elips.
 * 
 * @author Swift
 */
public class Cincin extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double semiMayorDalam;
    public double semiMinorDalam;

    // ====== CONSTRUCTOR ======
    public Cincin(
            double semiMayorLuar,
            double semiMinorLuar,
            double semiMayorDalam,
            double semiMinorDalam,
            int jumlahData
    ) throws Exception {

        // memanggil constructor parent Elips
        super(semiMayorLuar, semiMinorLuar, jumlahData);

        if (semiMayorDalam <= 0 || semiMinorDalam <= 0) {

            throw new IllegalArgumentException(
                    "[Cincin] Dimensi dalam harus positif!"
            );
        }

        if (semiMayorDalam >= semiMayorLuar
                || semiMinorDalam >= semiMinorLuar) {

            throw new IllegalArgumentException(
                    "[Cincin] Elips dalam harus lebih kecil dari elips luar!"
            );
        }

        this.semiMayorDalam = semiMayorDalam;
        this.semiMinorDalam = semiMinorDalam;

        this.namaThread = "Thread-Cincin";

        System.out.println(
                "[LOG][Cincin] Constructor dipanggil: aLuar="
                + semiMayorLuar
                + ", aDalam="
                + semiMayorDalam
        );
    }

    // ====== GETTER / SETTER ======
    public double getSemiMayorDalam() {
        return semiMayorDalam;
    }

    public void setSemiMayorDalam(double semiMayorDalam) {
        this.semiMayorDalam = semiMayorDalam;
    }

    public double getSemiMinorDalam() {
        return semiMinorDalam;
    }

    public void setSemiMinorDalam(double semiMinorDalam) {
        this.semiMinorDalam = semiMinorDalam;
    }

    // ====== OVERRIDE HITUNG LUAS ======
    @Override
    public double hitungLuas() {

        if (semiMayor <= 0
                || semiMinor <= 0
                || semiMayorDalam <= 0
                || semiMinorDalam <= 0) {

            throw new ArithmeticException(
                    "[Cincin] Dimensi tidak valid!"
            );
        }

        double luasLuar =
                Math.PI * semiMayor * semiMinor;

        double luasDalam =
                Math.PI * semiMayorDalam * semiMinorDalam;

        hasilLuas = luasLuar - luasDalam;

        return hasilLuas;
    }

    // ====== OVERLOADING LUAS SESUAI INTERFACE ======
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {

            throw new IllegalArgumentException(
                    "[Cincin] Parameter harus positif!"
            );
        }

        return Math.PI * a * b;
    }

    // ====== OVERLOADING KHUSUS CINCIN ======
    public double hitungLuas(
            double aLuar,
            double bLuar,
            double aDalam,
            double bDalam
    ) {

        if (aLuar <= 0
                || bLuar <= 0
                || aDalam <= 0
                || bDalam <= 0) {

            throw new IllegalArgumentException(
                    "[Cincin] Semua dimensi harus positif!"
            );
        }

        return (Math.PI * aLuar * bLuar)
                - (Math.PI * aDalam * bDalam);
    }

    // ====== HITUNG KELILING ======
    @Override
    public double hitungKeliling() {

        return super.hitungKeliling();
    }

    // ====== OVERLOADING KELILING ======
    @Override
    public double hitungKeliling(double a, double b) {

        return super.hitungKeliling(a, b);
    }

    // ====== TAMPIL INFO ======
    public void tampilInfo() {

        System.out.println("=== CINCIN ELIPS ===");

        System.out.println(
                "Semi Mayor Luar (a) : "
                + semiMayor
        );

        System.out.println(
                "Semi Minor Luar (b) : "
                + semiMinor
        );

        System.out.println(
                "Semi Mayor Dalam    : "
                + semiMayorDalam
        );

        System.out.println(
                "Semi Minor Dalam    : "
                + semiMinorDalam
        );

        try {

            System.out.printf(
                    "Luas Cincin         : %.4f%n",
                    hitungLuas()
            );

            System.out.printf(
                    "Keliling Elips      : %.4f%n",
                    hitungKeliling()
            );

        } catch (Exception e) {

            System.out.println(
                    "[ERROR] tampilInfo: "
                    + e.getMessage()
            );
        }
    }

    // ====== MULTITHREADING ======
    @Override
    public void run() {

        try {

            statusThread = "RUNNING";

            progress = 0;

            System.out.println(
                    "[" + namaThread
                    + "] START – hitung Cincin"
            );

            for (int i = 1; i <= jumlahData; i++) {

                hasilLuas = hitungLuas();

                hitungKeliling();

                Thread.sleep(1);

                progress = (i * 100) / jumlahData;

                if (progress % 25 == 0
                        && progress > 0) {

                    System.out.println(
                            "[" + namaThread
                            + "] Progress: "
                            + progress + "%"
                    );
                }
            }

            progress = 100;

            statusThread = "DONE";

            System.out.println(
                    "[" + namaThread
                    + "] DONE – Luas="
                    + String.format("%.4f", hasilLuas)
            );

        } catch (InterruptedException ie) {

            statusThread = "INTERRUPTED";

            Thread.currentThread().interrupt();

            System.out.println(
                    "[" + namaThread
                    + "] INTERRUPTED"
            );

        } catch (Exception e) {

            statusThread = "ERROR";

            System.out.println(
                    "[" + namaThread
                    + "] ERROR: "
                    + e.getMessage()
            );
        }
    }
}