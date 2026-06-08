const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = {
  getHeaders: () => {
    const user = JSON.parse(localStorage.getItem('user'));
    const headers = { 'Content-Type': 'application/json' };
    if (user && user.token) {
      headers['Authorization'] = `Bearer ${user.token}`;
    }
    return headers;
  },

  auth: {
    login: async (credentials) => {
      const response = await fetch(`${BASE_URL}/auth/signin`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
      });
      if (!response.ok) throw new Error('Login failed');
      const data = await response.json();
      localStorage.setItem('user', JSON.stringify(data));
      return { success: true, data };
    },
    signup: async (userData) => {
      const response = await fetch(`${BASE_URL}/auth/signup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      });
      if (!response.ok) {
          const error = await response.text();
          throw new Error(error || 'Signup failed');
      }
      return { success: true };
    }
  },

  interview: {
    start: async (jobTitle) => {
      const response = await fetch(`${BASE_URL}/interviews/start?jobTitle=${encodeURIComponent(jobTitle)}`, {
        method: 'POST',
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to start interview');
      return await response.json();
    },
    getInterview: async (id) => {
      const response = await fetch(`${BASE_URL}/interviews/${id}`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch interview');
      return await response.json();
    },
    saveAnswer: async (answerData) => {
      const response = await fetch(`${BASE_URL}/answers`, {
        method: 'POST',
        headers: api.getHeaders(),
        body: JSON.stringify(answerData)
      });
      if (!response.ok) throw new Error('Failed to save answer');
      return await response.json();
    },
    complete: async (id) => {
      const response = await fetch(`${BASE_URL}/interviews/${id}/complete`, {
        method: 'POST',
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to complete interview');
      return await response.text();
    },
    evaluate: async (question, answer) => {
      const response = await fetch(`${BASE_URL}/answers/evaluate`, {
        method: 'POST',
        headers: api.getHeaders(),
        body: JSON.stringify({ question, answer })
      });
      if (!response.ok) throw new Error('AI Evaluation failed');
      return await response.json();
    },
    getNextQuestion: async (sessionId) => {
      const response = await fetch(`${BASE_URL}/interview/next-question?sessionId=${sessionId}`, {
        method: 'POST',
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to get adaptive next question');
      return await response.json();
    },
    evaluateAdaptiveAnswer: async (questionId, answerText, durationSeconds) => {
      const response = await fetch(`${BASE_URL}/interview/evaluate-answer?questionId=${questionId}`, {
        method: 'POST',
        headers: api.getHeaders(),
        body: JSON.stringify({ answerText, durationSeconds })
      });
      if (!response.ok) throw new Error('Failed to evaluate adaptive answer');
      return await response.json();
    },
    getAdaptiveSummary: async (sessionId) => {
      const response = await fetch(`${BASE_URL}/interview/adaptive-summary/${sessionId}`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch adaptive summary');
      return await response.json();
    }
  },

  resume: {
    upload: async (file) => {
      const formData = new FormData();
      formData.append('file', file);
      
      const user = JSON.parse(localStorage.getItem('user'));
      const response = await fetch(`${BASE_URL}/resumes/upload`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${user?.token}`
        },
        body: formData
      });
      if (!response.ok) throw new Error('Upload failed');
      return await response.text();
    }
  },

  analytics: {
    getSummary: async () => {
      const response = await fetch(`${BASE_URL}/analytics/summary`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch summary');
      return await response.json();
    },
    getTrends: async (period = 'all') => {
      const response = await fetch(`${BASE_URL}/analytics/trends?period=${period}`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch trends');
      return await response.json();
    },
    getSkills: async () => {
      const response = await fetch(`${BASE_URL}/analytics/skills`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch skills');
      return await response.json();
    },
    getHistory: async () => {
      const response = await fetch(`${BASE_URL}/analytics/history`, {
        headers: api.getHeaders()
      });
      if (!response.ok) throw new Error('Failed to fetch history');
      return await response.json();
    }
  }
};

export default api;
