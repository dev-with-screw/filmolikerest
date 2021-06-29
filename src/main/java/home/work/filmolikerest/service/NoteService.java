package home.work.filmolikerest.service;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;
import home.work.filmolikerest.model.User;

import java.util.List;

public interface NoteService {
    List<Note> getNotes();
    Note getById(Long id);
    Note save(Note note);

    boolean delete(Long id, User user);
}
