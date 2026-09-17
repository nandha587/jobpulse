import React from 'react';

const STATUS_LABELS = {
  APPLIED: 'Applied',
  ONLINE_TEST: 'Online Test',
  INTERVIEW: 'Interview',
  OFFER: 'Offer 🎉',
  REJECTED: 'Rejected',
  WITHDRAWN: 'Withdrawn',
};

export const StatusBadge = ({ status }) => {
  const className = `badge badge-${status?.toLowerCase()}`;
  return <span className={className}>{STATUS_LABELS[status] || status}</span>;
};
