import React, { useState, useEffect } from 'react';
import { noteAPI } from '../services/api';
import NoteItem from './NoteItem';

const ArchivedNotes = () => {
  const [archivedNotes, setArchivedNotes] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchArchivedNotes = async () => {
    try {
      const response = await noteAPI.getArchivedNotes();
      setArchivedNotes(response.data);
    } catch (error) {
      console.error('Error fetching archived notes:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchArchivedNotes();
  }, []);

  const handleUpdate = async (id, updatedNote) => {
    try {
      await noteAPI.updateNote(id, updatedNote);
      fetchArchivedNotes();
    } catch (error) {
      console.error('Error updating note:', error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this archived note?')) {
      try {
        await noteAPI.deleteNote(id);
        fetchArchivedNotes();
      } catch (error) {
        console.error('Error deleting note:', error);
      }
    }
  };

  const handleUnarchive = async (id) => {
    try {
      await noteAPI.unarchiveNote(id);
      fetchArchivedNotes();
    } catch (error) {
      console.error('Error unarchiving note:', error);
    }
  };

  if (loading) {
    return <div className="loading">Loading archived notes...</div>;
  }

  return (
    <div className="archived-notes">
      <h2>Archived Notes ({archivedNotes.length})</h2>
      {archivedNotes.length === 0 ? (
        <p className="empty-message">No archived notes.</p>
      ) : (
        <div className="notes-grid">
          {archivedNotes.map((note) => (
            <NoteItem
              key={note.id}
              note={note}
              onUpdate={handleUpdate}
              onDelete={handleDelete}
              onUnarchive={handleUnarchive}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default ArchivedNotes;