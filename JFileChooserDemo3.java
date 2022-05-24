import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.SwingUtilities;

import java.io.File;
import javax.swing.filechooser.*;

class ImageFileView extends FileView {
    ImageIcon jpgIcon = ImageFileView.createImageIcon("images/jpgIcon.gif");
    ImageIcon gifIcon = ImageFileView.createImageIcon("images/gifIcon.gif");
    ImageIcon tiffIcon = ImageFileView.createImageIcon("images/tiffIcon.gif");
    ImageIcon pngIcon = ImageFileView.createImageIcon("images/pngIcon.png");

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = JFileChooserDemo3.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public String getName(File f) {
        return null; // let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; // let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; // let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(Utils.gif)) {
                type = "GIF Image";
            } else if (extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(Utils.png)) {
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = Utils.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(Utils.gif)) {
                icon = gifIcon;
            } else if (extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif)) {
                icon = tiffIcon;
            } else if (extension.equals(Utils.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}

public class JFileChooserDemo3 extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;

    final JFileChooser fc = new JFileChooser();

    public JFileChooserDemo3() {
        super(new BorderLayout());

        // Create the log first, because the action listeners
        // need to refer to it.
        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        // Create the open button. We use the image from the JLF
        // Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...",
                ImageFileView.createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        // Create the save button. We use the image from the JLF
        // Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...",
                ImageFileView.createImageIcon("images/Save16.gif"));
        saveButton.addActionListener(this);

        // For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        // Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        // Handle open button action.
        if (e.getSource() == openButton) {

            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileView(new ImageFileView());
            int returnVal = fc.showOpenDialog(this);
            var file = fc.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    // Count number of lines in the open file.
                    try (var br = new BufferedReader(new FileReader(file))) {
                        int lines = 0;
                        while ((br.readLine()) != null) {
                            lines++;
                        }

                        javax.swing.JOptionPane.showMessageDialog(null, "Number of lines in the file:" + lines);
                        log.append("Opened file: " + file.getAbsolutePath()
                                + ". There are " + lines + " in the file." + newline);
                    }
                } catch (IOException exc) {
                    log.append("Problem opening file: "
                            + exc.getLocalizedMessage()
                            + newline);
                }
            } else {
                log.append("User canceled open request." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }

        // Handle save button action.
        if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(this);
            var file = fc.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try (var pw = new PrintWriter(file)) {
                    pw.print("Saved by JFileChooserDemo3\n");
                } catch (Exception exc) {
                    log.append("Save command failed: "
                            + exc.getLocalizedMessage()
                            + newline);
                    log.setCaretPosition(log.getDocument().getLength());
                }
            } else {
                log.append("User canceled save request." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("JFileChooserDemo3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add content to the window.
        frame.add(new JFileChooserDemo3());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}