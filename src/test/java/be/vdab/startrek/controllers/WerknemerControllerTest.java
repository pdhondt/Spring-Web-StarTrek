package be.vdab.startrek.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql({ "/werknemers.sql", "/bestellingen.sql" })
@AutoConfigureMockMvc
class WerknemerControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String WERKNEMERS = "werknemers";
    private final static String BESTELLINGEN = "bestellingen";
    private final static Path TEST_RESOURCES = Path.of("src/test/resources");
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
    @ParameterizedTest
    @ValueSource(strings = { "bestellingMetLegeOmschrijving.json", "bestellingZonderOmschrijving.json",
    "bestellingZonderBedrag.json", "bestellingMetNegatiefBedrag.json" })
    void createMetVerkeerdeDataMislukt(String fileName) throws Exception {
        var werknemerId = findIdTestWerknemer1();
        var jsonData = Files.readString(TEST_RESOURCES.resolve(fileName));
        mockMvc.perform(post("/werknemers/{werknemerId}/nieuwebestelling", werknemerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }
}
