import React, { useState } from 'react';
import { interviewService } from '../services/interviewService';

export const ScheduleInterviewModal = ({ isOpen, onClose, applicationId, onScheduled }) => {
  const [formData, setFormData] = useState({
    interviewDate: '',
    interviewType: 'TECHNICAL',
    interviewer: '',
    notes: '',
    result: 'SCHEDULED'
  });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  if (!isOpen) return null;

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.interviewDate) {
      setError('Interview date and time is required.');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      const payload = {
        ...formData,
        interviewDate: new Date(formData.interviewDate).toISOString()
      };
      await interviewService.scheduleInterview(applicationId, payload);
      onScheduled();
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to schedule interview');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Schedule Interview Round</h2>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        {error && <div className="alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Interview Date & Time *</label>
            <input
              type="datetime-local"
              name="interviewDate"
              className="form-control"
              value={formData.interviewDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Interview Round Type *</label>
            <select
              name="interviewType"
              className="form-control"
              value={formData.interviewType}
              onChange={handleChange}
            >
              <option value="ONLINE_ASSESSMENT">Online Assessment</option>
              <option value="HR">HR Screen</option>
              <option value="TECHNICAL">Technical Round</option>
              <option value="MANAGERIAL">Managerial Round</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label>Interviewer Name / Role</label>
            <input
              type="text"
              name="interviewer"
              className="form-control"
              value={formData.interviewer}
              onChange={handleChange}
              placeholder="e.g. Alex (Engineering Manager)"
            />
          </div>

          <div className="form-group">
            <label>Preparation Notes & Topics</label>
            <textarea
              name="notes"
              rows="3"
              className="form-control"
              value={formData.notes}
              onChange={handleChange}
              placeholder="System design, Java concurrency, Spring DI questions..."
            />
          </div>

          <div className="modal-actions">
            <button type="button" className="btn btn-outline" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Scheduling...' : 'Save Interview'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
