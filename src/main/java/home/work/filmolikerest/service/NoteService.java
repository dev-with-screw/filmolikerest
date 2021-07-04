package home.work.filmolikerest.service;

import home.work.filmolikerest.dto.NoteDto;
import home.work.filmolikerest.model.Note;

import java.util.List;

public interface NoteService
{
    List<Note> getNotes();

    NoteDto getById(Long id);

    Note save(Note note);

    Note update(Long id, Note note);

    boolean delete(Long id);
}
