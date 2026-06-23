"use client";

import React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { LayoutDashboard, Target, Activity, Settings, LogOut } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';

export const Sidebar = () => {
  const pathname = usePathname();
  const { logout } = useAuth();

  const navItems = [
    { name: 'Dashboard', href: '/dashboard', icon: LayoutDashboard },
    { name: 'Campaigns', href: '/campaigns', icon: Target },
    { name: 'Analytics', href: '/analytics', icon: Activity },
    { name: 'Settings', href: '/settings', icon: Settings },
  ];

  return (
    <div className="w-64 glass-panel h-screen flex flex-col border-r-0 rounded-none rounded-r-2xl fixed top-0 left-0 z-10">
      <div className="p-6">
        <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-400 to-indigo-400 bg-clip-text text-transparent">
          AdSafe RTB
        </h1>
      </div>
      
      <nav className="flex-1 px-4 space-y-2 mt-4">
        {navItems.map((item) => {
          const isActive = pathname === item.href;
          return (
            <Link
              key={item.name}
              href={item.href}
              className={`flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-300 ${
                isActive 
                  ? 'bg-blue-600/20 text-blue-400 border border-blue-500/30 shadow-[0_0_15px_rgba(59,130,246,0.15)]' 
                  : 'text-gray-400 hover:text-gray-200 hover:bg-white/5'
              }`}
            >
              <item.icon className="w-5 h-5" />
              <span className="font-medium">{item.name}</span>
            </Link>
          );
        })}
      </nav>

      <div className="p-4 border-t border-white/10">
        <button 
          onClick={logout}
          className="flex items-center space-x-3 px-4 py-3 w-full text-left text-gray-400 hover:text-red-400 hover:bg-red-500/10 rounded-xl transition-all duration-300"
        >
          <LogOut className="w-5 h-5" />
          <span className="font-medium">Logout</span>
        </button>
      </div>
    </div>
  );
};
