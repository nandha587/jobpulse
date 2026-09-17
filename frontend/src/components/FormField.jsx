import React from 'react';

export const FormField = ({ label, error, required, children }) => {
  return (
    <div className="form-group">
      {label && (
        <label>
          {label} {required && <span style={{ color: 'var(--danger)' }}>*</span>}
        </label>
      )}
      {children}
      {error && <div className="form-error">{error}</div>}
    </div>
  );
};
