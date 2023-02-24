package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import be.vdab.startrek.repositories.BestellingRepository;
import be.vdab.startrek.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final WerknemerRepository werknemerRepository;

    public BestellingService(BestellingRepository bestellingRepository, WerknemerRepository werknemerRepository) {
        this.bestellingRepository = bestellingRepository;
        this.werknemerRepository = werknemerRepository;
    }
    public List<Bestelling> findByWerknemerId(long werknemerId) {
        return bestellingRepository.findByWerknemerId(werknemerId);
    }
    @Transactional
    public long create(Bestelling bestelling) {
        var werknemerId = bestelling.getWerknemerId();
        var werknemer = werknemerRepository.findAndLockById(werknemerId)
                .orElseThrow(() -> new WerknemerNietGevondenException(werknemerId));
        werknemer.bestel(bestelling.getBedrag());
        werknemerRepository.updateBudget(werknemerId, werknemer.getBudget());
        return bestellingRepository.create(bestelling);
    }
}
