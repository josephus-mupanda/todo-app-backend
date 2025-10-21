package com.example.todo_app.common.repositories;
import com.example.todo_app.common.domains.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    boolean existsByToken(String token);
}
