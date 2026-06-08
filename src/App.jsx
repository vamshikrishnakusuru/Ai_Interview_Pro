import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import UploadResume from './pages/UploadResume';
import Interview from './pages/Interview';
import InterviewSummary from './pages/InterviewSummary';
import Dashboard from './pages/Dashboard';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-slate-50 flex flex-col">
        <Navbar />
        <main className="flex-grow">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/" element={<Navigate to="/signup" replace />} />
            
            <Route 
              path="/upload" 
              element={
                <ProtectedRoute>
                  <UploadResume />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/interview" 
              element={
                <ProtectedRoute>
                  <Interview />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/summary" 
              element={
                <ProtectedRoute>
                  <InterviewSummary />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/dashboard" 
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              } 
            />
            <Route path="*" element={<Navigate to="/signup" replace />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
