import React from 'react';
import { motion } from 'framer-motion';

const ProgressBar = ({ progress, label, current, total }) => {
  return (
    <div className="w-full">
      <div className="flex justify-between items-end mb-2">
        <span className="text-sm font-medium text-slate-500">{label}</span>
        {current !== undefined && total !== undefined && (
          <span className="text-xs font-semibold text-primary-600 bg-primary-50 px-2 py-1 rounded-md">
            {current} of {total}
          </span>
        )}
      </div>
      <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
        <motion.div
          initial={{ width: 0 }}
          animate={{ width: `${progress}%` }}
          transition={{ duration: 0.5, ease: "easeOut" }}
          className="h-full bg-gradient-to-r from-primary-500 to-primary-600 rounded-full"
        />
      </div>
    </div>
  );
};

export default ProgressBar;
