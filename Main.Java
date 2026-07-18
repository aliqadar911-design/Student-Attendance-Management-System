package com.company;
public class Main {

    public static void main(
            String[] args) {

        javax.swing.SwingUtilities.invokeLater(
                () -> {

                    MainFrame frame =
                            new MainFrame();

                    frame.setVisible(true);
                }
        );
    }
}
