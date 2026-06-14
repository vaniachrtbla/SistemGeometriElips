package Bangun3Dimensi;

    /**
     * TemberengBolaElips:
     * bagian bola elips yang dipotong bidang datar.
     */
    public class TemberengBolaElips extends BolaElips implements Runnable {

        // ====== ATRIBUT PUBLIC ======
        public double tinggiTembereng;

        // ====== CONSTRUCTOR ======
        public TemberengBolaElips(
                double semiMayor,
                double semiMinor,
                double semiAxisC,
                double tinggiTembereng,
                int jumlahData
        ) throws Exception {

            super(semiMayor, semiMinor, semiAxisC, jumlahData);

            if (tinggiTembereng <= 0 || tinggiTembereng > semiAxisC) {
                throw new IllegalArgumentException(
                    "[TemberengBolaElips] tinggi tidak valid"
                );
            }

            this.tinggiTembereng = tinggiTembereng;
            this.namaThread = "Thread-TemberengBolaElips";

            System.out.println(
                "[LOG][TemberengBolaElips] Constructor dipanggil: h="
                + tinggiTembereng
            );
        }
        
        @Override
        public double hitungLuas() {
            return super.hitungLuas();
        }
        
        @Override
        public double hitungLuas(double a, double b) {

        if (a <= 0 || b <= 0) {
        throw new IllegalArgumentException(
            "[TemberengBolaElips] parameter tidak valid"
        );
        }

        return super.hitungLuas(a, b);
        }

        // ====== OVERRIDE VOLUME ======
        @Override
        public double hitungVolume() {

            double volumePenuh = super.hitungVolume();

            double rasio = tinggiTembereng / semiAxisC;

            return Math.pow(rasio, 2) * volumePenuh;
        }

        // ====== OVERLOADING VOLUME (PARAMETER VERSION) ======
        public double hitungVolume(double a, double b, double c, double h) {

            if (a <= 0 || b <= 0 || c <= 0 || h <= 0) {
                throw new IllegalArgumentException(
                    "[TemberengBolaElips] parameter tidak valid" );
            }
            double volumePenuh = (4.0 / 3.0) * Math.PI * a * b * c;

            double rasio = h / c;

            return Math.pow(rasio, 2) * volumePenuh;
        }

        // ====== THREAD (MONITORING) ======
        @Override
        public void run() {

            try {
                statusThread = "RUNNING";
                progress = 0;

                System.out.println("[" + namaThread + "] START");

                for (int i = 1; i <= jumlahData; i++) {

                    hitungLuas();
                    hitungVolume();

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
                System.out.println(
                    "[" + namaThread + "] ERROR: " + e.getMessage()
                );
            }
        }
    }