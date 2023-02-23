package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WerknemerRepository {
    private final JdbcTemplate template;
    private final RowMapper<Werknemer> werknemerMapper = (rs, rowNum) ->
            new Werknemer(rs.getLong("id"), rs.getString("voornaam"),
                    rs.getString("familienaam"), rs.getBigDecimal("budget"));

    public WerknemerRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Werknemer> findAll() {
        var sql = """
                select id, voornaam, familienaam, budget
                from werknemers
                order by voornaam
                """;
        return template.query(sql, werknemerMapper);
    }
}
