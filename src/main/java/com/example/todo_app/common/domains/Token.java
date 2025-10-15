package com.example.todo_app.common.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
@SQLDelete(sql = "UPDATE tokens SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Token extends BasicEntity {
    @Column(unique = true)
    public String token;

    public LocalDateTime revokedAt;

    public LocalDateTime expiredAt;

    @Column(name = "user_id", nullable = false)
    public String userId;
}

