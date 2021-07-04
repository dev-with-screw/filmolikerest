package home.work.filmolikerest.restcontroller;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class NoteController
{
    private final NoteService noteService;

    @GetMapping("/notes")
    public ResponseEntity<List<NoteDto>> getNotes()
    {
        try {
            final List<NoteDto> notes = noteService.getNotes().stream().map(NoteDto::toDto).collect(Collectors.toList());

            return ResponseEntity.ok()
                    .location((new URI("/notes")))
                    .body(notes);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable @Min(1) Long id)
    {
        final NoteDto foundNote = noteService.getById(id);

        if (foundNote.equals(NoteDto.NULL_NOTE)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Not found or No rights for viewing note with id %d", id));
        }

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/note/" + id))
                    .body(foundNote);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/note")
    public ResponseEntity<NoteDto> saveNote(@Valid @RequestBody NoteDto noteDto)
    {
        log.info("IN saveNote - Received note: title: {}, watched: {}, estimate: {}", noteDto.getTitle(), noteDto.getWatched(), noteDto.getEstimate());

        final Note note = noteDto.toNote();

        Note savedNote = noteService.save(note);

        log.info("IN saveNote - Saved note: id: {}, title: {}, watched: {}, estimate: {}", savedNote.getId(), savedNote.getTitle(), savedNote.isWatched(), savedNote.getEstimate());

        try {
            return ResponseEntity
                    .created(new URI("/rest/note/" + savedNote.getId()))
                    .body(NoteDto.toDto(savedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteDto noteDto)
    {

        Note updatedNote = noteService.update(id, noteDto.toNote());

        if (updatedNote.equals(Note.NULL_NOTE)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Not found or No rights for updating note with id %d", id));
        }

        try {
            return ResponseEntity
                    .ok()
                    .location(new URI("/rest/note/" + updatedNote.getId()))
                    .body(NoteDto.toDto(updatedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id)
    {
        boolean isDeleted = noteService.delete(id);

        if (isDeleted) {
            return ResponseEntity.ok().body("Note was deleted successful");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("Not found or No rights for deleting note with id %d", id));
        }
    }

}
