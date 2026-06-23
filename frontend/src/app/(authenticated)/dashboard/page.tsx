"use client";

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle } from '@/components/ui/Card';
import { Activity, DollarSign, Target, Zap, Loader2 } from 'lucide-react';
import { 
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  BarChart, Bar
} from 'recharts';
import api from '@/services/api';

const mockLatencyData = [
  { time: '10:00', latency: 42 },
  { time: '10:05', latency: 48 },
  { time: '10:10', latency: 35 },
  { time: '10:15', latency: 50 },
  { time: '10:20', latency: 45 },
  { time: '10:25', latency: 38 },
];

const mockRevenueData = [
  { name: 'DSP 1', revenue: 4000 },
  { name: 'DSP 2', revenue: 3000 },
  { name: 'DSP 3', revenue: 2000 },
  { name: 'DSP 4', revenue: 2780 },
];

const StatCard = ({ title, value, icon: Icon, trend }: any) => (
  <Card hoverable className="relative overflow-hidden group">
    <div className="absolute -right-6 -top-6 w-24 h-24 bg-blue-500/10 rounded-full group-hover:bg-blue-500/20 transition-all duration-500"></div>
    <div className="flex justify-between items-start">
      <div>
        <p className="text-gray-400 font-medium text-sm">{title}</p>
        <h3 className="text-3xl font-bold text-white mt-2 tracking-tight">{value}</h3>
      </div>
      <div className="p-3 bg-white/5 rounded-xl border border-white/10">
        <Icon className="w-6 h-6 text-blue-400" />
      </div>
    </div>
    <div className="mt-4 flex items-center text-sm">
      <span className={`font-medium ${trend > 0 ? 'text-emerald-400' : 'text-red-400'}`}>
        {trend > 0 ? '+' : ''}{trend}%
      </span>
      <span className="text-gray-500 ml-2">from last hour</span>
    </div>
  </Card>
);

interface DashboardStats {
  totalAuctions: number;
  winRate: number;
  avgLatency: number;
  totalSpend: number;
}

export default function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await api.get('/dashboard/stats');
        setStats(response.data);
      } catch (error) {
        console.error('Failed to fetch dashboard stats', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[50vh]">
        <Loader2 className="w-8 h-8 animate-spin text-blue-500" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end mb-8">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Overview</h1>
          <p className="text-gray-400 mt-1">Real-time metrics for your ad auctions.</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard title="Total Auctions" value={stats?.totalAuctions.toLocaleString() || '0'} icon={Activity} trend={12.5} />
        <StatCard title="Win Rate" value={`${stats?.winRate.toFixed(1) || '0.0'}%`} icon={Target} trend={4.2} />
        <StatCard title="Avg Latency" value={`${stats?.avgLatency.toFixed(1) || '0.0'}ms`} icon={Zap} trend={-2.4} />
        <StatCard title="Total Spend" value={`$${stats?.totalSpend.toLocaleString() || '0.00'}`} icon={DollarSign} trend={8.1} />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-8">
        <Card className="h-96">
          <CardHeader>
            <CardTitle>P99 Latency (ms)</CardTitle>
          </CardHeader>
          <div className="h-64 mt-4 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockLatencyData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" vertical={false} />
                <XAxis dataKey="time" stroke="#888" tickLine={false} axisLine={false} />
                <YAxis stroke="#888" tickLine={false} axisLine={false} />
                <Tooltip 
                  contentStyle={{ backgroundColor: 'rgba(0,0,0,0.8)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px' }}
                />
                <Line type="monotone" dataKey="latency" stroke="#3b82f6" strokeWidth={3} dot={{ r: 4, fill: '#3b82f6', strokeWidth: 0 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card className="h-96">
          <CardHeader>
            <CardTitle>Spend by DSP</CardTitle>
          </CardHeader>
          <div className="h-64 mt-4 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={mockRevenueData} layout="vertical" margin={{ top: 0, right: 0, left: 0, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" horizontal={false} />
                <XAxis type="number" stroke="#888" tickLine={false} axisLine={false} />
                <YAxis dataKey="name" type="category" stroke="#888" tickLine={false} axisLine={false} width={60} />
                <Tooltip 
                  contentStyle={{ backgroundColor: 'rgba(0,0,0,0.8)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px' }}
                  cursor={{fill: 'rgba(255,255,255,0.05)'}}
                />
                <Bar dataKey="revenue" fill="#4f46e5" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>
    </div>
  );
}
