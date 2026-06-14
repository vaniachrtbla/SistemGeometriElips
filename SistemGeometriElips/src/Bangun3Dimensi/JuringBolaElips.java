package Bangun3Dimensi;

public class JuringBolaElips extends BolaElips implements Runnable {

    // ====== ATRIBUT PUBLIC ======
    public double sudut;

    // ====== CONSTRUCTOR ======
    public JuringBolaElips(double semiMayor,
                           double semiMinor,
                           double semiAxisC,
                           double sudut,
                           int jumlahData) throws Exception {
        super(semiMayor, semiMinor, semiAxisC, jumlahData);

        if (sudut <= 0 || sudut > 360) {
            throw new IllegalArgumentException(
                "[JuringBolaElips] Sudut harus 0 < sudut ≤ 360"
            );
        }

        this.sudut = sudut;
        this.namaThread = "Thread-JuringBolaElips";

        System.out.println("[LOG] JuringBolaElips dibuat: sudut = " + sudut);
    }

    // ====== OVERRIDE LUAS P DAN VOLUME (POLYMORPHISM CORE) ======
    @Override
    public double hitungLuas() {
        double luasPenuh = super.hitungLuas(); // luas permukaan bola elips
        hasilLuas = (sudut / 360.0) * luasPenuh;
        return hasilLuas;
    }
    
    @Override
    public double hitungVolume() {
        double volumePenuh = super.hitungVolume(); // reuse parent logic; volume bola elips
        hasilVolume = (sudut / 360.0) * volumePenuh;
        return hasilVolume;
    } 
   
    // ====== OVERLOADING LUAS P DAN VOLUME  (MANUAL INPUT) ======
    public double hitungLuas(double a, double b, double c, double sudutDeg) {
        if (a <= 0 || b <= 0 || c <= 0 || sudutDeg <= 0 || sudutDeg > 360) {
            throw new IllegalArgumentException("Parameter tidak valid!");
        }
        double p = 1.6075;
        double ap = Math.pow(a, p);
        double bp = Math.pow(b, p);
        double cp = Math.pow(c, p);

        double luasPenuh = 4 * Math.PI * Math.pow((ap * bp + ap * cp + bp * cp) / 3.0, 1.0 / p);
        return (sudutDeg / 360.0) * luasPenuh;
    }
    
    public double hitungVolume(double a, double b, double c, double sudutDeg) {
        if (a <= 0 || b <= 0 || c <= 0 || sudutDeg <= 0 || sudutDeg > 360) {
            throw new IllegalArgumentException(
                "[JuringBolaElips] Parameter tidak valid!"
            );
        }
        double volumePenuh = (4.0 / 3.0) * Math.PI * a * b * c;
        return (sudutDeg / 360.0) * volumePenuh;
    }

    // ====== THREAD MONITORING ======
    @Override
    public void run() {
        try {
            statusThread = "RUNNING";
            progress = 0;           
            System.out.println("[" + namaThread + "] START");            
            for (int i = 1; i <= jumlahData; i++) {                
                hitungLuas(); // polymorphism call
                hitungVolume();                 
                Thread.sleep(1);
                progress = (i * 100) / jumlahData;
                if (progress % 25 == 0 && progress > 0) {
                    System.out.println("[" + namaThread + "] Progress: " + progress + "%");
                }
            }
            statusThread = "DONE";
            progress = 100;
            System.out.println("[" + namaThread + "] DONE");
        } catch (InterruptedException e) {
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            statusThread = "ERROR";
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}