package br.com.fateczl.apihae.useCase.service;


import org.springframework.stereotype.Service;




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
