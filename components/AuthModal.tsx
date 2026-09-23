"use client";

import React, { useState } from "react";
import { X, Phone, Mail, Lock, User } from "lucide-react";

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (name: string, identifier: string) => void;
}

export default function AuthModal({ isOpen, onClose, onSuccess }: AuthModalProps) {
  const [isRegister, setIsRegister] = useState(false);
  const [type, setType] = useState<"PHONE" | "EMAIL">("PHONE");
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (identifier.trim() && password.length >= 4) {
      onSuccess(fullName.trim() || "Member " + Math.floor(100 + Math.random() * 900), identifier);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-[#1e293b] border border-slate-700 w-full max-w-sm rounded-2xl p-6 relative shadow-2xl">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-lg font-bold text-white">
              {isRegister ? "Create Account" : "Member Login"}
            </h3>
            <p className="text-xs text-slate-400">
              {isRegister ? "Get $10 instant welcome bonus!" : "Access your daily tasks & balance"}
            </p>
          </div>
          <button onClick={onClose} className="p-1 rounded-full text-slate-400 hover:text-white bg-slate-800">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Tab switch phone vs email */}
        <div className="grid grid-cols-2 p-1 rounded-xl bg-slate-900 mb-4 border border-slate-700/60">
          <button
            type="button"
            onClick={() => setType("PHONE")}
            className={`py-1.5 rounded-lg text-xs font-bold transition flex items-center justify-center gap-1.5 ${
              type === "PHONE" ? "bg-orange-500 text-white shadow-sm" : "text-slate-400 hover:text-white"
            }`}
          >
            <Phone className="w-3.5 h-3.5" /> Mobile Phone
          </button>
          <button
            type="button"
            onClick={() => setType("EMAIL")}
            className={`py-1.5 rounded-lg text-xs font-bold transition flex items-center justify-center gap-1.5 ${
              type === "EMAIL" ? "bg-orange-500 text-white shadow-sm" : "text-slate-400 hover:text-white"
            }`}
          >
            <Mail className="w-3.5 h-3.5" /> Email
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-3">
          {isRegister && (
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Full Name</label>
              <div className="relative">
                <User className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
                <input
                  type="text"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white text-sm focus:outline-none focus:border-orange-500"
                  placeholder="e.g. Ali Khan"
                  required={isRegister}
                />
              </div>
            </div>
          )}

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              {type === "PHONE" ? "Phone Number" : "Email Address"}
            </label>
            <div className="relative">
              {type === "PHONE" ? (
                <Phone className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
              ) : (
                <Mail className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
              )}
              <input
                type={type === "PHONE" ? "tel" : "email"}
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
                className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white text-sm focus:outline-none focus:border-orange-500 font-mono"
                placeholder={type === "PHONE" ? "03xx-xxxxxxx" : "name@example.com"}
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Password</label>
            <div className="relative">
              <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full pl-9 pr-3 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white text-sm focus:outline-none focus:border-orange-500"
                placeholder="••••••••"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full mt-2 py-3 rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-sm shadow-lg shadow-orange-500/25 active:scale-[0.98] transition"
          >
            {isRegister ? "Sign Up & Get $10 Bonus" : "Sign In"}
          </button>
        </form>

        <div className="mt-4 text-center">
          <button
            type="button"
            onClick={() => setIsRegister(!isRegister)}
            className="text-xs text-orange-400 hover:underline font-medium"
          >
            {isRegister ? "Already registered? Login here" : "Don't have an account? Sign Up free"}
          </button>
        </div>
      </div>
    </div>
  );
}
