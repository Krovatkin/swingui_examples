import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;

public class JFileChooserDemo extends JPanel
                                implements ActionListener {
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;

    final JFileChooser fc  = new JFileChooser();

    public JFileChooserDemo() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...", 
                                 createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...",
                                 createImageIcon("images/Save16.gif"));
        saveButton.addActionListener(this); 

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {

            int returnVal = fc.showOpenDialog(this);
            var file = fc.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    //This is where a real application would do something
                    //with the file.

                    var br = new BufferedReader(new FileReader(file));
                    javax.swing.JOptionPane.showMessageDialog(null, br.readLine());
                    log.append("Opened file: " + file.getAbsolutePath()
                               + "." + newline);
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

        //Handle save button action.
        if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(this);
            var file = fc.getSelectedFile();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try (var pw = new PrintWriter(file)) {
                    pw.print("Saved by JFileChooserDemo\n");
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

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = JFileChooserDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("JFileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new JFileChooserDemo());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}