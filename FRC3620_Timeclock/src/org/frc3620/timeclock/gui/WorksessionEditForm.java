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

    static final String DATE_FORMAT = "hh:mm:ss a";
    final SimpleDateFormat sdt = new SimpleDateFormat(DATE_FORMAT);

    SpinnerDateModel startTimeModel, endTimeModel;
    boolean okHit = false;
    boolean settingUp = true;

    /**
     * Creates new form NewJDialog
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

    void fixupDateEditor(JSpinner e) {
        JComponent rv = e.getEditor();
        JFormattedTextField field = (JFormattedTextField) rv.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        formatter.setAllowsInvalid(false);
    }

    void resetSpinnerMaxMin() {
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

    void setStartTime(Date d) {
        Date d0 = Utils.getTimeOfDay(d);
        startTimeModel.setValue(d0);
        logger.info("set start time {} -> {}", d, d0);
        logSpinners("setStartTime");
    }

    void setEndTime(Date d) {
        Date d0 = Utils.getTimeOfDay(d);
        endTimeModel.setValue(d0);
        logger.info("set end time {} -> {}", d, d0);
        logSpinners("setEndTime");
    }

    public boolean showDialog(Date s, Date e) {
        s = Utils.dropFractionalSeconds(s);
        if (null != e) e = Utils.dropFractionalSeconds(e);
        settingUp = true;
        // resetSpinnerMaxMin();
        setStartTime(s);
        setPreviousStartTime(s);
        setEndTime((null != e) ? e : s);
        setPreviousEndTime(e);
        endDateIsNullCheckbox.setSelected(null == e);
        updateEndSpinnerEnabled();
        settingUp = false;
        resetStartSpinnerLimit("resetStart@setup");
        resetEndSpinnerLimit("resetEnd@setup");

        okHit = false;
        setVisible(true);
        return okHit;
    }

    void resetStartSpinnerLimit(String s) {
        if (!settingUp) {
            logger.info("touched startTimeModel.setEnd");
            startTimeModel.setEnd((Date) endSpinner.getValue());
            logSpinners(s);
        }
    }

    void resetEndSpinnerLimit(String s) {
        if (!settingUp) {
            logger.info("touched endTimeModel.setStart");
            endTimeModel.setStart((Date) startSpinner.getValue());
            logSpinners(s);
        }
    }

    void logSpinners(String s) {
        logger.info("logging spinners: {}", s);
        logger.info("start spinner {} {} {} {}", diagDate(startTimeModel.getStart()), diagDate(startTimeModel.getDate()), diagDate(startSpinner.getValue()), diagDate(startTimeModel.getEnd()));
        logger.info("  end spinner {} {} {} {}", diagDate(endTimeModel.getStart()), diagDate(endTimeModel.getDate()), diagDate(endSpinner.getValue()), diagDate(endTimeModel.getEnd()));
    }

    public Date getStartTime() {
        return startTimeModel.getDate();
    }

    public Date getEndTime() {
        return endTimeModel.getDate();
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

        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        startSpinner = new javax.swing.JSpinner();
        endSpinner = new javax.swing.JSpinner();
        previousStartTimeLabel = new javax.swing.JLabel();
        previousEndTimeLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        endDateIsNullCheckbox = new javax.swing.JCheckBox();

        jButton3.setText("jButton3");

        setTitle("Edit Worksession");

        jLabel1.setText("Start Time");

        jLabel2.setText("Previous Start Time");

        jLabel3.setText("End Time");

        jLabel4.setText("Previous End Time");

        startSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.SECOND));
        startSpinner.setEditor(new javax.swing.JSpinner.DateEditor(startSpinner, "h:mm:ss a"));
        startSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startSpinnerStateChanged(evt);
            }
        });

        endSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.SECOND));
        endSpinner.setEditor(new javax.swing.JSpinner.DateEditor(endSpinner, "h:mm:ss a"));
        endSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endSpinnerStateChanged(evt);
            }
        });

        previousStartTimeLabel.setText("s");

        previousEndTimeLabel.setText("e");

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Title");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        endDateIsNullCheckbox.setText("blank");
        endDateIsNullCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDateIsNullCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(previousStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startSpinner)
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(previousEndTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(endSpinner))
                                .addGap(5, 5, 5)
                                .addComponent(endDateIsNullCheckbox))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(okButton)
                                .addGap(18, 18, 18)
                                .addComponent(cancelButton)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(startSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(previousStartTimeLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(endSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(endDateIsNullCheckbox)))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(previousEndTimeLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        okHit = true;
        Date s = startTimeModel.getDate();
        Date e = endTimeModel.getDate();
        if (!s.before(e)) {
            JOptionPane.showMessageDialog(null, "Sorry, start time needs to be before end time");
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
    private javax.swing.JCheckBox endDateIsNullCheckbox;
    private javax.swing.JSpinner endSpinner;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel previousEndTimeLabel;
    private javax.swing.JLabel previousStartTimeLabel;
    private javax.swing.JSpinner startSpinner;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
