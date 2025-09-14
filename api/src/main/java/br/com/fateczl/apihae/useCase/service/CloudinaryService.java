package br.com.fateczl.apihae.useCase.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.domain.enums.Status;
import br.com.fateczl.apihae.useCase.Interface.IHaeRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final IHaeRepository haeRepository;

    public String uploadFile(MultipartFile file, String haeId) throws IOException {
        String resourceType = file.getContentType() != null && file.getContentType().startsWith("image")
                ? "image"
                : "raw";

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", resourceType));

        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));

      
        if (hae.getComprovanteDoc() == null) {
            hae.setComprovanteDoc(new ArrayList<>());
        }
        
        hae.getComprovanteDoc().add(uploadResult.get("secure_url").toString());
        hae.setStatus(Status.FECHAMENTO_SOLICITADO);
        haeRepository.save(hae);

        return uploadResult.get("secure_url").toString();
    }

    public List<String> uploadMultipleFiles(MultipartFile[] files, String haeId) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String resourceType = file.getContentType() != null && file.getContentType().startsWith("image")
                    ? "image"
                    : "raw";

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", resourceType));
            urls.add(uploadResult.get("secure_url").toString());
        }

        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));

        hae.setComprovanteDoc(urls);
        haeRepository.save(hae);

        return urls;
    }

    public void deleteFile(String url) throws IOException {
        cloudinary.uploader().destroy(url, ObjectUtils.emptyMap());
    }

    public void deleteFiles(List<String> urls) throws IOException {
        for (String url : urls) {
            cloudinary.uploader().destroy(url, ObjectUtils.emptyMap());
        }
    }
}