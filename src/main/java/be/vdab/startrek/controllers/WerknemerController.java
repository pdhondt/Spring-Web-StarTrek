package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.services.WerknemerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class WerknemerController {
    private final WerknemerService werknemerService;

    WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }
    @GetMapping("werknemers")
    List<Werknemer> findAll() {
        return werknemerService.findAll();
    }
}
