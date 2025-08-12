package br.com.fateczl.apihae.adapter.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import br.com.fateczl.apihae.domain.entity.Hae;
import br.com.fateczl.apihae.useCase.service.HaeService;

@Service
public class HaeCacheTransientService {

    private final Map<String, Boolean> visualizations = new ConcurrentHashMap<>();
    private final HaeService haeService;

    public HaeCacheTransientService(HaeService haeService) {
        this.haeService = haeService;
    }

    public void markAsViewed(String id) {
        visualizations.put(id, true);
    }

    public boolean wasViewed(String id) {
        return visualizations.getOrDefault(id, false);
    }

    public void cleanVisualization(String id) {
        visualizations.remove(id);
    }

    public List<String> getAllUnviewedIds() {
        return haeService.getAllHaes().stream()
                .filter(hae -> !wasViewed(hae.getId()))
                .map(Hae::getId)
                .toList();
    }

    public List<Hae> getAllUnviewed() {
        return haeService.getAllHaes().stream()
                .filter(hae -> !wasViewed(hae.getId()))
                .toList();
    }
}
