/* 
 * Vincent Testagrossa
 * Project 2: Polynomial Linked list implementation with Strong and Weak Order checking.
 * 11JUN2022
 *
 * Requirements: Uses Polynomial, OrderedList, and InvalidPolynomialSyntax.
 *
 * The main class is used to instantiate the JFrame object and move it to the front.
 * 
 * ProjectFrame class builds the JFrame object with 2 panels, 2 buttons, and a JTextArea for displaying 
 * output for the user. The Run button is used to run the program on the file once the fileButton has been
 * used and a file is succesfully loaded. fileButton uses the JFileChooser object to get the path to the file.
 * All other functions initiate when the run button is activated.
 * 
 * The Run button 
 */

package Project2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class Main {
    public static void main(String[] args) {
        ProjectFrame window = new ProjectFrame();
        window.toFront();
    }

    private static class ProjectFrame extends JFrame implements ActionListener {
        private JPanel topPanel, bottomPanel;
        private JButton runButton, fileButton;
        private JTextArea outputArea;
        private JScrollPane scroll;
        private BoxLayout layout;
        private JFileChooser file = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());;
    
        private File f;

        private ArrayList<Polynomial> polylist;
    
        ProjectFrame(){
            //instantiate the Boxlayout and set the axis to Y. Instantiate the panels to be added to the frame using the layout.
            layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
            topPanel = new JPanel();
            bottomPanel = new JPanel();
    
            //set the close operation to exit, set the title and set the layout for the frame.
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("Polynomial List Functions");
            this.setLayout(layout);
    
            //create the output area for the program.
            outputArea = new JTextArea(10,25);
            outputArea.setEditable(false);
            scroll = new JScrollPane(outputArea);
    
            //create the buttons
            runButton = new JButton("Run");
            runButton.addActionListener(this);
            fileButton = new JButton("Choose a file");
            fileButton.addActionListener(this);
            
            //set the top panel layout to be a flowLayout, then add the top panel to the frame and add the
            //buttons to the top panel
            topPanel.setLayout(new FlowLayout());
            this.add(topPanel);
            topPanel.add(runButton);
            topPanel.add(fileButton);
            
            //set the bottom panel layout to be a flowlayout, then add the bottom panel to the frame and add the
            //JScrollPane
            bottomPanel.setLayout(new FlowLayout());
            this.add(bottomPanel);
            bottomPanel.add(scroll);
    
            //Pack the frame to fit to the width of the panels/components. Disable resizing, and set the frame to visible.
            this.pack();
            this.setResizable(false);
            this.setVisible(true);
        }
        @Override 
        public void actionPerformed(ActionEvent e){
            /*
             * Run button creates a new Arraylist of polynomials and then checks if the file is null.
             * If the file isn't null, a new scanner is instantiated with the path and the polylist is
             * populated with new polynomials. The polynomial.toString() method is called and appended to
             * a String, which will then be appended by the orderType() method.
             * 
             * Exceptions: 
             * -file not found pops up an error in a new JOPtionPane
             * -InvalidPolynomialSyntax creates a new JOptionPane with the message provided.
             */
            if (e.getSource() == runButton){
                String outputString = "";
                polylist = new ArrayList<Polynomial>();
                if (f != null) {
                    try {
                        Scanner scn = new Scanner(f);
                        while (scn.hasNextLine()) {
                            polylist.add(new Polynomial(scn.nextLine()));
                        }
                        scn.close();
                        outputArea.setText("");
                    } catch (FileNotFoundException e1) {
                        String message = f + ": File not found";
                        JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (InvalidPolynomialSyntax e1) {
                        JOptionPane.showMessageDialog(this, e1.getMessage(), "Syntax Error", JOptionPane.ERROR_MESSAGE);
                    }
                    for (Polynomial p : polylist) {
                        outputString += p.toString() + "\n";
                    }
                    if (outputString != "") {
                        outputString += orderType(polylist);
                    }
                        outputArea.setText(outputString);
                }
            }
            if (e.getSource() == fileButton){
                int ret = file.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    f = file.getSelectedFile();
                }
            }
        }
    }
    /*
     * Takes an ArrayList of polynomials and determines the order type by calling each method version of
     * OrderedList.checkSorted(). The comparator is created with an anonymous inner class that checks the
     * weak order of the polynomials.
     */
    static private String orderType(ArrayList<Polynomial> polylist){
        String ret = "\n";
        Comparator<Polynomial> bigo = new Comparator<Polynomial>(){

            @Override
            public int compare(Polynomial p1, Polynomial p2) {
                Iterator<Polynomial.Term> i1 = p1.iterator();
                Iterator<Polynomial.Term> i2 = p2.iterator();
                while(i1.hasNext() && i2.hasNext()){
                    double d1 = i1.next().getExponent();
                    double d2 = i2.next().getExponent();
                    if (d1 > d2) { // 
                        return 1;
                    }
                    else if (d1 < d2) {
                        return -1;
                    }
                    else if (!i1.hasNext() && !i2.hasNext()){
                        return 0;
                    }
                    else if (!i1.hasNext()){
                        return 1;
                    }
                    else if (!i2.hasNext()) {
                        return -1;
                    }
                }
                return 0;
            }

        };
        boolean strong = false, weak = false;
        if (OrderedList.checkSorted(polylist)){
            strong = true;
        }
        if (OrderedList.checkSorted(polylist, bigo)){
            weak = true;
        }
        ret = "\nSorted on Strong Order: " + String.valueOf(strong) +
              "\nSorted on Weak Order: " + String.valueOf(weak);

        return ret;
    }
}
