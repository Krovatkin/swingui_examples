import java.awt.*;
import javax.swing.*;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.*;


// A formatted text field's text and its value are two different properties, 
// and the value often lags behind the text.

// The text property is defined by the JTextField class. 
// This property always reflects what the field displays. 
// The value property, defined by the JFormattedTextField class, 
// might not reflect the latest text displayed in the field. 
// While the user is typing, the text property changes, 
// but the value property does not change until the changes are committed.

// The value of a formatted text field can be set 
// by using either the setValue method or the commitEdit method

// The commitEdit method sets the value to whatever object 
// the formatter determines is represented by the field's text.
// e.g. When the user presses Enter while the field has the focus.

// When you set the value of a formatted text field, the field's text is updated to reflect the value. 
// Exactly how the value is represented as text depends on the field's formatter.

// To obtain a formatted text field's current value, use the getValue method. If necessary, you can ensure that the value reflects 
// the text by calling the commitEdit method before getValue.

public class FormattedTextFieldDemo extends JPanel
                                    implements PropertyChangeListener {
    //Values for the fields
    private double amount = 100000;
    private double rate = 7.5;  //7.5%
    private int numPeriods = 30;

    //Labels to identify the fields
    private JLabel amountLabel;
    private JLabel rateLabel;
    private JLabel numPeriodsLabel;
    private JLabel paymentLabel;

    //Strings for the labels
    private static String amountString = "Loan Amount: ";
    private static String rateString = "APR (%): ";
    private static String numPeriodsString = "Years: ";
    private static String paymentString = "Monthly Payment: ";

    //Fields for data entry
    private JFormattedTextField amountField;
    private JFormattedTextField rateField;
    private JFormattedTextField numPeriodsField;
    private JFormattedTextField paymentField;

    // set up in `setUpFormats`
    private NumberFormat amountFormat;
    private NumberFormat percentFormat;
    private NumberFormat paymentFormat;

    public FormattedTextFieldDemo() {
        super(new BorderLayout());
        setUpFormats();
        double payment = computePayment(amount,
                                        rate,
                                        numPeriods);

        amountLabel = new JLabel(amountString);
        rateLabel = new JLabel(rateString);
        numPeriodsLabel = new JLabel(numPeriodsString);
        paymentLabel = new JLabel(paymentString);

        amountField = new JFormattedTextField(amountFormat);
        amountField.setValue(Double.valueOf(amount));
        amountField.setColumns(10);
        // !!! This is pretty cool. We can listen to changes to 
        // any propert of a component
        amountField.addPropertyChangeListener("value", this);

        rateField = new JFormattedTextField(percentFormat);
        rateField.setValue(Double.valueOf(rate));
        rateField.setColumns(10);
        rateField.addPropertyChangeListener("value", this);

        numPeriodsField = new JFormattedTextField();
        // `setValue(Integer)` forces `numPeriodsField` to use
        // the default formatter for Integer
        // There are no default formatters for `Double` 
        numPeriodsField.setValue(Integer.valueOf(numPeriods));
        numPeriodsField.setColumns(10);
        numPeriodsField.addPropertyChangeListener("value", this);

        paymentField = new JFormattedTextField(paymentFormat);
        paymentField.setValue(Double.valueOf(payment));
        paymentField.setColumns(10);
        paymentField.setEditable(false);
        paymentField.setForeground(Color.red);

        //Tell accessibility tools about label/textfield pairs.
        amountLabel.setLabelFor(amountField);
        rateLabel.setLabelFor(rateField);
        numPeriodsLabel.setLabelFor(numPeriodsField);
        paymentLabel.setLabelFor(paymentField);


        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(amountLabel);
        labelPane.add(rateLabel);
        labelPane.add(numPeriodsLabel);
        labelPane.add(paymentLabel);

        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(amountField);
        fieldPane.add(rateField);
        fieldPane.add(numPeriodsField);
        fieldPane.add(paymentField);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
    }

    /** Called when a field's "value" property changes. */
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        if (source == amountField) {
            amount = ((Number)amountField.getValue()).doubleValue();
        } else if (source == rateField) {
            rate = ((Number)rateField.getValue()).doubleValue();
        } else if (source == numPeriodsField) {
            numPeriods = ((Number)numPeriodsField.getValue()).intValue();
        }

        double payment = computePayment(amount, rate, numPeriods);
        paymentField.setValue(Double.valueOf(payment));
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FormattedTextFieldDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new FormattedTextFieldDemo());

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

    //Compute the monthly payment based on the loan amount,
    //APR, and length of loan.
    double computePayment(double loanAmt, double rate, int numPeriods) {
        double I, partial1, denominator, answer;

        numPeriods *= 12;        //get number of months
        if (rate > 0.01) {
            I = rate / 100.0 / 12.0;         //get monthly rate from annual
            partial1 = Math.pow((1 + I), (0.0 - numPeriods));
            denominator = (1 - partial1) / I;
        } else { //rate ~= 0
            denominator = numPeriods;
        }

        answer = (-1 * loanAmt) / denominator;
        return answer;
    }

    private void setUpFormats() {
        amountFormat = NumberFormat.getNumberInstance();

        percentFormat = NumberFormat.getNumberInstance();
        percentFormat.setMinimumFractionDigits(3);

        paymentFormat = NumberFormat.getCurrencyInstance();
    }
}