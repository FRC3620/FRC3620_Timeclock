package org.frc3620.timeclock.gui;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.*;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Utils;
import org.frc3620.timeclock.Worksession;
import org.frc3620.timeclock.db.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author wegscd
 */
@org.springframework.stereotype.Component
public class PersonsStatusTableModel extends AbstractTableModel {

    Logger logger = LoggerFactory.getLogger(getClass());
    List<CurrentStatus> personsStatus = new ArrayList<>();

    final SimpleDateFormat sdt = new SimpleDateFormat("hh:mm:ss a");

    public TableColumnModel getTableColumnModel() {
        TableColumnModel rv = new DefaultTableColumnModel();
        rv.addColumn(new TableColumn(0, 180));
        rv.getColumn(0).setHeaderValue("Name");
        rv.addColumn(new TableColumn(1, 80));
        rv.getColumn(1).setHeaderValue("Status");
        rv.addColumn(new TableColumn(2, 80));
        rv.getColumn(2).setHeaderValue("When");

        rv.getColumn(0).setCellRenderer(new MyTableCellRenderer(0, JLabel.LEFT));
        rv.getColumn(1).setCellRenderer(new MyTableCellRenderer(1, JLabel.CENTER));
        rv.getColumn(2).setCellRenderer(new MyTableCellRenderer(2, JLabel.CENTER));

        //DefaultTableCellRenderer rRenderer = new DefaultTableCellRenderer();
        //rRenderer.setHorizontalAlignment(JLabel.CENTER);
        //rv.getColumn(1).setCellRenderer(rRenderer);
        //rv.getColumn(2).setCellRenderer(rRenderer);
        return rv;
    }

    class MyTableCellRenderer extends DefaultTableCellRenderer {

        Integer column;

        public MyTableCellRenderer(Integer _column, int alignment) {
            super();
            column = _column;
            setHorizontalAlignment(alignment);
        }

        @Override
        protected void setValue(Object value) {
            Object v = value;
            if (value instanceof CurrentStatus) {
                CurrentStatus s = (CurrentStatus) value;
                switch (column) {
                    case 0:
                        v = s.getName();
                        break;
                    case 1:
                        v = s.getWhere().getLabel();
                        break;
                    case 2:
                        if (s.getWhen() == null) {
                            v = "";
                        } else {
                            synchronized (sdt) {
                                v = sdt.format(s.getWhen());
                            }
                        }
                        break;
                }
            }
            super.setValue(v);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component renderer
                    = super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);

            if (value instanceof CurrentStatus) {
                CurrentStatus c = (CurrentStatus) value;
                if (c.getWhere() == Where.IN) {
                    setForeground(isSelected ? Color.PINK : Color.RED);
                } else {
                    setForeground(isSelected ? Color.WHITE : Color.BLACK);
                }
            } else {
                    setForeground(isSelected ? Color.WHITE : Color.BLACK);
            }
            return renderer;
        }
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

    public void reload(Person p) {
        for (int i = 0; i < personsStatus.size(); i++) {
            if (personsStatus.get(i).getPerson().getPersonId().equals(p.getPersonId())) {
                // Person p1 = dao.fetchPerson(p.getPersonId());
                CurrentStatus c = new CurrentStatus(p);
                personsStatus.set(i, c);
                return;
            }
        }
        logger.error("could not find person {} in model", p);
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
                return CurrentStatus.class;
            case 1:
                return CurrentStatus.class;
            case 2:
                return CurrentStatus.class;
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
                o = s;
                break;
            case 1:
                o = s;
                break;
            case 2:
                o = s;
                break;
            default:
                o = String.format("column %d for person %s", columnIndex, s);
        }
        logger.debug("Data for {} {}: {}", rowIndex, columnIndex, o);
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
                    this.where = Where.IN;
                    this.when = worksession.getStartDate();
                } else {
                    this.where = Where.OUT;
                    this.when = worksession.getEndDate();
                }
            } else {
                // last worksession does not exist or was a previous day
                this.where = Where.UNKNOWN;
                this.when = null;
            }
        }

        public Person getPerson() {
            return person;
        }

        public String getName() {
            return person.getLastName() + ", " + person.getFirstName();
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

        UNKNOWN(""), IN("In"), OUT("Out");

        String label;

        Where(String _label) {
            label = _label;
        }

        public String getLabel() {
            return label;
        }

    }

}
