package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.Problema;
import mx.edu.utez.integradora.infrastructure.repository.ProblemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemaService {

    @Autowired
    private ProblemaRepository problemaRepository;

    public List<Problema> getProblemasPorUsuario(Integer usuarioId) {
        return problemaRepository.findByUsuarioId(usuarioId);
    }

    public Problema crearProblema(Problema problema) {
        return problemaRepository.save(problema);
    }

    public Problema actualizarProblema(Integer problemaId, Problema datosProblema) {
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new RuntimeException("Problema no encontrado"));

        problema.setTitulo(datosProblema.getTitulo());
        problema.setDescripcion(datosProblema.getDescripcion());
        problema.setFotografia(datosProblema.getFotografia());
        problema.setEstado(datosProblema.getEstado());
        return problemaRepository.save(problema);
    }
}
