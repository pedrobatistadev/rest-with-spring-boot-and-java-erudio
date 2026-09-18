package br.com.erudio.repository;

import br.com.erudio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User as u WHERE u.username = :userName")
    public User findByUsername(@Param("userName") String userName);
}
