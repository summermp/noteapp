import React, { useState } from 'react';
import { FaEdit, FaArchive, FaTrash, FaUndo, FaSave, FaTimes } from 'react-icons/fa';

const NoteItem = ({ note, onUpdate, onDelete, onArchive, onUnarchive }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState(note.title);
  const [content, setContent] = useState(note.content);

  const handleSave = () => {
    onUpdate(note.id, { title, content, archived: note.archived });
    setIsEditing(false);
  };

  const handleCancel = () => {
    setTitle(note.title);
    setContent(note.content);
    setIsEditing(false);
  };

  return (
    <div className={`note-item ${note.archived ? 'archived' : ''}`}>
      {isEditing ? (
        <div className="edit-mode">
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="edit-input"
          />
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="edit-textarea"
            rows="3"
          />
          <div className="edit-actions">
            <button onClick={handleSave} className="save-btn">
              <FaSave /> Save
            </button>
            <button onClick={handleCancel} className="cancel-btn">
              <FaTimes /> Cancel
            </button>
          </div>
        </div>
      ) : (
        <>
          <div className="note-header">
            <h3>{note.title}</h3>
            <span className="note-date">
              {new Date(note.updatedAt).toLocaleDateString()}
            </span>
          </div>
          <p className="note-content">{note.content}</p>
          <div className="note-actions">
            <button onClick={() => setIsEditing(true)} className="edit-btn">
              <FaEdit /> Edit
            </button>
            {note.archived ? (
              <button onClick={() => onUnarchive(note.id)} className="unarchive-btn">
                <FaUndo /> Unarchive
              </button>
            ) : (
              <button onClick={() => onArchive(note.id)} className="archive-btn">
                <FaArchive /> Archive
              </button>
            )}
            <button onClick={() => onDelete(note.id)} className="delete-btn">
              <FaTrash /> Delete
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default NoteItem;