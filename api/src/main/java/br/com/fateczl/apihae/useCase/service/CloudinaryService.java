package br.com.fateczl.apihae.useCase.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.driver.repository.HaeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HaeRepository haeRepository;

    

    public String uploadImage(MultipartFile file, String haeId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));

        List<String> comprovanteDocs = new ArrayList<>();
        comprovanteDocs.add(uploadResult.get("secure_url").toString());
        hae.setComprovanteDoc(comprovanteDocs);
        haeRepository.save(hae);

        return uploadResult.get("secure_url").toString();
    }

    public String uploadImageSupport(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public List<String> uploadMultipleImages(MultipartFile[] files, String haeId) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadImageSupport(file);
            urls.add(url);
        }
        if (urls.isEmpty()) {
            throw new IOException("Nenhuma imagem foi enviada.");
        }

        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));

        hae.setComprovanteDoc(urls);
        haeRepository.save(hae);

        return urls;
    }

    public List<String> updateImages(MultipartFile[] files, String haeId) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadImageSupport(file);
            urls.add(url);
        }

        Hae hae = haeRepository.findById(haeId)
                .orElseThrow(() -> new IllegalArgumentException("HAE não encontrado."));

        hae.setComprovanteDoc(urls);
        haeRepository.save(hae);

        return urls;
    }

    private List<String> getComprovanteDoc(Hae hae) {
        if (hae.getComprovanteDoc() == null) {
            hae.setComprovanteDoc(new ArrayList<>());
        }
        return hae.getComprovanteDoc();
    }
}
