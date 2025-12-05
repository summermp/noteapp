package com.notes.api.service;

import com.notes.api.entity.Category;
import com.notes.api.entity.Note;
import com.notes.api.repository.CategoryRepository;
import com.notes.api.repository.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    public Optional<Note> addCategoryToNote(Long noteId, Long categoryId) {
        return noteRepository.findById(noteId).flatMap(note ->
            categoryRepository.findById(categoryId).map(category -> {
                note.addCategory(category);
                return noteRepository.save(note);
            })
        );
    }

    public Optional<Note> removeCategoryFromNote(Long noteId, Long categoryId) {
        return noteRepository.findById(noteId).map(note -> {
           categoryRepository.findById(categoryId).ifPresent(note::removeCategory);
           return noteRepository.save(note);
        });
    }

    public List<Note> getAllNotesByCategory(String categoryName) {
        return categoryRepository.findByName(categoryName).
                map(Category::getNotes)
                .orElse(new HashSet<>())
                .stream()
                .collect(Collectors.toList());
    }

    public Note createNoteWithCategories(Note note, Set<String> categoryNames) {
        Set<Category> categories = new HashSet<>();
        for(String categoryName : categoryNames) {
            Category category = categoryRepository.findByName(categoryName).
            orElseGet(
            ()-> categoryRepository.save(
                Category.builder().name(categoryName).build()
                )
            );
            categories.add(category);
        }
        note.setCategories(categories);
        return noteRepository.save(note);
    }

    public Optional<Note> updateNoteWithCategories(Long id, Note noteDetails, Set<String> categoryNames) {
        return noteRepository.findById(id).
                map(note -> {
                    note.setTitle(noteDetails.getTitle());
                    note.setContent(noteDetails.getContent());
                    Set<Category> categories = new HashSet<>();
                    for(String categoryName : categoryNames) {
                        Category category = categoryRepository.findByName(categoryName).
                        orElseGet(
                        ()-> categoryRepository.save(
                            Category.builder().name(categoryName).build()
                            )
                        );
                    }
                    note.setCategories(categories);
                    return noteRepository.save(note);
                });
    }

    public Note createNote(Note note) {
        if (note.getCategories() == null || note.getCategories().isEmpty()) {
            note.setCategories(new HashSet<>());
        }
        return noteRepository.save(note);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> getAllActiveNotes() {
        return noteRepository.findByArchivedFalse();
    }
    public List<Note> getAllArchivedNotes() {
        return noteRepository.findByArchivedTrue();
    }

    public Optional<Note> updateNote(Long id, Note noteDetails) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(noteDetails.getTitle());
            note.setContent(noteDetails.getContent());
            return noteRepository.save(note);
        });
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Optional<Note> archiveNote(Long id) {
        return noteRepository.findById(id).map(note -> {
            note.setArchived(true);
            return noteRepository.save(note);
        });
    }

    public Optional<Note> unArchiveNote(Long id) {
        return noteRepository.findById(id).map(note -> {
            note.setArchived(false);
            return noteRepository.save(note);
        });
    }

}
