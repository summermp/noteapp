package com.notes.api.repository;

import com.notes.api.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByArchivedFalse();
    List<Note> findByArchivedTrue();
}
