import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const noteAPI = {
  getAllNotes: () => API.get('/notes'),
  getActiveNotes: () => API.get('/notes/active'),
  getArchivedNotes: () => API.get('/notes/archived'),
  createNote: (note) => API.post('/notes', note),
  updateNote: (id, note) => API.put(`/notes/${id}`, note),
  deleteNote: (id) => API.delete(`/notes/${id}`),
  archiveNote: (id) => API.post(`/notes/${id}/archive`),
  unarchiveNote: (id) => API.post(`/notes/${id}/unarchive`)
};