package org.frc3620.timeclock.gui;

import java.util.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import org.frc3620.timeclock.Person;
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
public class TimeclockStatusModel implements TableModel {

    Logger logger = LoggerFactory.getLogger(getClass());
    List<CurrentStatus> personsStatus = new ArrayList<>();

    public void reload() {
        logger.info ("{} using dao {}", this, dao);
        List<Person> persons = dao.fetchPersons();
        personsStatus.clear();
        for (Person p : persons) {
            CurrentStatus c = new CurrentStatus(p);
            personsStatus.add(c);
        }
        Collections.sort(personsStatus);
    }
    
    public Person getPersonAt(int i) {
        return personsStatus.get(i).getPerson();
    }

    @Override
    public int getRowCount() {
        return personsStatus.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Status";
            case 2:
                return "When";
            default:
                return "" + columnIndex;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Date.class;
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
        CurrentStatus s = personsStatus.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return s.getName();
            case 1:
                return s.getWhere();
            case 2:
                return s.getWhen();
            default:
                return String.format("column %d for person %s", columnIndex, s);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    DAO dao;

    @Autowired
    @Required
    public void setDao(DAO dao) {
        logger.info ("{} got dao {}", this, dao);
        this.dao = dao;
    }

    class CurrentStatus implements Comparable<CurrentStatus> {

        private Person person;
        private Where where;
        private Date when;

        public CurrentStatus(Person person) {
            this.person = person;
            this.where = Where.UNKNOWN;
            this.when = null;
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
