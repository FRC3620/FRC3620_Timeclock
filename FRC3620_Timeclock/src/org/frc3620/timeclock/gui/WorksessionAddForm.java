package org.frc3620.timeclock.gui;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import org.frc3620.timeclock.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wegscd
 */
public class WorksessionAddForm extends javax.swing.JDialog {

    Logger logger = LoggerFactory.getLogger(getClass());

    static final String TIME_FORMAT = "h:mm:ss a";
    final SimpleDateFormat sdt = new SimpleDateFormat(TIME_FORMAT);
    final SimpleDateFormat dsdt = new SimpleDateFormat("EEEE MMMM dd, yyyy");


    SpinnerDateModel startTimeModel;
    boolean okHit = false;

    /**
     * Creates new form NewJDialog
     *
     * @param parent
     * @param modal
     */
    public WorksessionAddForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();

        startTimeModel = (SpinnerDateModel) startSpinner.getModel();

        fixupDateEditor(startSpinner);
        resetSpinnerMaxMin();
    }

    private void fixupDateEditor(JSpinner e) {
        JComponent rv = e.getEditor();
        JFormattedTextField field = (JFormattedTextField) rv.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        formatter.setAllowsInvalid(false);
    }

    private void resetSpinnerMaxMin() {
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.clear();
        cal1.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        Date min = cal1.getTime();

        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.clear();
        cal2.set(1970, Calendar.JANUARY, 1, 23, 59, 59);
        cal2.set(Calendar.MILLISECOND, 999);
        Date max = cal2.getTime();

        startTimeModel.setStart(min);
        startTimeModel.setEnd(max);

    }

    public void setPersonTitle(String s) {
        titleLabel.setText(s);
    }

    public boolean showDialog() {
        resetEndSpinnerLimit("resetEnd@setup");

        okHit = false;
        setVisible(true);
        return okHit;
    }

    void resetEndSpinnerLimit(String s) {
        logger.debug("touched endTimeModel.setStart");
        // endTimeModel.setStart((Date) startSpinner.getValue());
        logSpinners(s);
    }

    void logSpinners(String s) {
        logger.info("logging spinners: {}", s);
        logger.info("start spinner {} {} {} {}", diagDate(startTimeModel.getStart()), diagDate(startTimeModel.getDate()), diagDate(startSpinner.getValue()), diagDate(startTimeModel.getEnd()));
    }

    public Date getStartTime() {
        Date rv = startTimeModel.getDate();
        logger.info("getStartTime = {}", rv);
        return rv;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");

    String diagDate(Object o) {
        if (null == o) {
            return "(null)";
        } else {
            if (o instanceof Date) {
                return sdf.format(o);
            } else {
                return o.toString();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titleLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        startSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Edit Worksession");

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Title");

        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0, 5, 0};
        jPanel1Layout.rowHeights = new int[] {0};
        jPanel1.setLayout(jPanel1Layout);

        jLabel1.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel1, gridBagConstraints);

        startSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.DAY_OF_YEAR));
        startSpinner.setEditor(new javax.swing.JSpinner.DateEditor(startSpinner, "EEEE MMMM dd, YYYY"));
        startSpinner.setMinimumSize(new java.awt.Dimension(200, 30));
        startSpinner.setPreferredSize(new java.awt.Dimension(200, 30));
        startSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel1.add(startSpinner, gridBagConstraints);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel2.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel2.add(cancelButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titleLabel)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        okHit = true;
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void startSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startSpinnerStateChanged
        logger.info("start spinner change event: {}", startSpinner.getValue());
        resetEndSpinnerLimit("startChanged");
    }//GEN-LAST:event_startSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner startSpinner;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
