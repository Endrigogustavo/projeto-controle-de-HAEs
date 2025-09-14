package br.com.fateczl.apihae;

import br.com.fateczl.apihae.adapter.controller.EmployeeController;
import br.com.fateczl.apihae.adapter.dto.request.EmployeeCreateByDiretorOrAdmRequest;
import br.com.fateczl.apihae.adapter.dto.response.EmployeeResponseDTO;
import br.com.fateczl.apihae.adapter.facade.EmployeeFacade;
import br.com.fateczl.apihae.domain.entity.Employee;
import br.com.fateczl.apihae.domain.entity.Institution;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeFacadeTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EmployeeFacade employeeFacade;

        @Autowired
        private ObjectMapper objectMapper;

        private EmployeeCreateByDiretorOrAdmRequest sampleRequest;
        private Employee sampleResponse;



    @BeforeEach
    void setup() {
        sampleRequest = new EmployeeCreateByDiretorOrAdmRequest();
        sampleRequest.setName("John Doe");
        sampleRequest.setEmail("john.doe@fatec.sp.gov.br");
        sampleRequest.setProvisoryPassword("password123");
        sampleRequest.setInstitutionCode(111);
        sampleRequest.setCourse("ADS");

        sampleResponse = new Employee();
        sampleResponse.setId("1");
        sampleResponse.setName("John Doe");
        sampleResponse.setEmail("john.doe@fatec.sp.gov.br");
        sampleResponse.setPassword("password123");
        sampleResponse.setInstitution(new Institution() {{
            setId("inst1");
            setName("FATEC Z2");
            setInstitutionCode(112);
            setAddress("Some Address");
            setHaeQtd(20);
        }});
        sampleResponse.setCourse("ADS");
        sampleResponse.setRole(br.com.fateczl.apihae.domain.enums.Role.PROFESSOR);
    }
        @Test
        void testCreateEmployee() throws Exception {
                Mockito.when(employeeFacade.createEmployeeByDiretorOrAdm(Mockito.any()))
                                .thenReturn(sampleResponse);

                mockMvc.perform(post("/employee/createEmployeeByDiretorOrAdmin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("John Doe"))
                                .andExpect(jsonPath("$.email").value("john.doe@fatec.sp.gov.br"));
                                
        }

        @Test
        void testGetAllEmployees() throws Exception {
                EmployeeResponseDTO dto = new EmployeeResponseDTO(sampleResponse);
                Mockito.when(employeeFacade.getAllEmployees()).thenReturn(List.of(dto));

                mockMvc.perform(get("/employee/getAllEmployee"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].course").value("ADS"));
        }

        @Test
        void testGetAllEmployeesByRole() throws Exception {
                Mockito.when(employeeFacade.getEmployeeSummaries(br.com.fateczl.apihae.domain.enums.Role.PROFESSOR))
                                .thenReturn(Collections.singletonList(new br.com.fateczl.apihae.adapter.dto.response.EmployeeSummaryDTO("1", "John Doe", "john.doe@fatec.sp.gov.br", "ADS", 5)));

                mockMvc.perform(get("/employee/getAllByRole/PROFESSOR"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].course").value("ADS"));
        }

        @Test
        void testGetProfessorById() throws Exception {
                Mockito.when(employeeFacade.getEmployeeById("1"))
                                .thenReturn(new EmployeeResponseDTO(sampleResponse));

                mockMvc.perform(get("/employee/get-professor/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.course").value("ADS"));
        }

        @Test
        void testUpdateEmployee() throws Exception {
                Mockito.when(employeeFacade.updateEmployee("1", "John Doe", "john.doe3@fatec.sp.gov.br")).thenReturn(sampleResponse);

                mockMvc.perform(put("/employee/update-account/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleRequest)))
                                .andExpect(status().isOk());
        }

        @Test
        void testDeleteEmployee() throws Exception {
                mockMvc.perform(delete("/employee/delete-account/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensagem").value("Conta deletada com sucesso."));
        }
}
