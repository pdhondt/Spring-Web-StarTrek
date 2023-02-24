package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BestellingRepository {
    private final JdbcTemplate template;
    private final RowMapper<Bestelling> bestellingMapper = (rs, rowNum) ->
            new Bestelling(rs.getLong("id"), rs.getLong("werknemerId"),
                    rs.getString("omschrijving"), rs.getBigDecimal("bedrag"),
                    rs.getObject("moment", LocalDateTime.class));

    public BestellingRepository(JdbcTemplate template) {
        this.template = template;
    }
    public List<Bestelling> findByWerknemerId(long werknemerId) {
        var sql = """
                select id, werknemerId, omschrijving, bedrag, moment
                from bestellingen
                where werknemerId = ?
                order by id
                """;
        return template.query(sql, bestellingMapper, werknemerId);
    }
}
