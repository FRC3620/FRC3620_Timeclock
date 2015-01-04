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
public class WorksessionEditForm extends javax.swing.JDialog {

    Logger logger = LoggerFactory.getLogger(getClass());

    static final String TIME_FORMAT = "h:mm:ss a";
    final SimpleDateFormat sdt = new SimpleDateFormat(TIME_FORMAT);
    final SimpleDateFormat dsdt = new SimpleDateFormat("EEEE MMMM dd, yyyy");

    Date originalStartTime, originalEndTime, originalStartTime0, originalEndTime0;
    SpinnerDateModel startTimeModel, endTimeModel;
    boolean okHit = false;

    /**
     * Creates new form NewJDialog
     *
     * @param parent
     * @param modal
     */
    public WorksessionEditForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();

        startTimeModel = (SpinnerDateModel) startSpinner.getModel();
        endTimeModel = (SpinnerDateModel) endSpinner.getModel();

        fixupDateEditor(startSpinner);
        fixupDateEditor(endSpinner);
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
        endTimeModel.setStart(min);
        startTimeModel.setEnd(max);
        endTimeModel.setEnd(max);

    }

    public void setPersonTitle(String s) {
        titleLabel.setText(s);
    }

    void setPreviousStartTime(Date d) {
        String s;
        synchronized (sdt) {
            s = sdt.format(d);
        }
        previousStartTimeLabel.setText(s);
    }

    void setPreviousEndTime(Date d) {
        String s = "";
        if (null != d) {
            synchronized (sdt) {
                s = sdt.format(d);
            }
        }
        previousEndTimeLabel.setText(s);
    }

    public boolean showDialog(Date s, Date e) {
        originalStartTime = Utils.dropFractionalSeconds(s);
        originalEndTime = Utils.dropFractionalSeconds(e);
       
        synchronized (dsdt) {
            dateLabel.setText (dsdt.format(s));
        }

        originalStartTime0 = Utils.getTimeOfDay(originalStartTime);
        logger.debug("set start time {} -> {}", s, originalStartTime0);
        startTimeModel.setValue(originalStartTime0);
        setPreviousStartTime(originalStartTime0);

        originalEndTime0 = Utils.getTimeOfDay(originalEndTime);
        logger.debug("set end time {} -> {}", e, originalEndTime0);
        if (null != originalEndTime0) {
            endTimeModel.setValue(originalEndTime0);
        } else {
            endTimeModel.setValue(originalStartTime0);
        }
        setPreviousEndTime(originalEndTime0);

        endDateIsNullCheckbox.setSelected(null == originalEndTime);
        updateEndSpinnerEnabled();

        resetStartSpinnerLimit("resetStart@setup");
        resetEndSpinnerLimit("resetEnd@setup");

        okHit = false;
        setVisible(true);
        return okHit;
    }

    void resetStartSpinnerLimit(String s) {
        logger.debug("touched startTimeModel.setEnd");
        startTimeModel.setEnd((Date) endSpinner.getValue());
        logSpinners(s);
    }

    void resetEndSpinnerLimit(String s) {
        logger.debug("touched endTimeModel.setStart");
        endTimeModel.setStart((Date) startSpinner.getValue());
        logSpinners(s);
    }

    void logSpinners(String s) {
        logger.info("logging spinners: {}", s);
        logger.info("start spinner {} {} {} {}", diagDate(startTimeModel.getStart()), diagDate(startTimeModel.getDate()), diagDate(startSpinner.getValue()), diagDate(startTimeModel.getEnd()));
        logger.info("  end spinner {} {} {} {}", diagDate(endTimeModel.getStart()), diagDate(endTimeModel.getDate()), diagDate(endSpinner.getValue()), diagDate(endTimeModel.getEnd()));
    }

    public boolean isStartTimeChanged() {
        return !originalStartTime0.equals(startTimeModel.getDate());
    }

    public boolean isEndTimeChanged() {
        if (null == originalEndTime0) {
            return !endDateIsNullCheckbox.isSelected();
        } else {
            if (endDateIsNullCheckbox.isSelected()) {
                return true;
            } else {
                return !originalEndTime0.equals(endTimeModel.getDate());
            }
        }
    }

    public Date getStartTime() {
        Date rv = Utils.makeCompositeDate(originalStartTime, startTimeModel.getDate());
        logger.info("getStartTime = {}", rv);
        return rv;
    }

    public Date getEndTime() {
        Date rv = null;
        if (!endDateIsNullCheckbox.isSelected()) {
            Date d = (null == originalEndTime) ? originalStartTime : originalEndTime;
            rv = Utils.makeCompositeDate(d, endTimeModel.getDate());
        }
        logger.info("getEndTime = {}", rv);
        return rv;
    }

    void updateEndSpinnerEnabled() {
        endSpinner.setEnabled(!endDateIsNullCheckbox.isSelected());
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
        dateLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        startSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        previousStartTimeLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        endSpinner = new javax.swing.JSpinner();
        endDateIsNullCheckbox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        previousEndTimeLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Edit Worksession");

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Title");

        java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
        jPanel1Layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        jPanel1Layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel1.setLayout(jPanel1Layout);

        dateLabel.setText("dateLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        jPanel1.add(dateLabel, gridBagConstraints);

        jLabel1.setText("Start Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel1, gridBagConstraints);

        startSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.SECOND));
        startSpinner.setEditor(new javax.swing.JSpinner.DateEditor(startSpinner, "h:mm:ss a"));
        startSpinner.setMinimumSize(new java.awt.Dimension(100, 30));
        startSpinner.setPreferredSize(new java.awt.Dimension(100, 30));
        startSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel1.add(startSpinner, gridBagConstraints);

        jLabel2.setText("Previous Start Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel2, gridBagConstraints);

        previousStartTimeLabel.setText("s");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(previousStartTimeLabel, gridBagConstraints);

        jLabel3.setText("End Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel3, gridBagConstraints);

        endSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.SECOND));
        endSpinner.setEditor(new javax.swing.JSpinner.DateEditor(endSpinner, "h:mm:ss a"));
        endSpinner.setMinimumSize(new java.awt.Dimension(100, 30));
        endSpinner.setPreferredSize(new java.awt.Dimension(100, 30));
        endSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        jPanel1.add(endSpinner, gridBagConstraints);

        endDateIsNullCheckbox.setText("blank");
        endDateIsNullCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDateIsNullCheckboxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(endDateIsNullCheckbox, gridBagConstraints);

        jLabel4.setText("Previous End Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel4, gridBagConstraints);

        previousEndTimeLabel.setText("e");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(previousEndTimeLabel, gridBagConstraints);

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
        Date s = startTimeModel.getDate();
        Date e = endTimeModel.getDate();
        if (s.after(e)) {
            JOptionPane.showMessageDialog(null, "Sorry, start time needs to be before or same as end time");
        } else {
            logger.info("date fields: {} {}", s, e);
            setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void startSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startSpinnerStateChanged
        logger.info("start spinner change event: {}", startSpinner.getValue());
        resetEndSpinnerLimit("startChanged");
    }//GEN-LAST:event_startSpinnerStateChanged

    private void endSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endSpinnerStateChanged
        logger.info("end spinner change event: {}", endSpinner.getValue());
        resetStartSpinnerLimit("endChanged");
    }//GEN-LAST:event_endSpinnerStateChanged

    private void endDateIsNullCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDateIsNullCheckboxActionPerformed
        updateEndSpinnerEnabled();
    }//GEN-LAST:event_endDateIsNullCheckboxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JCheckBox endDateIsNullCheckbox;
    private javax.swing.JSpinner endSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel previousEndTimeLabel;
    private javax.swing.JLabel previousStartTimeLabel;
    private javax.swing.JSpinner startSpinner;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
