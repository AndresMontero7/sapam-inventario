package com.sapam.inventario.service;

import com.sapam.inventario.entity.Receptor;
import com.sapam.inventario.repository.ReceptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReceptorService {

    @Autowired
    private ReceptorRepository receptorRepository;

    public List<Receptor> listarTodos() {
        return receptorRepository.findAll();
    }

    public Optional<Receptor> buscarPorId(Integer id) {
        return receptorRepository.findById(id);
    }

    public Receptor guardar(Receptor receptor) {
        return receptorRepository.save(receptor);
    }

    public void eliminar(Integer id) {
        receptorRepository.deleteById(id);
    }
}