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

    public List<Worksession> fetchWorksessions(String where, Map<String, Object> args) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from SA.WORKSESSIONS ");
        if (where != null && where.length() > 0) {
            sql.append("where ");
            sql.append(where);
            sql.append(" ");
        }
        sql.append("ORDER BY START_DATE DESC");
        // logger.info(sql.toString());

        List<Worksession> rv = jdbcTemplate.query(sql.toString(), args, worksessionMapper);
        return rv;
    }

    public List<Worksession> fetchWorksessionsForPerson(Integer personId) {
        Map<String, Object> args = new TreeMap<>();
        args.put("person_id", personId);
        String where = "person_id = :person_id";
        return fetchWorksessions(where, args);
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