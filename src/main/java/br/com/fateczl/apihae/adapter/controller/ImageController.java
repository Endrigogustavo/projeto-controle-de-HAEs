package br.com.fateczl.apihae.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fateczl.apihae.useCase.service.CloudinaryService;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Images", description = "API para upload de imagens")
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem e retorna a URL pública")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("haeId") String haeId) {
        try {
            String url = cloudinaryService.uploadImage(file, haeId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar imagem: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload-multiple", consumes = {"multipart/form-data"})
    public ResponseEntity<List<String>> uploadMultipleImages(@RequestParam("files") MultipartFile[] files, 
                                                             @RequestParam("haeId") String haeId) {
        try {
            List<String> urls = cloudinaryService.uploadMultipleImages(files, haeId);
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Erro ao enviar imagens: " + e.getMessage()));
        }
    }

}
