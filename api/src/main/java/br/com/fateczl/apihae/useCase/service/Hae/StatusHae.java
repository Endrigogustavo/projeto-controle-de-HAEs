package br.com.fateczl.apihae.useCase.service.Hae;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.entity.HaeClosureRecord;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.useCase.Interface.IEmployeeRepository;
import br.com.fateczl.apihae.useCase.Interface.IHaeClosureRecordRepository;
import br.com.fateczl.apihae.useCase.Interface.IHaeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatusHae {
    private final IHaeRepository haeRepository;
    private final IEmployeeRepository employeeRepository;
    private final IHaeClosureRecordRepository closureRecordRepository;

    @Transactional
    public void wasViewed(String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + haeId));

        hae.setViewed(true);
        haeRepository.save(hae);
    }

    @Transactional
    public Hae toggleViewedStatus(String haeId) {
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrada com ID: " + haeId));

        hae.setViewed(!hae.getViewed());
        return haeRepository.save(hae);
    }

    public List<Hae> getHaeWasViewed() {
        return haeRepository.findByViewed(true);
    }

    public List<Hae> getHaeWasNotViewed() {
        return haeRepository.findByViewed(false);
    }

    @Transactional
    public Hae changeHaeStatus(String id, Status newStatus, String coordenadorId) {
        Hae hae = haeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado com ID: " + id));

        employeeRepository.findById(coordenadorId)
                .filter(emp -> emp.getRole() == Role.COORDENADOR || emp.getRole() == Role.DEV)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Coordenador com id " + coordenadorId + " não encontrado ou não é coordenador/dev."));

        Status previousStatus = hae.getStatus();
        hae.setStatus(newStatus);
        hae.setCoordenatorId(coordenadorId);
        Hae updatedHae = haeRepository.save(hae);

        // Armazenar registro de fechamento quando coordenador aprovar
        if (previousStatus == Status.FECHAMENTO_SOLICITADO && newStatus == Status.COMPLETO) {
            saveClosureRecord(updatedHae, coordenadorId);
        }

        return updatedHae;
    }

    private void saveClosureRecord(Hae hae, String coordenadorId) {
        HaeClosureRecord record = new HaeClosureRecord();
        record.setHae(hae);
        record.setCoordenadorId(coordenadorId);

        // Buscar nome do coordenador
        employeeRepository.findById(coordenadorId)
                .ifPresent(coordinator -> record.setCoordenadorName(coordinator.getName()));

        // Copiar informações de fechamento baseado no tipo de projeto
        switch (hae.getProjectType()) {
            case TCC:
                record.setTccRole(hae.getTccRole());
                record.setTccStudentCount(hae.getTccStudentCount());
                record.setTccStudentNames(hae.getTccStudentNames());
                record.setTccApprovedStudents(hae.getTccApprovedStudents());
                record.setTccProjectInfo(hae.getTccProjectInfo());
                break;

            case Estagio:
                record.setEstagioStudentInfo(hae.getEstagioStudentInfo());
                record.setEstagioApprovedStudents(hae.getEstagioApprovedStudents());
                break;

            case ApoioDirecao:
                record.setApoioType(hae.getApoioType());
                record.setApoioGeralDescription(hae.getApoioGeralDescription());
                record.setApoioApprovedStudents(hae.getApoioApprovedStudents());
                record.setApoioCertificateStudents(hae.getApoioCertificateStudents());
                break;

            default:
                // Para outros tipos, apenas salvar o registro básico
                break;
        }

        closureRecordRepository.save(record);
    }

}
