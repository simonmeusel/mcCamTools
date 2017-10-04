package de.simonmeusel;

import javax.swing.*;
import java.io.File;

public class Arguments {

    String mode;
    Object[] modes = {"advancements"};

    boolean relative;

    String command;

    File file;

    public Arguments(String[] args) {
        if (args.length == 0) {
            // GUI

            mode = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose a mode",
                    "mcCamTools",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    modes,
                    modes[0]);

            relative = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                    null,
                    "Should the coordinates be transformed into relative coordinates?",
                    "mcCamTools",
                    JOptionPane.YES_NO_OPTION);

            command = JOptionPane.showInputDialog(
                    null,
                    "Enter the command you want to generate! %x, %y, %z, %yaw and %pitch will be replayed with recorded values",
                    "mcCamTools",
                    JOptionPane.PLAIN_MESSAGE);

            if (command == null || command == "") {
                System.exit(1);
            }

            file = new File(JOptionPane.showInputDialog(
                    null,
                    "Enter the path to a file containing the recorded values",
                    "mcCamTools",
                    JOptionPane.PLAIN_MESSAGE));

            /*
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Choose file containing recorded values");
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            } else {
                System.exit(1);
            }
             */
        } else {
            // CLI
            mode = args[0];
            relative = args[1].equals("true");
            command = args[2];
            file = new File(args[3]);
        }
    }

    public String getMode() {
        return mode;
    }

    public Object[] getModes() {
        return modes;
    }

    public boolean isRelative() {
        return relative;
    }

    public String getCommand() {
        return command;
    }

    public File getFile() {
        return file;
    }
}
