package br.com.fateczl.apihae;

import br.com.fateczl.apihae.adapter.controller.InstitutionController;
import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.adapter.dto.response.InstitutionResponseDTO;
import br.com.fateczl.apihae.adapter.facade.InstitutionFacade;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstitutionController.class)
public class InstitutionFacadeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstitutionFacade institutionFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private InstitutionCreateRequest sampleRequest;
    private Institution sampleResponse;

    @BeforeEach
    void setup() {
        sampleRequest = new InstitutionCreateRequest();
        sampleRequest.setName("FATEC Z2");
        sampleRequest.setInstitutionCode(112);
        sampleRequest.setAddress("Some Address");
        sampleRequest.setHaeQtd(20);

        sampleResponse = new Institution();
        sampleResponse.setId("1");
        sampleResponse.setName("FATEC Z2");
        sampleResponse.setInstitutionCode(112);
        sampleResponse.setAddress("Some Address");
        sampleResponse.setHaeQtd(20);
    }

    @Test
    void testCreateInstitution() throws Exception {
        Mockito.doNothing()
                .when(institutionFacade)
                .createInstitution(Mockito.any());
        ;

        mockMvc.perform(post("/institution/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllInstitutions() throws Exception {
        InstitutionResponseDTO dto = new InstitutionResponseDTO(sampleResponse);
        Mockito.when(institutionFacade.getAllInstitutions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/institution/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FATEC Z2"))
                .andExpect(jsonPath("$[0].institutionCode").value(112));
    }

    @Test
    void testUpdateInstitution() throws Exception {
        Mockito.when(institutionFacade.updateInstitution(Mockito.anyInt(), Mockito.any()))
                .thenReturn(sampleResponse);

        mockMvc.perform(put("/institution/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FATEC Z2"))
                .andExpect(jsonPath("$.institutionCode").value(112))
                .andExpect(jsonPath("$.address").value("Some Address"))
                .andExpect(jsonPath("$.haeQtd").value(20));
    }

    @Test
    void testGetAvailableHaesCount() throws Exception {
        Mockito.when(institutionFacade.getAvailableHaesCount("1"))
                .thenReturn(5);

        mockMvc.perform(get("/institution/getAvailableHaesCount?institutionId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    /*
     * @Test
     * void testGetHaesByInstitutionId() throws Exception {
     * Mockito.when(institutionFacade.getHaesByInstitutionId("1")).thenReturn(List.
     * of(new HaeResponseDTO()));
     * 
     * mockMvc.perform(get("/institution/getHaesByInstitutionId?institutionId=1"))
     * .andExpect(status().isOk())
     * .andExpect(jsonPath("$[0].name").value("FATEC Z2"))
     * .andExpect(jsonPath("$[0].institutionCode").value(112))
     * .andExpect(jsonPath("$[0].address").value("Some Address"))
     * .andExpect(jsonPath("$[0].haeQtd").value(20));
     * }
     */
    @Test
    void testGetInstitutionByInstitutionCode() throws Exception {
        Mockito.when(institutionFacade.getInstitutionByInstitutionCode(112)).thenReturn(sampleResponse);

        mockMvc.perform(get("/institution/getInstitutionByInstitutionCode?institutionCode=112"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FATEC Z2"))
                .andExpect(jsonPath("$.institutionCode").value(112))
                .andExpect(jsonPath("$.address").value("Some Address"))
                .andExpect(jsonPath("$.haeQtd").value(20));
    }

    @Test
    void testGetInstitutionById() throws Exception {
        Mockito.when(institutionFacade.getInstitutionById("1")).thenReturn(sampleResponse);

        mockMvc.perform(get("/institution/getInstitutionById?institutionId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FATEC Z2"))
                .andExpect(jsonPath("$.institutionCode").value(112))
                .andExpect(jsonPath("$.address").value("Some Address"))
                .andExpect(jsonPath("$.haeQtd").value(20));
    }

    /*
     * @Test
     * void testGetEmployeesByInstitutionId() throws Exception {
     * Mockito.when(institutionFacade.getEmployeesByInstitutionId("1")).thenReturn(
     * List.of(new EmployeeResponseDTO(new Employee() {{
     * setId("1");
     * setName("John Doe");
     * }})));
     * 
     * mockMvc.perform(get(
     * "/institution/getEmployeesByInstitutionId?institutionId=1"))
     * .andExpect(status().isOk())
     * .andExpect(jsonPath("$[0].name").value("John Doe"))
     * .andExpect(jsonPath("$[0].id").value("1"))
     * .andExpect(jsonPath("$[0].position").value("Professor"));
     * }
     */

    @Test
    void testGetRemainingHours() throws Exception {
        Mockito.when(institutionFacade.getRemainingHours("1")).thenReturn(10);

        mockMvc.perform(get("/institution/getRemainingHours?institutionId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }
}
