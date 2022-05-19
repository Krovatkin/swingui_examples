import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import java.awt.Dimension;
import java.awt.GridLayout;

class Year {
    public Year(int year) {
        this.year_ = year;
    }

    @Override
    public String toString() {
        return String.valueOf(year_);
    }
    private int year_;
}

public class DefaultTableCellRendererDemo extends JPanel {

    public DefaultTableCellRendererDemo() {
        super(new GridLayout(1,0));

        String[] columnNames = {"First Name",
                                "Last Name",
                                "Sport",
                                "# of Years",
                                "Vegetarian"};

        Object[][] data = {
	    {"Kathy", "Smith",
	     "Snowboarding", new Year(5), false},
	    {"John", "Doe",
	     "Rowing", new Year(3), true},
	    {"Sue", "Black",
	     "Knitting", new Year(2), false},
	    {"Jane", "White",
	     "Speed reading", new Year(20), true},
	    {"Joe", "Brown",
	     "Pool", new Year(10), false}
        };


        var tm = new DefaultTableModel(data, columnNames) {
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };

        var renderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                // this is basically a JLabel widget
                // setText or setIcon
                setText(value + "y");
            }
        };


        final JTable table = new JTable(tm);
        // if we are using the default renderer
        // we also have to implement DefaultTableModel.getColumnClass
        // so a table would know for which column `renderer` should
        // be used for
        table.setDefaultRenderer(Year.class, renderer);

        
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);




        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        DefaultTableCellRendererDemo newContentPane = new DefaultTableCellRendererDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}