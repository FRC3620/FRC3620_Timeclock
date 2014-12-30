package org.frc3620.timeclock.gui;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Utils;
import org.frc3620.timeclock.Worksession;
import org.frc3620.timeclock.db.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

/**
 *
 * @author wegscd
 */
@Component
public class PersonsStatusTableModel extends AbstractTableModel {

    Logger logger = LoggerFactory.getLogger(getClass());
    List<CurrentStatus> personsStatus = new ArrayList<>();

    SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");

    public TableColumnModel getTableColumnModel() {
        TableColumnModel rv = new DefaultTableColumnModel();
        rv.addColumn(new TableColumn(0, 200));
        rv.getColumn(0).setHeaderValue("Name");
        rv.addColumn(new TableColumn(1, 80));
        rv.getColumn(1).setHeaderValue("Status");
        rv.addColumn(new TableColumn(2, 80));
        rv.getColumn(2).setHeaderValue("When");
        DefaultTableCellRenderer rRenderer = new DefaultTableCellRenderer();
        rRenderer.setHorizontalAlignment(JLabel.CENTER);
        rv.getColumn(1).setCellRenderer(rRenderer);
        rv.getColumn(2).setCellRenderer(new HHMMRenderer());
        return rv;
    }

    public void reload() {
        logger.info("{} using dao {}", this, dao);
        List<Person> persons = dao.fetchPersons();
        personsStatus.clear();
        for (Person p : persons) {
            CurrentStatus c = new CurrentStatus(p);
            personsStatus.add(c);
        }
        
        Collections.sort(personsStatus);
    }
    
    public void reload (Person p) {
        for (int i = 0; i < personsStatus.size(); i++) {
            if (personsStatus.get(i).getPerson().getPersonId().equals(p.getPersonId())) {
                Person p1 = dao.fetchPerson(p.getPersonId());
                CurrentStatus c = new CurrentStatus(p1);
                personsStatus.set(i, c);
                return;
            }
        }
        logger.error ("could not find person {} in model", p);
    }

    public Person getPersonAt(int i) {
        return personsStatus.get(i).getPerson();
    }

    @Override
    public int getRowCount() {
        // logger.info("personstatus = {}", personsStatus);
        return personsStatus.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o = null;
        CurrentStatus s = personsStatus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                o = s.getName();
                break;
            case 1:
                o = s.getWhere();
                break;
            case 2:
                if (s.getWhen() == null) {
                    o = "";
                } else {
                    synchronized (sdt) {
                        o = sdt.format(s.getWhen());
                    }
                }
                break;
            default:
                o = String.format("column %d for person %s", columnIndex, s);
        }
        logger.info("Data for {} {}: {}", rowIndex, columnIndex, o);
        return o;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DAO dao;

    @Autowired
    @Required
    public void setDao(DAO dao) {
        logger.info("{} got dao {}", this, dao);
        this.dao = dao;
    }

    class CurrentStatus implements Comparable<CurrentStatus> {

        private Person person;
        private Where where;
        private Date when;

        public CurrentStatus(Person person) {
            this.person = person;

            Worksession worksession = dao.fetchLastWorksessionForPerson(person);
            Date beginningOfToday = Utils.getStartOfDay(new Date());
            Date beginningOfLastSession = (worksession == null) ? null : worksession.getStartDate();
            logger.info("person: {}, today: {}, last session start: {}", person.getPersonId(), beginningOfToday, beginningOfLastSession);
            if (worksession != null && worksession.isToday()) {
                if (worksession.getEndDate() == null) {
                    logger.info("in");
                    this.where = Where.IN;
                    this.when = worksession.getStartDate();
                } else {
                    logger.info("out");
                    this.where = Where.OUT;
                    this.when = worksession.getEndDate();
                }
            } else {
                // last worksession does not exist or was a previous day
                logger.info("unknown");
                this.where = Where.UNKNOWN;
                this.when = null;
            }
        }

        public Person getPerson() {
            return person;
        }

        public String getName() {
            return person.getLastname() + ", " + person.getFirstname();
        }

        public Where getWhere() {
            return where;
        }

        public void setWhere(Where where) {
            this.where = where;
        }

        public Date getWhen() {
            return when;
        }

        public void setWhen(Date when) {
            this.when = when;
        }

        @Override
        public int compareTo(CurrentStatus o) {
            return getName().compareToIgnoreCase(o.getName());
        }

    }

    enum Where {

        UNKNOWN(), IN(), OUT();
    }
    
}
