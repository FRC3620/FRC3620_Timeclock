package org.frc3620.timeclock.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
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
        // setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
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

    private boolean windowClosing = false;

    public boolean isWindowClosing() {
        return windowClosing;
    }

    class PasswordPanel extends JPanel {

        private final JPasswordField passwordField = new JPasswordField(12);
        private boolean gainedFocusBefore;

        /**
         * "Hook" method that causes the JPasswordField to request focus the
         * first time this method is called.
         */
        void gainedFocus() {
            if (!gainedFocusBefore) {
                gainedFocusBefore = true;
                passwordField.requestFocusInWindow();
            }
        }

        public PasswordPanel() {
            super(new FlowLayout());

            add(new JLabel("Password: "));
            add(passwordField);
        }

        public char[] getPassword() {
            return passwordField.getPassword();
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

        worksessionTablePopupMenu = new javax.swing.JPopupMenu();
        worksessionEditMenuItem = new javax.swing.JMenuItem();
        mentorModeMenuItem = new javax.swing.JCheckBoxMenuItem();
        currentTime = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        personsTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        personNameLabel = new javax.swing.JLabel();
        checkinButton = new javax.swing.JButton();
        checkoutButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        worksessionTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();

        worksessionTablePopupMenu.setLabel("Worksession Context Menu");

        worksessionEditMenuItem.setText("Edit Worksession");
        worksessionEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                worksessionEditMenuItemActionPerformed(evt);
            }
        });
        worksessionTablePopupMenu.add(worksessionEditMenuItem);

        mentorModeMenuItem.setText("Mentor Mode");
        mentorModeMenuItem.setEnabled(false);
        mentorModeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentorModeMenuItemActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
                .addComponent(checkinButton, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(currentTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(currentTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
            final PasswordPanel pPnl = new PasswordPanel();
            String[] options = new String[]{"OK", "Cancel"};
            JOptionPane op = new JOptionPane(pPnl, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            JDialog dlg = op.createDialog("Who Goes There?");

            // Wire up FocusListener to ensure JPasswordField is able to request focus when the dialog is first shown.
            dlg.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    pPnl.gainedFocus();
                }
            });

            dlg.setVisible(true);
            logger.debug("dialog getValue = {} from {}", op.getValue(), op);
            if (op.getValue() != null && op.getValue().equals(options[0])) {
                logger.debug("hit ok");
                String enteredPassword = new String(pPnl.getPassword());
                logger.debug("password = {}", enteredPassword);
                if (!"".equals(enteredPassword)) {
                    logger.debug("bad password");
                    mentorModeMenuItem.setSelected(false);
                    formEventListener.mentorMode(null);
                } else {
                    logger.debug("good password");
                    formEventListener.mentorMode(personsTable.getSelectionModel().getLeadSelectionIndex());
                }
            } else {
                logger.debug("did not hit ok");
                formEventListener.mentorMode(null);
            }
        }
        if (mentorModeMenuItem.isSelected()) {
            mentorModeMenuItem.setForeground(Color.red);
        } else {
            mentorModeMenuItem.setForeground(Color.black);
        }
    }//GEN-LAST:event_mentorModeMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkinButton;
    private javax.swing.JButton checkoutButton;
    private javax.swing.JLabel currentTime;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBoxMenuItem mentorModeMenuItem;
    private javax.swing.JLabel personNameLabel;
    private javax.swing.JTable personsTable;
    private javax.swing.JMenuItem worksessionEditMenuItem;
    private javax.swing.JTable worksessionTable;
    private javax.swing.JPopupMenu worksessionTablePopupMenu;
    // End of variables declaration//GEN-END:variables
}
