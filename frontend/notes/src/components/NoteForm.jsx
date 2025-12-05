import React, { useState } from 'react';
import { FaPlus } from 'react-icons/fa';

const NoteForm = ({ onSubmit }) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (title.trim() && content.trim()) {
      onSubmit({ title, content });
      setTitle('');
      setContent('');
    }
  };

  return (
    <div className="note-form">
      <h2><FaPlus /> Create New Note</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <input
            type="text"
            placeholder="Note title..."
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="form-input"
            required
          />
        </div>
        <div className="form-group">
          <textarea
            placeholder="Note content..."
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="form-textarea"
            rows="4"
            required
          />
        </div>
        <button type="submit" className="submit-btn">
          Add Note
        </button>
      </form>
    </div>
  );
};

export default NoteForm;