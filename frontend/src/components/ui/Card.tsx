import React from 'react';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  hoverable?: boolean;
}

export const Card: React.FC<CardProps> = ({ children, className = '', hoverable = false }) => {
  return (
    <div className={`glass-panel rounded-xl p-6 ${hoverable ? 'glass-panel-hover' : ''} ${className}`}>
      {children}
    </div>
  );
};

export const CardHeader: React.FC<{ children: React.ReactNode, className?: string }> = ({ children, className = '' }) => (
  <div className={`mb-4 border-b border-white/10 pb-4 ${className}`}>
    {children}
  </div>
);

export const CardTitle: React.FC<{ children: React.ReactNode, className?: string }> = ({ children, className = '' }) => (
  <h3 className={`text-xl font-semibold tracking-tight text-white ${className}`}>
    {children}
  </h3>
);
