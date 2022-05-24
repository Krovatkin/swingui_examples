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

import java.beans.*;

class ImagePreview extends JComponent
                          implements PropertyChangeListener {
    ImageIcon thumbnail = null;
    File file = null;

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage() {
        if (file == null) {
            thumbnail = null;
            return;
        }

        //!!!!!!
        //Don't use createImageIcon (which is a wrapper for getResource)
        //because the image we're trying to load is probably not one
        //of this program's own resources.
        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        if (tmpIcon != null) {
            if (tmpIcon.getIconWidth() > 90) {
                thumbnail = new ImageIcon(tmpIcon.getImage().
                                          getScaledInstance(90, -1,
                                                      Image.SCALE_DEFAULT));
            } else if (tmpIcon.getIconHeight() > 50) {
                thumbnail = new ImageIcon(tmpIcon.getImage().
                                          getScaledInstance(-1, 50,
                                                      Image.SCALE_DEFAULT));
            } else { //no need to miniaturize
                thumbnail = tmpIcon;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;

        //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update) {
            thumbnail = null;
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    // !!!! this is how we paint the icon
    protected void paintComponent(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }
        if (thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}

public class JFileChooserDemo4 extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;

    final JFileChooser fc = new JFileChooser();

    public JFileChooserDemo4() {
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

            // !!! the button label and title are set to "Attach"
            fc.setAccessory(new ImagePreview(fc));
            int returnVal = fc.showDialog(JFileChooserDemo4.this, "Attach");

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
                    pw.print("Saved by JFileChooserDemo4\n");
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
        JFrame frame = new JFrame("JFileChooserDemo4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add content to the window.
        frame.add(new JFileChooserDemo4());

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
