package Bangun3Dimensi;

public class CincinBolaElips extends BolaElips implements Runnable {

    public double radiusDalam;

    public CincinBolaElips(double semiMayor, double semiMinor, double semiAxisC,
                            double radiusDalam, int jumlahData) throws Exception {
        super(semiMayor, semiMinor, semiAxisC, jumlahData);
        if (radiusDalam <= 0)
            throw new Exception("[Cincin3Dimensi] Radius dalam harus positif!");
        if (radiusDalam >= semiMayor || radiusDalam >= semiMinor)
            throw new Exception("[Cincin3Dimensi] Radius dalam harus lebih kecil dari semi mayor dan semi minor!");
        this.radiusDalam = radiusDalam;
        this.namaThread = "Thread-Cincin3D";
        System.out.println("[LOG][Cincin3Dimensi] Constructor dipanggil: rDalam=" + radiusDalam);
    }

    @Override
    public double hitungLuas() {
        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0 || radiusDalam <= 0)
            throw new ArithmeticException("[Cincin3Dimensi] Dimensi tidak valid saat hitungLuas!");
        double luasLuar = super.hitungLuas();
        double luasDalam = 2 * Math.PI * radiusDalam * (2 * semiAxisC);
        hasilLuas = Math.max(0, luasLuar - luasDalam);
        return hasilLuas;
    }

    public double hitungLuas(double a, double b, double c, double rDalam) throws Exception {
        if (a <= 0 || b <= 0 || c <= 0 || rDalam <= 0)
            throw new Exception("[Cincin3Dimensi] Semua dimensi harus positif!");
        double luasLuar = super.hitungLuas(a, b, c);
        double luasDalam = 2 * Math.PI * rDalam * (2 * c);
        double hasil = luasLuar - luasDalam;
        if (hasil < 0) throw new Exception("[Cincin3Dimensi] Radius dalam terlalu besar!");
        return hasil;
    }

    @Override
    public double hitungVolume() {
        if (semiMayor <= 0 || semiMinor <= 0 || semiAxisC <= 0 || radiusDalam <= 0)
            throw new ArithmeticException("[Cincin3Dimensi] Dimensi tidak valid saat hitungVolume!");
        double volumeLuar = super.hitungVolume();
        double volumeDalam = Math.PI * radiusDalam * radiusDalam * (2 * semiAxisC);
        hasilVolume = Math.max(0, volumeLuar - volumeDalam);
        return hasilVolume;
    }

    public double hitungVolume(double a, double b, double c, double rDalam) throws Exception {
        if (a <= 0 || b <= 0 || c <= 0 || rDalam <= 0)
            throw new Exception("[Cincin3Dimensi] Semua dimensi harus positif!");
        double vLuar = super.hitungVolume(a, b, c);
        double vDalam = Math.PI * rDalam * rDalam * (2 * c);
        double hasil = vLuar - vDalam;
        if (hasil < 0) throw new Exception("[Cincin3Dimensi] Radius dalam terlalu besar!");
        return hasil;
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
                // Pastikan radiusDalam < min(a,b)
                double rD = Math.min(radiusDalam, Math.min(a, b) * 0.9);
                if (rD <= 0) rD = Math.min(a, b) * 0.3;
                dataSemiMayor[i] = a;
                dataSemiMinor[i] = b;
                dataSemiAxisC[i] = c;
                dataHasilLuas[i] = Math.max(0, super.hitungLuas(a, b, c) - 2 * Math.PI * rD * (2 * c));
                dataHasilVolume[i] = Math.max(0, super.hitungVolume(a, b, c) - Math.PI * rD * rD * (2 * c));

                if (Thread.interrupted()) throw new InterruptedException();
                
                progress = ((i + 1) * 100) / jumlahData;
            }

            progress = 100;
            statusThread = "DONE";
            System.out.println("[" + namaThread + "] DONE");

        } catch (InterruptedException ie) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[" + namaThread + "] ERROR: " + e.getMessage());
        }
    }
}