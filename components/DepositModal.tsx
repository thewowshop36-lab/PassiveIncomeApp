"use client";

import React, { useState } from "react";
import { X, Copy, Check, UploadCloud } from "lucide-react";

interface DepositModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (amount: number, method: string, tid: string) => void;
}

export default function DepositModal({ isOpen, onClose, onSubmit }: DepositModalProps) {
  const [method, setMethod] = useState<"JazzCash" | "EasyPaisa" | "USDT">("JazzCash");
  const [amount, setAmount] = useState("50");
  const [tid, setTid] = useState("");
  const [copied, setCopied] = useState(false);
  const [slip, setSlip] = useState<string | null>(null);

  if (!isOpen) return null;

  const accounts = {
    JazzCash: "0304-9842109 (Title: Passive Global Corp)",
    EasyPaisa: "0335-7761042 (Title: Smart Venture)",
    USDT: "TX9aKpR88bV3mZ1eN88wqPo4Xyt89v (TRC20)",
  };

  const copyNumber = () => {
    navigator.clipboard.writeText(accounts[method].split(" ")[0]);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const val = parseFloat(amount);
    if (val > 0) {
      onSubmit(val, method, tid || `DEP-${Math.floor(100000 + Math.random() * 900000)}`);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-end sm:items-center justify-center p-0 sm:p-4">
      <div className="bg-[#1e293b] border-t sm:border border-slate-700 w-full max-w-md rounded-t-3xl sm:rounded-2xl p-6 relative shadow-2xl animate-in slide-in-from-bottom">
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-lg font-bold text-white">Deposit Funds</h3>
            <p className="text-xs text-slate-400">Instant verification via Official Channels</p>
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
                  ? "bg-orange-500 border-orange-400 text-white shadow-md shadow-orange-500/20"
                  : "bg-slate-800/80 border-slate-700 text-slate-300 hover:bg-slate-800"
              }`}
            >
              {m}
            </button>
          ))}
        </div>

        {/* Account Info Box */}
        <div className="p-3.5 rounded-xl bg-slate-900 border border-slate-700/80 mb-4">
          <div className="text-[11px] text-slate-400 uppercase tracking-wider font-semibold mb-1">
            Send Payment To ({method})
          </div>
          <div className="flex items-center justify-between gap-2">
            <span className="text-sm font-mono font-bold text-orange-400 truncate">
              {accounts[method]}
            </span>
            <button
              type="button"
              onClick={copyNumber}
              className="p-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300 active:scale-95 shrink-0"
            >
              {copied ? <Check className="w-4 h-4 text-green-400" /> : <Copy className="w-4 h-4" />}
            </button>
          </div>
          {copied && <p className="text-[11px] text-green-400 mt-1">Copied to clipboard!</p>}
        </div>

        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Deposit Amount ($ USD)</label>
            <input
              type="number"
              min="5"
              step="any"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white font-semibold text-sm focus:outline-none focus:border-orange-500"
              placeholder="Min $5"
              required
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Transaction ID / Hash (TID)</label>
            <input
              type="text"
              value={tid}
              onChange={(e) => setTid(e.target.value)}
              className="w-full px-3.5 py-2.5 rounded-xl bg-slate-900 border border-slate-700 text-white font-mono text-sm focus:outline-none focus:border-orange-500"
              placeholder="e.g. 1829471928"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Payment Proof Screenshot</label>
            <button
              type="button"
              onClick={() => setSlip(`receipt_proof_${Date.now() % 1000}.jpg`)}
              className="w-full py-2.5 px-3 rounded-xl border border-dashed border-slate-600 bg-slate-900/50 hover:bg-slate-800 text-xs text-slate-300 flex items-center justify-center gap-2"
            >
              <UploadCloud className="w-4 h-4 text-orange-400" />
              <span>{slip ? `Attached: ${slip}` : "Click to attach screenshot proof"}</span>
            </button>
          </div>

          <button
            type="submit"
            className="w-full mt-2 py-3 rounded-xl bg-gradient-to-r from-orange-500 to-amber-600 hover:from-orange-600 hover:to-amber-700 text-white font-bold text-sm shadow-lg shadow-orange-500/20 active:scale-[0.98] transition"
          >
            Submit Deposit Request
          </button>
        </form>
      </div>
    </div>
  );
}
