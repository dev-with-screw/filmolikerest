package home.work.filmolikerest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

// В проекте filmolike реализация списка ролей сделана через enum.
// Реализация через enum, думаю, более правильная. Чтобы лишний раз не обращаться в базу за списком ролей.
// Но тут оставлено в таком виде, чтобы показать реализацию c использованием @JoinTable
@Entity
@Table(name = "roles")
@Data
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

}
