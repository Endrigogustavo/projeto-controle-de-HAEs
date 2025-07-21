package br.com.fateczl.apihae.adapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.enums.Role;
import br.com.fateczl.apihae.domain.singleton.CalendarioSingleton;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.fateczl.apihae.useCase.service.EmployeeService;

@RestController
@RequestMapping("/calendario")
public class CalendarioController {

    @Autowired
    private CalendarioSingleton calendarioSingleton;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/dias-nao-letivos")
    public String listarDiasNaoLetivos() {
        return calendarioSingleton.getCalendario().getDiasNaoLetivos().toString();
    }

    @DeleteMapping("/remover/{data}")
    public ResponseEntity<?> remover(@PathVariable String data, String id) {
         Employee currentUser = employeeService.getEmployeeById(id);
        if (currentUser == null || !currentUser.getRole().equals(Role.ADMIN) && !currentUser.getRole().equals(Role.DIRETOR) 
                && !currentUser.getRole().equals(Role.COORDENADOR)) {
            return ResponseEntity.status(403).body("Acesso negado. Apenas funcionários com permissão podem adicionar dias não letivos.");
        }
        LocalDate dia = LocalDate.parse(data);
        if (!calendarioSingleton.getCalendario().isDiaNaoLetivo(dia)) {
            return ResponseEntity.badRequest().body("Dia não letivo não encontrado: " + dia);
        }
        calendarioSingleton.getCalendario().removerDiaNaoLetivo(dia);
        return ResponseEntity.ok("Dia não letivo removido: " + dia);
    }

    @PostMapping("/adicionar-list")
    public ResponseEntity<?> adicionarVarios(@RequestBody List<String> datas, @RequestParam String id) {
        Employee currentUser = employeeService.getEmployeeById(id);
        if (currentUser == null || !currentUser.getRole().equals(Role.ADMIN) && !currentUser.getRole().equals(Role.DIRETOR) 
                && !currentUser.getRole().equals(Role.COORDENADOR)) {
            return ResponseEntity.status(403).body("Acesso negado. Apenas funcionários com permissão podem adicionar dias não letivos.");
        }
        List<String> datasInvalidas = new ArrayList<>();
        for (String dataStr : datas) {
            try {
                LocalDate data = LocalDate.parse(dataStr);
                calendarioSingleton.getCalendario().adicionarDiaNaoLetivo(data);
            } catch (DateTimeParseException e) {
                datasInvalidas.add(dataStr);
            }
        }
        if (!datasInvalidas.isEmpty()) {
            return ResponseEntity.badRequest().body("Datas inválidas: " + datasInvalidas);
        }
        return ResponseEntity.ok("Todas as datas foram adicionadas com sucesso.");
    }

    @PostMapping("/adicionar/{data}")
    public ResponseEntity<?> adicionar(@PathVariable String data, String id) {
         Employee currentUser = employeeService.getEmployeeById(id);
        if (currentUser == null || !currentUser.getRole().equals(Role.ADMIN) && !currentUser.getRole().equals(Role.DIRETOR) 
                && !currentUser.getRole().equals(Role.COORDENADOR)) {
            return ResponseEntity.status(403).body("Acesso negado. Apenas funcionários com permissão podem adicionar dias não letivos.");
        }
        LocalDate dia = LocalDate.parse(data);
        if (calendarioSingleton.getCalendario().isDiaNaoLetivo(dia)) {
            return ResponseEntity.badRequest().body("Dia não letivo já existe: " + dia);
        }
        calendarioSingleton.getCalendario().adicionarDiaNaoLetivo(dia);
        return ResponseEntity.ok("Dia não letivo adicionado: " + dia);
    }

    @GetMapping("/verificar/{data}")
    public ResponseEntity<?> verificar(@PathVariable String data) {
        LocalDate dia = LocalDate.parse(data);
        boolean ehNaoLetivo = calendarioSingleton.getCalendario().isDiaNaoLetivo(dia);
        return ehNaoLetivo ? ResponseEntity.ok("É dia não letivo") : ResponseEntity.ok("É dia letivo");
    }
}
