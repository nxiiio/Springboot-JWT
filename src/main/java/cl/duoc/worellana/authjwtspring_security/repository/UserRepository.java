package cl.duoc.worellana.authjwtspring_security.repository;

import cl.duoc.worellana.authjwtspring_security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);

}
