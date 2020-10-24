/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mummyproject;



import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// * USER DEFINED EXCEPTION CLASSES
class OfficeNameException extends Exception {
    public String toString() {
        return ("Office name must only contain alphabets");
    }
}

class InvalidReasonException extends Exception {
    private String message;

    InvalidReasonException(String msg) {
        message = msg;
    }

    public String toString() {
        return message;
    }
}

// * APP
class App {
    // ! CONSTRUCTOR
    App() {
        JFrame jf = new JFrame("INDIA POST");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // jf.setLayout(new FlowLayout());
        jf.setSize(600, 600);
        JTabbedPane jtp = new JTabbedPane();
        // jtp.addTab("Home", new Home());
        jtp.addTab("Configuration", new Configuration(jf));
        jtp.addTab("Manifest", new Manifest(jf));
        // jtp.addTab("Remove", new Remove(jf));
        jtp.addTab("Track and Trace", new Track_Trace());
        jf.add(jtp);
        jf.setVisible(true);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}

// * TRACK AND TRACE
class Track_Trace extends JPanel implements ActionListener {
    JLabel track_id;
    JTextField track_field;
    Connection con = null;
    ResultSet r2, r3;
    Statement stmt;
    int p;
    JTextArea txt;
    String s, pname, s1;
    JButton view, reset;
    Dimension screenSize;
    float total;

    public Track_Trace() {
        track_id = new JLabel("Bag/Article No:");
        track_field = new JTextField();
        view = new JButton("View");
        setLayout(null);
        add(track_id);
        add(track_field);
        add(view);
        track_id.setBounds(50, 10, 100, 30);
        track_field.setBounds(200, 10, 100, 30);
        view.setBounds(400, 10, 100, 30);

        view.addActionListener(this);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    }

    public void actionPerformed(ActionEvent ae) {
        System.out.println("Connected");

    }

}

// * CONFIGURATION
class Configuration extends JPanel implements ActionListener {
    JButton b1, b2, b3, b4, b5, b6;
    JLabel l1, l2, l3, l4;
    JLabel labelarr[];
    JTextField textarr[];

    JFrame temp;
    JComboBox config_list, printer_list;
    Checkbox checkarr[];

    Connection con = null;
    ResultSet r2, r3;
    Statement stmt;
    PreparedStatement stat;
    // int counter, qty = 0, max1 = 0, max2 = 0;

    // Drop down list
    String[] config_options = { "Office Config", "Add Next Office", "Add User", "Add Reason", "Add Printer" };
    ArrayList<String> printer_names = new ArrayList<String>();
    PrintService[] ps = PrinterJob.lookupPrintServices();

    // ! isStringOnlyAlphabet()
    public boolean isStringOnlyAlphabet(String str) {
        return ((!str.equals("")) && (str != null) && (str.matches("^[ A-Za-z]+$")));
    }

    // ! checkReason()
    public boolean checkReason(String id, String name) {
        try {
            int temp_id = Integer.parseInt(id);
            if (!isStringOnlyAlphabet(name)) {
                throw new InvalidReasonException("Reason name must contain only alphabets");

            }
            if (id.length() > 2) {
                throw new InvalidReasonException("Reason ID cannot be more than 2 digits");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Reason ID must be an integer");
            return false;
        } catch (InvalidReasonException e) {
            JOptionPane.showMessageDialog(this, e.toString());
            return false;
        }
        return true;
    }

    // ! Constructor
    public Configuration(JFrame jf) {
        temp = jf;
        for (int count = 0; count < ps.length; ++count) {
            printer_names.add(ps[count].getName());
        }

        labelarr = new JLabel[4];
        textarr = new JTextField[4];
        checkarr = new Checkbox[4];
        labelarr[0] = new JLabel("Office Name");
        labelarr[1] = new JLabel("Address");
        labelarr[2] = new JLabel("Office Code");
        textarr[0] = new JTextField(20);
        textarr[1] = new JTextField(20);
        textarr[2] = new JTextField(20);

        checkarr[0] = new Checkbox("Configuration");
        checkarr[1] = new Checkbox("Manifest");
        checkarr[2] = new Checkbox("Track and Trace");

        checkarr[1].setState(true);
        checkarr[2].setState(true);

        b1 = new JButton("Submit");
        b2 = new JButton("Add ");
        b3 = new JButton("Add ");
        b4 = new JButton("Add ");
        b5 = new JButton("Add ");
        b6 = new JButton("Add ");

        // Drop down
        config_list = new JComboBox(config_options);
        printer_list = new JComboBox(printer_names.toArray());

        // Display as null
        setLayout(null);

        // Adding the elements
        add(config_list);
        add(b1);

        // Setting bounds
        config_list.setBounds(100, 60, 200, 20);
        b1.setBounds(400, 60, 100, 30);

        // Adding Event Listeners
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);

        // config_list.addItemListener(this);

    }

    // ! Action Listeners
    public void actionPerformed(ActionEvent ae) {

        // * DATABASE CONNECTIVITY
        // : OFFICE CONFIG DATABASE CONNECTIVITY
        if (ae.getSource() == b2) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                        "bethebest");

                String officeName = textarr[0].getText();
                String officeAddress = textarr[1].getText();
                String officeCode = textarr[2].getText();

                if (!isStringOnlyAlphabet(officeName)) {
                    throw new OfficeNameException();
                }

                // ? Checking data already exits or not
                stmt = con.createStatement();
                r2 = stmt.executeQuery("select officeID from mainOffice");

                if (r2.next()) {
                    int temp_officeID = r2.getInt(1);
                    stat = con.prepareStatement("update mainOffice set officeID=?, officeName=?, officeAddress=? ");

                    stat.setInt(1, Integer.parseInt(officeCode));
                    stat.setString(2, officeName);
                    stat.setString(3, officeAddress);
                    stat.executeUpdate();
                    stat = con.prepareStatement("commit");
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Main Office configuration changed successfully");

                } else {

                    stat = con.prepareStatement("insert into mainOffice values(?,?,?)");

                    stat.setInt(1, Integer.parseInt(officeCode));
                    stat.setString(2, officeName);
                    stat.setString(3, officeAddress);

                    stat.executeUpdate();
                    stat = con.prepareStatement("commit");
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Main Office configuration done");
                }
                textarr[0].setText("");
                textarr[1].setText("");
                textarr[2].setText("");

                App a = new App();
                temp.dispose();

            } catch (OfficeNameException e) {
                JOptionPane.showMessageDialog(this, e.toString());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Office ID must contain only numbers");
                textarr[0].setText("");
                textarr[1].setText("");
                textarr[2].setText("");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        // : ADD NEXT OFFICE DATABASE CONNECTIVITY
        else if (ae.getSource() == b3) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                        "bethebest");
                stat = con.prepareStatement("insert into office values(?,?)");

                String officeCode = textarr[0].getText();
                String officeName = textarr[1].getText();

                if (!isStringOnlyAlphabet(officeName)) {
                    throw new OfficeNameException();
                }

                stat.setInt(1, Integer.parseInt(officeCode));
                stat.setString(2, officeName);

                stat.executeUpdate();
                stat = con.prepareStatement("commit");
                stat.executeUpdate();
                JOptionPane.showMessageDialog(this, "Office Added Successfully");
                textarr[0].setText("");
                textarr[1].setText("");

                App a = new App();
                temp.dispose();

            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Office already added");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Office ID must contain only numbers");
                textarr[0].setText("");
                textarr[1].setText("");
                textarr[2].setText("");
            } catch (OfficeNameException e) {
                JOptionPane.showMessageDialog(this, e.toString());
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        // : ADD USER DATABASE CONNECTIVITY
        else if (ae.getSource() == b4) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                        "bethebest");
                stat = con.prepareStatement("insert into users values(?,?,?,?,?)");
                String config, manifest, trackTrace;
                String userID = textarr[0].getText();
                String userName = textarr[1].getText();
                if (checkarr[0].getState() == true)
                    config = "1";
                else
                    config = "0";
                if (checkarr[1].getState() == true)
                    manifest = "1";
                else
                    manifest = "0";
                if (checkarr[2].getState() == true)
                    trackTrace = "1";
                else
                    trackTrace = "0";

                stat.setInt(1, Integer.parseInt(userID));
                stat.setString(2, userName);
                stat.setInt(3, Integer.parseInt(config));
                stat.setInt(4, Integer.parseInt(manifest));
                stat.setInt(5, Integer.parseInt(trackTrace));

                stat.executeUpdate();
                stat = con.prepareStatement("commit");
                stat.executeUpdate();
                JOptionPane.showMessageDialog(this, "User added successfully");
                App a = new App();
                temp.dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Office ID must contain only numbers");
                textarr[0].setText("");
                textarr[1].setText("");
                textarr[2].setText("");
            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "User already added");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        // : ADD REASON DATABASE CONNECTIVITY
        else if (ae.getSource() == b5) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                        "bethebest");
                stat = con.prepareStatement("insert into reasons values(?,?)");
                String reasonCode = textarr[0].getText();
                String reason = textarr[1].getText();

                if (checkReason(reasonCode, reason)) {
                    stat.setString(1, reasonCode);
                    stat.setString(2, reason);

                    stat.executeUpdate();
                    stat = con.prepareStatement("commit");
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Reason added successfully");
                    App a = new App();
                    temp.dispose();
                }

            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Reason already added");
                textarr[0].setText("");
                textarr[1].setText("");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

        // * RENDERING ELEMENTS
        // : OFFICE CONFIG RENDERING
        else if (config_list.getSelectedItem() == "Office Config") {
            checkVisibility();

            // CHANGING TEXT
            labelarr[0].setText("Office Name");
            labelarr[1].setText("Address");
            labelarr[2].setText("Office Code");

            textarr[0].setText("");
            textarr[1].setText("");
            textarr[2].setText("");

            // ADDING
            for (int i = 0; i < 3; i++) {
                add(labelarr[i]);
                add(textarr[i]);
            }
            add(b2);
            // SETTING BOUNDS

            labelarr[0].setBounds(100, 140, 100, 20);
            labelarr[1].setBounds(100, 220, 100, 20);
            labelarr[2].setBounds(100, 300, 100, 20);
            textarr[0].setBounds(300, 140, 200, 20);
            textarr[1].setBounds(300, 220, 200, 20);
            textarr[2].setBounds(300, 300, 200, 20);
            b2.setBounds(250, 400, 100, 20);

        }

        // :RENDERING ADD NEXT OFFICE
        else if (config_list.getSelectedItem() == "Add Next Office") {
            checkVisibility();

            // CHANGING TEXT
            labelarr[0].setText("Office Code");
            labelarr[1].setText("Office Name");

            textarr[0].setText("");
            textarr[1].setText("");
            textarr[2].setText("");

            // ADDING
            for (int i = 0; i < 2; i++) {
                add(labelarr[i]);
                add(textarr[i]);
            }
            add(b3);

            // Setting bounds

            labelarr[0].setBounds(100, 140, 100, 20);
            labelarr[1].setBounds(100, 220, 100, 20);
            textarr[0].setBounds(300, 140, 200, 20);
            textarr[1].setBounds(300, 220, 200, 20);

            b3.setBounds(250, 300, 100, 20);

        }

        // :RENDERING ADD NEXT USER
        else if (config_list.getSelectedItem() == "Add User") {
            checkVisibility();

            // CHANGING TEXT
            labelarr[0].setText("User ID");
            labelarr[1].setText("User Name");
            labelarr[2].setText("User Rights");

            textarr[0].setText("");
            textarr[1].setText("");
            textarr[2].setText("");

            // ADDING
            for (int i = 0; i < 3; i++) {
                add(labelarr[i]);
                if (i != 2)
                    add(textarr[i]);
                add(checkarr[i]);
            }
            add(b4);

            // Setting bounds

            labelarr[0].setBounds(100, 140, 100, 20);
            labelarr[1].setBounds(100, 220, 100, 20);
            labelarr[2].setBounds(100, 300, 100, 20);
            textarr[0].setBounds(300, 140, 200, 20);
            textarr[1].setBounds(300, 220, 200, 20);
            textarr[2].setBounds(300, 300, 200, 20);
            checkarr[0].setBounds(300, 300, 100, 20);
            checkarr[1].setBounds(400, 300, 100, 20);
            checkarr[2].setBounds(500, 300, 200, 20);
            b4.setBounds(250, 400, 100, 20);

        }

        // :RENDERING ADD REASON
        else if (config_list.getSelectedItem() == "Add Reason") {
            checkVisibility();

            // CHANGING TEXT
            labelarr[0].setText("Reason Code");
            labelarr[1].setText("Reason");

            textarr[0].setText("");
            textarr[1].setText("");
            textarr[2].setText("");

            // ADDING
            for (int i = 0; i < 2; i++) {
                add(labelarr[i]);
                add(textarr[i]);
            }
            add(b5);

            // Setting bounds

            labelarr[0].setBounds(100, 140, 100, 20);
            labelarr[1].setBounds(100, 220, 100, 20);
            textarr[0].setBounds(300, 140, 200, 20);
            textarr[1].setBounds(300, 220, 200, 20);

            b5.setBounds(250, 300, 100, 20);

        }

        // :RENDERING ADD PRINTER
        else if (config_list.getSelectedItem() == "Add Printer") {
            checkVisibility();

            // CHANGING TEXT
            labelarr[0].setText("Default Printer");

            textarr[0].setText("");
            textarr[1].setText("");
            textarr[2].setText("");

            // ADDING

            add(labelarr[0]);
            add(printer_list);
            add(b6);

            // Setting bounds

            labelarr[0].setBounds(100, 140, 100, 20);
            printer_list.setBounds(300, 140, 200, 20);

            b6.setBounds(250, 300, 100, 20);

        }
    }

    // ! checkVisibility()
    public void checkVisibility() {

        for (int i = 0; i < 3; i++) {
            if (labelarr[i].isDisplayable())
                remove(labelarr[i]);
            if (textarr[i].isDisplayable())
                remove(textarr[i]);
            if (checkarr[i].isDisplayable())
                remove(checkarr[i]);
        }
        if (b2.isDisplayable())
            remove(b2);
        if (b3.isDisplayable())
            remove(b3);
        if (b4.isDisplayable())
            remove(b4);
        if (b5.isDisplayable())
            remove(b5);
        if (b6.isDisplayable())
            remove(b6);
        if (printer_list.isDisplayable())
            remove(printer_list);
        temp.repaint();

    }

}

// * MANIFEST
class Manifest extends JPanel implements ActionListener {

    JLabel nextOfficeLabel, bagNumberLabel, weightLabel, articlesNumLabel, articleCount, articleLabel, reasonLabel;
    JTextField bagNumber, weight, article;
    JButton submit, remove, print;
    ArrayList nextOffices, reasonList;
    JComboBox offices, reason;
    java.awt.List windowArea;
    Statement stmt;
    PreparedStatement prepStmt;
    ResultSet rs, rs2;
    JTable table;
    DefaultTableModel model;
    String header[];
    JFrame temp;
    ArrayList counter;
    private static com.itextpdf.text.Font catFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD);

    private static com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD);

    // ! CONSTRUCTOR

    public Manifest(JFrame jf) {
        temp = jf;
        temp.repaint();
        counter = new ArrayList();
        // : Labels
        nextOfficeLabel = new JLabel("Next Office");
        bagNumberLabel = new JLabel("Bag No.");
        weightLabel = new JLabel("Weight");
        articlesNumLabel = new JLabel("Total no. of articles");
        articleCount = new JLabel(Integer.toString(counter.size()));
        articleLabel = new JLabel("Article No.");
        reasonLabel = new JLabel("Reason");

        // : TextFields
        bagNumber = new JTextField(20);
        weight = new JTextField(20);
        article = new JTextField(20);

        // : Buttons
        submit = new JButton("Submit");
        remove = new JButton("Remove");
        print = new JButton("Print");

        // : AWT List
        // windowArea = new java.awt.List();

        // : List of next offices
        nextOffices = new ArrayList();
        reasonList = new ArrayList();

        // : JTable
        table = new JTable();

        header = new String[] { "Article Number", "Reason", "Weight", "Next Office" };
        model = new DefaultTableModel(0, 0);

        model.setColumnIdentifiers(header);
        table.setModel(model);
        JTableHeader tableHeader = table.getTableHeader();
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar", "bethebest");

            stmt = con.createStatement();
            rs = stmt.executeQuery("select officeCode, officeName from office");

            while (rs.next()) {
                nextOffices.add(String.format("%s - %s", rs.getString(1), rs.getString(2)));
            }

            offices = new JComboBox(nextOffices.toArray());
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar", "bethebest");

            stmt = con.createStatement();
            rs2 = stmt.executeQuery("select reasonID, reasonName from reasons");
            while (rs2.next()) {
                reasonList.add(String.format("%-5s - %s", rs2.getString(1), rs2.getString(2)));
            }
            System.out.println(reasonList.size());
            // String[] temp ={"1,2,3,", "4.5.6"};
            reason = new JComboBox(reasonList.toArray());

        } catch (Exception e) {
            System.out.println(e);
        }

        // : Rendering
        setLayout(null);

        nextOfficeLabel.setBounds(50, 20, 100, 20);
        offices.setBounds(230, 20, 300, 20);

        bagNumberLabel.setBounds(50, 70, 100, 20);
        bagNumber.setBounds(230, 70, 100, 20);

        articleLabel.setBounds(50, 120, 100, 20);
        article.setBounds(230, 120, 150, 20);

        reasonLabel.setBounds(400, 120, 50, 20);
        reason.setBounds(460, 120, 200, 20);

        weightLabel.setBounds(700, 120, 50, 20);
        weight.setBounds(800, 120, 50, 20);

        articlesNumLabel.setBounds(50, 170, 150, 20);
        articleCount.setBounds(230, 170, 150, 20);

        submit.setBounds(300, 220, 100, 20);

        tableHeader.setBounds(50, 270, 700, 30);
        table.setBounds(50, 300, 700, 300);
        remove.setBounds(800, 250, 100, 20);
        print.setBounds(800, 300, 100, 20);

        add(nextOfficeLabel);
        add(offices);

        add(bagNumberLabel);
        add(bagNumber);

        add(articleLabel);
        add(article);

        add(reasonLabel);
        add(reason);

        add(weightLabel);
        add(weight);

        add(articlesNumLabel);
        add(articleCount);

        add(submit);

        // add(windowArea);
        add(tableHeader);
        add(table);
        add(remove);
        add(print);

        // : Action Listeners
        submit.addActionListener(this);
        remove.addActionListener(this);
        print.addActionListener(this);
    }

    // ! repaintManifest()
    public void repaintManifest() {
        temp.repaint();
    }

    // ! checkInput()
    public boolean checkInput() {
        return true;
    }

    // ! print
    private void print1() {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Manifest.pdf"));

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            cb.saveState();
            Graphics2D g2 = cb.createGraphicsShapes(500, 500);

            Shape oldClip = g2.getClip();
            g2.clipRect(0, 0, 500, 500);

            table.print(g2);
            g2.setClip(oldClip);

            g2.dispose();
            cb.restoreState();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        document.close();
    }

    // ! addEmptyLine()
    public void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    // ! pdfHeader()
    public void pdfHeader(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("ITEM INTERNAL MANIFEST", catFont));
        addEmptyLine(preface, 1);
        document.add(preface);

    }

    // ! getCurrentOffice()
    public String getCurrentOffice() {
        ResultSet r;
        String ans = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar", "bethebest");
            r = stmt.executeQuery("select officeID, officeName from mainOffice");

            if (r.next()) {
                int temp_officeID = r.getInt(1);
                String temp_officeName = r.getString(2);
                ans = temp_officeID + "-" + temp_officeName;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ans;
    }

    // ! pdfUpperSection()
    public void pdfUpperSection(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        preface.setAlignment(Element.ALIGN_LEFT);

        preface.add(new Paragraph("Office \t:  " + getCurrentOffice(), smallFont));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Paragraph preface2 = new Paragraph();
        preface2.setAlignment(Element.ALIGN_RIGHT);
        preface2.add(new Paragraph("Date \t:  " + now, smallFont));

        Paragraph preface3 = new Paragraph();
        preface3.setAlignment(Element.ALIGN_LEFT);
        preface3.add(new Paragraph("Next Office \t:  " + offices.getSelectedItem(), smallFont));

        Paragraph preface4 = new Paragraph();
        preface4.setAlignment(Element.ALIGN_LEFT);
        preface4.add(new Paragraph("Bag No. \t:  " + bagNumber.getText(), smallFont));

        Paragraph preface5 = new Paragraph();
        preface5.setAlignment(Element.ALIGN_LEFT);
        preface5.add(new Paragraph("Number of Items \t\t:  " + counter.size(), smallFont));

        document.add(preface);
        addEmptyLine(preface, 1);
        document.add(preface2);
        addEmptyLine(preface2, 1);
        document.add(preface3);
        document.add(preface4);
    }

    // ! print()
    public void print() {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("Manifest.pdf"));
            doc.open();
            pdfHeader(doc);
            pdfUpperSection(doc);
            doc.add(Chunk.NEWLINE);
            // doc.addTitle("Manifest");
            PdfPTable pdfTable = new PdfPTable(4);
            // doc.addHeader("ITEM INTERNAL MANIFEST", "");

            // : Headers
            pdfTable.addCell("Article Number");
            pdfTable.addCell("Reason");
            pdfTable.addCell("Weight");
            pdfTable.addCell("Next Office");

            for (int i = 0; i < table.getRowCount(); i++) {
                String articleNumber = table.getValueAt(i, 0).toString();
                String reason = table.getValueAt(i, 1).toString();
                String weight = table.getValueAt(i, 2).toString();
                String nextOffice = table.getValueAt(i, 3).toString();

                pdfTable.addCell(articleNumber);
                pdfTable.addCell(reason);
                pdfTable.addCell(weight);
                pdfTable.addCell(nextOffice);
            }

            // : Adding to the pdf file
            doc.addTitle("ITEM INTERNAL MANIFEST");
            doc.add(pdfTable);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        doc.close();
    }

    // ! ACTION LISTENER
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            if (checkInput()) {

                // : DATABASE
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                            "bethebest");

                    prepStmt = con.prepareStatement("insert into articles values(?,?,?,?)");

                    prepStmt.setString(1, bagNumber.getText());
                    prepStmt.setString(2, article.getText());
                    prepStmt.setString(3, String.valueOf(reason.getSelectedItem()));
                    prepStmt.setString(4, weight.getText());

                    prepStmt.executeUpdate();

                    prepStmt = con.prepareStatement("commit");
                    prepStmt.executeUpdate();
                    counter.add(1);

                    // : Rendering
                    articleCount.setText(Integer.toString(counter.size()));

                    // : JTABLE
                    model.addRow(new Object[] { article.getText(), reason.getSelectedItem(), weight.getText(),
                            offices.getSelectedItem() });

                } catch (SQLIntegrityConstraintViolationException e) {
                    JOptionPane.showMessageDialog(this, "Article already added");
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }

        if (ae.getSource() == remove) {
            // : Database
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/indiapost", "omkar",
                        "bethebest");
                prepStmt = con.prepareStatement("delete from articles where bagNumber=? and articleNumber=?");
                prepStmt.setString(1, bagNumber.getText());
                prepStmt.setString(2, article.getText());

                prepStmt.executeUpdate();

                prepStmt = con.prepareStatement("commit");
                prepStmt.executeUpdate();
                if (counter.size() > 0) {
                    counter.remove(0);
                    articleCount.setText(Integer.toString(counter.size()));
                }

            } catch (Exception e) {
                System.out.println(e);
            }

            // : Rendering
            // check for selected row first
            if (table.getSelectedRow() != -1) {
                // remove selected row from the model
                model.removeRow(table.getSelectedRow());
                JOptionPane.showMessageDialog(null, "Article Removed");
            }

        }
        if (ae.getSource() == print) {

            // MessageFormat header = new MessageFormat("Item Internal Manifest");
            // MessageFormat footer = new MessageFormat("Page{0,number,integer}");
            // try {
            // table.print(JTable.PrintMode.NORMAL, header, footer);
            // } catch (java.awt.print.PrinterException e) {
            // System.out.println(e.getMessage());

            // }

            print();

        }
    }

}

// * MAIN CLASS
class MummyProject {
    public static void main(String args[]) {
        App a = new App();

    }

}

