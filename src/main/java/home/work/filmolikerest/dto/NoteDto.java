package home.work.filmolikerest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import home.work.filmolikerest.model.Estimate;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.validation.EnumValidator;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NoteDto
{
    private Long id;

    @NotBlank(message = "Shouldn't be empty")
    @Length(max=50, message = "Should contain no more than 50 characters")
    private String title;

    @NotNull
    @Pattern(regexp = "^true$|^false$", message = "Should be true or false")
    private String watched;

    @EnumValidator(enumClazz = Estimate.class, message = "Should contain value from enum Estimate")
    private String estimate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changed;

    public NoteDto(@NotBlank(message = "Shouldn't be empty") @Length(max = 50, message = "Should contain no more than 50 characters") String title, @NotNull @Pattern(regexp = "^true$|^false$", message = "Should be true or false") String watched, String estimate) {
        this.title = title;
        this.watched = watched;
        this.estimate = estimate;
    }

    public static final NoteDto NULL_NOTE = new NoteDto();

    public Note toNote() {
        Note note = new Note();

        note.setTitle(title);
        note.setWatched(watched.equals("true"));
        note.setEstimate(Estimate.valueOf(estimate));

        return note;
    }

    public static NoteDto toDto(Note note) {
        NoteDto noteDto = new NoteDto();

        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setWatched(note.isWatched() ? "true" : "false");
        noteDto.setEstimate(note.getEstimate().toString());
        noteDto.setChanged(note.getChangedDate());

        return noteDto;
    }
}
