package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.motoCare.domain.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM tbl_user WHERE id = :id AND status = :status",
            nativeQuery = true)
    UserEntity checkStatusUser(@Param("status") String status, @Param("id") Long id);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM tbl_user WHERE email = :email)", nativeQuery = true)
    Long existsByEmail(@Param("email") String email);

    UserEntity findUserByEmailAndRefreshToken(String email, String refresh);
}
