"use client";

import React, { useEffect, useState } from "react";
import { X, Play, CheckCircle, Award } from "lucide-react";

interface VideoModalProps {
  ad: { id: string; title: string; sponsor: string; reward: number; duration: number; category: string } | null;
  onClose: () => void;
  onClaim: (reward: number) => void;
}

export default function VideoModal({ ad, onClose, onClaim }: VideoModalProps) {
  const [secondsLeft, setSecondsLeft] = useState(10);
  const [canClaim, setCanClaim] = useState(false);

  useEffect(() => {
    if (!ad) return;
    setSecondsLeft(ad.duration);
    setCanClaim(false);

    const interval = setInterval(() => {
      setSecondsLeft((prev) => {
        if (prev <= 1) {
          clearInterval(interval);
          setCanClaim(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [ad]);

  if (!ad) return null;

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-[#1e293b] border border-slate-700 w-full max-w-sm rounded-2xl p-5 relative shadow-2xl flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between mb-4">
          <span className="text-[11px] font-bold tracking-wider px-2.5 py-1 rounded-full bg-orange-500/20 text-orange-400 border border-orange-500/30">
            SPONSORED REWARD AD
          </span>
          {canClaim ? (
            <button onClick={onClose} className="p-1 rounded-full text-slate-400 hover:text-white bg-slate-800">
              <X className="w-5 h-5" />
            </button>
          ) : (
            <span className="text-xs font-bold px-2.5 py-1 rounded-full bg-slate-800 text-slate-300">
              {secondsLeft}s left
            </span>
          )}
        </div>

        {/* Video simulation screen */}
        <div className="w-full h-44 rounded-xl bg-gradient-to-br from-slate-900 via-slate-800 to-slate-950 flex flex-col items-center justify-center p-4 border border-slate-700/60 relative overflow-hidden">
          <div className="w-14 h-14 rounded-full bg-orange-500/20 border border-orange-500/40 flex items-center justify-center mb-3">
            {canClaim ? (
              <CheckCircle className="w-8 h-8 text-green-400" />
            ) : (
              <Play className="w-7 h-7 text-orange-400 fill-orange-400 ml-1 animate-pulse" />
            )}
          </div>
          <h4 className="text-sm font-bold text-center text-white line-clamp-1">{ad.title}</h4>
          <p className="text-xs text-slate-400 mt-1">{ad.sponsor} • {ad.category}</p>

          {/* Progress bar */}
          <div className="absolute bottom-0 left-0 right-0 h-1 bg-slate-800">
            <div
              className="h-full bg-orange-500 transition-all duration-1000 linear"
              style={{ width: `${((ad.duration - secondsLeft) / ad.duration) * 100}%` }}
            />
          </div>
        </div>

        {/* Reward Box */}
        <div className="mt-4 p-3 rounded-xl bg-slate-800/80 border border-slate-700 flex items-center gap-3">
          <div className="w-9 h-9 rounded-lg bg-amber-500/20 flex items-center justify-center">
            <Award className="w-5 h-5 text-amber-400" />
          </div>
          <div>
            <div className="text-xs text-slate-400">Completion Reward</div>
            <div className="text-base font-bold text-green-400">+${ad.reward.toFixed(2)} USD</div>
          </div>
        </div>

        {/* Action Button */}
        <div className="mt-5">
          {canClaim ? (
            <button
              onClick={() => onClaim(ad.reward)}
              className="w-full py-3 rounded-xl bg-gradient-to-r from-green-600 to-emerald-600 hover:from-green-500 hover:to-emerald-500 text-white font-bold text-sm shadow-lg shadow-green-900/30 transition active:scale-[0.98]"
            >
              🎉 Claim +${ad.reward.toFixed(2)} Now
            </button>
          ) : (
            <button
              onClick={onClose}
              className="w-full py-2.5 rounded-xl border border-slate-700 text-slate-400 text-xs hover:bg-slate-800 transition"
            >
              Skip (No Reward)
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
