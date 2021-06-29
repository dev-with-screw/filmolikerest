package home.work.filmolikerest.service.impl;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.model.User;
import home.work.filmolikerest.repository.NoteRepository;
import home.work.filmolikerest.security.jwt.JwtAuthenticationException;
import home.work.filmolikerest.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }


//    public List<NoteDto> findByUser(User user) {
//        return null;
//    }


//    public Note save(Note note) {
//        return noteRepo.save(note);
//    }
//
    public Note save(Note note) {

        note.setUser(getUserFromContext());
        note.setChangedDate(LocalDateTime.now());

        log.info("IN save - Saving note: title: {}, watched: {}, estimate: {}", note.getTitle(), note.isWatched(), note.getEstimate());

        return noteRepository.save(note);
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

    //
//    public Note get(long id) {
//        return noteRepo.getOne(id);
//    }
//
    public Note getById(Long id) {

        return noteRepository.findByIdAndUser(id, getUserFromContext()).orElse(Note.NULL_NOTE);
    }
//
    public boolean delete(Long id, User user) {
        Optional<Note> optionalNote = noteRepository.findByIdAndUser(id, user);

        if (!optionalNote.isPresent()) {
            return false;
        }

        noteRepository.deleteById(id);

        return true;
    }
//
//    public NoteDto findByIdDto(Long id) {
//        Optional<Note> noteFromDb = noteRepo.findById(id);
//
//        return noteFromDb.map(NoteConverter::toDto).orElse(NoteConverter.NULL_NOTE);
//    }
//
//    public List<NoteDto> findAllDto() {
//        return noteRepo.findAll().stream()
//                .map(NoteConverter::toDto)
//                .collect(Collectors.toList());
//    }
//

    @Override
    public List<Note> getNotes() {
        return noteRepository.findByUser(getUserFromContext());
    }
}
