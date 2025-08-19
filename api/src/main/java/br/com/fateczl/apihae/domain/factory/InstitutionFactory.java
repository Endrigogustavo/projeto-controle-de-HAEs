package br.com.fateczl.apihae.domain.factory;

import br.com.fateczl.apihae.adapter.dto.request.InstitutionCreateRequest;
import br.com.fateczl.apihae.domain.entity.Institution;

public class InstitutionFactory {

    public static Institution create(InstitutionCreateRequest request) {
        Institution institution = new Institution();
        institution.setName(request.getName());
        institution.setInstitutionCode(request.getInstitutionCode());
        institution.setHaeQtd(request.getHaeQtd());
        institution.setAddress(request.getAddress());
        institution.setActive(true);
        return institution;
    }

}
