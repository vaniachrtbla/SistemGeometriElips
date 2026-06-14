package Bangun2Dimensi;

/**
 * CLASS JURING ELIPS
 * Child dari Elips
 * Konsep: bagian (fraction) dari luas elips berdasarkan sudut
 */
public class Juring extends Elips implements Runnable {

    // ===== ATRIBUT PUBLIC =====
    public double sudut;

    // ===== CONSTRUCTOR (pakai parameter + super + this) =====
    public Juring(double semiMayor, double semiMinor, double sudut, int jumlahData)throws Exception{
        super(semiMayor, semiMinor, jumlahData); // memanggil constructor parent (inheritance)
        // validasi input (exception handling)
        if(sudut <= 0 || sudut > 360){
            throw new IllegalArgumentException("[Juring] Sudut harus 0-360");
        }
        this.sudut = sudut; // this digunakan utk assign atribut objek saat ini
        this.namaThread = "Thread-Juring"; // monitoring thread
        System.out.println("[LOG][Juring] Constructor dipanggil");
    }

    // ===== OVERRIDE METHOD LUAS (POLYMORPHISM) =====
    @Override // non-parameter; pakai atribut
    public double hitungLuas(){
        if(sudut <= 0 || sudut > 360){
            throw new ArithmeticException("[Juring] Sudut tidak valid");
        }
        // konsep utama: juring = fraksi dari elips penuh
        hasilLuas = (sudut/360.0) * super.hitungLuas();
        return hasilLuas;
    }
    
    // ===== OVERLOADING LUAS (sesuai interface) =====
    @Override 
    // pakai input manual elips
    public double hitungLuas(double a, double b){
        if(a <= 0 || b <= 0){
            throw new IllegalArgumentException("[Juring] Parameter tidak valid");
        }
        return super.hitungLuas(a,b); // reuse logic parent (tidak duplikasi)
    }
    // hitung full manual tanpa state
    public double hitungLuas(double a, double b, double sudutDeg){
        if(a <= 0 || b <= 0 || sudutDeg <= 0 || sudutDeg > 360){
            throw new IllegalArgumentException("[Juring] Input tidak valid");
        }
        return (sudutDeg/360.0) * super.hitungLuas(a,b);
    }

    // ===== OVERRIDE KELILING =====
    @Override
    public double hitungKeliling(){
        return super.hitungKeliling();
    }
    @Override
    public double hitungKeliling(double a, double b){
        return super.hitungKeliling(a, b);
    }

    // ===== THREAD (RUNNABLE IMPLEMENTATION) =====
    @Override
    public void run(){
        try {
            statusThread = "RUNNING";
            progress = 0;

            System.out.println("["+namaThread+"] START");
            for(int i=1; i <= jumlahData; i++){
                // simulasi proses hitung
                hitungLuas();
                hitungKeliling();

                Thread.sleep(1);

                // monitoring progress
                progress=(i * 100)/jumlahData;
                if(progress%25 == 0 && progress > 0){
                    System.out.println("["+namaThread+"] "+progress+"%");
                }
            }
            statusThread = "DONE";
            progress = 100;
            System.out.println("["+namaThread+"] FINISH");

        } catch(InterruptedException e){
            statusThread = "INTERRUPTED";
            Thread.currentThread().interrupt();

        } catch(Exception e){
            statusThread = "ERROR";
            System.out.println(e.getMessage());
        }
    }
}