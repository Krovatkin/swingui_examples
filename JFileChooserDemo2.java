import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;

public class JFileChooserDemo2 extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;

    final JFileChooser fc = new JFileChooser();

    public JFileChooserDemo2() {
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
                Utils.createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        // Create the save button. We use the image from the JLF
        // Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...",
                Utils.createImageIcon("images/Save16.gif"));
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
            // JFileChooser supports three different kinds of filtering. 
            // The filters are checked in the order
            // Built-in filtering, Application-controlled filtering,
            // User-choosable filtering
            fc.setFileHidingEnabled(false); // the only built-in filter


            // !!!! This is a demo of an Application-controlled filtering
            // !!!! All image exts are allowed
            fc.setFileFilter(new ImageFilter(Utils.getImageExtensions(), "Just Images"));
            // User-choosable filtering
            fc.addChoosableFileFilter(new ImageFilter(List.of(Utils.jpeg, Utils.jpg), "JPEG Images"));
            fc.addChoosableFileFilter(new ImageFilter(List.of(Utils.gif), "GIF Images"));

            // !!! the button label and title are set to "Attach"
            int returnVal = fc.showDialog(this, "Attach");

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
                    pw.print("Saved by JFileChooserDemo2\n");
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

    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("JFileChooserDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add content to the window.
        frame.add(new JFileChooserDemo2());

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

 class ImageFilter extends FileFilter {

    Set<String> whitelist = new HashSet<String>();
    String description = "Invalid";

    ImageFilter(Collection<String> wl, String desc) {
        whitelist.addAll(wl);
        description = desc;
    }
 
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                extension.equals(Utils.tif) ||
                extension.equals(Utils.gif) ||
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg) ||
                extension.equals(Utils.png)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return description;
    }
}

class Utils {

    public static Collection<String> getImageExtensions() {
        return List.<String>of(jpeg, jpg, gif, tiff, tif, png);
    }

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}