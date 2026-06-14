package Bangun3Dimensi;

/**
 * Cincin3Dimensi: bola elips berlubang (torus elips).
 * Extends BolaElips. Atribut public, constructor berparameter satu-satunya,
 * polimorfisme eksplisit via super, overloading hitungVolume,
 * override dengan throw Exception, try-catch, thread log.
 * @author Swift
 */
public class CincinBolaElips extends BolaElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double radiusDalam;

    // ====== CONSTRUCTOR DENGAN PARAMETER (satu-satunya, pakai super eksplisit) ======
    public CincinBolaElips(double semiMayor, double semiMinor,
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
    
    // ====== OVERRIDE HITUNG LUAS (tanpa parameter) ======
    @Override
    public double hitungLuas() throws ArithmeticException {

        if (semiMayor <= 0 || semiMinor <= 0
                || semiAxisC <= 0 || radiusDalam <= 0) {

            throw new ArithmeticException(
                    "[Cincin3Dimensi] Dimensi tidak valid saat hitungLuas!"
            );
        }

        // reuse luas permukaan parent (BolaElips)
        double luasLuar = super.hitungLuas();

        // pendekatan luas bagian lubang
        double luasDalam = 2 * Math.PI * radiusDalam * (2 * semiAxisC);

        hasilLuas = luasLuar - luasDalam;

        // mencegah hasil negatif
        if (hasilLuas < 0) {
            hasilLuas = 0;
        }

        return hasilLuas;
    }

    // ====== OVERLOADING LUAS (dengan parameter) ======
    public double hitungLuas(double a, double b, double c, double rDalam
    ) throws Exception {

        if (a <= 0 || b <= 0 || c <= 0 || rDalam <= 0) {
            throw new Exception("[Cincin3Dimensi] Semua dimensi harus positif!");
        }

        // reuse luas permukaan ellipsoid parent
        double luasLuar = super.hitungLuas(a, b, c);

        // pendekatan luas permukaan bagian dalam cincin 3D
        // karena luas permukaan ellipsoid berlubang tidak memiliki
        // rumus sederhana seperti bangun dasar lain, maka digunakan
        // pendekatan geometris sederhana.
        
        // konsep yang digunakan mirip luas selimut tabung:
        // luas ≈ keliling lubang × panjang lubang

        // dengan:
        // keliling lubang = 2πr dan panjang lubang = 2c
        // pada implementasi ini, lubang diasumsikan menembus
        // sepanjang sumbu c (semiAxisC), sehingga panjang total
        // lubang didekati menggunakan 2 × semiAxisC.
        
        // sehingga diperoleh pendekatan: luasDalam ≈ 2πr × (2c)
        double luasDalam = 2 * Math.PI * rDalam * (2 * c);
        double hasil = luasLuar - luasDalam;

        if (hasil < 0) {throw new Exception("[Cincin3Dimensi] Radius dalam terlalu besar!");}
        return hasil;
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
