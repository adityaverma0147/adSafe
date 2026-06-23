"use client";

import React from 'react';
import { Bell, User } from 'lucide-react';

export const Header = () => {
  return (
    <header className="h-20 glass-panel border-t-0 border-x-0 rounded-none rounded-b-2xl sticky top-0 z-10 flex items-center justify-between px-8 ml-64">
      <div className="flex items-center">
        {/* Breadcrumbs or page title could go here */}
        <h2 className="text-gray-300 font-medium">Platform Overview</h2>
      </div>

      <div className="flex items-center space-x-6">
        <button className="text-gray-400 hover:text-white transition-colors relative">
          <Bell className="w-5 h-5" />
          <span className="absolute -top-1 -right-1 w-2.5 h-2.5 bg-blue-500 rounded-full border-2 border-background"></span>
        </button>

        <div className="flex items-center space-x-3 pl-6 border-l border-white/10 cursor-pointer group">
          <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white font-semibold shadow-[0_0_15px_rgba(79,70,229,0.4)] group-hover:shadow-[0_0_20px_rgba(79,70,229,0.6)] transition-shadow">
            <User className="w-4 h-4" />
          </div>
          <div className="hidden md:block">
            <p className="text-sm font-medium text-gray-200">AdSafe User</p>
            <p className="text-xs text-gray-400">Admin</p>
          </div>
        </div>
      </div>
    </header>
  );
};
