package br.com.fateczl.apihae.adapter.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fateczl.apihae.adapter.facade.FileFacade;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "API para upload de imagens e PDFs")
public class FileController {
    private final FileFacade fileFacade;

    @Operation(summary = "Upload de arquivo", description = "Faz upload de uma imagem ou PDF e retorna a URL pública")
    @PostMapping(value = "/upload", consumes = { "multipart/form-data" })
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("haeId") String haeId) {
        try {
            return ResponseEntity.ok(fileFacade.uploadFile(file, haeId));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    @Operation(summary = "Upload múltiplo", description = "Faz upload de múltiplos arquivos (imagens ou PDFs) e retorna as URLs públicas")
    @PostMapping(value = "/upload-multiple", consumes = { "multipart/form-data" })
    public ResponseEntity<List<String>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
            @RequestParam("haeId") String haeId) {
        try {
            return ResponseEntity.ok(fileFacade.uploadMultipleFiles(files, haeId));
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonList("Erro ao enviar arquivos: " + e.getMessage()));
        }
    }
}
