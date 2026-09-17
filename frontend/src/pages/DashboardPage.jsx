import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { dashboardService } from '../services/dashboardService';
import { StatusBadge } from '../components/StatusBadge';
import { useAuth } from '../context/AuthContext';

export const DashboardPage = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const data = await dashboardService.getStats();
        setStats(data);
      } catch (err) {
        console.error('Failed to load dashboard stats', err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return <div className="empty-state">Loading your dashboard...</div>;
  }

  const {
    totalApplications = 0,
    appliedThisMonth = 0,
    activeInterviews = 0,
    offers = 0,
    rejections = 0,
    inProgress = 0,
    statusCounts = {},
    upcomingInterviews = [],
    recentApplications = [],
  } = stats || {};

  const calculatePercentage = (count) => {
    if (!totalApplications || totalApplications === 0) return 0;
    return Math.round((count / totalApplications) * 100);
  };

  return (
    <div className="dashboard-container">
      <div className="page-header">
        <div>
          <h1>Welcome back, {user?.name?.split(' ')[0]} 👋</h1>
          <p>Here is an overview of your job search progress and upcoming milestones.</p>
        </div>
        <button className="btn btn-primary" onClick={() => navigate('/applications')}>
          Go to Applications
        </button>
      </div>

      <div className="kpi-grid">
        <div className="card kpi-card">
          <div className="kpi-icon">📁</div>
          <div className="kpi-content">
            <span className="kpi-label">Total Applications</span>
            <span className="kpi-value">{totalApplications}</span>
          </div>
        </div>

        <div className="card kpi-card">
          <div className="kpi-icon">📅</div>
          <div className="kpi-content">
            <span className="kpi-label">Applied This Month</span>
            <span className="kpi-value">{appliedThisMonth}</span>
          </div>
        </div>

        <div className="card kpi-card">
          <div className="kpi-icon">⏳</div>
          <div className="kpi-content">
            <span className="kpi-label">In Progress</span>
            <span className="kpi-value">{inProgress}</span>
          </div>
        </div>

        <div className="card kpi-card highlight-purple">
          <div className="kpi-icon">🎯</div>
          <div className="kpi-content">
            <span className="kpi-label">Active Interviews</span>
            <span className="kpi-value">{activeInterviews}</span>
          </div>
        </div>

        <div className="card kpi-card highlight-green">
          <div className="kpi-icon">🎉</div>
          <div className="kpi-content">
            <span className="kpi-label">Offers Received</span>
            <span className="kpi-value">{offers}</span>
          </div>
        </div>

        <div className="card kpi-card highlight-red">
          <div className="kpi-icon">❌</div>
          <div className="kpi-content">
            <span className="kpi-label">Rejections</span>
            <span className="kpi-value">{rejections}</span>
          </div>
        </div>
      </div>

      <div className="dashboard-grid">
        <div className="card">
          <h2 className="section-title">Application Status Breakdown</h2>
          <p className="section-subtitle">Progression across your active pipeline</p>

          <div className="status-bars">
            {[
              { key: 'APPLIED', label: 'Applied', color: '#0284c7' },
              { key: 'ONLINE_TEST', label: 'Online Test', color: '#d97706' },
              { key: 'INTERVIEW', label: 'Interviewing', color: '#7c3aed' },
              { key: 'OFFER', label: 'Offers', color: '#059669' },
              { key: 'REJECTED', label: 'Rejected', color: '#dc2626' },
              { key: 'WITHDRAWN', label: 'Withdrawn', color: '#64748b' },
            ].map(({ key, label, color }) => {
              const count = statusCounts[key] || 0;
              const percentage = calculatePercentage(count);
              return (
                <div key={key} className="status-bar-row">
                  <div className="status-bar-header">
                    <span>{label}</span>
                    <span className="status-bar-count">
                      {count} ({percentage}%)
                    </span>
                  </div>
                  <div className="progress-track">
                    <div
                      className="progress-fill"
                      style={{ width: `${percentage}%`, backgroundColor: color }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <div className="dashboard-side-col">
          <div className="card">
            <h2 className="section-title">Upcoming Interviews</h2>
            {upcomingInterviews.length === 0 ? (
              <p className="text-muted" style={{ padding: '1rem 0' }}>
                No upcoming interviews scheduled.
              </p>
            ) : (
              <div className="event-list">
                {upcomingInterviews.map((interview) => (
                  <div
                    key={interview.id}
                    className="event-item"
                    onClick={() => navigate(`/applications/${interview.applicationId}`)}
                  >
                    <div className="event-date">
                      {new Date(interview.interviewDate).toLocaleDateString(undefined, {
                        month: 'short',
                        day: 'numeric',
                      })}
                    </div>
                    <div className="event-details">
                      <strong>{interview.companyName}</strong>
                      <p>{interview.jobTitle} &bull; {interview.interviewType}</p>
                    </div>
                    <span className="badge badge-interview">Scheduled</span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="card" style={{ marginTop: '1.5rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h2 className="section-title">Recent Applications</h2>
              <Link to="/applications" style={{ fontSize: '0.875rem', color: 'var(--primary)' }}>
                View All &rarr;
              </Link>
            </div>
            {recentApplications.length === 0 ? (
              <p className="text-muted" style={{ padding: '1rem 0' }}>
                No applications submitted yet.
              </p>
            ) : (
              <div className="recent-list">
                {recentApplications.map((app) => (
                  <div
                    key={app.id}
                    className="recent-item"
                    onClick={() => navigate(`/applications/${app.id}`)}
                  >
                    <div>
                      <strong>{app.companyName}</strong>
                      <p className="text-muted">{app.jobTitle} &bull; {app.applicationDate}</p>
                    </div>
                    <StatusBadge status={app.status} />
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
