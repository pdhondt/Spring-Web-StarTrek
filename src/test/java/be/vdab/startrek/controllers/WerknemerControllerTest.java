package be.vdab.startrek.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Sql({ "/werknemers.sql", "/bestellingen.sql" })
@AutoConfigureMockMvc
class WerknemerControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String WERKNEMERS = "werknemers";
    private final static String BESTELLINGEN = "bestellingen";
    private final MockMvc mockMvc;

    WerknemerControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    private long findIdTestWerknemer1() {
        return jdbcTemplate.queryForObject(
                "select id from werknemers where voornaam = 'testvoornaam1'", Long.class);
    }
    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/werknemers"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(countRowsInTable(WERKNEMERS)));
    }
    @Test
    void findByWerknemerId() throws Exception {
        var werknemerId = findIdTestWerknemer1();
        mockMvc.perform(get("/werknemers/{werknemerId}/bestellingen", werknemerId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()").value(
                                countRowsInTableWhere(BESTELLINGEN, "werknemerId = " + werknemerId)));
    }
}
