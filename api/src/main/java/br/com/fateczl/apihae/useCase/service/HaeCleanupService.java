package br.com.fateczl.apihae.useCase.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.driver.repository.HaeRepository;


@Service
public class HaeCleanupService {
/* 
    @Autowired
    private HaeRepository haeRepository;

    @Scheduled
    (cron = "0 0 2 * * *")
    public void deleteExpiredHaes() {
        LocalDate limite = LocalDate.now().minusDays(10);
        List<Hae> haesParaDeletar = haeRepository.findByEndDateBefore(limite);

        haeRepository.deleteAll(haesParaDeletar);
    }
*/
}
