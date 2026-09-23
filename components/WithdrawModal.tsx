"use client";

import React, { useState } from "react";
import { X, ArrowUpRight } from "lucide-react";

interface WithdrawModalProps {
  isOpen: boolean;
  balance: number;
  onClose: () => void;
  onSubmit: (amount: number, method: string, account: string) => void;
}

export default function WithdrawModal({ isOpen, balance, onClose, onSubmit }: WithdrawModalProps) {
  const [method, setMethod] = useState<"JazzCash" | "EasyPaisa" | "USDT">("JazzCash");
  const [amount, setAmount] = useState("10");
  const [account, setAccount] = useState("");
  const [name, setName] = useState("");

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const val = parseFloat(amount);
    if (val >= 5 && val <= balance && account.trim()) {
      onSubmit(val, method, `${account} (${name || "User"})`);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-end sm:items-center justify-center p-0 sm:p-4">
      <div className="bg-[#1e293b] border-t sm:border border-slate-700 w-full max-w-md rounded-t-3xl sm:rounded-2xl p-6 relative shadow-2xl animate-in slide-in-from-bottom">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-lg font-bold text-white">Withdraw Funds</h3>
            <p className="text-xs text-green-400 font-semibold">Available: ${balance.toFixed(2)} USD</p>
          </div>
          <button onClick={onClose} className="p-1 rounded-full text-slate-400 hover:text-white bg-slate-800">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Method selector */}
        <div className="grid grid-cols-3 gap-2 mb-4">
          {(["JazzCash", "EasyPaisa", "USDT"] as const).map((m) => (
            <button
              key={m}
              type="button"
              onClick={() => setMethod(m)}
              className={`py-2 px-3 rounded-xl text-xs font-bold border transition ${
                method === m
                  ? "bg-green-600 border-green-500 text-white shadow-md shadow-green-600/20"
                  : "bg-slate-800/80 border-slate-700 text-slate-300 hover:bg-slate-800"
              }`}
            >
              {m}
            </button>
          ))}
        </div>

        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Withdraw Amount ($ USD)</label>
            <input
              type="number"
              min="5"
              max={balance}
              step="any"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white font-semibold text-sm focus:outline-none focus:border-green-500"
              placeholder="Min $5.00"
              required
            />
            <span className="text-[11px] text-slate-400 mt-1 block">Min $5.00 • 0% Fee for VIP members</span>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              {method === "USDT" ? "TRC20 Wallet Address" : `${method} Mobile Number`}
            </label>
            <input
              type="text"
              value={account}
              onChange={(e) => setAccount(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-green-500"
              placeholder={method === "USDT" ? "e.g. Txyz..." : "03xx-xxxxxxx"}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Account Holder Full Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white text-sm focus:outline-none focus:border-green-500"
              placeholder="e.g. Muhammad Ali"
            />
          </div>

          <button
            type="submit"
            disabled={parseFloat(amount) < 5 || parseFloat(amount) > balance || !account.trim()}
            className="w-full mt-2 py-3 rounded-xl bg-gradient-to-r from-emerald-600 to-green-600 disabled:opacity-50 disabled:cursor-not-allowed hover:from-emerald-500 hover:to-green-500 text-white font-bold text-sm shadow-lg shadow-green-900/20 active:scale-[0.98] transition flex items-center justify-center gap-2"
          >
            <ArrowUpRight className="w-4 h-4" />
            Process Withdrawal Request
          </button>
        </form>
      </div>
    </div>
  );
}
