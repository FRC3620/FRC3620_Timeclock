package org.frc3620.timeclock.app;

import org.frc3620.timeclock.workhistory.HistoryPeriod;
import org.frc3620.timeclock.workhistory.PersonWithHistory;
import org.frc3620.timeclock.workhistory.Period;
import java.util.*;
import org.frc3620.timeclock.*;
import org.frc3620.timeclock.db.DAO;

/**
 *
 * @author wegscd
 */
public class WorkHistoryBuilder {

    DAO dao;

    public WorkHistoryBuilder(DAO dao) {
        super();
        this.dao = dao;
    }

    public List<PersonWithHistory> getHistory() {
        List<Person> persons = dao.fetchPersons();
        List<Worksession> worksessions = dao.fetchWorksessions();

        Map<Integer, PersonWithHistory> rvMap = new TreeMap<>();
        Map<Integer, Person> personMap = Person.createPersonMap(persons);
        for (Worksession w : worksessions) {
            Integer personId = w.getPersonId();
            PersonWithHistory personWithHistory = rvMap.get(personId);
            if (null == personWithHistory) {
                personWithHistory = new PersonWithHistory(personMap.get(personId));
                rvMap.put(personId, personWithHistory);
            }
            
            Date startOfInterval = Utils.getStartOfWeek(w.getStartDate());
            HistoryPeriod historyPeriod = personWithHistory.getOrCreateHistoryPeriod(startOfInterval, Period.WEEK);

            Double h = w.getHours();
            if (null != h) {
                historyPeriod.addValidWorksession(w);
                historyPeriod.addDay(Utils.getStartOfDay(w.getStartDate()));
                historyPeriod.addHours(h);
            } else {
                historyPeriod.addOpenWorksession(w);
            }
        }
        return new ArrayList<>(rvMap.values());
    }



}
