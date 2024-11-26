package mx.edu.utez.integradora.application.services;

import mx.edu.utez.integradora.domain.entities.RepairPost;
import mx.edu.utez.integradora.domain.entities.User;
import mx.edu.utez.integradora.infrastructure.repository.RepairPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairPostService {

    @Autowired
    private RepairPostRepository repairPostRepository;

    public List<RepairPost> getAllRepairPost() {
        return repairPostRepository.findAll();
    }

    public Optional<RepairPost> getRepairPostById(Integer id) {
        return repairPostRepository.findById(id);
    }

    public RepairPost saveRepairPost(RepairPost repairPost) {
        return repairPostRepository.save(repairPost);
    }

    public boolean deleteRepairPost(Integer id) {
        if (repairPostRepository.existsById(id)) {
            repairPostRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RepairPost> getPostByTitle(String title) {
        return repairPostRepository.findByTitleContaining(title);
    }

    public List<RepairPost> getPostByUser(User user) {
        return repairPostRepository.findByUser(user);
    }

    public List<RepairPost> getPostByUserId(Integer userId){
        return repairPostRepository.findByUserId(userId);
    }
}
