package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.dto.NieuweBestelling;
import be.vdab.startrek.services.BestellingService;
import be.vdab.startrek.services.WerknemerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("werknemers")
class WerknemerController {
    private final WerknemerService werknemerService;
    private final BestellingService bestellingService;

    WerknemerController(WerknemerService werknemerService, BestellingService bestellingService) {
        this.werknemerService = werknemerService;
        this.bestellingService = bestellingService;
    }
    @GetMapping
    List<Werknemer> findAll() {
        return werknemerService.findAll();
    }
    @GetMapping("{werknemerId}/bestellingen")
    List<Bestelling> findByWerknemerId(@PathVariable long werknemerId) {
        return bestellingService.findByWerknemerId(werknemerId);
    }
    @PostMapping("{werknemerId}/nieuwebestelling")
    long create(@PathVariable long werknemerId,
                @RequestBody @Valid NieuweBestelling bestelling) {
        return bestellingService.create(new Bestelling(werknemerId, bestelling.omschrijving(),
                bestelling.bedrag()));
    }
}
