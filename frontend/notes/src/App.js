import React, { useState } from 'react';
import { FaStickyNote, FaArchive, FaHome } from 'react-icons/fa';
import NoteForm from './components/NoteForm';
import NoteList from './components/NoteList';
import ArchivedNotes from './components/ArchivedNotes';
import { noteAPI } from './services/api';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('active');
  const [refreshKey, setRefreshKey] = useState(0);

  const handleCreateNote = async (note) => {
    try {
      await noteAPI.createNote(note);
      setRefreshKey(prev => prev + 1);
    } catch (error) {
      console.error('Error creating note:', error);
    }
  };

  return (
    <div className="App">
      <header className="app-header">
        <div className="header-content">
          <FaStickyNote className="header-icon" />
          <h1>Notes App</h1>
        </div>
        <p className="header-subtitle">Organize your thoughts efficiently</p>
      </header>

      <main className="app-main">
        <div className="container">
          <div className="sidebar">
            <NoteForm onSubmit={handleCreateNote} />
          </div>

          <div className="main-content">
            <div className="tabs">
              <button
                className={`tab ${activeTab === 'active' ? 'active' : ''}`}
                onClick={() => setActiveTab('active')}
              >
                <FaHome /> Active Notes
              </button>
              <button
                className={`tab ${activeTab === 'archived' ? 'active' : ''}`}
                onClick={() => setActiveTab('archived')}
              >
                <FaArchive /> Archived Notes
              </button>
            </div>

            <div className="tab-content">
              {activeTab === 'active' ? (
                <NoteList key={`active-${refreshKey}`} />
              ) : (
                <ArchivedNotes key={`archived-${refreshKey}`} />
              )}
            </div>
          </div>
        </div>
      </main>

      <footer className="app-footer">
        <p>Notes App &copy; {new Date().getFullYear()} - Full Stack Implementation</p>
      </footer>
    </div>
  );
}

export default App;