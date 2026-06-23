"use client";

import React, { useState } from 'react';
import { useAuth } from '@/context/AuthContext';
import { Card, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Activity } from 'lucide-react';

export default function LoginPage() {
  const [email, setEmail] = useState('admin@adsafe.com');
  const [password, setPassword] = useState('password');
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      // Simulate API call to /api/auth/login
      await new Promise((resolve) => setTimeout(resolve, 1000));
      // In a real app, this token comes from the backend
      const dummyToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dummy";
      login(dummyToken);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden">
      {/* Decorative blurred blobs */}
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-blue-600/30 rounded-full mix-blend-screen filter blur-[128px] animate-pulse"></div>
      <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-indigo-600/30 rounded-full mix-blend-screen filter blur-[128px] animate-pulse delay-1000"></div>

      <div className="z-10 w-full max-w-md p-6">
        <div className="flex flex-col items-center mb-8">
          <div className="w-16 h-16 bg-blue-600/20 border border-blue-500/50 rounded-2xl flex items-center justify-center mb-4 shadow-[0_0_30px_rgba(37,99,235,0.3)]">
            <Activity className="w-8 h-8 text-blue-400" />
          </div>
          <h1 className="text-3xl font-bold text-white tracking-tight">AdSafe RTB</h1>
          <p className="text-gray-400 mt-2 text-center">Login to manage campaigns and monitor auctions.</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Sign In</CardTitle>
          </CardHeader>
          <form onSubmit={handleLogin} className="space-y-6">
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-300">Email Address</label>
              <input 
                type="email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full bg-black/40 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                placeholder="Enter your email"
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium text-gray-300">Password</label>
              <input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full bg-black/40 border border-white/10 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                placeholder="Enter your password"
              />
            </div>
            <Button 
              type="submit" 
              fullWidth 
              isLoading={isLoading}
              className="py-3 text-base"
            >
              Sign In to Platform
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
}
