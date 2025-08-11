package br.com.fateczl.apihae.domain.entity;

import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.singleton.HaeQtdSingleton;
import br.com.fateczl.apihae.useCase.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HaeQtd {

    @Autowired
    private EmployeeService employeeService;

    public int getQuantidade() {
        return HaeQtdSingleton.getInstance().getQuantidade();
    }

    public void setQuantidade(int quantidade, String usuarioId) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }

        Employee employee = employeeService.getEmployeeById(usuarioId);
        if (employee == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (employee.getRole() != Role.DIRETOR) {
            throw new IllegalArgumentException(
                    "Usuário não é um diretor da unidade, impossivel definir a quantidade de HAEs.");
        }

        // Atualiza a quantidade de HAEs no singleton
        HaeQtdSingleton.getInstance().setQuantidade(quantidade);
    }
}
