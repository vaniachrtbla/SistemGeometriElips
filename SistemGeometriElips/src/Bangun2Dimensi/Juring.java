package Bangun2Dimensi;

/**
 * Juring: potongan elips dengan sudut tertentu.
 * Extends Elips.
 * 
 * @author Swift
 */
public class Juring extends Elips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double sudut;

    // ====== CONSTRUCTOR ======
    public Juring(
            double semiMayor,
            double semiMinor,
            double sudut,
            int jumlahData
    ) throws Exception {

        // memanggil constructor parent Elips
        super(semiMayor, semiMinor, jumlahData);

        if (sudut <= 0 || sudut > 360) {

            throw new IllegalArgumentException(
                    "[Juring] Sudut juring harus antara 0 - 360 derajat!"
            );
        }

        this.sudut = sudut;

        this.namaThread = "Thread-Juring";

        System.out.println(
                "[LOG][Juring] Constructor dipanggil: a="
                + semiMayor
                + ", b="
                + semiMinor
                + ", sudut="
                + sudut
        );
    }

    // ====== GETTER / SETTER ======
    public double getSudut() {
        return sudut;
    }

    public void setSudut(double sudut) {
        this.sudut = sudut;
    }

    // ====== OVERRIDE HITUNG LUAS ======
    @Override
    public double hitungLuas() {

        if (sudut <= 0 || sudut > 360) {

            throw new ArithmeticException(
                    "[Juring] Sudut tidak valid!"
            );
        }

        hasilLuas =
                (sudut / 360.0)
                * Math.PI
                * semiMayor
                * semiMinor;

        return hasilLuas;
    }

    // ====== OVERLOADING LUAS SESUAI INTERFACE ======
    @Override
    public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {

            throw new IllegalArgumentException(
                    "[Juring] Parameter harus positif!"
            );
        }

        return Math.PI * a * b;
    }

    // ====== OVERLOADING KHUSUS JURING ======
    public double hitungLuas(
            double a,
            double b,
            double sudutDeg
    ) {

        if (a <= 0 || b <= 0) {

            throw new IllegalArgumentException(
                    "[Juring] Dimensi harus positif!"
            );
        }

        if (sudutDeg <= 0 || sudutDeg > 360) {

            throw new IllegalArgumentException(
                    "[Juring] Sudut tidak valid!"
            );
        }

        return (sudutDeg / 360.0)
                * Math.PI
                * a
                * b;
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

        System.out.println("=== JURING ELIPS ===");

        System.out.println(
                "Semi Mayor (a) : "
                + semiMayor
        );

        System.out.println(
                "Semi Minor (b) : "
                + semiMinor
        );

        System.out.println(
                "Sudut          : "
                + sudut
                + " derajat"
        );

        try {

            System.out.printf(
                    "Luas Juring    : %.4f%n",
                    hitungLuas()
            );

            System.out.printf(
                    "Keliling       : %.4f%n",
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
                    + "] START – hitung Juring"
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