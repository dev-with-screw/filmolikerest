package home.work.filmolikerest.model;


import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
public class Note {
    public final static Note NULL_NOTE = new Note();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    @Length(max = 50, message = "Должно содержать не более 50 символов")
    private String title;

    private boolean watched;

    @Enumerated(EnumType.STRING)
    private Estimate estimate;

    @Column(name = "changed")
    private LocalDateTime changedDate;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @Column(name = "user_id")
//    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}