import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  Mic, Square, ChevronRight, CheckCircle2, Volume2, 
  Timer, Loader2, RotateCcw, FastForward, Activity, 
  Sparkles, Headphones, Award, Compass, MessageSquare
} from 'lucide-react';
import Button from '../components/common/Button';
import ProgressBar from '../components/common/ProgressBar';
import api from '../services/api';

// ==========================================
// 1. DYNAMIC CYBER AVATAR ENGINE
// ==========================================
const AIAvatar = ({ state }) => {
  const isSpeaking = state === 'speaking';
  const isListening = state === 'listening';
  const isThinking = state === 'thinking';
  const isIdle = state === 'idle';

  let coreGradient = "from-cyan-500 via-blue-600 to-indigo-700";
  let ringColor = "text-cyan-400";
  let glowColor = "rgba(6, 182, 212, 0.35)";
  let pulseSpeed = 1.8;
  
  if (isListening) {
    coreGradient = "from-rose-500 via-red-600 to-amber-600";
    ringColor = "text-rose-400";
    glowColor = "rgba(244, 63, 94, 0.45)";
    pulseSpeed = 0.9;
  } else if (isThinking) {
    coreGradient = "from-purple-500 via-violet-600 to-fuchsia-700";
    ringColor = "text-purple-400";
    glowColor = "rgba(168, 85, 247, 0.4)";
    pulseSpeed = 1.2;
  } else if (isSpeaking) {
    coreGradient = "from-teal-400 via-cyan-500 to-blue-600";
    ringColor = "text-teal-400";
    glowColor = "rgba(20, 184, 166, 0.45)";
    pulseSpeed = 0.8;
  }

  return (
    <div className="relative w-48 h-48 flex items-center justify-center">
      {/* Dynamic Glowing Backing Ring */}
      <motion.div
        className="absolute inset-0 rounded-full"
        style={{
          boxShadow: `0 0 50px 15px ${glowColor}`,
          border: `2px dashed ${
            isListening ? 'rgba(244, 63, 94, 0.35)' : 
            isThinking ? 'rgba(168, 85, 247, 0.35)' : 
            isSpeaking ? 'rgba(20, 184, 166, 0.35)' : 'rgba(37, 99, 235, 0.25)'
          }`
        }}
        animate={isListening ? {
          scale: [1, 1.15, 1],
          opacity: [0.7, 1, 0.7]
        } : isSpeaking ? {
          scale: [1, 1.08, 1],
          opacity: [0.8, 1, 0.8]
        } : isThinking ? {
          scale: [1, 1.04, 1],
          opacity: [0.6, 0.9, 0.6]
        } : {
          scale: [1, 1.02, 1],
          opacity: [0.4, 0.6, 0.4]
        }}
        transition={{
          duration: pulseSpeed,
          repeat: Infinity,
          ease: "easeInOut"
        }}
      />

      {/* Floating Cyber-Face Core */}
      <motion.div
        className="relative z-10 w-36 h-36 flex items-center justify-center"
        animate={{
          y: isThinking ? [0, -3, 0] : isSpeaking ? [0, -6, 0] : [0, -10, 0]
        }}
        transition={{
          duration: isThinking ? 1.5 : isSpeaking ? 2.5 : 4,
          repeat: Infinity,
          ease: "easeInOut"
        }}
      >
        {/* Orbital Dash-Array Ring (Outer) */}
        <motion.svg
          className={`absolute w-[120%] h-[120%] ${ringColor} opacity-80`}
          viewBox="0 0 100 100"
          animate={{ rotate: 360 }}
          transition={{
            duration: isThinking ? 4 : isListening ? 10 : 22,
            repeat: Infinity,
            ease: "linear"
          }}
        >
          <circle
            cx="50"
            cy="50"
            r="46"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeDasharray="12 18 36 18"
          />
        </motion.svg>

        {/* Reverse Orbital Dash-Array Ring (Inner) */}
        <motion.svg
          className={`absolute w-[102%] h-[102%] ${ringColor} opacity-50`}
          viewBox="0 0 100 100"
          animate={{ rotate: -360 }}
          transition={{
            duration: isThinking ? 3 : isListening ? 7 : 16,
            repeat: Infinity,
            ease: "linear"
          }}
        >
          <circle
            cx="50"
            cy="50"
            r="43"
            fill="none"
            stroke="currentColor"
            strokeWidth="1"
            strokeDasharray="4 8 16 8"
          />
        </motion.svg>

        {/* Liquid Gradient Core Sphere */}
        <div className={`w-28 h-28 rounded-full bg-gradient-to-tr ${coreGradient} p-[3px] shadow-2xl flex items-center justify-center relative overflow-hidden`}>
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_30%,rgba(255,255,255,0.25),transparent_60%)] z-10" />
          <div className="absolute inset-0 bg-black/10 mix-blend-overlay" />
          <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.05)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.05)_1px,transparent_1px)] bg-[size:8px_8px] opacity-40" />

          {/* Futuristic Face Shell */}
          <div className="relative flex flex-col items-center justify-center gap-3.5 z-20 w-full h-full">
            {/* Animated Holographic Eyes */}
            <div className="flex gap-7 justify-center items-center h-4">
              <motion.div
                className="w-3.5 h-2 bg-white rounded-full shadow-[0_0_12px_rgba(255,255,255,1),0_0_4px_rgba(255,255,255,0.8)]"
                animate={isThinking ? {
                  scaleX: [1, 1.4, 1],
                  scaleY: [1, 0.15, 1],
                  y: [-1, 1, -1]
                } : isListening ? {
                  scale: [1, 1.25, 1]
                } : isSpeaking ? {
                  scaleX: [1, 1.1, 1],
                  scaleY: [1, 0.85, 1]
                } : {
                  scaleY: [1, 1, 0.05, 1, 1, 1, 1]
                }}
                transition={{
                  duration: isThinking ? 1.2 : isListening ? 0.8 : 3.8,
                  repeat: Infinity,
                  times: [0, 0.46, 0.5, 0.54, 1],
                  ease: "easeInOut"
                }}
              />
              <motion.div
                className="w-3.5 h-2 bg-white rounded-full shadow-[0_0_12px_rgba(255,255,255,1),0_0_4px_rgba(255,255,255,0.8)]"
                animate={isThinking ? {
                  scaleX: [1, 1.4, 1],
                  scaleY: [1, 0.15, 1],
                  y: [-1, 1, -1]
                } : isListening ? {
                  scale: [1, 1.25, 1]
                } : isSpeaking ? {
                  scaleX: [1, 1.1, 1],
                  scaleY: [1, 0.85, 1]
                } : {
                  scaleY: [1, 1, 0.05, 1, 1, 1, 1]
                }}
                transition={{
                  duration: isThinking ? 1.2 : isListening ? 0.8 : 3.8,
                  repeat: Infinity,
                  times: [0, 0.46, 0.5, 0.54, 1],
                  ease: "easeInOut"
                }}
              />
            </div>

            {/* Interactive Audio Wave Mouth */}
            <motion.div
              className="w-12 bg-white/90 rounded-full shadow-[0_0_10px_rgba(255,255,255,0.9),0_0_4px_rgba(255,255,255,0.7)]"
              animate={isSpeaking ? {
                height: [3, 14, 4, 18, 2, 12, 3],
                width: [26, 32, 22, 36, 20, 28, 26],
                borderRadius: ["8px", "4px", "8px", "2px", "8px"]
              } : isListening ? {
                height: [2, 3, 2],
                width: [18, 26, 18]
              } : isThinking ? {
                height: 3,
                width: [8, 12, 8],
                borderRadius: "50%"
              } : {
                height: 3,
                width: 18
              }}
              transition={{
                duration: isSpeaking ? 0.55 : isListening ? 1.2 : 1.4,
                repeat: Infinity,
                ease: "easeInOut"
              }}
            />
          </div>

          {/* Glitch CRT Overlay */}
          {isSpeaking && (
            <motion.div 
              className="absolute inset-0 bg-teal-300/10 mix-blend-color-dodge pointer-events-none"
              animate={{ opacity: [0, 0.35, 0, 0.2, 0] }}
              transition={{ duration: 0.4, repeat: Infinity }}
            />
          )}
        </div>
      </motion.div>
    </div>
  );
};

const VoiceWaveform = () => {
  const bars = Array.from({ length: 11 });
  return (
    <div className="flex items-center justify-center gap-1.5 h-10 px-4 py-2 bg-slate-900/40 rounded-full backdrop-blur-md border border-white/10 shadow-lg">
      <span className="text-[10px] font-bold text-teal-400 uppercase tracking-widest mr-1.5 animate-pulse">AI Voice Core</span>
      {bars.map((_, i) => (
        <motion.div
          key={i}
          className="w-1 bg-gradient-to-t from-teal-400 to-cyan-400 rounded-full"
          style={{ height: "6px" }}
          animate={{
            height: [6, Math.random() * 26 + 10, 6]
          }}
          transition={{
            duration: 0.35 + i * 0.04,
            repeat: Infinity,
            repeatType: "reverse",
            ease: "easeInOut"
          }}
        />
      ))}
    </div>
  );
};

const MicWaveform = () => {
  const bars = Array.from({ length: 17 });
  return (
    <div className="flex items-center justify-center gap-1.5 h-12 w-full max-w-sm mx-auto px-6 bg-slate-950/40 rounded-2xl border border-white/5 backdrop-blur-xl">
      <span className="text-[10px] font-bold text-rose-400 uppercase tracking-widest mr-2.5 animate-pulse">Audio Feed</span>
      {bars.map((_, i) => (
        <motion.div
          key={i}
          className="w-1.5 bg-gradient-to-t from-rose-500 to-red-400 rounded-full shadow-[0_0_6px_rgba(244,63,94,0.4)]"
          style={{ height: "8px" }}
          animate={{
            height: [8, Math.random() * 34 + 12, 8]
          }}
          transition={{
            duration: 0.3 + (i % 6) * 0.07,
            repeat: Infinity,
            repeatType: "reverse",
            ease: "easeInOut"
          }}
        />
      ))}
    </div>
  );
};

// ==========================================
// MAIN ADAPTIVE INTERVIEW PAGE
// ==========================================
const Interview = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [questions, setQuestions] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [recording, setRecording] = useState(false);
  const [audioURL, setAudioURL] = useState(null);
  const [transcript, setTranscript] = useState("");
  const [seconds, setSeconds] = useState(0);
  const [saving, setSaving] = useState(false);
  const [avatarState, setAvatarState] = useState('idle');
  
  // Adaptive Notification/Status States
  const [notification, setNotification] = useState("");
  const [sessionDifficulty, setSessionDifficulty] = useState("MEDIUM");
  const [historyAnswers, setHistoryAnswers] = useState([]);

  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);
  const recognitionRef = useRef(null);
  const timerRef = useRef(null);

  const maxQuestions = 5;

  // Initialize Speech and Fetch Initial Question
  useEffect(() => {
    const startAdaptiveSession = async () => {
      try {
        const stored = JSON.parse(localStorage.getItem('currentInterview'));
        if (stored && stored.id) {
          // Generate first dynamic warmup question
          setAvatarState('thinking');
          setNotification("AI is compiling your custom interview warmup...");
          const firstQuestion = await api.interview.getNextQuestion(stored.id);
          setQuestions([firstQuestion]);
          setSessionDifficulty(firstQuestion.difficultyLevel || "EASY");
          
          const localAnswers = JSON.parse(localStorage.getItem('interviewAnswers')) || [];
          setHistoryAnswers(localAnswers);
          setNotification("");
        } else {
          navigate("/upload");
        }
      } catch (error) {
        console.error("Failed to start adaptive session", error);
        setNotification("Error starting session. Please try again.");
      } finally {
        setLoading(false);
        setAvatarState('idle');
      }
    };
    startAdaptiveSession();
    localStorage.removeItem("interviewAnswers");

    return () => {
      window.speechSynthesis.cancel();
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [navigate]);

  const currentQuestion = questions[currentIndex];

  // TTS audio player
  const speakQuestion = (text) => {
    window.speechSynthesis.cancel();
    if (!text) return;

    const speech = new SpeechSynthesisUtterance(text);
    speech.lang = "en-US";
    
    speech.onstart = () => setAvatarState('speaking');
    speech.onend = () => setAvatarState('idle');
    speech.onerror = () => setAvatarState('idle');
    
    window.speechSynthesis.speak(speech);
  };

  // Autoplay voice synthesis on new question load
  useEffect(() => {
    if (currentQuestion) {
      speakQuestion(currentQuestion.questionText);
    }
  }, [currentQuestion]);

  const handleRepeat = () => {
    if (currentQuestion) {
      speakQuestion(currentQuestion.questionText);
    }
  };

  // Evaluate and dynamically load next step
  const transitionToNextStep = async (isSkip = false) => {
    window.speechSynthesis.cancel();
    setAvatarState('thinking');
    setSaving(true);
    setNotification(isSkip ? "Skipping question..." : "AI is analyzing your response...");

    try {
      const stored = JSON.parse(localStorage.getItem('currentInterview'));
      if (!stored || !stored.id) throw new Error("No session found in local storage");

      // 1. Submit answer for evaluation
      const answerVal = isSkip ? "Skipped" : transcript;
      const durationVal = isSkip ? 0 : seconds;

      await api.interview.evaluateAdaptiveAnswer(currentQuestion.id, answerVal, durationVal);

      // Save local memory representation for summary page compatibility
      const localAnswers = JSON.parse(localStorage.getItem('interviewAnswers')) || [];
      localAnswers.push({
        question: currentQuestion.questionText,
        answer: answerVal,
        duration: durationVal
      });
      localStorage.setItem('interviewAnswers', JSON.stringify(localAnswers));
      setHistoryAnswers(localAnswers);

      // 2. Determine if session completed
      if (currentIndex === maxQuestions - 1) {
        setNotification("Finalizing your performance coaching metrics...");
        await api.interview.complete(stored.id);
        navigate("/summary");
        return;
      }

      // 3. Fetch next dynamic question from adaptive engine
      setNotification("AI is analyzing performance and generating next question...");
      const nextQuestion = await api.interview.getNextQuestion(stored.id);

      // Set alert transitions
      let transitionAlert = "";
      if (nextQuestion.isFollowUp) {
        transitionAlert = "AI generated a conversational follow-up question.";
      } else if (nextQuestion.difficultyLevel !== sessionDifficulty) {
        transitionAlert = `Difficulty adjusted to ${nextQuestion.difficultyLevel}.`;
      } else {
        transitionAlert = `Topic shifted to: ${nextQuestion.category || "Next Module"}`;
      }
      setNotification(transitionAlert);
      setTimeout(() => setNotification(""), 4500);

      // 4. Update states
      setSessionDifficulty(nextQuestion.difficultyLevel || "MEDIUM");
      setQuestions(prev => [...prev, nextQuestion]);
      setTranscript("");
      setAudioURL(null);
      setSeconds(0);
      setCurrentIndex(prev => prev + 1);
      setAvatarState('idle');

    } catch (error) {
      console.error("Adaptive step failed", error);
      setNotification("Failed to save step. Please try again.");
      setAvatarState('idle');
    } finally {
      setSaving(false);
    }
  };

  // Configure Web Speech API
  useEffect(() => {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SpeechRecognition) return;

    const recognition = new SpeechRecognition();
    recognition.lang = "en-US";
    recognition.continuous = true;
    recognition.interimResults = true;

    recognition.onresult = (event) => {
      let text = "";
      for (let i = event.resultIndex; i < event.results.length; i++) {
        text += event.results[i][0].transcript;
      }
      setTranscript(text);
    };

    recognitionRef.current = recognition;
  }, []);

  const startRecording = async () => {
    window.speechSynthesis.cancel();
    setTranscript("");
    setAudioURL(null);
    setSeconds(0);
    setAvatarState('listening');

    if (recognitionRef.current) recognitionRef.current.start();

    timerRef.current = setInterval(() => {
      setSeconds((prev) => prev + 1);
    }, 1000);

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);
      mediaRecorderRef.current.ondataavailable = (e) => audioChunksRef.current.push(e.data);
      mediaRecorderRef.current.onstop = () => {
        const blob = new Blob(audioChunksRef.current, { type: "audio/webm" });
        setAudioURL(URL.createObjectURL(blob));
        audioChunksRef.current = [];
      };
      mediaRecorderRef.current.start();
      setRecording(true);
    } catch (err) {
      setAvatarState('idle');
      alert("Microphone access denied or error occurred.");
    }
  };

  const stopRecording = () => {
    if (recognitionRef.current) recognitionRef.current.stop();
    if (mediaRecorderRef.current) mediaRecorderRef.current.stop();
    clearInterval(timerRef.current);
    setRecording(false);
    setAvatarState('idle');
  };

  const progress = ((currentIndex + 1) / maxQuestions) * 100;

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-950 text-white">
        <div className="flex flex-col items-center gap-5">
          <Loader2 className="w-14 h-14 text-cyan-500 animate-spin" />
          <p className="text-slate-400 font-bold text-lg tracking-wider animate-pulse">Initializing Virtual Interview Room...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-tr from-slate-50 via-slate-100 to-blue-50/30 text-slate-800 py-10 px-4 sm:px-6 lg:px-8 font-sans overflow-x-hidden">
      <div className="max-w-5xl mx-auto space-y-6">
        
        {/* 1. TOP INTERACTION STATUS & PANEL */}
        <div className="flex flex-col md:flex-row justify-between items-center bg-white/90 border border-slate-200/80 backdrop-blur-xl rounded-2xl p-5 shadow-lg gap-4">
          <div className="flex items-center gap-3">
            <div className="p-2.5 bg-cyan-50 border border-cyan-200 text-cyan-600 rounded-xl">
              <Headphones size={20} className="animate-pulse" />
            </div>
            <div>
              <h1 className="text-xs font-bold tracking-widest text-slate-500 uppercase">Adaptive Conversational Engine</h1>
              <p className="text-xs text-cyan-700 font-bold">Connected ➔ Active Session ID: {JSON.parse(localStorage.getItem('currentInterview'))?.id || 'ONGOING'}</p>
            </div>
          </div>
          
          <div className="w-full md:w-60">
            <ProgressBar 
              progress={progress} 
              label="Evaluation Turn Progression" 
              current={currentIndex + 1} 
              total={maxQuestions} 
            />
          </div>
        </div>

        {/* Dynamic AI Alert Transitions Banner */}
        <AnimatePresence>
          {notification && (
            <motion.div
              initial={{ opacity: 0, y: -15 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -15 }}
              className="bg-cyan-600/90 text-white rounded-xl py-3 px-5 text-sm font-black text-center shadow-lg border border-cyan-500 flex items-center justify-center gap-2"
            >
              <Sparkles size={16} className="animate-spin" />
              {notification}
            </motion.div>
          )}
        </AnimatePresence>

        {/* 2. COCKPIT CONSOLE MAIN LAYOUT */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          
          {/* A. LEFT SIDEBAR: ANIMATED Recruiter Core */}
          <div className="lg:col-span-1 bg-white/90 border border-slate-200/80 backdrop-blur-xl rounded-3xl p-6 flex flex-col items-center justify-between shadow-xl relative overflow-hidden group">
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(6,182,212,0.04)_0%,transparent_70%)] pointer-events-none" />
            
            <div className="w-full flex justify-between items-center z-10">
              <span className="text-[10px] font-black tracking-widest text-slate-400 uppercase">AI Recruiter Core</span>
              <AnimatePresence mode="wait">
                <motion.div
                  key={avatarState}
                  initial={{ opacity: 0, scale: 0.8 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.8 }}
                  className={`flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black tracking-widest uppercase border ${
                    avatarState === 'speaking' ? 'bg-teal-50 text-teal-700 border-teal-200' :
                    avatarState === 'listening' ? 'bg-rose-50 text-rose-700 border-rose-200 animate-pulse' :
                    avatarState === 'thinking' ? 'bg-purple-50 text-purple-700 border-purple-200' :
                    'bg-blue-50 text-blue-700 border-blue-200'
                  }`}
                >
                  <Activity size={10} className={avatarState !== 'idle' ? 'animate-spin' : ''} />
                  {avatarState}
                </motion.div>
              </AnimatePresence>
            </div>

            <div className="my-6">
              <AIAvatar state={avatarState} />
            </div>

            <div className="w-full text-center space-y-3 z-10">
              <div className="h-6 flex items-center justify-center">
                <AnimatePresence mode="wait">
                  {avatarState === 'speaking' && (
                    <motion.p key="spk" initial={{ opacity: 0, y: 5 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -5 }} className="text-xs font-bold text-teal-600 flex items-center gap-1.5">
                      <Sparkles size={13} className="animate-spin" /> AI is speaking the question...
                    </motion.p>
                  )}
                  {avatarState === 'listening' && (
                    <motion.p key="lst" initial={{ opacity: 0, y: 5 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -5 }} className="text-xs font-bold text-rose-600 animate-pulse">
                      🎙️ Capturing your voice answer...
                    </motion.p>
                  )}
                  {avatarState === 'thinking' && (
                    <motion.p key="thk" initial={{ opacity: 0, y: 5 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -5 }} className="text-xs font-bold text-purple-600">
                      Processing AI response context...
                    </motion.p>
                  )}
                  {avatarState === 'idle' && (
                    <motion.p key="idl" initial={{ opacity: 0, y: 5 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -5 }} className="text-xs font-bold text-slate-500">
                      Awaiting your answer
                    </motion.p>
                  )}
                </AnimatePresence>
              </div>

              <div className="h-10 flex items-center justify-center overflow-hidden">
                <AnimatePresence>
                  {avatarState === 'speaking' && (
                    <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }}>
                      <VoiceWaveform />
                    </motion.div>
                  )}
                  {avatarState === 'listening' && (
                    <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }}>
                      <MicWaveform />
                    </motion.div>
                  )}
                </AnimatePresence>
              </div>
            </div>
          </div>

          {/* B. RIGHT PANEL: ADAPTIVE QUESTION CONSOLE */}
          <div className="lg:col-span-2 flex flex-col gap-6">
            <motion.div 
              key={currentIndex}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -20 }}
              className="bg-white/90 border border-slate-200/80 rounded-3xl p-8 flex-1 flex flex-col justify-between shadow-xl relative overflow-hidden"
            >
              <div className="absolute top-0 right-0 w-24 h-24 bg-cyan-500/5 blur-3xl rounded-full" />
              
              <div className="space-y-4 relative z-10">
                <div className="flex justify-between items-center">
                  <div className="flex gap-2 items-center">
                    <span className="text-[10px] font-black text-cyan-600 tracking-widest uppercase">
                      ROUND {currentIndex + 1} OF {maxQuestions}
                    </span>
                    <span className="text-[10px] font-bold text-slate-400">|</span>
                    <span className="text-[10px] font-black text-indigo-600 tracking-widest uppercase bg-indigo-50 px-2 py-0.5 rounded border border-indigo-100">
                      {currentQuestion?.category || "General"}
                    </span>
                  </div>
                  
                  {/* Difficulty indicators */}
                  <div className="flex items-center gap-1.5">
                    <Award size={12} className="text-amber-500 animate-pulse" />
                    <span className={`text-[10px] font-black uppercase tracking-wider px-2.5 py-0.5 rounded-full border ${
                      sessionDifficulty === "HARD" ? "bg-red-50 text-red-600 border-red-200" :
                      sessionDifficulty === "EASY" ? "bg-emerald-50 text-emerald-600 border-emerald-200" :
                      "bg-blue-50 text-blue-600 border-blue-200"
                    }`}>
                      {sessionDifficulty}
                    </span>
                  </div>
                </div>
                
                <h2 className="text-xl sm:text-2xl font-black text-slate-800 leading-relaxed">
                  "{currentQuestion?.questionText || "Preparing next question..."}"
                </h2>

                {currentQuestion?.adaptiveReason && (
                  <p className="text-[11px] text-slate-500 italic bg-slate-50 p-3 rounded-xl border border-slate-100 mt-2">
                    <Compass className="inline mr-1 w-3.5 h-3.5 text-cyan-600" /> 
                    <strong>AI Router Insight:</strong> {currentQuestion.adaptiveReason}
                  </p>
                )}
              </div>

              <div className="flex items-center gap-3 mt-6 relative z-10 pt-4 border-t border-slate-100">
                <button 
                  onClick={handleRepeat}
                  disabled={recording || saving}
                  className="flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-50 border border-slate-200 hover:bg-slate-100 text-cyan-600 font-bold text-xs transition-all hover:scale-105 active:scale-95 disabled:opacity-30"
                >
                  <RotateCcw size={14} /> Replay AI Voice
                </button>
              </div>
            </motion.div>

            {/* Answer Timer Counter */}
            <div className="bg-white/90 border border-slate-200/80 backdrop-blur-xl rounded-3xl p-6 shadow-xl flex items-center justify-between">
              <div className="flex items-center gap-4">
                <div className={`p-3 rounded-2xl ${recording ? 'bg-red-50 text-red-500 border border-red-200 animate-pulse' : 'bg-slate-100 text-slate-500 border border-slate-200'}`}>
                  <Timer size={22} />
                </div>
                <div>
                  <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">Speaking Counter</p>
                  <p className="text-lg font-mono font-black text-slate-800">
                    {Math.floor(seconds / 60)}:{(seconds % 60).toString().padStart(2, '0')}
                  </p>
                </div>
              </div>
              
              {recording ? (
                <div className="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 font-bold text-xs animate-pulse">
                  <span className="w-2 h-2 bg-rose-500 rounded-full" />
                  Voice Capture Active
                </div>
              ) : (
                <div className="flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-500 font-bold text-xs">
                  <span className="w-2 h-2 bg-slate-400 rounded-full" />
                  Engine Standby
                </div>
              )}
            </div>
          </div>
        </div>

        {/* 3. COGNITIVE WORKSPACE: RECORDING CONTROLS & TRANSCRIPT PANEL */}
        <div className="bg-white/90 border border-slate-200/80 backdrop-blur-xl rounded-3xl p-6 sm:p-8 shadow-xl space-y-6 relative overflow-hidden">
          <div className="absolute bottom-0 left-0 w-36 h-36 bg-purple-500/5 blur-3xl rounded-full" />

          <div className="flex flex-col sm:flex-row items-center justify-between gap-6 pb-6 border-b border-slate-100">
            <div className="flex items-center gap-4">
              {!recording ? (
                <button 
                  onClick={startRecording}
                  disabled={saving}
                  className="w-16 h-16 sm:w-20 sm:h-20 flex items-center justify-center bg-gradient-to-tr from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white rounded-full shadow-lg shadow-cyan-500/20 border border-cyan-400/30 transition-all hover:scale-110 active:scale-95 group disabled:opacity-50"
                >
                  <Mic className="w-7 h-7 sm:w-8 sm:h-8 group-hover:scale-110 transition-transform" />
                </button>
              ) : (
                <button 
                  onClick={stopRecording}
                  className="w-16 h-16 sm:w-20 sm:h-20 flex items-center justify-center bg-gradient-to-tr from-rose-600 to-red-700 hover:from-rose-500 hover:to-red-600 text-white rounded-full shadow-lg shadow-red-500/20 border border-rose-400/30 transition-all hover:scale-110 active:scale-95 animate-pulse"
                >
                  <Square className="w-7 h-7 sm:w-8 sm:h-8 fill-current" />
                </button>
              )}

              <div>
                <h3 className="text-sm font-black tracking-widest text-slate-800 uppercase">
                  {!recording ? "Click Mic to Answer" : "Recording Audio Answer..."}
                </h3>
                <p className="text-xs text-slate-500 mt-1">
                  {!recording 
                    ? "Ensure your surroundings are quiet before speaking." 
                    : "Speak clearly. When finished, press the stop square."}
                </p>
              </div>
            </div>

            <div className="flex items-center gap-3 w-full sm:w-auto justify-end">
              <button 
                onClick={() => transitionToNextStep(true)}
                disabled={recording || saving}
                className="flex items-center justify-center gap-2 px-4 py-3 rounded-xl border border-slate-200 hover:bg-slate-50 text-slate-500 hover:text-slate-700 font-bold text-xs transition-all disabled:opacity-30 disabled:pointer-events-none"
              >
                <FastForward size={14} /> Skip Question
              </button>

              <Button 
                onClick={() => transitionToNextStep(false)}
                disabled={recording || (!audioURL && !transcript)}
                loading={saving}
                className="px-6 py-3 min-w-[150px] bg-gradient-to-r from-cyan-600 to-blue-600 hover:from-cyan-500 hover:to-blue-500 text-white border-0 shadow-md"
                icon={ChevronRight}
              >
                {currentIndex === maxQuestions - 1 ? 'Finish Interview' : 'Next Question'}
              </Button>
            </div>
          </div>

          {/* Live Transcript Textarea */}
          <div className="space-y-3">
            <div className="flex justify-between items-center">
              <span className="text-[10px] font-black tracking-widest text-slate-400 uppercase">Live Transcribed Stream</span>
              {recording && (
                <div className="flex items-center gap-1 text-[10px] text-rose-600 font-bold uppercase animate-pulse">
                  <Activity size={10} className="animate-spin" /> Speech Recognition Active
                </div>
              )}
            </div>
            
            <textarea 
              value={transcript}
              onChange={(e) => setTranscript(e.target.value)}
              placeholder="Type your answer here or click the microphone to transcribe your speech in real-time..."
              className="w-full h-28 p-4 bg-slate-50 border border-slate-200 rounded-2xl focus:ring-2 focus:ring-cyan-500/20 focus:border-cyan-500 outline-none transition-all resize-none text-slate-700 font-semibold leading-relaxed italic placeholder-slate-400"
              disabled={recording}
            />

            <AnimatePresence>
              {audioURL && (
                <motion.div 
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: 10 }}
                  className="flex flex-col sm:flex-row items-center gap-4 bg-emerald-50 border border-emerald-200 p-4 rounded-2xl text-emerald-700 justify-between shadow-sm"
                >
                  <div className="flex items-center gap-2">
                    <CheckCircle2 size={16} />
                    <span className="text-xs font-black uppercase tracking-widest">Audio Captured Successfully</span>
                  </div>
                  <audio src={audioURL} controls className="h-8 max-w-full sm:max-w-xs" />
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </div>

        {/* 4. CONVERSATIONAL TIMELINE / CHAT HISTORY PANEL */}
        <div className="bg-white/90 border border-slate-200/80 backdrop-blur-xl rounded-3xl p-6 sm:p-8 shadow-xl space-y-4">
          <div className="flex items-center gap-2">
            <MessageSquare className="text-cyan-600 w-5 h-5 animate-pulse" />
            <h3 className="text-sm font-black tracking-widest text-slate-800 uppercase">Interview Conversation History Timeline</h3>
          </div>
          
          <div className="space-y-4 max-h-[300px] overflow-y-auto pr-2 divide-y divide-slate-100">
            {questions.map((q, idx) => (
              <div key={idx} className={`pt-4 ${idx === 0 ? 'pt-0' : ''}`}>
                <div className="flex justify-between items-start mb-2">
                  <div className="flex gap-2 items-center">
                    <span className="w-1.5 h-1.5 bg-cyan-600 rounded-full" />
                    <span className="text-[10px] font-black text-slate-400 uppercase">ROUND {idx + 1}</span>
                    <span className="text-[10px] font-bold text-slate-500 uppercase bg-slate-100 px-2 py-0.5 rounded border">
                      {q.category || "Skill Area"}
                    </span>
                  </div>
                  <span className="text-[10px] font-black text-indigo-600">{q.difficultyLevel || "MEDIUM"}</span>
                </div>
                
                <p className="text-sm font-black text-slate-800 mb-2">"{q.questionText}"</p>
                
                {/* Candidate response box if answered */}
                {idx < currentIndex ? (
                  <div className="bg-slate-50/80 p-3 rounded-xl border border-slate-100 flex flex-col gap-1.5">
                    <div className="flex justify-between items-center text-[10px]">
                      <span className="font-bold text-slate-400">Candidate Response:</span>
                      <span className="text-emerald-600 font-bold uppercase">Evaluated</span>
                    </div>
                    <p className="text-xs text-slate-600 leading-relaxed italic">
                      "{historyAnswers[idx]?.answer || "Transcribing/Saving Response..."}"
                    </p>
                  </div>
                ) : (
                  <div className="text-xs text-cyan-600 animate-pulse font-bold">
                    💬 AI Interviewer is listening for your response...
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
};

export default Interview;
