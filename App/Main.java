package App;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Main
 */
public class Main {

    public static void main(String[] a) {
        String text = "Sleeping is necessary for a healthy body.\n"
                + " But sleeping in unnecessary times may spoil our health, wealth and studies."
                + " Doctors advise that the sleeping at improper timings may lead for obesity during the students days.";
        (new Main()).draw(text);
    }

    public void draw(String text) {
        JFrame f = new JFrame();
        JTextArea area = new JTextArea(text);
        area.setBounds(0, 0, 1000, 1000);
        area.setBackground(java.awt.Color.BLACK);
        area.setForeground(java.awt.Color.WHITE);
        area.setFont(new java.awt.Font("Courier", java.awt.Font.CENTER_BASELINE, 20));
        f.add(area);
        f.setSize(1000, 1000);
        f.setLayout(null);
        f.setVisible(true);
    }
}