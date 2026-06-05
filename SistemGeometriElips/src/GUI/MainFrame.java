package GUI;

import Bangun3Dimensi.*;
import Bangun2Dimensi.Elips;
import Bangun3Dimensi.TabungElips;
import Bangun3Dimensi.KerucutElips;
import Bangun3Dimensi.Ellipsoid;
import Bangun3Dimensi.KerucutTerpancungElips;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements ActionListener {

    // --- Warna Tema ---
    private final Color PRIMARY_COLOR = new Color(20, 60, 110);      // biru gelap
    private final Color ACCENT_COLOR  = new Color(0, 150, 136);      // teal
    private final Color BG_COLOR      = new Color(240, 244, 248);
    private final Color DANGER_COLOR  = new Color(198, 40, 40);

    // --- Input Fields ---
    JTextField tfData   = new JTextField("3");
    JTextField tfThread = new JTextField("1");

    // Dimensi elips alas
    JTextField tfA = new JTextField("6");   // semi-mayor
    JTextField tfB = new JTextField("4");   // semi-minor

    // Dimensi ruang
    JTextField tfTinggiTabung  = new JTextField("10");
    JTextField tfTinggiKerucut = new JTextField("12");
    JTextField tfC             = new JTextField("5");   // semi-axis c (ellipsoid)
    JTextField tfA2            = new JTextField("3");   // semi-mayor alas atas (frustum)
    JTextField tfB2            = new JTextField("2");   // semi-minor alas atas (frustum)
    JTextField tfTinggiTrunc   = new JTextField("8");   // tinggi frustum

    // --- Tombol ---
    JButton bRun   = new JButton("MULAI PROSES KOMPUTASI");
    JButton bReset = new JButton("RESET DASHBOARD");

    // --- Output ---
    JTextArea logArea = new JTextArea();
    DefaultTableModel tableModel;
    JTable tabelHasil;
    private int nomorBaris = 1;

    JPanel pProgressContainer = new JPanel();

    // ==================== Constructor ====================
    public MainFrame() {
        super("Ellipse Geometry Engine - Multithreading Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Container c = getContentPane();
        c.setLayout(new BorderLayout(0, 0));
        c.setBackground(BG_COLOR);

        // ---- Header ----
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(PRIMARY_COLOR);
        pHeader.setPreferredSize(new Dimension(100, 80));
        JLabel lTitle = new JLabel("  \u25CF  ELLIPSE GEOMETRIC MULTITHREADING", JLabel.LEFT);
        lTitle.setForeground(Color.WHITE);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pHeader.add(lTitle, BorderLayout.CENTER);
        c.add(pHeader, BorderLayout.NORTH);

        // ---- Panel Kiri (input) ----
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setPreferredSize(new Dimension(340, 100));
        pLeft.setBackground(Color.WHITE);
        pLeft.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        pLeft.add(createInputCard("KONTROL SISTEM",
            new String[]{"Jumlah Data", "Jumlah Thread"},
            new JTextField[]{tfData, tfThread}));
        pLeft.add(Box.createVerticalStrut(12));

        pLeft.add(createInputCard("DIMENSI ALAS ELIPS",
            new String[]{"Semi-Mayor (a)", "Semi-Minor (b)"},
            new JTextField[]{tfA, tfB}));
        pLeft.add(Box.createVerticalStrut(12));

        pLeft.add(createInputCard("DIMENSI RUANG",
            new String[]{"Tinggi Tabung", "Tinggi Kerucut",
                         "Semi-Axis C (Ellipsoid)",
                         "Semi-Mayor Atas (a2)", "Semi-Minor Atas (b2)",
                         "Tinggi Frustum"},
            new JTextField[]{tfTinggiTabung, tfTinggiKerucut, tfC, tfA2, tfB2, tfTinggiTrunc}));
        pLeft.add(Box.createVerticalGlue());

        // Tombol Run
        styleButton(bRun, ACCENT_COLOR);
        pLeft.add(bRun);
        pLeft.add(Box.createVerticalStrut(8));

        // Tombol Reset
        styleButton(bReset, DANGER_COLOR);
        pLeft.add(bReset);

        c.add(pLeft, BorderLayout.WEST);

        // ---- Panel Tengah (output) ----
        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.setBackground(BG_COLOR);
        pCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Tab 1: Monitoring Log
        logArea.setBackground(new Color(28, 28, 28));
        logArea.setForeground(new Color(105, 240, 174));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setMargin(new Insets(10, 10, 10, 10));
        tabs.addTab("  MONITORING LOG  ", new JScrollPane(logArea));

        // Tab 2: Data Calculations
        String[] kolom = {"No", "Thread", "Bangun", "Data",
                          "Luas Alas", "Keliling", "Volume", "L. Permukaan"};
        tableModel = new DefaultTableModel(kolom, 0);
        tabelHasil = new JTable(tableModel);
        tabelHasil.setRowHeight(28);
        tabelHasil.setIntercellSpacing(new Dimension(10, 8));
        tabelHasil.setShowVerticalLines(false);
        tabelHasil.setGridColor(new Color(220, 220, 220));
        JTableHeader th = tabelHasil.getTableHeader();
        th.setPreferredSize(new Dimension(100, 35));
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(240, 244, 248));
        tabs.addTab("  DATA CALCULATIONS  ", new JScrollPane(tabelHasil));

        // Tab 3: Progress Thread
        pProgressContainer.setLayout(new BoxLayout(pProgressContainer, BoxLayout.Y_AXIS));
        pProgressContainer.setBackground(Color.WHITE);
        pProgressContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.addTab("  PROGRESS THREAD  ", new JScrollPane(pProgressContainer));

        pCenter.add(tabs, BorderLayout.CENTER);
        c.add(pCenter, BorderLayout.CENTER);

        bRun.addActionListener(this);
        bReset.addActionListener(this);

        setSize(1200, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ==================== Helper: style button ====================
    private void styleButton(JButton btn, Color color) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ==================== Helper: input card ====================
    private JPanel createInputCard(String title, String[] labels, JTextField[] fields) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTitle.setForeground(PRIMARY_COLOR);
        lTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lTitle);
        card.add(Box.createVerticalStrut(8));

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.setMaximumSize(new Dimension(300, 28));
            JLabel lb = new JLabel(labels[i]);
            lb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            row.add(lb, BorderLayout.WEST);
            fields[i].setPreferredSize(new Dimension(80, 24));
            row.add(fields[i], BorderLayout.EAST);
            card.add(row);
            card.add(Box.createVerticalStrut(4));
        }
        return card;
    }

    // ==================== tambahBarisTabel (thread-safe) ====================
    public synchronized void tambahBarisTabel(Object[] rowData) {
        Object[] rowBaru = new Object[rowData.length + 1];
        rowBaru[0] = nomorBaris++;
        System.arraycopy(rowData, 0, rowBaru, 1, rowData.length);
        SwingUtilities.invokeLater(() -> tableModel.addRow(rowBaru));
    }

    // ==================== buatProgressBar ====================
    public JProgressBar buatProgressBar(String namaThread) {
        JPanel pBar = new JPanel(new BorderLayout(10, 0));
        pBar.setBackground(Color.WHITE);
        pBar.setMaximumSize(new Dimension(1000, 45));
        pBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblNama = new JLabel(namaThread);
        lblNama.setPreferredSize(new Dimension(200, 20));
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setForeground(ACCENT_COLOR);
        pb.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        pBar.add(lblNama, BorderLayout.WEST);
        pBar.add(pb, BorderLayout.CENTER);
        pProgressContainer.add(pBar);
        pProgressContainer.add(Box.createVerticalStrut(5));
        pProgressContainer.revalidate();
        pProgressContainer.repaint();
        return pb;
    }

    // ==================== actionPerformed ====================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == bRun) {
            logArea.setText("[SYSTEM] Menyiapkan environment...\n");
            tableModel.setRowCount(0);
            nomorBaris = 1;
            pProgressContainer.removeAll();
            pProgressContainer.revalidate();
            bRun.setEnabled(false);

            final int nData    = Integer.parseInt(tfData.getText().trim());
            int inputThread    = Integer.parseInt(tfThread.getText().trim());
            if (inputThread < 1) inputThread = 1;
            final int nThread  = inputThread;

            final double vA  = parseDouble(tfA,             6.0);
            final double vB  = parseDouble(tfB,             4.0);
            final double vTT = parseDouble(tfTinggiTabung,  10.0);
            final double vTK = parseDouble(tfTinggiKerucut, 12.0);
            final double vC  = parseDouble(tfC,             5.0);
            final double vA2 = parseDouble(tfA2,            3.0);
            final double vB2 = parseDouble(tfB2,            2.0);
            final double vTF = parseDouble(tfTinggiTrunc,   8.0);

            new Thread(() -> {

                // Pembagian data ke 4 jenis bangun
                int dataElips    = nData / 4;
                int dataTabung   = nData / 4;
                int dataKerucut  = nData / 4;
                int dataEllipsoid = nData / 4;
                // sisa data ke frustum (KerucutTerpancung)
                int dataFrustum  = nData - dataElips - dataTabung - dataKerucut - dataEllipsoid;

                // Thread minimum 1 jika ada data
                int threadElips    = Math.max(1, nThread / 4);
                int threadTabung   = Math.max(1, nThread / 4);
                int threadKerucut  = Math.max(1, nThread / 4);
                int threadEllipsoid = Math.max(1, nThread / 4);
                int threadFrustum  = Math.max(1, nThread - threadElips - threadTabung - threadKerucut - threadEllipsoid);

                int totalThreadAktif = threadElips + threadTabung + threadKerucut + threadEllipsoid + threadFrustum;
                logArea.append("[SYSTEM] Eksekusi " + totalThreadAktif + " thread dimulai...\n");
                long start = System.currentTimeMillis();

                // Array thread
                Elips[]                   elipsThreads    = new Elips[threadElips];
                TabungElips[]             tabungThreads   = new TabungElips[threadTabung];
                KerucutElips[]            kerucutThreads  = new KerucutElips[threadKerucut];
                Ellipsoid[]               ellipsoidThreads = new Ellipsoid[threadEllipsoid];
                KerucutTerpancungElips[]  frustumThreads  = new KerucutTerpancungElips[threadFrustum];

                int cursor = 1;

                // --- Elips 2D ---
                if (dataElips > 0) {
                    int perThread = dataElips / threadElips;
                    int sisa      = dataElips % threadElips;
                    int awal      = cursor;
                    for (int i = 0; i < threadElips; i++) {
                        int jatah = perThread + (i < sisa ? 1 : 0);
                        int akhir = awal + jatah - 1;
                        String nama = "Thread-Elips-" + (i + 1);
                        JProgressBar pb = buatProgressBar(nama);
                        elipsThreads[i] = new Elips(vA, vB, nama, awal, akhir, logArea, MainFrame.this, pb);
                        awal = akhir + 1;
                    }
                    cursor = awal;
                }

                // --- Tabung Elips ---
                if (dataTabung > 0) {
                    int perThread = dataTabung / threadTabung;
                    int sisa      = dataTabung % threadTabung;
                    int awal      = cursor;
                    for (int i = 0; i < threadTabung; i++) {
                        int jatah = perThread + (i < sisa ? 1 : 0);
                        int akhir = awal + jatah - 1;
                        String nama = "Thread-Tabung-" + (i + 1);
                        JProgressBar pb = buatProgressBar(nama);
                        tabungThreads[i] = new TabungElips(vA, vB, vTT, nama, awal, akhir, logArea, MainFrame.this, pb);
                        awal = akhir + 1;
                    }
                    cursor = awal;
                }

                // --- Kerucut Elips ---
                if (dataKerucut > 0) {
                    int perThread = dataKerucut / threadKerucut;
                    int sisa      = dataKerucut % threadKerucut;
                    int awal      = cursor;
                    for (int i = 0; i < threadKerucut; i++) {
                        int jatah = perThread + (i < sisa ? 1 : 0);
                        int akhir = awal + jatah - 1;
                        String nama = "Thread-Kerucut-" + (i + 1);
                        JProgressBar pb = buatProgressBar(nama);
                        kerucutThreads[i] = new KerucutElips(vA, vB, vTK, nama, awal, akhir, logArea, MainFrame.this, pb);
                        awal = akhir + 1;
                    }
                    cursor = awal;
                }

                // --- Ellipsoid ---
                if (dataEllipsoid > 0) {
                    int perThread = dataEllipsoid / threadEllipsoid;
                    int sisa      = dataEllipsoid % threadEllipsoid;
                    int awal      = cursor;
                    for (int i = 0; i < threadEllipsoid; i++) {
                        int jatah = perThread + (i < sisa ? 1 : 0);
                        int akhir = awal + jatah - 1;
                        String nama = "Thread-Ellipsoid-" + (i + 1);
                        JProgressBar pb = buatProgressBar(nama);
                        ellipsoidThreads[i] = new Ellipsoid(vA, vB, vC, nama, awal, akhir, logArea, MainFrame.this, pb);
                        awal = akhir + 1;
                    }
                    cursor = awal;
                }

                // --- Kerucut Terpancung Elips (Frustum) ---
                if (dataFrustum > 0) {
                    int perThread = dataFrustum / threadFrustum;
                    int sisa      = dataFrustum % threadFrustum;
                    int awal      = cursor;
                    for (int i = 0; i < threadFrustum; i++) {
                        int jatah = perThread + (i < sisa ? 1 : 0);
                        int akhir = awal + jatah - 1;
                        String nama = "Thread-Frustum-" + (i + 1);
                        JProgressBar pb = buatProgressBar(nama);
                        frustumThreads[i] = new KerucutTerpancungElips(
                            vA, vB, vA2, vB2, vTF, nama, awal, akhir, logArea, MainFrame.this, pb);
                        awal = akhir + 1;
                    }
                }

                // --- Start semua thread ---
                for (Elips t : elipsThreads)    if (t != null) t.getThread().start();
                for (TabungElips t : tabungThreads) if (t != null) t.getThread().start();
                for (KerucutElips t : kerucutThreads) if (t != null) t.getThread().start();
                for (Ellipsoid t : ellipsoidThreads)  if (t != null) t.getThread().start();
                for (KerucutTerpancungElips t : frustumThreads) if (t != null) t.getThread().start();

                // --- Join semua thread ---
                try {
                    for (Elips t : elipsThreads)    if (t != null) t.getThread().join();
                    for (TabungElips t : tabungThreads) if (t != null) t.getThread().join();
                    for (KerucutElips t : kerucutThreads) if (t != null) t.getThread().join();
                    for (Ellipsoid t : ellipsoidThreads)  if (t != null) t.getThread().join();
                    for (KerucutTerpancungElips t : frustumThreads) if (t != null) t.getThread().join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                long totalTime = System.currentTimeMillis() - start;
                double detik   = totalTime / 1000.0;

                logArea.append("\n====================================\n");
                logArea.append(" DONE: Komputasi Selesai!\n");
                logArea.append(" Total Waktu: " + String.format("%.2f", detik) + " s\n");
                logArea.append("====================================\n");

                SwingUtilities.invokeLater(() -> bRun.setEnabled(true));

            }).start();

        } else if (e.getSource() == bReset) {
            // Reset semua field ke default
            tfData.setText("3");  tfThread.setText("1");
            tfA.setText("6");     tfB.setText("4");
            tfTinggiTabung.setText("10");  tfTinggiKerucut.setText("12");
            tfC.setText("5");     tfA2.setText("3");
            tfB2.setText("2");    tfTinggiTrunc.setText("8");

            logArea.setText("[SYSTEM] Dashboard berhasil di-reset ke pengaturan awal.\n");
            tableModel.setRowCount(0);
            nomorBaris = 1;
            pProgressContainer.removeAll();
            pProgressContainer.revalidate();
            pProgressContainer.repaint();
            bRun.setEnabled(true);
        }
    }

    // ==================== Helper: parseDouble ====================
    private double parseDouble(JTextField tf, double defaultVal) {
        try {
            String s = tf.getText().trim();
            return s.isEmpty() ? defaultVal : Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return defaultVal;
        }
    }
}
