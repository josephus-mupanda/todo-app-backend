package com.example.todo_app.common.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@SQLDelete(sql = "UPDATE roles SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Role extends BasicEntity {
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean managed = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();
}
