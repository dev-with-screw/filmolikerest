package home.work.filmolikerest.model;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
public class Note
{
    public final static Note NULL_NOTE = new Note();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 50)
    private String title;

    private boolean watched;

    @Enumerated(EnumType.STRING)
    private Estimate estimate;

    @Column(name = "changed")
    private LocalDateTime changedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}