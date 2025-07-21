package br.com.fateczl.apihae.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar imagem: " + e.getMessage());
        }
    }
}
