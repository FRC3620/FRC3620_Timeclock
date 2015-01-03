package org.frc3620.timeclock.db;

import java.util.*;

import javax.sql.DataSource;
import org.frc3620.timeclock.Person;
import org.frc3620.timeclock.Worksession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DAO {

    Logger logger = LoggerFactory.getLogger(getClass());

    BeanPropertyRowMapper<Person> personMapper = BeanPropertyRowMapper.newInstance(Person.class);
    BeanPropertyRowMapper<Worksession> worksessionMapper = BeanPropertyRowMapper
            .newInstance(Worksession.class);

    public void createWorksession(Person person) {
        Map<String, Object> args = new HashMap<>();
        args.put("person_id", person.getPersonId());
        args.put("start", new Date());
        jdbcTemplate.update("insert into SA.WORKSESSIONS (PERSON_ID, START_DATE, ORIGINAL_START_DATE) VALUES (:person_id, :start, :start)", args);
    }

    public void closeWorksession(Worksession worksession) {
        Map<String, Object> args = new HashMap<>();
        args.put("worksession_id", worksession.getWorksessionId());
        args.put("end", new Date());
        jdbcTemplate.update("update SA.WORKSESSIONS SET END_DATE = :end, ORIGINAL_END_DATE = :end WHERE WORKSESSION_ID = :worksession_id", args);
    }

    public Person fetchPerson(Integer id) {
        Map<String, Object> args = new HashMap<>();
        args.put("person_id", id);
        String where = "person_id = :person_id";
        List<Person> rv = fetchPersons(where, args);
        return justOne(rv, "person");
    }

    public List<Person> fetchPersons(String where, Map<String, Object> args) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from SA.PERSONS ");
        if (where != null && where.length() > 0) {
            sql.append("where ");
            sql.append(where);
            sql.append(" ");
        }
        sql.append("ORDER BY LASTNAME, FIRSTNAME");
        logger.info(sql.toString());

        List<Person> persons = jdbcTemplate.query(sql.toString(), args, personMapper);
        return persons;
    }

    public List<Person> fetchPersons() {
        Map<String, Object> m = Collections.emptyMap();
        return fetchPersons("", m);
    }

    public List<Worksession> fetchWorksessions(String where, String post, Map<String, Object> args) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from SA.WORKSESSIONS ");
        if (where != null && where.length() > 0) {
            sql.append("where ");
            sql.append(where);
            sql.append(" ");
        }
        sql.append("ORDER BY START_DATE DESC");
        if (post != null) {
            sql.append(" ");
            sql.append(post);
        }
        logger.info("query: sql={} {}", sql, args);

        List<Worksession> rv = jdbcTemplate.query(sql.toString(), args, worksessionMapper);

        logger.info("query: received {} rows", rv.size());
        return rv;
    }

    public List<Worksession> fetchWorksessions(String where, Map<String, Object> args) {
        return fetchWorksessions(where, null, args);
    }

    public Worksession fetchLastWorksessionForPerson(Person person) {
        Map<String, Object> args = new HashMap<>();
        args.put("person_id", person.getPersonId());
        String where = "person_id = :person_id";
        List<Worksession> rv = fetchWorksessions(where, "LIMIT 1", args);
        return justOne(rv, "lastworksession");
    }

    public List<Worksession> fetchWorksessionsForPerson(Integer personId) {
        Map<String, Object> args = new TreeMap<>();
        args.put("person_id", personId);
        String where = "person_id = :person_id";
        return fetchWorksessions(where, null, args);
    }

    public void updateStartTime(Worksession worksession, Date newDate, Person correctorPerson) {
        {
            Map<String, Object> args = new HashMap<>();
            args.put("worksession_id", worksession.getWorksessionId());
            args.put("newdate", newDate);
            jdbcTemplate.update("update SA.WORKSESSIONS SET START_DATE = :newdate WHERE WORKSESSION_ID = :worksession_id", args);
        }
        {
            Map<String, Object> args = new HashMap<>();
            args.put("worksession_id", worksession.getWorksessionId());
            args.put("new_date", newDate);
            args.put("old_date", worksession.getStartDate());
            args.put("correction_date", new Date());
            args.put("corrected_by", correctorPerson.getPersonId());
            jdbcTemplate.update("insert into SA.CORRECTIONS (WORKSESSION_ID, START_OR_END, NEW_DATE, OLD_DATE, CORRECTION_DATE, CORRECTED_BY) VALUES (:worksession_id, 'start', :new_date, :old_date, :correction_date, :corrected_by)", args);
        }

    }

    public void updateEndTime(Worksession worksession, Date newDate, Person correctorPerson) {
        {
            Map<String, Object> args = new HashMap<>();
            args.put("worksession_id", worksession.getWorksessionId());
            args.put("newdate", newDate);
            jdbcTemplate.update("update SA.WORKSESSIONS SET END_DATE = :newdate WHERE WORKSESSION_ID = :worksession_id", args);
        }
        {
            Map<String, Object> args = new HashMap<>();
            args.put("worksession_id", worksession.getWorksessionId());
            args.put("new_date", newDate);
            args.put("old_date", worksession.getEndDate());
            args.put("correction_date", new Date());
            args.put("corrected_by", correctorPerson.getPersonId());
            jdbcTemplate.update("insert into SA.CORRECTIONS (WORKSESSION_ID, START_OR_END, NEW_DATE, OLD_DATE, CORRECTION_DATE, CORRECTED_BY) VALUES (:worksession_id, 'end', :new_date, :old_date, :correction_date, :corrected_by)", args);
        }

    }

    <T> T justOne(List<T> l, String with) {
        if (l.size() > 2) {
            throw new RuntimeException("too many objects with" + with);
        }
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }

    <T> T exactlyOne(List<T> l, String with) {
        if (l.isEmpty()) {
            throw new RuntimeException("no objects with " + with);
        }
        if (l.size() > 2) {
            throw new RuntimeException("too many objects with" + with);
        }
        return l.get(0);
    }

    private NamedParameterJdbcTemplate jdbcTemplate;

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    @Required
    public void setDataSource(DataSource dataSource) {
        logger.info("got datasource");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
