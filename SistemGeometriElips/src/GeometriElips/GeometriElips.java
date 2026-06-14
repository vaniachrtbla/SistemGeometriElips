package GeometriElips;

import BangunGeometri.BangunGeometri;
import Bangun2Dimensi.Elips;
import Bangun2Dimensi.JuringElips;
import Bangun2Dimensi.TemberengElips;
import Bangun2Dimensi.CincinElips;
import Bangun3Dimensi.BolaElips;
import Bangun3Dimensi.PrismaElips;
import Bangun3Dimensi.LimasElips;
import Bangun3Dimensi.LimasElipsTerpancung;
import Bangun3Dimensi.JuringBolaElips;
import Bangun3Dimensi.TemberengBolaElips;
import Bangun3Dimensi.CincinBolaElips;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GeometriElips extends JFrame {

    private JTextField tfJumlahData;
    private JTextField tfSemiMayor, tfSemiMinor;
    private JTextField tfTinggiPrisma, tfTinggiLimas, tfSemiAxisC;
    private JTextField tfSemiMayorAtas, tfSemiMinorAtas, tfRadiusDalam;
    private JButton    btnMulai;
    private JButton    btnReset;
    private JLabel     lblStatus;

    private JPanel               panelProgressThread;
    private Map<String, JProgressBar> mapProgress = new LinkedHashMap<>();

    private DefaultTableModel modelElips2D;
    private DefaultTableModel modelPrisma;
    private DefaultTableModel modelLimas;
    private DefaultTableModel modelBola;

    private volatile boolean sedangBerjalan = false;
    private List<Thread> threadAktif = new ArrayList<>();
    private SwingWorker<Void, Void> workerAktif;

    private final Color CLR_BG     = new Color(250, 250, 252);
    private final Color CLR_PANEL  = Color.WHITE;
    private final Color CLR_BORDER = new Color(225, 228, 235);
    private final Color CLR_ACCENT = new Color(60, 110, 100);
    private final Color CLR_TEXT   = new Color(40, 45, 55);

    public GeometriElips() {
        initComponents();
        setTitle("Sistem Geometri Elips – OOP & Multithreading");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CLR_BG);
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(CLR_BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        top.add(buatJudul(),      BorderLayout.NORTH);
        top.add(buatPanelInput(), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabs.addTab("Progress Thread",   buatTabProgress());
        tabs.addTab("Hasil Perhitungan", buatTabHasil());

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        center.add(tabs, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        add(buatFooter(), BorderLayout.SOUTH);
    }

    private JPanel buatJudul() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel lblJudul = new JLabel("Sistem Geometri Elips");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(CLR_TEXT);
        JLabel lblSub = new JLabel("Perhitungan Luas & Volume – OOP, Multithreading, Polymorphism");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(120, 128, 140));
        p.add(lblJudul);
        p.add(lblSub);
        return p;
    }

    private JPanel buatPanelInput() {
        JPanel p = new JPanel();
        p.setBackground(CLR_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_BORDER, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        p.setLayout(new GridLayout(2, 6, 10, 8));

        tfJumlahData    = new JTextField("100", 5);
        tfSemiMayor     = new JTextField("6",   5);
        tfSemiMinor     = new JTextField("4",   5);
        tfSemiAxisC     = new JTextField("5",   5);
        tfTinggiPrisma  = new JTextField("10",  5);
        tfTinggiLimas   = new JTextField("12",  5);
        tfSemiMayorAtas = new JTextField("3",   5);
        tfSemiMinorAtas = new JTextField("2",   5);
        tfRadiusDalam   = new JTextField("1.5", 5);

        p.add(buatField("Jumlah Data",          tfJumlahData));
        p.add(buatField("Semi-Mayor (a)",        tfSemiMayor));
        p.add(buatField("Semi-Minor (b)",        tfSemiMinor));
        p.add(buatField("Semi-Axis C (Bola)",    tfSemiAxisC));
        p.add(buatField("Tinggi Prisma",         tfTinggiPrisma));
        p.add(buatField("Tinggi Limas",          tfTinggiLimas));

        p.add(buatField("Semi-Mayor Atas (a2)",  tfSemiMayorAtas));
        p.add(buatField("Semi-Minor Atas (b2)",  tfSemiMinorAtas));
        p.add(buatField("Radius Dalam (Cincin)", tfRadiusDalam));

        btnMulai = buatTombol("▶  Jalankan Komputasi", new Color(46, 125, 50));
        btnMulai.addActionListener(e -> mulaiProses());

        btnReset = buatTombol("↺  Reset Dashboard", new Color(198, 40, 40));
        btnReset.addActionListener(e -> resetDashboard());

        p.add(btnMulai);
        p.add(btnReset);
        return p;
    }

    private JButton buatTombol(String teks, Color bg) {
        JButton btn = new JButton(teks);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buatField(String label, JTextField tf) {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setForeground(new Color(110, 118, 130));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_BORDER, 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        p.add(lbl, BorderLayout.NORTH);
        p.add(tf,  BorderLayout.CENTER);
        return p;
    }

    private JPanel buatTabProgress() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(CLR_PANEL);
        panelProgressThread = new JPanel();
        panelProgressThread.setLayout(new BoxLayout(panelProgressThread, BoxLayout.Y_AXIS));
        panelProgressThread.setBackground(CLR_PANEL);
        panelProgressThread.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        JScrollPane scroll = new JScrollPane(panelProgressThread);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private void buatProgressBar(String namaThread) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JLabel lblNama = new JLabel(namaThread);
        lblNama.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblNama.setForeground(CLR_TEXT);
        lblNama.setPreferredSize(new Dimension(200, 22));
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setValue(0);
        pb.setStringPainted(true);
        pb.setString("0%");
        pb.setBackground(new Color(230, 233, 238));
        pb.setForeground(CLR_ACCENT);
        row.add(lblNama, BorderLayout.WEST);
        row.add(pb,      BorderLayout.CENTER);
        mapProgress.put(namaThread, pb);
        panelProgressThread.add(row);
        panelProgressThread.revalidate();
        panelProgressThread.repaint();
    }

    private void monitorProgress(Elips bangun, Thread th) {
        new Thread(() -> {
            JProgressBar pb = mapProgress.get(bangun.getNamaThread());
            int lastValue = -1;
            while (th.isAlive()) {
                final int val = bangun.progress;
                if (val != lastValue) {
                    lastValue = val;
                    SwingUtilities.invokeLater(() -> {
                        pb.setValue(val);
                        pb.setString(val + "%");
                        pb.setForeground(CLR_ACCENT);
                    });
                }
                try { Thread.sleep(1); }
                catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            }
            final int finalVal = bangun.progress;
            final String status = bangun.statusThread;
            SwingUtilities.invokeLater(() -> {
                pb.setValue(finalVal);
                pb.setString(finalVal + "% [" + status + "]");
                if ("ERROR".equals(status) || "INTERRUPTED".equals(status))
                    pb.setForeground(new Color(198, 40, 40));
            });
        }, "Monitor-" + bangun.getNamaThread()).start();
    }

    private JPanel buatTabHasil() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        modelElips2D = buatModel(new String[]{"No", "Bangun", "Semi Mayor (a)", "Semi Minor (b)", "Luas"});
        modelPrisma  = buatModel(new String[]{"No", "Bangun", "Semi Mayor (a)", "Semi Minor (b)", "Tinggi", "Luas Permukaan", "Volume"});
        modelLimas   = buatModel(new String[]{"No", "Bangun", "Semi Mayor (a)", "Semi Minor (b)", "Tinggi", "Luas Permukaan", "Volume"});
        modelBola    = buatModel(new String[]{"No", "Bangun", "Semi Mayor (a)", "Semi Minor (b)", "Semi Axis C (c)", "Luas Permukaan", "Volume"});

        p.add(buatPanelTabel("Bangun 2D (Elips, Juring, Tembereng, Cincin)", modelElips2D));
        p.add(buatPanelTabel("Prisma Elips",                                  modelPrisma));
        p.add(buatPanelTabel("Limas Elips & Turunannya",                      modelLimas));
        p.add(buatPanelTabel("Bola Elips & Turunannya",                       modelBola));
        return p;
    }

    private DefaultTableModel buatModel(String[] kolom) {
        return new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JPanel buatPanelTabel(String judul, DefaultTableModel model) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(CLR_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        JLabel lbl = new JLabel(judul);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(CLR_ACCENT);
        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("SansSerif", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.setGridColor(CLR_BORDER);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(CLR_BORDER));
        p.add(lbl,    BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(CLR_PANEL);
        footer.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, CLR_BORDER),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        lblStatus = new JLabel("Status: siap dijalankan");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(110, 118, 130));
        footer.add(lblStatus, BorderLayout.WEST);
        return footer;
    }

    private void mulaiProses() {
        if (sedangBerjalan) {
            JOptionPane.showMessageDialog(this,
                "Proses sedang berjalan! Tunggu hingga selesai atau lakukan Reset.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int    jumlahData = Integer.parseInt(tfJumlahData.getText().trim());
            double a          = Double.parseDouble(tfSemiMayor.getText().trim());
            double b          = Double.parseDouble(tfSemiMinor.getText().trim());
            double c          = Double.parseDouble(tfSemiAxisC.getText().trim());
            double tPrisma    = Double.parseDouble(tfTinggiPrisma.getText().trim());
            double tLimas     = Double.parseDouble(tfTinggiLimas.getText().trim());
            double a2         = Double.parseDouble(tfSemiMayorAtas.getText().trim());
            double b2         = Double.parseDouble(tfSemiMinorAtas.getText().trim());
            double rDalam     = Double.parseDouble(tfRadiusDalam.getText().trim());

            if (jumlahData <= 0)  throw new Exception("Jumlah data harus lebih dari 0!");
            if (a <= 0 || b <= 0) throw new Exception("Semi Mayor dan Semi Minor harus positif!");
            if (c <= 0)           throw new Exception("Semi Axis C harus positif!");
            if (tPrisma <= 0)     throw new Exception("Tinggi Prisma harus positif!");
            if (tLimas <= 0)      throw new Exception("Tinggi Limas harus positif!");
            if (a2 <= 0 || b2 <= 0) throw new Exception("Semi Mayor/Minor Atas harus positif!");
            if (a2 >= a || b2 >= b)  throw new Exception("Alas atas harus lebih kecil dari alas bawah!");
            if (rDalam <= 0)      throw new Exception("Radius Dalam harus positif!");
            if (rDalam >= a || rDalam >= b)
                throw new Exception("Radius Dalam harus lebih kecil dari Semi Mayor dan Semi Minor!");

            mapProgress.clear();
            panelProgressThread.removeAll();
            panelProgressThread.revalidate();
            panelProgressThread.repaint();
            modelElips2D.setRowCount(0);
            modelPrisma.setRowCount(0);
            modelLimas.setRowCount(0);
            modelBola.setRowCount(0);

            sedangBerjalan = true;
            btnMulai.setEnabled(false);
            lblStatus.setText("Status: membangun objek & thread...");

            // Buat objek
            Elips elips = new Elips(a, b, jumlahData);
            elips.setNamaThread("Elips");

            JuringElips juring = new JuringElips(a, b, 90, jumlahData);
            juring.setNamaThread("JuringElips");

            TemberengElips tembereng = new TemberengElips(a, b, 120, jumlahData);
            tembereng.setNamaThread("TemberengElips");

            CincinElips cincin = new CincinElips(a, b, a * 0.4, b * 0.4, jumlahData);
            cincin.setNamaThread("CincinElips");

            BolaElips bola = new BolaElips(a, b, c, jumlahData);
            bola.setNamaThread("BolaElips");

            JuringBolaElips juringBola = new JuringBolaElips(a, b, c, 120, jumlahData);
            juringBola.setNamaThread("JuringBolaElips");

            TemberengBolaElips temberengBola = new TemberengBolaElips(a, b, c, c * 0.5, jumlahData);
            temberengBola.setNamaThread("TemberengBolaElips");

            CincinBolaElips cincin3D = new CincinBolaElips(a, b, c, rDalam, jumlahData);
            cincin3D.setNamaThread("CincinBolaElips");

            PrismaElips prisma = new PrismaElips(a, b, tPrisma, jumlahData);
            prisma.setNamaThread("PrismaElips");

            LimasElips limas = new LimasElips(a, b, tLimas, jumlahData);
            limas.setNamaThread("LimasElips");

            LimasElipsTerpancung limasTerpancung = new LimasElipsTerpancung(a, b, tLimas, a2, b2, jumlahData);
            limasTerpancung.setNamaThread("LimasElipsTerpancung");

            BangunGeometri[] semuaBangun = {
                elips, juring, tembereng, cincin,
                bola, juringBola, temberengBola, cincin3D,
                prisma, limas, limasTerpancung
            };

            List<Thread> threads      = new ArrayList<>();
            List<Elips>  daftarBangun = new ArrayList<>();

            for (BangunGeometri bg : semuaBangun) {
                Elips e = (Elips) bg;
                buatProgressBar(e.getNamaThread());
                Thread th = new Thread((Runnable) bg, e.getNamaThread());
                threads.add(th);
                daftarBangun.add(e);
            }

            threadAktif = threads;
            for (int i = 0; i < threads.size(); i++) {
                threads.get(i).start();
                monitorProgress(daftarBangun.get(i), threads.get(i));
            }

            lblStatus.setText("Status: " + threads.size() + " thread sedang berjalan...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    for (Thread th : threads) {
                        try { th.join(); }
                        catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                    }
                    return null;
                }
                @Override
                protected void done() {
                    if (isCancelled()) return;
                    selesaiProses(semuaBangun);
                }
            };
            workerAktif = worker;
            worker.execute();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid! Pastikan semua field terisi angka.",
                "Error Input", JOptionPane.ERROR_MESSAGE);
            unlockUI("Status: error input – silakan perbaiki dan coba lagi");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Exception", JOptionPane.ERROR_MESSAGE);
            unlockUI("Status: exception – " + ex.getMessage());
        }
    }

    private void unlockUI(String statusMsg) {
        sedangBerjalan = false;
        btnMulai.setEnabled(true);
        btnReset.setEnabled(true);
        lblStatus.setText(statusMsg);
    }

    private void selesaiProses(BangunGeometri[] semuaBangun) {
        long errorCount = Arrays.stream(semuaBangun)
                .map(bg -> ((Elips) bg).statusThread)
                .filter(s -> "ERROR".equals(s) || "INTERRUPTED".equals(s))
                .count();

        for (BangunGeometri bg : semuaBangun) {
            String nama = bg.getClass().getSimpleName();

            if (bg.getClass() == Elips.class) {
                Elips e = (Elips) bg;
                for (int i = 0; i < e.jumlahData; i++)
                    modelElips2D.addRow(new Object[]{i+1, nama, fmt(e.dataSemiMayor[i]), fmt(e.dataSemiMinor[i]), fmt4(e.dataHasilLuas[i])});
            }
            else if (bg instanceof JuringElips) {
                JuringElips j = (JuringElips) bg;
                for (int i = 0; i < j.jumlahData; i++)
                    modelElips2D.addRow(new Object[]{i+1, nama, fmt(j.dataSemiMayor[i]), fmt(j.dataSemiMinor[i]), fmt4(j.dataHasilLuas[i])});
            }
            else if (bg instanceof TemberengElips) {
                TemberengElips t = (TemberengElips) bg;
                for (int i = 0; i < t.jumlahData; i++)
                    modelElips2D.addRow(new Object[]{i+1, nama, fmt(t.dataSemiMayor[i]), fmt(t.dataSemiMinor[i]), fmt4(t.dataHasilLuas[i])});
            }
            else if (bg instanceof CincinElips) {
                CincinElips ci = (CincinElips) bg;
                for (int i = 0; i < ci.jumlahData; i++)
                    modelElips2D.addRow(new Object[]{i+1, nama, fmt(ci.dataSemiMayor[i]), fmt(ci.dataSemiMinor[i]), fmt4(ci.dataHasilLuas[i])});
            }
            else if (bg instanceof PrismaElips) {
                PrismaElips pr = (PrismaElips) bg;
                for (int i = 0; i < pr.jumlahData; i++)
                    modelPrisma.addRow(new Object[]{i+1, nama, fmt(pr.dataSemiMayor[i]), fmt(pr.dataSemiMinor[i]), fmt(pr.dataTinggi[i]), fmt4(pr.dataHasilLuas[i]), fmt4(pr.dataHasilVolume[i])});
            }
            else if (bg instanceof LimasElipsTerpancung) {
                LimasElipsTerpancung l = (LimasElipsTerpancung) bg;
                for (int i = 0; i < l.jumlahData; i++)
                    modelLimas.addRow(new Object[]{i+1, nama, fmt(l.dataSemiMayor[i]), fmt(l.dataSemiMinor[i]), fmt(l.dataTinggi[i]), fmt4(l.dataHasilLuas[i]), fmt4(l.dataHasilVolume[i])});
            }
            else if (bg instanceof LimasElips) {
                LimasElips l = (LimasElips) bg;
                for (int i = 0; i < l.jumlahData; i++)
                    modelLimas.addRow(new Object[]{i+1, nama, fmt(l.dataSemiMayor[i]), fmt(l.dataSemiMinor[i]), fmt(l.dataTinggi[i]), fmt4(l.dataHasilLuas[i]), fmt4(l.dataHasilVolume[i])});
            }
            else if (bg instanceof JuringBolaElips) {
                JuringBolaElips jb = (JuringBolaElips) bg;
                for (int i = 0; i < jb.jumlahData; i++)
                    modelBola.addRow(new Object[]{i+1, nama, fmt(jb.dataSemiMayor[i]), fmt(jb.dataSemiMinor[i]), fmt(jb.dataSemiAxisC[i]), fmt4(jb.dataHasilLuas[i]), fmt4(jb.dataHasilVolume[i])});
            }
            else if (bg instanceof TemberengBolaElips) {
                TemberengBolaElips tb = (TemberengBolaElips) bg;
                for (int i = 0; i < tb.jumlahData; i++)
                    modelBola.addRow(new Object[]{i+1, nama, fmt(tb.dataSemiMayor[i]), fmt(tb.dataSemiMinor[i]), fmt(tb.dataSemiAxisC[i]), fmt4(tb.dataHasilLuas[i]), fmt4(tb.dataHasilVolume[i])});
            }
            else if (bg instanceof CincinBolaElips) {
                CincinBolaElips cb = (CincinBolaElips) bg;
                for (int i = 0; i < cb.jumlahData; i++)
                    modelBola.addRow(new Object[]{i+1, nama, fmt(cb.dataSemiMayor[i]), fmt(cb.dataSemiMinor[i]), fmt(cb.dataSemiAxisC[i]), fmt4(cb.dataHasilLuas[i]), fmt4(cb.dataHasilVolume[i])});
            }
            else if (bg instanceof BolaElips) {
                BolaElips bo = (BolaElips) bg;
                for (int i = 0; i < bo.jumlahData; i++)
                    modelBola.addRow(new Object[]{i+1, nama, fmt(bo.dataSemiMayor[i]), fmt(bo.dataSemiMinor[i]), fmt(bo.dataSemiAxisC[i]), fmt4(bo.dataHasilLuas[i]), fmt4(bo.dataHasilVolume[i])});
            }
        }

        String msg = errorCount > 0
            ? "Status: selesai dengan " + errorCount + " error"
            : "Status: semua thread selesai";
        unlockUI(msg);
    }

    private String fmt(double v)  { return String.format("%.2f", v); }
    private String fmt4(double v) { return String.format("%.4f", v); }

    private void resetDashboard() {
        boolean sebelumnya = sedangBerjalan;
        if (sedangBerjalan) {
            for (Thread th : threadAktif)
                if (th.isAlive()) th.interrupt();
            if (workerAktif != null) workerAktif.cancel(true);
            sedangBerjalan = false;
            btnMulai.setEnabled(true);
            lblStatus.setText("Status: proses dihentikan, dashboard direset");
            System.out.println("[GUI] Proses dihentikan paksa, dashboard direset.");
        }

        tfJumlahData.setText("100");
        tfSemiMayor.setText("6");
        tfSemiMinor.setText("4");
        tfSemiAxisC.setText("5");
        tfTinggiPrisma.setText("10");
        tfTinggiLimas.setText("12");
        tfSemiMayorAtas.setText("3");
        tfSemiMinorAtas.setText("2");
        tfRadiusDalam.setText("1.5");

        mapProgress.clear();
        panelProgressThread.removeAll();
        panelProgressThread.revalidate();
        panelProgressThread.repaint();

        modelElips2D.setRowCount(0);
        modelPrisma.setRowCount(0);
        modelLimas.setRowCount(0);
        modelBola.setRowCount(0);

        threadAktif = new ArrayList<>();
        workerAktif = null;

        if (!sebelumnya) lblStatus.setText("Status: dashboard berhasil direset");
        System.out.println("[GUI] Dashboard direset.");
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SISTEM GEOMETRI ELIPS – DEMO OOP DI CONSOLE");
        System.out.println("=================================================");

        try {
            System.out.println("\n--- POLYMORPHISM: BangunGeometri[] ---");
            BangunGeometri[] dataBangun = new BangunGeometri[]{
                new Elips(6, 4, 1),
                new PrismaElips(6, 4, 10, 1),
                new LimasElips(6, 4, 12, 1),
                new BolaElips(6, 4, 5, 1),
                new LimasElipsTerpancung(6, 4, 12, 3, 2, 1)
            };
            for (BangunGeometri bg : dataBangun) {
                System.out.println("\n=================================");
                System.out.println("Class : " + bg.getClass().getSimpleName());
                try {
                    System.out.printf("Luas : %.4f%n", bg.hitungLuas());
                    System.out.printf("Keliling : %.4f%n", bg.hitungKeliling());

                    // ====== KHUSUS BANGUN 3D ======
                    if (bg instanceof BolaElips) {
                        BolaElips bola = (BolaElips) bg;
                        System.out.printf("Volume : %.4f%n", bola.hitungVolume());

                    } else if (bg instanceof PrismaElips) {
                        PrismaElips prisma = (PrismaElips) bg;
                        System.out.printf("Volume : %.4f%n", prisma.hitungVolume());

                    } else if (bg instanceof LimasElips) {
                        LimasElips limas = (LimasElips) bg;
                        System.out.printf("Volume : %.4f%n", limas.hitungVolume());

                    } else if (bg instanceof LimasElipsTerpancung) {
                        LimasElipsTerpancung limasT = (LimasElipsTerpancung) bg;
                        System.out.printf("Volume : %.4f%n", limasT.hitungVolume());
                    }

                } catch (Exception e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }

            System.out.println("--- OVERLOADING hitungLuas ---");
            Elips eDemo = new Elips(6, 4, 1);
            System.out.println("hitungLuas()    = " + String.format("%.4f", eDemo.hitungLuas()));
            System.out.println("hitungLuas(5,3) = " + String.format("%.4f", eDemo.hitungLuas(5, 3)));

            PrismaElips pDemo = new PrismaElips(6, 4, 10, 1);
            System.out.println("hitungVolume()       = " + String.format("%.4f", pDemo.hitungVolume()));
            System.out.println("hitungVolume(6,4,10) = " + String.format("%.4f", pDemo.hitungVolume(6, 4, 10)));

            System.out.println("\n--- EXCEPTION HANDLING ---");
            try { new Elips(-5, 4, 1); } catch (Exception ex) { System.out.println("[CATCH] " + ex.getMessage()); }
            try { new JuringElips(6, 4, 400, 1); } catch (Exception ex) { System.out.println("[CATCH] " + ex.getMessage()); }

            System.out.println("\n--- MULTITHREADING ---");
            Elips t1Obj = new Elips(6, 4, 5);
            t1Obj.setNamaThread("Thread-Demo-Elips");
            PrismaElips t2Obj = new PrismaElips(6, 4, 10, 5);
            t2Obj.setNamaThread("Thread-Demo-Prisma");
            Thread t1 = new Thread(t1Obj, t1Obj.getNamaThread());
            Thread t2 = new Thread(t2Obj, t2Obj.getNamaThread());
            t1.start(); t2.start();
            t1.join(); t2.join();
            System.out.println("Semua thread demo selesai!");

        } catch (Exception ex) {
            System.out.println("[MAIN ERROR] " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new GeometriElips().setVisible(true);
        });
    }
}