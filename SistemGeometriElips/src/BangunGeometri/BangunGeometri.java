package BangunGeometri;

/**
 * Interface utama sebagai abstraksi semua bangun geometri
 * @author Swift
 */
public interface BangunGeometri {
    // ====== METHOD TANPA PARAMETER ======
    public double hitungLuas();
    public double hitungKeliling();
    // ====== OVERLOADING ======
    public double hitungLuas(double a, double b);
    public double hitungKeliling(double a, double b); 
}
