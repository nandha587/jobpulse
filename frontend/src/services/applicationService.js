import api from './api';

export const applicationService = {
  getApplications: async (params = {}) => {
    const response = await api.get('/applications', { params });
    return response.data;
  },
  getApplicationById: async (id) => {
    const response = await api.get(`/applications/${id}`);
    return response.data;
  },
  createApplication: async (data) => {
    const response = await api.post('/applications', data);
    return response.data;
  },
  updateApplication: async (id, data) => {
    const response = await api.put(`/applications/${id}`, data);
    return response.data;
  },
  updateStatus: async (id, status) => {
    const response = await api.patch(`/applications/${id}/status`, { status });
    return response.data;
  },
  deleteApplication: async (id) => {
    await api.delete(`/applications/${id}`);
  },
  getStatusHistory: async (id) => {
    const response = await api.get(`/applications/${id}/history`);
    return response.data;
  }
};
