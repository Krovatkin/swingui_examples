import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleTableDemo2 extends JPanel implements TableModelListener {
    private boolean DEBUG = false;


    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        // this is the updated value
        System.out.println(data);
    }

    public SimpleTableDemo2() {
        super(new GridLayout(1,0));

        System.out.println("SimpleTableDemo2 c-tor");

        String[] columnNames = {"First Name",
                                "Last Name",
                                "Sport",
                                "# of Years",
                                "Vegetarian"};

        Object[][] data = {
	    {"Kathy", "Smith",
	     "Snowboarding", Integer.valueOf(5), false},
	    {"John", "Doe",
	     "Rowing", Integer.valueOf(3), true},
	    {"Sue", "Black",
	     "Knitting", Integer.valueOf(2), false},
	    {"Jane", "White",
	     "Speed reading", Integer.valueOf(20), true},
	    {"Joe", "Brown",
	     "Pool", Integer.valueOf(10), false}
        };


        // !!!!! To get the old value as well override `DefaultTableModel`
        var tm = new DefaultTableModel(data, columnNames) {
            public void setValueAt(Object value, int row, int col) {
                System.out.println("oldValue " + this.getValueAt(row, col));
                super.setValueAt(value, row, col);
            }

        };
        final JTable table = new JTable(tm);
        table.getModel().addTableModelListener(this);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        // Set preferred widths
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 2) {
                column.setPreferredWidth(100); //third column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //The scroll pane automatically places the table header at the top of the viewport.
        /*
        
        If you are using a table without a scroll pane, then you must get the table header component 
        and place it yourself. For example:
            container.setLayout(new BorderLayout());
            container.add(table.getTableHeader(), BorderLayout.PAGE_START);
            container.add(table, BorderLayout.CENTER);
        */

        add(scrollPane);
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SimpleTableDemo2 newContentPane = new SimpleTableDemo2();
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
                System.out.println("createAndShowGUI");
                createAndShowGUI();
            }
        });
    }
}