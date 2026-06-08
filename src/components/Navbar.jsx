import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { BrainCircuit, LogOut, User as UserIcon } from 'lucide-react';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem('user'));

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/login');
  };

  const isAuthPage = ['/login', '/signup'].includes(location.pathname);

  if (isAuthPage) return null;

  return (
    <nav className="bg-white/80 backdrop-blur-lg border-b border-slate-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex items-center gap-2 group cursor-pointer" onClick={() => navigate('/')}>
            <div className="bg-primary-600 p-2 rounded-xl group-hover:scale-110 transition-transform">
              <BrainCircuit className="text-white" size={24} />
            </div>
            <span className="text-xl font-bold bg-gradient-to-r from-slate-900 to-slate-700 bg-clip-text text-transparent">
              AI Interview <span className="text-primary-600 font-extrabold">Pro</span>
            </span>
          </div>

          {user && (
            <div className="flex items-center gap-6">
              <div className="hidden md:flex items-center gap-6 mr-6 border-r border-slate-200 pr-6">
                <Link to="/dashboard" className="text-sm font-semibold text-slate-600 hover:text-primary-600 transition-colors">
                  Dashboard
                </Link>
                <Link to="/" className="text-sm font-semibold text-slate-600 hover:text-primary-600 transition-colors">
                  Interviews
                </Link>
              </div>

              <div className="hidden md:flex items-center gap-2 text-slate-500 hover:text-primary-600 transition-colors cursor-pointer group">
                <div className="w-8 h-8 bg-slate-100 rounded-full flex items-center justify-center group-hover:bg-primary-50 transition-colors">
                  <UserIcon size={16} />
                </div>
                <span className="text-sm font-semibold">{user.username || user.email}</span>
              </div>
              
              <button 
                onClick={handleLogout}
                className="flex items-center gap-2 px-4 py-2 text-slate-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all text-sm font-medium"
              >
                <LogOut size={18} />
                <span className="hidden sm:inline">Logout</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
