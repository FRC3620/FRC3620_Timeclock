package org.frc3620.timeclock.app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Utils;
import org.frc3620.timeclock.Worksession;
import org.frc3620.timeclock.db.DAO;
import org.frc3620.timeclock.gui.FormEventListener;
import org.frc3620.timeclock.gui.PasswordPanel;
import org.frc3620.timeclock.gui.PersonsStatusTableModel;
import org.frc3620.timeclock.gui.TimeclockFrame;
import org.frc3620.timeclock.gui.WorksessionAddForm;
import org.frc3620.timeclock.gui.WorksessionEditForm;
import org.frc3620.timeclock.gui.WorksessionTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author wegscd
 */
@Component
public class App implements FormEventListener {

    Logger logger = LoggerFactory.getLogger(getClass());

    TimeclockFrame timeclockFrame;
    WorksessionEditForm worksessionEditForm;
    WorksessionAddForm worksessionAddForm;
    private PersonsStatusTableModel personsStatusTableModel;
    private WorksessionTableModel worksessionTableModel;

    private DAO dao;

    Person mentor = null;

    void go() {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.error("Trouble setting look and feel", ex);
        }
        //</editor-fold>

        personsStatusTableModel.reload();

        timeclockFrame = new TimeclockFrame(personsStatusTableModel, worksessionTableModel, this);
        worksessionEditForm = new WorksessionEditForm(timeclockFrame, true);
        worksessionAddForm = new WorksessionAddForm(timeclockFrame, true);

        final TimeclockFrame timeclockFrame2 = timeclockFrame;
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                timeclockFrame2.setVisible(true);
            }
        });

        String previousFormattedTime = null;
        Date previousBeginningOfDay = Utils.getStartOfDay(new Date());

        SimpleDateFormat sdt = new SimpleDateFormat("EEEE, MMMM d YYYY, hh:mm:ss a");
        while (true) {
            boolean windowClosed = timeclockFrame.isWindowClosing();
            // logger.info ("timeClockFrame closed = {}", windowClosed);
            if (windowClosed) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }

            Date now = new Date();

            final String formattedTime = sdt.format(new Date());
            if (!formattedTime.equals(previousFormattedTime)) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        timeclockFrame.setTimeText(formattedTime);
                    }
                });
                previousFormattedTime = formattedTime;
            }

            Date beginningOfDay = Utils.getStartOfDay(now);
            logger.debug("doing date compare {}, {}", beginningOfDay, previousBeginningOfDay);
            if (!beginningOfDay.equals(previousBeginningOfDay)) {
                // it's a new day, so rework the left hand pane to reset
                // all the IN/OUT markers.
                logger.info("new day, reloading!");
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // turn off mentor mode
                        clearMentorMode();
                        timeclockFrame.setMentorModeMenuSelected(false);

                        backup();

                        personsStatusTableModel.reload();
                    }
                });
                previousBeginningOfDay = beginningOfDay;
            }
        }

        logger.info("timeClockFrame closed");
        timeclockFrame.setVisible(false);
        logger.info("app.go() exiting");

    }

    @Autowired
    public void setDao(DAO dao) {
        logger.info("{} set DAO to {}", this, dao);
        this.dao = dao;
    }

    @Autowired
    public void setPersonsStatusTableModel(PersonsStatusTableModel t) {
        this.personsStatusTableModel = t;
    }

    @Autowired
    public void setWorksessionTableModel(WorksessionTableModel w) {
        this.worksessionTableModel = w;
    }

    @Override
    public void personSelected(Integer i) {
        Person person = personsStatusTableModel.getPersonAt(i);
        logger.info("selected {}: {}", i, person);
        updatePersonInfoOnScreen(person);
        updateMentorMenuItem(person);
    }

    void updateMentorMenuItem(Person person) {
        if (!timeclockFrame.isMentorMode()) {
            timeclockFrame.setMentorModeMenuItemEnabled(person.getMentor());
        }
    }

    void updatePersonStatus(Person person, Integer personIndex) {
        timeclockFrame.setStatus("refreshing status for " + person.getName());
        personsStatusTableModel.reload(person);
        personsStatusTableModel.fireTableRowsUpdated(personIndex, personIndex);
        timeclockFrame.setStatus("getting updated worksessions for " + person.getName());
        updatePersonInfoOnScreen(person);
        timeclockFrame.setStatus(null);
    }

    void updatePersonInfoOnScreen(Person person) {
        timeclockFrame.setSubstatus("fetching worksessions for " + person.getName());
        List<Worksession> worksessions = dao.fetchWorksessionsForPerson(person.getPersonId());
        timeclockFrame.setSubstatus(null);
        timeclockFrame.setPersonNameText(person.getName());
        Worksession lastWorksession = (worksessions.size() > 0) ? worksessions.get(0) : null;
        if (lastWorksession != null && lastWorksession.isToday() && lastWorksession.getEndDate() == null) {
            timeclockFrame.setCheckInButtonEnabled(false);
            timeclockFrame.setCheckOutButtonEnabled(true);
        } else {
            timeclockFrame.setCheckInButtonEnabled(true);
            timeclockFrame.setCheckOutButtonEnabled(false);
        }
        worksessionTableModel.reload(worksessions);
    }

    @Override
    public void checkin(Integer personIndex) {
        Person person = personsStatusTableModel.getPersonAt(personIndex);
        logger.info("checkin {}: {}", personIndex, person);
        dao.createWorksession(person);

        updatePersonStatus(person, personIndex);
    }

    @Override
    public void checkout(Integer personIndex) {
        Person person = personsStatusTableModel.getPersonAt(personIndex);
        logger.info("checkout {}: {}", personIndex, person);
        dao.closeWorksession(worksessionTableModel.getLastWorksession());

        updatePersonStatus(person, personIndex);
    }

    @Override
    public void editWorksession(Integer personIndex, Integer workstationIndex) {
        Person person = personsStatusTableModel.getPersonAt(personIndex);
        Worksession worksession = worksessionTableModel.getWorksessionAt(workstationIndex);
        logger.info("editing worksession @ {}: {}", workstationIndex, worksession);
        boolean okHit = worksessionEditForm.showDialog(person.getName(), worksession.getStartDate(), worksession.getEndDate());
        if (okHit) {
            if (worksessionEditForm.isStartTimeChanged()) {
                Date newStartTime = worksessionEditForm.getStartTime();
                logger.info("new start time {}", newStartTime);
                dao.updateStartTime(worksession, newStartTime, mentor);
            }
            if (worksessionEditForm.isEndTimeChanged()) {
                Date newEndTime = worksessionEditForm.getEndTime();
                logger.info("new end time {}", newEndTime);
                dao.updateEndTime(worksession, newEndTime, mentor);
            }
        }

        updatePersonStatus(person, personIndex);
    }

    @Override
    public void removeWorksession(Integer personIndex, Integer workstationIndex) {
        Person person = personsStatusTableModel.getPersonAt(personIndex);
        Worksession worksession = worksessionTableModel.getWorksessionAt(workstationIndex);
        logger.info("removing worksession @ {}: {}", workstationIndex, worksession);

        //Custom button text
        Object[] options = {"Yes",
            "No"};
        int reply = JOptionPane.showOptionDialog(timeclockFrame, "Are you sure you want to remove this worksession?",
                "Remove worksession",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[1]);
        if (0 == reply) {
            reply = JOptionPane.showOptionDialog(timeclockFrame, "Are you DARN sure you want to remove this worksession?",
                    "Remove worksession",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
        }
        if (0 == reply) {
            dao.removeWorksession(worksession, mentor);
        }

        updatePersonStatus(person, personIndex);
    }

    @Override
    public void setMentorMode(Integer personIndex) {
        mentor = personsStatusTableModel.getPersonAt(personIndex);
        logger.info("mentor mode set: {}", mentor.getName());
    }

    @Override
    public void clearMentorMode() {
        mentor = null;
        logger.info("mentor mode cleared");
    }

    @Override
    public boolean checkMentorModePassword() {
        boolean rv = false;

        final PasswordPanel pPnl = new PasswordPanel();
        String[] options = new String[]{"OK", "Cancel"};
        JOptionPane op = new JOptionPane(pPnl, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        JDialog dlg = op.createDialog(timeclockFrame, "Who Goes There?");

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
            rv = ("".equals(enteredPassword));
        }
        return rv;
    }

    @Override
    public void addWorksession(Integer personIndex) {
        Person person = personsStatusTableModel.getPersonAt(personIndex);
        boolean okHit = worksessionAddForm.showDialog(person.getName());
        if (okHit) {
            Date rv = Utils.getMidDay(worksessionAddForm.getDate());
            dao.createWorksession(person, rv);
        }

        updatePersonStatus(person, personIndex);
    }

    @Override
    public void backup() {
        logger.info("app.backup() called");
        timeclockFrame.setSubstatus("running backup");
        dao.backup();
        timeclockFrame.setSubstatus(null);
    }

}
