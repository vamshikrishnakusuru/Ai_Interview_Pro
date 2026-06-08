import React, { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { 
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, 
  BarChart, Bar, Cell, PieChart, Pie, Legend
} from 'recharts';
import { 
  Trophy, Target, Zap, Clock, Brain, AlertCircle, 
  TrendingUp, Activity, Award, ChevronRight, MessageSquare
} from 'lucide-react';
import api from '../services/api';

const Dashboard = () => {
  const [summary, setSummary] = useState(null);
  const [trends, setTrends] = useState([]);
  const [skills, setSkills] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [sumRes, trendRes, skillRes, histRes] = await Promise.all([
          api.analytics.getSummary(),
          api.analytics.getTrends(),
          api.analytics.getSkills(),
          api.analytics.getHistory()
        ]);
        setSummary(sumRes);
        setTrends(trendRes);
        setSkills(skillRes);
        setHistory(histRes);
      } catch (err) {
        console.error("Failed to fetch analytics", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50">
      <div className="flex flex-col items-center gap-4">
        <div className="w-12 h-12 border-4 border-primary-600 border-t-transparent rounded-full animate-spin"></div>
        <p className="text-slate-500 font-medium italic">Building your performance profile...</p>
      </div>
    </div>
  );

  const COLORS = ['#ef4444', '#f59e0b', '#10b981'];
  const distributionData = summary?.scoreDistribution ? Object.entries(summary.scoreDistribution).map(([name, value]) => ({ name, value })) : [];

  return (
    <div className="min-h-screen bg-slate-50 py-8 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-7xl mx-auto space-y-8">
        
        {/* Header */}
        <header className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="space-y-1">
            <h1 className="text-3xl font-black text-slate-900 tracking-tight flex items-center gap-3">
              Performance <span className="bg-primary-600 text-white px-3 py-1 rounded-xl shadow-lg shadow-primary-200">Analytics</span>
            </h1>
            <p className="text-slate-500 font-medium italic">Your personalized AI interview growth dashboard</p>
          </div>
          <div className="bg-white p-1 rounded-2xl border border-slate-200 shadow-sm flex">
            {['All Time', 'Monthly', 'Weekly'].map((tab) => (
              <button key={tab} className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${tab === 'All Time' ? 'bg-primary-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-600'}`}>
                {tab}
              </button>
            ))}
          </div>
        </header>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCard 
            title="Avg AI Score" 
            value={summary?.averageScore + "/10" || "0/10"} 
            icon={<Brain className="text-white" />} 
            color="bg-primary-600" 
            trend="+12% vs last week"
          />
          <StatCard 
            title="Total Sessions" 
            value={summary?.totalInterviews || 0} 
            icon={<Clock className="text-white" />} 
            color="bg-indigo-600" 
            trend="Consistently Active"
          />
          <StatCard 
            title="Strongest Skill" 
            value={summary?.strongestSkill || "N/A"} 
            icon={<Trophy className="text-white" />} 
            color="bg-emerald-600" 
            trend="Top Performance"
          />
          <StatCard 
            title="Needs Focus" 
            value={summary?.weakestSkill || "N/A"} 
            icon={<Zap className="text-white" />} 
            color="bg-rose-600" 
            trend="Opportunity Zone"
          />
        </div>

        {/* Top Section Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Trend Line Chart */}
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="lg:col-span-2 glass-card p-8 bg-white border border-slate-200"
          >
            <div className="flex items-center justify-between mb-8">
              <h3 className="text-xl font-black text-slate-800 flex items-center gap-2">
                <TrendingUp className="text-primary-600" /> Performance Trend
              </h3>
            </div>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={trends}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e2e8f0" />
                  <XAxis 
                    dataKey="date" 
                    axisLine={false} 
                    tickLine={false} 
                    tick={{fill: '#64748b', fontSize: 12}}
                    tickFormatter={(str) => {
                      const date = new Date(str);
                      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
                    }}
                  />
                  <YAxis hide domain={[0, 10]} />
                  <Tooltip 
                    contentStyle={{borderRadius: '16px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)'}}
                    labelFormatter={(label) => new Date(label).toLocaleDateString()}
                  />
                  <Line 
                    type="monotone" 
                    dataKey="score" 
                    stroke="#2563eb" 
                    strokeWidth={4} 
                    dot={{fill: '#2563eb', strokeWidth: 2, r: 4, stroke: '#fff'}}
                    activeDot={{r: 8, strokeWidth: 0, fill: '#2563eb'}}
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </motion.div>

          {/* AI Coach Summary Card */}
          <motion.div 
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="glass-card bg-gradient-to-br from-slate-900 to-slate-800 p-8 text-white relative overflow-hidden"
          >
            <div className="relative z-10 space-y-6">
              <div className="flex items-center gap-3">
                <div className="p-2 bg-white/10 rounded-xl backdrop-blur-md border border-white/20">
                  <Award className="text-primary-400" />
                </div>
                <h3 className="text-xl font-bold">AI Coach Insights</h3>
              </div>
              <p className="text-slate-300 leading-relaxed font-medium">
                {summary?.personalCoachSummary || "Analyze your interview performance to get personalized insights from your AI Coach."}
              </p>
              <div className="pt-4 space-y-3">
                <h4 className="text-xs font-bold text-primary-400 uppercase tracking-widest">Recommended Actions</h4>
                <ul className="space-y-2">
                  <CoachTip text="Boost response depth with real-world examples" />
                  <CoachTip text="Strengthen technical fundamentals in MySQL" />
                </ul>
              </div>
            </div>
            <div className="absolute bottom-0 right-0 w-48 h-48 bg-primary-600/20 rounded-full -mb-24 -mr-24 blur-3xl"></div>
          </motion.div>
        </div>

        {/* Lower Section Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
           {/* Skill Breakdown */}
           <motion.div 
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="glass-card p-8 bg-white border border-slate-200"
          >
            <h3 className="text-xl font-black text-slate-800 mb-8 flex items-center gap-2">
              <Target className="text-primary-600" /> Skill Proficiency
            </h3>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={skills} layout="vertical">
                  <XAxis type="number" hide domain={[0, 10]} />
                  <YAxis dataKey="skill" type="category" axisLine={false} tickLine={false} width={100} tick={{fill: '#1e293b', fontWeight: 'bold'}} />
                  <Tooltip cursor={{fill: '#f1f5f9'}} />
                  <Bar dataKey="averageScore" radius={[0, 8, 8, 0]} barSize={24}>
                    {skills.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.averageScore >= 8 ? '#10b981' : entry.averageScore >= 5 ? '#f59e0b' : '#ef4444'} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>
          </motion.div>

          {/* Score Distribution Chart */}
          <motion.div 
             initial={{ opacity: 0, scale: 0.95 }}
             animate={{ opacity: 1, scale: 1 }}
             className="glass-card p-8 bg-white border border-slate-200"
          >
            <h3 className="text-xl font-black text-slate-800 mb-8 flex items-center gap-2">
               <Activity className="text-primary-600" /> Score Distribution
            </h3>
            <div className="h-[300px] w-full flex flex-col md:flex-row items-center justify-around">
               <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={distributionData}
                      cx="50%"
                      cy="50%"
                      innerRadius={60}
                      outerRadius={100}
                      paddingAngle={5}
                      dataKey="value"
                    >
                      {distributionData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip />
                    <Legend iconType="circle" />
                  </PieChart>
               </ResponsiveContainer>
            </div>
          </motion.div>
        </div>

        {/* History & Gaps Section */}
        <div className="grid grid-cols-1 lg:grid-cols-5 gap-8">
           {/* Interview History List */}
           <div className="lg:col-span-3 glass-card p-8 bg-white border border-slate-200">
             <h3 className="text-xl font-black text-slate-800 mb-6 font-sans">Interview History</h3>
             <div className="space-y-4">
                {history.map((item, idx) => (
                  <div key={item.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-2xl hover:bg-slate-100 transition-colors border border-slate-100 group cursor-pointer">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 bg-white rounded-xl flex items-center justify-center font-black text-primary-600 border border-slate-200">
                        {item.score || "-"}
                      </div>
                      <div>
                        <h4 className="font-bold text-slate-800">{item.jobTitle}</h4>
                        <p className="text-xs text-slate-400 font-semibold">{item.date} • {item.strongestSkill} Peak</p>
                      </div>
                    </div>
                    <div className="flex items-center gap-4">
                       <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-widest ${item.status === 'COMPLETED' ? 'bg-green-100 text-green-600' : 'bg-blue-100 text-blue-600'}`}>
                         {item.status}
                       </span>
                       <ChevronRight className="text-slate-300 group-hover:text-primary-600 transition-colors" />
                    </div>
                  </div>
                ))}
             </div>
           </div>

           {/* Knowledge Gaps */}
           <div className="lg:col-span-2 glass-card p-8 bg-white border border-slate-200">
              <h3 className="text-xl font-black text-slate-800 mb-6 flex items-center gap-2">
                <AlertCircle className="text-rose-500" /> Knowledge Gaps
              </h3>
              <p className="text-slate-500 text-sm mb-6">Frequently missing concepts across your sessions.</p>
              <div className="flex flex-wrap gap-3">
                 {summary?.missingConcepts?.map((concept, i) => (
                   <span key={i} className="px-3 py-2 bg-slate-100 text-slate-700 text-xs font-bold rounded-xl border border-slate-200 hover:border-primary-300 transition-colors">
                     {concept}
                   </span>
                 ))}
                 {(!summary?.missingConcepts || summary.missingConcepts.length === 0) && (
                   <p className="text-slate-400 italic text-sm">No significant gaps detected yet.</p>
                 )}
              </div>
              <div className="mt-8 p-4 bg-blue-50 rounded-2xl border border-blue-100 flex items-start gap-3">
                 <div className="p-2 bg-white rounded-xl shadow-sm text-blue-600">
                    <MessageSquare size={16} />
                 </div>
                 <div className="space-y-1">
                    <span className="text-[10px] font-black text-blue-400 uppercase">Pro Tip</span>
                    <p className="text-xs font-semibold text-blue-700 leading-relaxed italic">
                       Try to elaborate more on "Why" you use certain tools, rather than just "How".
                    </p>
                 </div>
              </div>
           </div>
        </div>

      </div>
    </div>
  );
};

// UI Atoms
const StatCard = ({ title, value, icon, color, trend }) => (
  <motion.div 
    whileHover={{ y: -5 }}
    className="glass-card bg-white p-6 border border-slate-200 flex flex-col justify-between"
  >
    <div className="flex items-center justify-between mb-4">
      <div className={`p-3 ${color} rounded-2xl shadow-lg`}>
        {React.cloneElement(icon, { size: 20 })}
      </div>
    </div>
    <div>
      <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">{title}</span>
      <h4 className="text-2xl font-black text-slate-900 leading-none mt-1 mb-2">{value}</h4>
      <div className="flex items-center gap-1 text-[10px] font-bold text-emerald-600">
        <TrendingUp size={12} /> {trend}
      </div>
    </div>
  </motion.div>
);

const CoachTip = ({ text }) => (
  <li className="flex items-start gap-2 text-sm">
    <div className="w-1.5 h-1.5 bg-primary-400 rounded-full mt-1.5 shrink-0" />
    <span className="text-slate-200">{text}</span>
  </li>
);

export default Dashboard;
