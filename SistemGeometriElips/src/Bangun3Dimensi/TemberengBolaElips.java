package Bangun3Dimensi;

public class TemberengBolaElips extends BolaElips implements Runnable {

    public double tinggiTembereng;

    public TemberengBolaElips(double semiMayor, double semiMinor, double semiAxisC,
                               double tinggiTembereng, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, semiAxisC, jumlahData);
        if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC)
            throw new IllegalArgumentException("[TemberengBolaElips] tinggi tidak valid");
        this.tinggiTembereng = tinggiTembereng;
        this.namaThread = "Thread-TemberengBolaElips";
        System.out.println("[LOG][TemberengBolaElips] Constructor dipanggil: h=" + tinggiTembereng);
    }

// ====== HITUNG LUAS ======

        @Override
        public double hitungLuas() {

            if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC)
                throw new IllegalArgumentException(
                        "[TemberengBolaElips] Tinggi tidak valid");

            // pendekatan proporsi luas
            double rasio = tinggiTembereng / semiAxisC;

            hasilLuas =
                    rasio * super.hitungLuas();

            return hasilLuas;
        }

        // OVERLOADING
        public double hitungLuas(double a, double b, double c) {
            if (a <= 0 || b <= 0 || c <= 0)
                throw new IllegalArgumentException(
                        "[TemberengBolaElips] Parameter tidak valid");
            double rasio = tinggiTembereng / c;
            hasilLuas =
                    rasio * super.hitungLuas(a, b, c);
            return hasilLuas;
        }

        // CUSTOM OVERLOADING
        public double hitungLuas(double a,double b,double c,double h) throws Exception {
            if (a <= 0 || b <= 0 || c <= 0 || h <= 0)
                throw new IllegalArgumentException(
                        "[TemberengBolaElips] Parameter tidak valid");
            double rasio = h / c;
            hasilLuas =
                    rasio * super.hitungLuas(a, b, c);
            return hasilLuas;
        }

        // ====== HITUNG VOLUME ======
        @Override
        public double hitungVolume() {
            if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC)
                throw new ArithmeticException(
                        "[TemberengBolaElips] Tinggi tidak valid");
            double rasio = tinggiTembereng / semiAxisC;
            hasilVolume =
                    Math.pow(rasio, 2)
                    * super.hitungVolume();
            return hasilVolume;
        }
        // OVERLOADING
        public double hitungVolume(double a,double b,double c,double h) {
            if (a <= 0 || b <= 0 || c <= 0 || h <= 0)
                throw new IllegalArgumentException(
                        "[TemberengBolaElips] Parameter tidak valid");
            double rasio = h / c;
            hasilVolume =
                    Math.pow(rasio, 2)
                    * super.hitungVolume(a, b, c);
            return hasilVolume;
        }
    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;
            System.out.println("[" + namaThread + "] START");

            for (int i = 0; i < jumlahData; i++) {
                double a = (i == 0) ? semiMayor : Math.max(0.01, variasi(semiMayor));
                double b = (i == 0) ? semiMinor : Math.max(0.01, variasi(semiMinor));
                double c = (i == 0) ? semiAxisC : Math.max(0.01, variasi(semiAxisC));
                // tinggiTembereng harus <= c
                double h = Math.min(tinggiTembereng, c * 0.99);
                if (h <= 0) h = c * 0.5;
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataSemiAxisC[i] = c;
                dataHasilLuas[i] = hitungLuas(a, b, c);
                dataHasilVolume[i] = hitungVolume(a, b, c, h);

                if (Thread.interrupted()) throw new InterruptedException();
                
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE");

        } catch (InterruptedException e) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}