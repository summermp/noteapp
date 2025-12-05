import React, { useState, useEffect } from 'react';
import { noteAPI } from '../services/api';
import NoteItem from './NoteItem';

const NoteList = () => {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchNotes = async () => {
    try {
      const response = await noteAPI.getActiveNotes();
      setNotes(response.data);
    } catch (error) {
      console.error('Error fetching notes:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotes();
  }, []);

  const handleUpdate = async (id, updatedNote) => {
    try {
      await noteAPI.updateNote(id, updatedNote);
      fetchNotes();
    } catch (error) {
      console.error('Error updating note:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this note?')) {
      try {
        await noteAPI.deleteNote(id);
        fetchNotes();
      } catch (error) {
        console.error('Error deleting note:', error);
      }
    }
  };

  const handleArchive = async (id) => {
    try {
      await noteAPI.archiveNote(id);
      fetchNotes();
    } catch (error) {
      console.error('Error archiving note:', error);
    }
  };

  const handleUnarchive = async (id) => {
    try {
      await noteAPI.unarchiveNote(id);
      fetchNotes();
    } catch (error) {
      console.error('Error unarchiving note:', error);
    }
  };

  if (loading) {
    return <div className="loading">Loading notes...</div>;
  }

  return (
    <div className="note-list">
      <h2>Active Notes ({notes.length})</h2>
      {notes.length === 0 ? (
        <p className="empty-message">No active notes. Create one!</p>
      ) : (
        <div className="notes-grid">
          {notes.map((note) => (
            <NoteItem
              key={note.id}
              note={note}
              onUpdate={handleUpdate}
              onDelete={handleDelete}
              onArchive={handleArchive}
              onUnarchive={handleUnarchive}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default NoteList;