package com.notes.api.controller;

import com.notes.api.dto.NoteDTO;
import com.notes.api.entity.Category;
import com.notes.api.entity.Note;
import com.notes.api.service.CategoryService;
import com.notes.api.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@Slf4j
//@CrossOrigin(origins = "http://localhost:3000")  // Specify your frontend URL
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/{noteId}/categories/{categoryId}")
    public ResponseEntity<NoteDTO> addCategoryToNote(@PathVariable Long noteId, @PathVariable Long categoryId){
        return noteService.addCategoryToNote(noteId, categoryId).
                map(this::convertToDTO).
                map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{noteId}/categories/{categoryId}")
    public ResponseEntity<NoteDTO> removeCategoryFromNote(@PathVariable Long noteId, @PathVariable Long categoryId){
        return noteService.removeCategoryFromNote(noteId, categoryId).map(this::convertToDTO).
                map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<NoteDTO>> getNotesByCategory(@PathVariable String categoryName){
        List<NoteDTO> notes = noteService.getAllNotesByCategory(categoryName).stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/with-categories")
    public ResponseEntity<List<NoteDTO>> createNoteWithCategories(
            @RequestBody NoteDTO noteDTO,
            @RequestParam Set<String> categories){
            Note note = Note.builder().
                    title(noteDTO.getTitle())
                    .content(noteDTO.getContent())
                    .build();

            Note createNote = noteService.createNoteWithCategories(note, categories);
            return ResponseEntity.ok(List.of(convertToDTO(createNote)));
    }

    private NoteDTO convertToDTO(Note note) {
        Set<Category> noteCategories = note.getCategories();
        if(noteCategories == null){
            noteCategories = new HashSet<>();
        }
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
//                .archived(note.isArchived())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .categories(note.getCategories().stream()
                        .map(category -> categoryService.convertToDTO(category))
                        .collect(Collectors.toSet()))
                .build();
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody(required = false) NoteDTO noteDTO){
        log.info("=== CREATE NOTE ENDPOINT HIT ===");

        if (noteDTO == null) {
            log.error("ERROR: Request body is null or empty!");
            // Now test if the endpoint is being hit
            return ResponseEntity.badRequest().body(null);
        }

        // Use archived from DTO or default to false
        Note note = Note.builder()
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .build();

        Note createdNote = noteService.createNote(note);
        return ResponseEntity.ok(convertToDTO(createdNote));
    }

    @GetMapping("/active")
    public ResponseEntity<List<NoteDTO>> getAllActiveNotes(){
        List<NoteDTO> notes = noteService.getAllActiveNotes().stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<NoteDTO>> getAllArchivedNotes(){
        List<NoteDTO> notes = noteService.getAllArchivedNotes().stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO){
        Note note = new Note(noteDTO.getTitle(), noteDTO.getContent());
        return noteService.updateNote(id, note).map(updatedNote -> ResponseEntity.ok(convertToDTO(updatedNote))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id){
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<NoteDTO> archiveNote(@PathVariable Long id){
        return noteService.archiveNote(id).map(archivedNote -> ResponseEntity.ok(convertToDTO(archivedNote))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/unarchive")
    public ResponseEntity<NoteDTO> unArchiveNote(@PathVariable Long id){
        return noteService.unArchiveNote(id).map(unArchivedNote -> ResponseEntity.ok(convertToDTO(unArchivedNote))).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
