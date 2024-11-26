package mx.edu.utez.integradora.infrastructure.repository;

import mx.edu.utez.integradora.domain.entities.RepairPost;
import mx.edu.utez.integradora.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepairPostRepository extends JpaRepository<RepairPost, Integer> {

    List<RepairPost> findByTitleContaining(String title);
    List<RepairPost> findByUser(User user);
    @Query("SELECT rp FROM RepairPost rp WHERE rp.user.id = :userId")
    List<RepairPost> findByUserId(@Param("userId") Integer userId);
}
