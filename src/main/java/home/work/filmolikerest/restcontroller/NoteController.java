package home.work.filmolikerest.restcontroller;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    public ResponseEntity<List<NoteDto>> getNotes() {
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
        Note foundNote = noteService.getById(id);

        if (foundNote.equals(Note.NULL_NOTE)) {
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
    public ResponseEntity<NoteDto> saveNote(@Valid @RequestBody NoteDto noteDto) {
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

    @PostMapping("/note2")
    public ResponseEntity<NoteDto> saveNote2(
            @Valid @RequestBody NoteDto noteDto
    ) {
        log.info("IN saveNote - Received note: title: {}, watched: {}, estimate: {}", noteDto.getTitle(), noteDto.getWatched(), noteDto.getEstimate());

        Note savedNote = noteService.save(noteDto.toNote());

        log.info("IN saveNote - Saved note: id: {}, title: {}, watched: {}, estimate: {}", savedNote.getId(), savedNote.getTitle(), savedNote.isWatched(), savedNote.getEstimate());

        try {
            return ResponseEntity
                    .created(new URI("/rest/note/" + savedNote.getId()))
                    .body(NoteDto.toDto(savedNote));
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



//
//    @PutMapping("/note/{id}")
//    public ResponseEntity<?> updateNote(
//            @PathVariable Long id,
//            @Valid @RequestBody NoteDto noteDto,
//            @AuthenticationPrincipal User user
//    ) {
//        Optional<Note> noteOptional = noteService.findById(id);
//
//        if (!noteOptional.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Note foundNote = noteOptional.get();
//
//        if (hasNotRights(foundNote, user)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет прав для изменения данных");
//        }
//
//        Note updatingNote = NoteConverter.toDomain(noteDto);
//        updatingNote.setId(id);
//
//        Note updatedNote = noteService.save(updatingNote, user);
//
//        try {
//            return ResponseEntity
//                    .ok()
//                    .location(new URI("/rest/note/" + updatedNote.getId()))
//                    .body(NoteConverter.toDto(updatedNote));
//        } catch (URISyntaxException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal User user)
    {
        boolean isDeleted = noteService.delete(id, user);

        if (isDeleted) {
            return ResponseEntity.ok().body("Note was deleted successful");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("Not found or No rights for deleting note with id %d", id));
        }
    }

    private boolean hasNotRights(Note note, User user) {
        return !user.equals(note.getUser());
    }
//

}
