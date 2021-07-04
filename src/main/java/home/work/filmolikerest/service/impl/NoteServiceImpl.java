package home.work.filmolikerest.service.impl;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.NoteRepository;
import home.work.filmolikerest.security.jwt.JwtAuthenticationException;
import home.work.filmolikerest.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService
{
    private final NoteRepository noteRepository;

    @Override
    public List<Note> getNotes() {
        return noteRepository.findByUser(getUserFromContext());
    }

    public NoteDto getById(Long id) {

        final Note note = noteRepository.findByIdAndUser(id, getUserFromContext()).orElse(Note.NULL_NOTE);
        if (note.equals(Note.NULL_NOTE)) {
            return NoteDto.NULL_NOTE;
        } else {
            return NoteDto.toDto(note);
        }
    }

    public Note save(Note note) {

        note.setUser(getUserFromContext());
        note.setChangedDate(LocalDateTime.now());

        log.info("IN save - Saving note: title: {}, watched: {}, estimate: {}", note.getTitle(), note.isWatched(), note.getEstimate());

        return noteRepository.save(note);
    }

    public Note update(Long id, Note note) {

        final Note updatingNote = noteRepository.findByIdAndUser(id, getUserFromContext()).orElse(Note.NULL_NOTE);

        if (updatingNote.equals(Note.NULL_NOTE)) {
            return Note.NULL_NOTE;
        }

        updatingNote.setTitle(note.getTitle());
        updatingNote.setWatched(note.isWatched());
        updatingNote.setEstimate(note.getEstimate());

        updatingNote.setChangedDate(LocalDateTime.now());

        return noteRepository.save(updatingNote);
    }

    public boolean delete(Long id) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUser(id, getUserFromContext());

        if (!optionalNote.isPresent()) {
            return false;
        }

        noteRepository.deleteById(id);

        return true;
    }

    private User getUserFromContext() {
        final Optional<User> user = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(p -> ((User) p));

        if (user.isPresent()) {
            log.info("IN getUserFromContext: user id {}", user.get().getId());
            return user.get();
        } else {
            throw new JwtAuthenticationException("");
        }
    }
}
