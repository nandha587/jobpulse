import api from './api';

export const emailSyncService = {
  getConfig: async () => {
    const response = await api.get('/email-sync/config');
    return response.data;
  },

  saveConfig: async (configData) => {
    const response = await api.post('/email-sync/config', configData);
    return response.data;
  },

  testConnection: async () => {
    const response = await api.post('/email-sync/test-connection');
    return response.data;
  },

  syncNow: async () => {
    const response = await api.post('/email-sync/sync');
    return response.data;
  },

  simulate: async (simulateData) => {
    const response = await api.post('/email-sync/simulate', simulateData);
    return response.data;
  },
};
