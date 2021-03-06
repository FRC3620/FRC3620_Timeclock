package org.frc3620.timeclock.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.frc3620.timeclock.app.AppSettingsAndResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wegscd
 */
public class TimeclockFrame extends javax.swing.JFrame {

    Logger logger = LoggerFactory.getLogger(getClass());
    FormEventListener formEventListener;

    /**
     * Creates new form TimeClockFrame
     *
     * @param _personsStatusTableModel
     * @param _worksessionTableModel
     * @param _formEventListener
     */
    public TimeclockFrame(PersonsStatusTableModel _personsStatusTableModel, WorksessionTableModel _worksessionTableModel, FormEventListener _formEventListener) {
        this.formEventListener = _formEventListener;
        this.personsTableModel = _personsStatusTableModel;
        personsTableColumnModel = _personsStatusTableModel.getTableColumnModel();
        this.worksessionTableModel = _worksessionTableModel;
        worksessionTableColumnModel = _worksessionTableModel.getTableColumnModel();

        setIconImages(AppSettingsAndResources.getInstance().getIcons());

        initComponents();

        personsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                logger.debug("person table selection event = {}", e);
                if (e.getValueIsAdjusting()) {
                    Integer selection = personsTable.getSelectionModel().getLeadSelectionIndex();
                    formEventListener.personSelected(selection);
                }
            }
        }
        );

        worksessionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                logger.debug("mouseReleasedEvent {}", e);
                worksessionPopup(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                logger.debug("mousePressedEvent {}", e);
                worksessionPopup(e);
            }

            void worksessionPopup(MouseEvent e) {
                if (!isMentorMode()) {
                    return;
                }
                int r = worksessionTable.rowAtPoint(e.getPoint());
                if (r >= 0 && r < worksessionTable.getRowCount()) {
                    worksessionTable.setRowSelectionInterval(r, r);
                } else {
                    worksessionTable.clearSelection();
                }

                int rowindex = worksessionTable.getSelectedRow();
                if (rowindex < 0) {
                    return;
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    worksessionTablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

        jMenuBar1.add(mentorModeMenuItem);

        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    TableModel personsTableModel = null;
    TableColumnModel personsTableColumnModel = null;
    TableModel worksessionTableModel = null;
    TableColumnModel worksessionTableColumnModel = null;

    MouseAdapter worksessionTableMouseAdapter = null;

    public void setCheckInButtonEnabled(boolean b) {
        checkinButton.setEnabled(b);
        logger.debug("checkinButtonEnabled: {}", b);
    }

    public void setCheckOutButtonEnabled(boolean b) {
        checkoutButton.setEnabled(b);
        logger.debug("checkOutButtonEnabled: {}", b);
    }

    public void setTimeText(String s) {
        currentTime.setText(s);
    }

    public void setPersonNameText(String s) {
        personNameLabel.setText(s);
    }

    public boolean isMentorMode() {
        return mentorModeMenuItem.isSelected();
    }

    public void setMentorModeMenuItemEnabled(boolean b) {
        mentorModeMenuItem.setEnabled(b);
    }

    final SimpleDateFormat statusSdf = new SimpleDateFormat("EEEE MMM dd, yyyy hh:mm:ss a");

    public void setStatus(String s) {
        if ("".equals(s) || null == s) {
            statusLabel.setText("");
        } else {
            statusLabel.setText(statusSdf.format(new Date()) + " " + s);
        }
        statusLabel.paintImmediately(statusLabel.getVisibleRect());
    }

    public void setSubstatus(String s) {
        if ("".equals(s) || null == s) {
            substatusLabel.setText("");
        } else {
            substatusLabel.setText(statusSdf.format(new Date()) + " " + s);
        }
        substatusLabel.paintImmediately(substatusLabel.getVisibleRect());
    }

    private boolean windowClosing = false;

    public boolean isWindowClosing() {
        return windowClosing;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        worksessionTablePopupMenu = new javax.swing.JPopupMenu();
        worksessionEditMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        worksessionAddMenuItem = new javax.swing.JMenuItem();
        worksessionRemoveMenuItem = new javax.swing.JMenuItem();
        mentorModeMenuItem = new javax.swing.JCheckBoxMenuItem();
        currentTime = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        personsTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        personNameLabel = new javax.swing.JLabel();
        checkinButton = new javax.swing.JButton();
        checkoutButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        worksessionTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        substatusLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        reportMenu = new javax.swing.JMenu();
        createCsvMenuItem = new javax.swing.JMenuItem();
        maintenanceMenu = new javax.swing.JMenu();
        backupMenuItem = new javax.swing.JMenuItem();

        worksessionTablePopupMenu.setLabel("Worksession Context Menu");

        worksessionEditMenuItem.setText("Edit Worksession");
        worksessionEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                worksessionEditMenuItemActionPerformed(evt);
            }
        });
        worksessionTablePopupMenu.add(worksessionEditMenuItem);
        worksessionTablePopupMenu.add(jSeparator1);

        worksessionAddMenuItem.setText("Add worksession");
        worksessionAddMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                worksessionAddMenuItemActionPerformed(evt);
            }
        });
        worksessionTablePopupMenu.add(worksessionAddMenuItem);

        worksessionRemoveMenuItem.setText("Remove worksession");
        worksessionRemoveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                worksessionRemoveMenuItemActionPerformed(evt);
            }
        });
        worksessionTablePopupMenu.add(worksessionRemoveMenuItem);

        mentorModeMenuItem.setText("Mentor Mode");
        mentorModeMenuItem.setEnabled(false);
        mentorModeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentorModeMenuItemActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Average Joes 3620 Timeclock");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        currentTime.setFont(new java.awt.Font("Courier New", 0, 24)); // NOI18N
        currentTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTime.setMaximumSize(new java.awt.Dimension(14, 28));
        currentTime.setMinimumSize(new java.awt.Dimension(14, 28));
        currentTime.setPreferredSize(new java.awt.Dimension(14, 28));

        personsTable.setModel(personsTableModel);
        jScrollPane1.setViewportView(personsTable);
        personsTable.setColumnModel(personsTableColumnModel);

        personNameLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        personNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        personNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        checkinButton.setText("Check In");
        checkinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkinButtonActionPerformed(evt);
            }
        });

        checkoutButton.setText("Check Out");
        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkoutButtonActionPerformed(evt);
            }
        });

        worksessionTable.setModel(worksessionTableModel);
        jScrollPane2.setViewportView(worksessionTable);
        worksessionTable.setColumnModel(worksessionTableColumnModel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(personNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(checkinButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(personNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkinButton)
                    .addComponent(checkoutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 24));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 24));

        statusLabel.setText(" ");
        jPanel3.add(statusLabel);
        jPanel3.add(substatusLabel);

        reportMenu.setText("Reports");

        createCsvMenuItem.setText("Create CSV file");
        createCsvMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createCsvMenuItemActionPerformed(evt);
            }
        });
        reportMenu.add(createCsvMenuItem);

        jMenuBar1.add(reportMenu);

        maintenanceMenu.setText("Maintenance");
        maintenanceMenu.setEnabled(false);

        backupMenuItem.setText("Backup");
        backupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupMenuItemActionPerformed(evt);
            }
        });
        maintenanceMenu.add(backupMenuItem);

        jMenuBar1.add(maintenanceMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(currentTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(currentTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkinButtonActionPerformed
        logger.debug("checkin event = {}", evt);
        int i = personsTable.getSelectionModel().getLeadSelectionIndex();
        if (i >= 0) {
            formEventListener.checkin(i);
        }
    }//GEN-LAST:event_checkinButtonActionPerformed

    private void checkoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutButtonActionPerformed
        logger.debug("checkout event = {}", evt);
        int i = personsTable.getSelectionModel().getLeadSelectionIndex();
        if (i >= 0) {
            formEventListener.checkout(i);
        }
    }//GEN-LAST:event_checkoutButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        logger.debug("window closing event fired");
        windowClosing = true;
    }//GEN-LAST:event_formWindowClosing
    private void worksessionEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_worksessionEditMenuItemActionPerformed
        int personIndex = personsTable.getSelectionModel().getLeadSelectionIndex();
        int workstationIndex = worksessionTable.getSelectionModel().getLeadSelectionIndex();
        formEventListener.editWorksession(personIndex, workstationIndex);
    }//GEN-LAST:event_worksessionEditMenuItemActionPerformed

    private void mentorModeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mentorModeMenuItemActionPerformed
        logger.debug("mentorModeMenuItem, selected {}, evt {}", mentorModeMenuItem.isSelected(), evt);
        if (mentorModeMenuItem.isSelected()) {
            mentorModeMenuItem.setSelected(false); // turn off checkbox
            boolean okToSet = formEventListener.checkMentorModePassword();

            if (okToSet) {
                mentorModeMenuItem.setSelected(true);
                formEventListener.setMentorMode(personsTable.getSelectionModel().getLeadSelectionIndex());
            }
        } else {
            formEventListener.clearMentorMode();
        }
        updateMenuStatusFromMentorMode();
    }//GEN-LAST:event_mentorModeMenuItemActionPerformed

    public void setMentorModeMenuSelected(boolean b) {
        mentorModeMenuItem.setSelected(b);
        updateMenuStatusFromMentorMode();
    }

    public void updateMenuStatusFromMentorMode() {
        boolean b = mentorModeMenuItem.isSelected();
        logger.info("updateMenuStatusFromMentorMode: {}", b);
        if (b) {
            mentorModeMenuItem.setForeground(Color.red);
            maintenanceMenu.setEnabled(true);
        } else {
            mentorModeMenuItem.setForeground(Color.black);
            maintenanceMenu.setEnabled(false);
        }
    }

    private void worksessionRemoveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_worksessionRemoveMenuItemActionPerformed
        int personIndex = personsTable.getSelectionModel().getLeadSelectionIndex();
        int workstationIndex = worksessionTable.getSelectionModel().getLeadSelectionIndex();
        formEventListener.removeWorksession(personIndex, workstationIndex);
    }//GEN-LAST:event_worksessionRemoveMenuItemActionPerformed

    private void worksessionAddMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_worksessionAddMenuItemActionPerformed
        int personIndex = personsTable.getSelectionModel().getLeadSelectionIndex();
        formEventListener.addWorksession(personIndex);
    }//GEN-LAST:event_worksessionAddMenuItemActionPerformed

    private void backupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupMenuItemActionPerformed
        formEventListener.backup();
    }//GEN-LAST:event_backupMenuItemActionPerformed

    private void createCsvMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createCsvMenuItemActionPerformed
        formEventListener.runCsvReport();
    }//GEN-LAST:event_createCsvMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem backupMenuItem;
    private javax.swing.JButton checkinButton;
    private javax.swing.JButton checkoutButton;
    private javax.swing.JMenuItem createCsvMenuItem;
    private javax.swing.JLabel currentTime;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenu maintenanceMenu;
    private javax.swing.JCheckBoxMenuItem mentorModeMenuItem;
    private javax.swing.JLabel personNameLabel;
    private javax.swing.JTable personsTable;
    private javax.swing.JMenu reportMenu;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel substatusLabel;
    private javax.swing.JMenuItem worksessionAddMenuItem;
    private javax.swing.JMenuItem worksessionEditMenuItem;
    private javax.swing.JMenuItem worksessionRemoveMenuItem;
    private javax.swing.JTable worksessionTable;
    private javax.swing.JPopupMenu worksessionTablePopupMenu;
    // End of variables declaration//GEN-END:variables
}
