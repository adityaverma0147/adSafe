"use client";

import React, { useEffect, useState } from 'react';
import { Card, CardHeader, CardTitle } from '@/components/ui/Card';
import { Button } from '@/components/ui/Button';
import { Plus, MoreHorizontal, Loader2 } from 'lucide-react';
import api from '@/services/api';

interface Campaign {
  id: string;
  name: string;
  status: string;
  budget: number;
  spent: number;
  impressions: number;
}

export default function CampaignsPage() {
  const [campaigns, setCampaigns] = useState<Campaign[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchCampaigns = async () => {
      try {
        const response = await api.get('/campaigns');
        setCampaigns(response.data);
      } catch (error) {
        console.error('Failed to fetch campaigns', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchCampaigns();
  }, []);
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end mb-8">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-tight">Campaigns</h1>
          <p className="text-gray-400 mt-1">Manage your advertising campaigns and budgets.</p>
        </div>
        <Button className="flex items-center space-x-2">
          <Plus className="w-4 h-4" />
          <span>New Campaign</span>
        </Button>
      </div>

      <Card>
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="border-b border-white/10 text-gray-400 text-sm">
                <th className="pb-4 font-medium px-4">Campaign Name</th>
                <th className="pb-4 font-medium px-4">Status</th>
                <th className="pb-4 font-medium px-4">Budget Progress</th>
                <th className="pb-4 font-medium px-4">Impressions</th>
                <th className="pb-4 font-medium px-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/5">
              {isLoading ? (
                <tr>
                  <td colSpan={5} className="py-8 text-center text-gray-400">
                    <Loader2 className="w-6 h-6 animate-spin mx-auto mb-2" />
                    Loading campaigns...
                  </td>
                </tr>
              ) : campaigns.length === 0 ? (
                <tr>
                  <td colSpan={5} className="py-8 text-center text-gray-400">
                    No active campaigns found.
                  </td>
                </tr>
              ) : (
                campaigns.map((campaign) => {
                  const percentSpent = campaign.budget ? (campaign.spent / campaign.budget) * 100 : 0;
                  return (
                    <tr key={campaign.id} className="hover:bg-white/5 transition-colors">
                      <td className="py-4 px-4 font-medium text-white">{campaign.name}</td>
                      <td className="py-4 px-4">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          campaign.status === 'ACTIVE' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-gray-500/10 text-gray-400 border border-gray-500/20'
                        }`}>
                          {campaign.status}
                        </span>
                      </td>
                      <td className="py-4 px-4 w-64">
                        <div className="flex justify-between text-xs mb-1">
                          <span className="text-gray-300">${campaign.spent?.toLocaleString()}</span>
                          <span className="text-gray-500">${campaign.budget?.toLocaleString()}</span>
                        </div>
                        <div className="w-full bg-white/5 rounded-full h-1.5 overflow-hidden">
                          <div 
                            className="bg-blue-500 h-1.5 rounded-full" 
                            style={{ width: `${Math.min(percentSpent, 100)}%` }}
                          ></div>
                        </div>
                      </td>
                      <td className="py-4 px-4 text-gray-300">{campaign.impressions?.toLocaleString()}</td>
                      <td className="py-4 px-4 text-right">
                        <button className="p-2 text-gray-400 hover:text-white transition-colors">
                          <MoreHorizontal className="w-5 h-5" />
                        </button>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
}
