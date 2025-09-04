package br.com.fateczl.apihae.adapter.facade;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.fateczl.apihae.useCase.service.CloudinaryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileFacade {
    private final CloudinaryService cloudinaryService;

    public String uploadFile(MultipartFile file, String haeId) throws IOException {
        return cloudinaryService.uploadFile(file, haeId);
    }

    public List<String> uploadMultipleFiles(MultipartFile[] files, String haeId) throws IOException {
        return cloudinaryService.uploadMultipleFiles(files, haeId);
    }
}
