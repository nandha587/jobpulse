import React, { useState, useEffect } from 'react';

export const ApplicationModal = ({ isOpen, onClose, onSave, application }) => {
  const initialFormState = {
    companyName: '',
    jobTitle: '',
    location: '',
    jobType: 'FULL_TIME',
    applicationDate: new Date().toISOString().split('T')[0],
    jobUrl: '',
    source: '',
    status: 'APPLIED',
    priority: 'MEDIUM',
    salaryInfo: '',
    notesSummary: '',
  };

  const [formData, setFormData] = useState(initialFormState);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (application) {
      setFormData({
        companyName: application.companyName || '',
        jobTitle: application.jobTitle || '',
        location: application.location || '',
        jobType: application.jobType || 'FULL_TIME',
        applicationDate: application.applicationDate || new Date().toISOString().split('T')[0],
        jobUrl: application.jobUrl || '',
        source: application.source || '',
        status: application.status || 'APPLIED',
        priority: application.priority || 'MEDIUM',
        salaryInfo: application.salaryInfo || '',
        notesSummary: application.notesSummary || '',
      });
    } else {
      setFormData(initialFormState);
    }
    setError('');
  }, [application, isOpen]);

  if (!isOpen) return null;

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.companyName || !formData.jobTitle) {
      setError('Company name and Job title are required.');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      await onSave(formData);
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save application');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <div className="modal-header">
          <h2>{application ? 'Edit Application' : 'Add New Application'}</h2>
          <button className="modal-close" onClick={onClose}>&times;</button>
        </div>

        {error && <div className="alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-grid">
            <div className="form-group">
              <label>Company Name *</label>
              <input
                type="text"
                name="companyName"
                className="form-control"
                value={formData.companyName}
                onChange={handleChange}
                placeholder="Google, Microsoft, etc."
                required
              />
            </div>

            <div className="form-group">
              <label>Job Title *</label>
              <input
                type="text"
                name="jobTitle"
                className="form-control"
                value={formData.jobTitle}
                onChange={handleChange}
                placeholder="Junior Backend Developer"
                required
              />
            </div>

            <div className="form-group">
              <label>Location</label>
              <input
                type="text"
                name="location"
                className="form-control"
                value={formData.location}
                onChange={handleChange}
                placeholder="Remote, Bengaluru, etc."
              />
            </div>

            <div className="form-group">
              <label>Job Type</label>
              <select name="jobType" className="form-control" value={formData.jobType} onChange={handleChange}>
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
                <option value="INTERNSHIP">Internship</option>
                <option value="CONTRACT">Contract</option>
              </select>
            </div>

            <div className="form-group">
              <label>Application Date *</label>
              <input
                type="date"
                name="applicationDate"
                className="form-control"
                value={formData.applicationDate}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Status</label>
              <select name="status" className="form-control" value={formData.status} onChange={handleChange}>
                <option value="APPLIED">Applied</option>
                <option value="ONLINE_TEST">Online Test</option>
                <option value="INTERVIEW">Interview</option>
                <option value="OFFER">Offer</option>
                <option value="REJECTED">Rejected</option>
                <option value="WITHDRAWN">Withdrawn</option>
              </select>
            </div>

            <div className="form-group">
              <label>Priority</label>
              <select name="priority" className="form-control" value={formData.priority} onChange={handleChange}>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            </div>

            <div className="form-group">
              <label>Source</label>
              <input
                type="text"
                name="source"
                className="form-control"
                value={formData.source}
                onChange={handleChange}
                placeholder="LinkedIn, Referral, etc."
              />
            </div>

            <div className="form-group">
              <label>Salary Info</label>
              <input
                type="text"
                name="salaryInfo"
                className="form-control"
                value={formData.salaryInfo}
                onChange={handleChange}
                placeholder="e.g., ₹12 LPA or $80,000"
              />
            </div>

            <div className="form-group">
              <label>Job Posting URL</label>
              <input
                type="url"
                name="jobUrl"
                className="form-control"
                value={formData.jobUrl}
                onChange={handleChange}
                placeholder="https://..."
              />
            </div>
          </div>

          <div className="form-group">
            <label>Notes / Overview</label>
            <textarea
              name="notesSummary"
              rows="3"
              className="form-control"
              value={formData.notesSummary}
              onChange={handleChange}
              placeholder="Initial notes, recruiter details, etc."
            />
          </div>

          <div className="modal-actions">
            <button type="button" className="btn btn-outline" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Saving...' : application ? 'Update Application' : 'Add Application'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
