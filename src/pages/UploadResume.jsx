import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Upload, FileText, X, CheckCircle2, ChevronRight, Loader2 } from 'lucide-react';
import Button from '../components/common/Button';
import api from '../services/api';

const UploadResume = () => {
  const navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [error, setError] = useState("");

  const handleFileChange = (e) => {
    const selected = e.target.files[0];
    const validTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (selected && validTypes.includes(selected.type)) {
      setFile(selected);
      setError("");
    } else {
      setError("Please upload a PDF or DOCX file");
    }
  };

  const handleUpload = async () => {
    if (!file) return;
    setLoading(true);
    setError("");
    
    try {
      // 1. Upload Resume
      setUploadProgress(40);
      await api.resume.upload(file);
      
      // 2. Start Interview Session
      setUploadProgress(80);
      const interview = await api.interview.start("Software Engineer"); 
      
      setUploadProgress(100);
      localStorage.setItem('currentInterview', JSON.stringify(interview));
      navigate('/interview');
    } catch (err) {
      setError(err.message.includes("not appear to be a valid resume") 
        ? "This file doesn't look like a valid resume. Please check the content." 
        : "Failed to analyze resume. Please try again.");
    } finally {
      setLoading(false);
      setUploadProgress(0);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-6">
      <div className="w-full max-w-xl space-y-8">
        <div className="text-center space-y-2">
          <motion.h1 
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            className="text-4xl font-bold text-slate-900"
          >
            Fine-tune your session
          </motion.h1>
          <p className="text-slate-500">Upload your resume to personalize interview questions</p>
        </div>

        {error && (
          <motion.div 
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-red-50 border border-red-100 text-red-600 px-6 py-4 rounded-2xl text-sm font-medium flex items-center gap-3 shadow-sm"
          >
            <div className="w-2 h-2 bg-red-500 rounded-full animate-pulse"></div>
            {error}
          </motion.div>
        )}

        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="glass-card p-10 text-center"
        >
          {!file ? (
            <div className="relative border-2 border-dashed border-slate-200 rounded-2xl p-12 hover:border-primary-400 transition-colors group cursor-pointer">
              <input
                type="file"
                className="absolute inset-0 opacity-0 cursor-pointer"
                onChange={handleFileChange}
                accept=".pdf,.docx"
              />
              <div className="space-y-4">
                <div className="w-16 h-16 bg-primary-50 text-primary-600 rounded-2xl flex items-center justify-center mx-auto group-hover:scale-110 transition-transform">
                  <Upload size={32} />
                </div>
                <div>
                  <p className="text-lg font-semibold text-slate-700">Click or drag to upload</p>
                  <p className="text-sm text-slate-400">PDF or DOCX files (Max 5MB)</p>
                </div>
              </div>
            </div>
          ) : (
            <div className="space-y-6">
              <div className="flex items-center gap-4 bg-primary-50 p-4 rounded-xl border border-primary-100">
                <div className="w-12 h-12 bg-white text-primary-600 rounded-lg flex items-center justify-center shadow-sm">
                  <FileText />
                </div>
                <div className="flex-1 text-left">
                  <p className="font-semibold text-slate-800 truncate">{file.name}</p>
                  <p className="text-xs text-slate-500">{(file.size / 1024).toFixed(1)} KB</p>
                </div>
                <button 
                  onClick={() => setFile(null)}
                  className="p-2 hover:bg-white rounded-full text-slate-400 hover:text-red-500 transition-colors"
                >
                  <X size={20} />
                </button>
              </div>

              {loading && (
                <div className="space-y-2">
                  <div className="flex justify-between text-xs font-bold text-slate-400 uppercase">
                    <span>Analyzing Professional Context</span>
                    <span>{uploadProgress}%</span>
                  </div>
                  <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-primary-600 transition-all duration-300" 
                      style={{ width: `${uploadProgress}%` }}
                    />
                  </div>
                </div>
              )}

              <Button 
                onClick={handleUpload} 
                disabled={loading}
                loading={loading}
                className="w-full"
                icon={ChevronRight}
              >
                Start AI Interview
              </Button>
            </div>
          )}
        </motion.div>

        <div className="grid grid-cols-3 gap-4 text-center">
          <div className="space-y-1">
            <CheckCircle2 className="mx-auto text-green-500" size={20} />
            <p className="text-[10px] font-bold text-slate-400 uppercase">Skill Analysis</p>
          </div>
          <div className="space-y-1">
            <CheckCircle2 className="mx-auto text-green-500" size={20} />
            <p className="text-[10px] font-bold text-slate-400 uppercase">Job Matching</p>
          </div>
          <div className="space-y-1">
            <CheckCircle2 className="mx-auto text-green-500" size={20} />
            <p className="text-[10px] font-bold text-slate-400 uppercase">Smart Q&A</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UploadResume;
