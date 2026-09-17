import api from './api';

export const noteService = {
  addNote: async (applicationId, content) => {
    const response = await api.post(`/applications/${applicationId}/notes`, { content });
    return response.data;
  },
  getNotes: async (applicationId) => {
    const response = await api.get(`/applications/${applicationId}/notes`);
    return response.data;
  },
  deleteNote: async (noteId) => {
    await api.delete(`/notes/${noteId}`);
  }
};
