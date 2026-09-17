import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { applicationService } from '../services/applicationService';
import { StatusBadge } from '../components/StatusBadge';
import { ApplicationModal } from '../components/ApplicationModal';

export const ApplicationsPage = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedApp, setSelectedApp] = useState(null);

  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const navigate = useNavigate();

  const fetchApplications = useCallback(async () => {
    setLoading(true);
    try {
      const data = await applicationService.getApplications({
        page,
        size: 10,
        search: search || undefined,
        status: statusFilter || undefined,
        priority: priorityFilter || undefined,
        sortBy: 'applicationDate',
        sortDir: 'desc',
      });
      setApplications(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.error('Failed to load applications', err);
    } finally {
      setLoading(false);
    }
  }, [page, search, statusFilter, priorityFilter]);

  useEffect(() => {
    fetchApplications();
  }, [fetchApplications]);

  const handleOpenAddModal = () => {
    setSelectedApp(null);
    setModalOpen(true);
  };

  const handleOpenEditModal = (app, e) => {
    e.stopPropagation();
    setSelectedApp(app);
    setModalOpen(true);
  };

  const handleSave = async (formData) => {
    if (selectedApp) {
      await applicationService.updateApplication(selectedApp.id, formData);
    } else {
      await applicationService.createApplication(formData);
    }
    fetchApplications();
  };

  const handleDelete = async (id, company, e) => {
    e.stopPropagation();
    if (window.confirm(`Are you sure you want to delete the application for ${company}?`)) {
      try {
        await applicationService.deleteApplication(id);
        fetchApplications();
      } catch (err) {
        alert('Failed to delete application.');
      }
    }
  };

  const handleStatusChange = async (id, newStatus, e) => {
    e.stopPropagation();
    try {
      await applicationService.updateStatus(id, newStatus);
      fetchApplications();
    } catch (err) {
      alert('Failed to update status');
    }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1>Job Applications</h1>
          <p>Manage and track your active job pipeline ({totalElements} total)</p>
        </div>
        <button className="btn btn-primary" onClick={handleOpenAddModal}>
          + Add Application
        </button>
      </div>

      <div className="card toolbar">
        <input
          type="text"
          className="form-control search-input"
          placeholder="Search by company or role..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(0);
          }}
        />

        <select
          className="form-control filter-select"
          value={statusFilter}
          onChange={(e) => {
            setStatusFilter(e.target.value);
            setPage(0);
          }}
        >
          <option value="">All Statuses</option>
          <option value="APPLIED">Applied</option>
          <option value="ONLINE_TEST">Online Test</option>
          <option value="INTERVIEW">Interview</option>
          <option value="OFFER">Offer</option>
          <option value="REJECTED">Rejected</option>
          <option value="WITHDRAWN">Withdrawn</option>
        </select>

        <select
          className="form-control filter-select"
          value={priorityFilter}
          onChange={(e) => {
            setPriorityFilter(e.target.value);
            setPage(0);
          }}
        >
          <option value="">All Priorities</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
        </select>
      </div>

      <div className="card table-container">
        {loading ? (
          <div className="empty-state">Loading applications...</div>
        ) : applications.length === 0 ? (
          <div className="empty-state">
            <h3>No applications found</h3>
            <p>Try clearing your filters or add your first application.</p>
            <button className="btn btn-primary" style={{ marginTop: '1rem' }} onClick={handleOpenAddModal}>
              Add Application
            </button>
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Company</th>
                <th>Role</th>
                <th>Location</th>
                <th>Date Applied</th>
                <th>Priority</th>
                <th>Status</th>
                <th style={{ textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id} onClick={() => navigate(`/applications/${app.id}`)} className="clickable-row">
                  <td className="company-cell">
                    <strong>{app.companyName}</strong>
                    {app.jobUrl && (
                      <a
                        href={app.jobUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="external-link"
                        onClick={(e) => e.stopPropagation()}
                      >
                        ↗
                      </a>
                    )}
                  </td>
                  <td>{app.jobTitle}</td>
                  <td>{app.location || '—'}</td>
                  <td>{app.applicationDate}</td>
                  <td>
                    <span className={`badge badge-${app.priority?.toLowerCase()}`}>
                      {app.priority}
                    </span>
                  </td>
                  <td>
                    <select
                      value={app.status}
                      className="status-dropdown"
                      onClick={(e) => e.stopPropagation()}
                      onChange={(e) => handleStatusChange(app.id, e.target.value, e)}
                    >
                      <option value="APPLIED">Applied</option>
                      <option value="ONLINE_TEST">Online Test</option>
                      <option value="INTERVIEW">Interview</option>
                      <option value="OFFER">Offer</option>
                      <option value="REJECTED">Rejected</option>
                      <option value="WITHDRAWN">Withdrawn</option>
                    </select>
                  </td>
                  <td style={{ textAlign: 'right' }}>
                    <button
                      className="btn btn-outline btn-sm"
                      onClick={(e) => handleOpenEditModal(app, e)}
                      style={{ marginRight: '0.5rem' }}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-danger btn-sm"
                      onClick={(e) => handleDelete(app.id, app.companyName, e)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {totalPages > 1 && (
          <div className="pagination">
            <button
              className="btn btn-outline btn-sm"
              disabled={page === 0}
              onClick={() => setPage((p) => Math.max(0, p - 1))}
            >
              Previous
            </button>
            <span>
              Page {page + 1} of {totalPages}
            </span>
            <button
              className="btn btn-outline btn-sm"
              disabled={page >= totalPages - 1}
              onClick={() => setPage((p) => p + 1)}
            >
              Next
            </button>
          </div>
        )}
      </div>

      <ApplicationModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        onSave={handleSave}
        application={selectedApp}
      />
    </div>
  );
};
