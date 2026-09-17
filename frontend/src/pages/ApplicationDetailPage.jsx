import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { applicationService } from '../services/applicationService';
import { interviewService } from '../services/interviewService';
import { noteService } from '../services/noteService';
import { StatusBadge } from '../components/StatusBadge';
import { ScheduleInterviewModal } from '../components/ScheduleInterviewModal';

export const ApplicationDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [application, setApplication] = useState(null);
  const [history, setHistory] = useState([]);
  const [interviews, setInterviews] = useState([]);
  const [notes, setNotes] = useState([]);
  const [newNote, setNewNote] = useState('');
  const [loading, setLoading] = useState(true);
  const [interviewModalOpen, setInterviewModalOpen] = useState(false);

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [appData, histData, intData, notesData] = await Promise.all([
        applicationService.getApplicationById(id),
        applicationService.getStatusHistory(id),
        interviewService.getInterviews(id),
        noteService.getNotes(id)
      ]);
      setApplication(appData);
      setHistory(histData);
      setInterviews(intData);
      setNotes(notesData);
    } catch (err) {
      console.error('Failed to load application details', err);
      navigate('/applications');
    } finally {
      setLoading(false);
    }
  }, [id, navigate]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleStatusChange = async (newStatus) => {
    try {
      await applicationService.updateStatus(id, newStatus);
      loadData();
    } catch (err) {
      alert('Failed to update status');
    }
  };

  const handleAddNote = async (e) => {
    e.preventDefault();
    if (!newNote.trim()) return;

    try {
      await noteService.addNote(id, newNote);
      setNewNote('');
      const updatedNotes = await noteService.getNotes(id);
      setNotes(updatedNotes);
    } catch (err) {
      alert('Failed to add note');
    }
  };

  const handleDeleteNote = async (noteId) => {
    try {
      await noteService.deleteNote(noteId);
      setNotes(notes.filter((n) => n.id !== noteId));
    } catch (err) {
      alert('Failed to delete note');
    }
  };

  const handleUpdateInterviewResult = async (interview, newResult) => {
    try {
      await interviewService.updateInterview(interview.id, {
        interviewDate: interview.interviewDate,
        interviewType: interview.interviewType,
        interviewer: interview.interviewer,
        notes: interview.notes,
        result: newResult
      });
      loadData();
    } catch (err) {
      alert('Failed to update interview result');
    }
  };

  if (loading) {
    return <div className="empty-state">Loading application details...</div>;
  }

  return (
    <div className="details-container">
      <button className="btn btn-outline btn-sm" onClick={() => navigate('/applications')}>
        &larr; Back to Applications
      </button>

      <div className="card details-header-card">
        <div className="details-header-main">
          <h1>{application.companyName}</h1>
          <p className="job-role-text">{application.jobTitle}</p>
          <div className="meta-tags">
            <span>📍 {application.location || 'Location Not Specified'}</span>
            <span>💼 {application.jobType}</span>
            <span>📅 Applied: {application.applicationDate}</span>
            {application.salaryInfo && <span>💰 {application.salaryInfo}</span>}
          </div>
        </div>

        <div className="details-header-status">
          <label>Current Status</label>
          <select
            value={application.status}
            className="status-dropdown"
            onChange={(e) => handleStatusChange(e.target.value)}
          >
            <option value="APPLIED">Applied</option>
            <option value="ONLINE_TEST">Online Test</option>
            <option value="INTERVIEW">Interview</option>
            <option value="OFFER">Offer</option>
            <option value="REJECTED">Rejected</option>
            <option value="WITHDRAWN">Withdrawn</option>
          </select>
          {application.jobUrl && (
            <a href={application.jobUrl} target="_blank" rel="noreferrer" className="btn btn-outline btn-sm" style={{ marginTop: '0.5rem' }}>
              View Posting ↗
            </a>
          )}
        </div>
      </div>

      <div className="details-grid">
        <div className="details-main-col">
          <div className="card">
            <div className="section-header-flex">
              <h2>Interview Rounds ({interviews.length})</h2>
              <button className="btn btn-primary btn-sm" onClick={() => setInterviewModalOpen(true)}>
                + Schedule Round
              </button>
            </div>

            {interviews.length === 0 ? (
              <p className="text-muted" style={{ padding: '1rem 0' }}>
                No interviews scheduled yet. Click "+ Schedule Round" to add one.
              </p>
            ) : (
              <div className="interview-cards-list">
                {interviews.map((item) => (
                  <div key={item.id} className="interview-card">
                    <div className="interview-card-header">
                      <div>
                        <strong>{item.interviewType} Round</strong>
                        <span className="interview-time">
                          📅 {new Date(item.interviewDate).toLocaleString()}
                        </span>
                      </div>
                      <select
                        value={item.result}
                        className="status-dropdown"
                        onChange={(e) => handleUpdateInterviewResult(item, e.target.value)}
                      >
                        <option value="SCHEDULED">Scheduled</option>
                        <option value="PASSED">Passed ✅</option>
                        <option value="FAILED">Failed ❌</option>
                        <option value="CANCELLED">Cancelled</option>
                      </select>
                    </div>
                    {item.interviewer && (
                      <p className="interviewer-name">Interviewer: {item.interviewer}</p>
                    )}
                    {item.notes && <p className="interview-notes">{item.notes}</p>}
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="card" style={{ marginTop: '1.5rem' }}>
            <h2>Notes & Follow-ups</h2>

            <form onSubmit={handleAddNote} style={{ marginTop: '1rem' }}>
              <div className="form-group">
                <textarea
                  className="form-control"
                  rows="3"
                  placeholder="Add a new note..."
                  value={newNote}
                  onChange={(e) => setNewNote(e.target.value)}
                />
              </div>
              <button type="submit" className="btn btn-primary btn-sm">Add Note</button>
            </form>

            <div className="notes-feed">
              {notes.length === 0 ? (
                <p className="text-muted" style={{ padding: '1rem 0' }}>No notes added yet.</p>
              ) : (
                notes.map((note) => (
                  <div key={note.id} className="note-item">
                    <div className="note-content">{note.content}</div>
                    <div className="note-footer">
                      <span>{new Date(note.createdAt).toLocaleString()}</span>
                      <button
                        className="btn-text-danger"
                        onClick={() => handleDeleteNote(note.id)}
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>

        <div className="details-side-col">
          <div className="card">
            <h2>Status History Audit</h2>
            <p className="section-subtitle">Chronological timeline of changes</p>

            <div className="timeline">
              {history.map((h) => (
                <div key={h.id} className="timeline-item">
                  <div className="timeline-marker" />
                  <div className="timeline-content">
                    <div className="timeline-transition">
                      {h.previousStatus ? (
                        <>
                          <StatusBadge status={h.previousStatus} /> &rarr; <StatusBadge status={h.newStatus} />
                        </>
                      ) : (
                        <>Created as <StatusBadge status={h.newStatus} /></>
                      )}
                    </div>
                    <span className="timeline-date">
                      {new Date(h.changedAt).toLocaleString()}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <ScheduleInterviewModal
        isOpen={interviewModalOpen}
        onClose={() => setInterviewModalOpen(false)}
        applicationId={id}
        onScheduled={loadData}
      />
    </div>
  );
};
