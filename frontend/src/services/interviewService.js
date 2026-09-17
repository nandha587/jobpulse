import api from './api';

export const interviewService = {
  scheduleInterview: async (applicationId, data) => {
    const response = await api.post(`/applications/${applicationId}/interviews`, data);
    return response.data;
  },
  getInterviews: async (applicationId) => {
    const response = await api.get(`/applications/${applicationId}/interviews`);
    return response.data;
  },
  updateInterview: async (interviewId, data) => {
    const response = await api.put(`/interviews/${interviewId}`, data);
    return response.data;
  },
  deleteInterview: async (interviewId) => {
    await api.delete(`/interviews/${interviewId}`);
  }
};
