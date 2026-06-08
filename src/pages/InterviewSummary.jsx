import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Trophy, Clock, CheckCircle, ChevronLeft, Download, Share2, AlertTriangle, MessageSquare, Lightbulb, Target, Loader2 } from 'lucide-react';
import api from '../services/api';
import Button from '../components/common/Button';

const InterviewSummary = () => {
  const navigate = useNavigate();
  const [interview, setInterview] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const storedStr = localStorage.getItem("currentInterview");
        console.log("Raw stored interview:", storedStr);
        
        if (!storedStr) {
          setError("Session data missing from storage.");
          setLoading(false);
          return;
        }

        const stored = JSON.parse(storedStr);
        if (stored && stored.id) {
          const data = await api.interview.getInterview(stored.id);
          console.log("Fetched interview data:", data);
          setInterview(data);
        } else {
          setError("No valid interview ID found.");
        }
      } catch (err) {
        console.error("Failed to fetch summary", err);
        setError("Failed to load interview results. Please try refreshing.");
      } finally {
        setLoading(false);
      }
    };
    fetchSummary();
  }, []);

  if (loading) return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-slate-50 gap-4">
      <Loader2 className="w-12 h-12 text-primary-600 animate-spin" />
      <p className="text-slate-500 font-medium italic">Gemini AI is finalizing your evaluation...</p>
    </div>
  );

  if (error || !interview) return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-slate-50 gap-6">
      <div className="w-20 h-20 bg-red-50 text-red-500 rounded-2xl flex items-center justify-center">
        <AlertTriangle size={40} />
      </div>
      <div className="text-center">
        <h2 className="text-2xl font-bold text-slate-800">Results Unavailable</h2>
        <p className="text-slate-500">{error || "We couldn't find your interview session."}</p>
      </div>
      <Button onClick={() => navigate('/upload')} icon={ChevronLeft}>Back to Upload</Button>
    </div>
  );

  const questions = interview.questions || [];

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-4xl mx-auto space-y-10">
        <header className="flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div className="space-y-1">
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Session Summary</h1>
            <p className="text-slate-500">Review your performance and key improvement areas</p>
          </div>
          <div className="flex gap-3">
            <Button variant="secondary" icon={Download}>Export PDF</Button>
            <Button variant="primary" icon={Share2}>Share</Button>
          </div>
        </header>

        {/* Completion Card */}
        <motion.div 
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-card bg-gradient-to-br from-primary-600 to-primary-800 p-8 text-white relative overflow-hidden"
        >
          <div className="relative z-10 flex flex-col md:flex-row items-center gap-8">
            <div className="w-20 h-20 bg-white/20 backdrop-blur-xl rounded-2xl flex items-center justify-center border border-white/30 shadow-2xl">
              <Trophy size={40} />
            </div>
            <div className="text-center md:text-left space-y-2">
              <h2 className="text-2xl font-bold">Interview Completed!</h2>
              <p className="text-primary-100 max-w-md">Great job! You've successfully finished all {questions.length} questions. Our Gemini AI has analyzed your responses for technical and communication patterns.</p>
            </div>
          </div>
          <div className="absolute top-0 right-0 w-64 h-64 bg-white/10 rounded-full -mr-32 -mt-32 blur-3xl"></div>
        </motion.div>

        {/* Responses */}
        <div className="space-y-6">
          <h3 className="text-xl font-bold text-slate-800">Detailed Responses</h3>
          <div className="grid gap-6">
            {questions.map((item, idx) => (
              <motion.div 
                key={idx}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: idx * 0.1 }}
                className="glass-card p-6 border-l-4 border-primary-500 space-y-6"
              >
                <div className="flex items-start justify-between gap-4">
                  <div className="space-y-1">
                    <span className="text-[10px] font-bold text-primary-600 uppercase tracking-widest">Question {idx + 1}</span>
                    <h4 className="text-lg font-bold text-slate-800">{item.questionText}</h4>
                  </div>
                  {item.answer?.aiScore !== null && (
                    <div className="bg-primary-50 text-primary-700 px-4 py-2 rounded-xl border border-primary-100 flex flex-col items-center">
                      <span className="text-xs font-bold uppercase tracking-tight">AI Score</span>
                      <span className="text-xl font-black">{item.answer?.aiScore}/10</span>
                    </div>
                  )}
                </div>

                <div className="bg-slate-50/50 p-4 rounded-xl border border-slate-100">
                  <p className="text-sm font-bold text-slate-400 uppercase mb-2">Your Answer</p>
                  <p className="text-slate-600 leading-relaxed italic">"{item.answer?.answerText || 'No transcript generated'}"</p>
                </div>

                {item.answer && (
                  <div className="grid md:grid-cols-2 gap-4">
                    <div className="space-y-3">
                      <div className="flex items-center gap-2 text-slate-700 font-bold text-sm">
                        <Target size={16} className="text-primary-500" /> Technical Accuracy
                      </div>
                      <p className="text-slate-600 text-sm leading-relaxed">{item.answer.technicalAccuracy}</p>
                      
                      {item.answer.missingConcepts && (
                        <div className="flex flex-wrap gap-2 pt-2">
                          {item.answer.missingConcepts.split(',').map((concept, i) => (
                            <span key={i} className="text-[10px] font-bold bg-slate-100 text-slate-500 px-2 py-1 rounded-md uppercase">
                              {concept.trim()}
                            </span>
                          ))}
                        </div>
                      )}
                    </div>

                    <div className="space-y-3">
                      <div className="flex items-center gap-2 text-slate-700 font-bold text-sm">
                        <MessageSquare size={16} className="text-primary-500" /> Communication
                      </div>
                      <p className="text-slate-600 text-sm leading-relaxed">{item.answer.communicationFeedback}</p>
                      
                      <div className="flex items-center gap-2 text-orange-600 font-bold text-sm pt-2">
                        <Lightbulb size={16} /> Key Suggestions
                      </div>
                      <ul className="list-disc list-inside text-slate-500 text-xs space-y-1">
                        {item.answer.suggestions?.split(',').map((s, i) => (
                          <li key={i}>{s.trim()}</li>
                        ))}
                      </ul>
                    </div>
                  </div>
                )}
              </motion.div>
            ))}
          </div>
        </div>

        <footer className="flex justify-between items-center pt-8 border-t border-slate-200">
          <Button variant="secondary" icon={ChevronLeft} onClick={() => navigate('/upload')}>
            New Session
          </Button>
          <div className="flex items-center gap-2 text-green-600 font-bold italic">
            <CheckCircle size={20} />
            Auto-saved to Profile
          </div>
        </footer>
      </div>
    </div>
  );
};

export default InterviewSummary;
