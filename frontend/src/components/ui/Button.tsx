import React, { ButtonHTMLAttributes } from 'react';
import { Loader2 } from 'lucide-react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  isLoading?: boolean;
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  fullWidth?: boolean;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  isLoading = false,
  variant = 'primary',
  fullWidth = false,
  className = '',
  disabled,
  ...props
}) => {
  const baseStyles = 'inline-flex items-center justify-center rounded-md text-sm font-medium transition-all duration-300 focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed';
  
  const variants = {
    primary: 'bg-blue-600 text-white hover:bg-blue-700 hover:shadow-[0_0_20px_rgba(37,99,235,0.4)]',
    secondary: 'bg-white/10 text-white hover:bg-white/20 border border-white/10',
    outline: 'border border-blue-500 text-blue-400 hover:bg-blue-950/30 hover:text-blue-300',
    ghost: 'text-gray-300 hover:text-white hover:bg-white/10'
  };

  const widthStyle = fullWidth ? 'w-full' : '';
  const paddingStyle = 'px-4 py-2';

  return (
    <button
      className={`${baseStyles} ${variants[variant]} ${widthStyle} ${paddingStyle} ${className}`}
      disabled={isLoading || disabled}
      {...props}
    >
      {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
      {children}
    </button>
  );
};
