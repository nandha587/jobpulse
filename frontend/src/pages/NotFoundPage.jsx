import React from 'react';
import { Link } from 'react-router-dom';

export const NotFoundPage = () => {
  return (
    <div className="empty-state" style={{ marginTop: '4rem' }}>
      <h1 style={{ fontSize: '4rem', fontWeight: '800', color: 'var(--primary)' }}>404</h1>
      <h2>Page Not Found</h2>
      <p style={{ margin: '1rem 0 2rem' }}>
        The page you are looking for doesn't exist or has been moved.
      </p>
      <Link to="/dashboard" className="btn btn-primary">
        Return to Dashboard
      </Link>
    </div>
  );
};
