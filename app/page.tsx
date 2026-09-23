"use client";

import React, { useState } from "react";
import { 
  Home, 
  Award, 
  Users, 
  User, 
  PlusCircle, 
  ArrowUpRight, 
  PlayCircle, 
  CheckCircle, 
  Copy, 
  Check, 
  ShieldCheck, 
  Megaphone,
  ArrowDownLeft,
  ArrowUpRight as ArrowUp
} from "lucide-react";
import { INVESTMENT_PLANS, INITIAL_ADS, INITIAL_TEAM_MEMBERS } from "@/lib/data";
import VideoModal from "@/components/VideoModal";
import DepositModal from "@/components/DepositModal";
import WithdrawModal from "@/components/WithdrawModal";
import AuthModal from "@/components/AuthModal";

export default function App() {
  const [activeTab, setActiveTab] = useState<"home" | "plans" | "team" | "account">("home");

  // User & Wallet State
  const [balance, setBalance] = useState(12.50);
  const [totalEarned, setTotalEarned] = useState(48.75);
  const [totalWithdrawn, setTotalWithdrawn] = useState(35.00);
  const [todayAdsWatched, setTodayAdsWatched] = useState(3);
  const [dailyLimit, setDailyLimit] = useState(12);
  const [activePlanId, setActivePlanId] = useState(3);
  const [vipTier, setVipTier] = useState("VIP 3 Gold");
  const [referralCode] = useState("PI-984251");
  const [user, setUser] = useState<{ name: string; identifier: string } | null>(null);

  // Transactions State
  const [transactions, setTransactions] = useState([
    { id: 1, title: "Daily Video Ad Rewards", amount: 0.15, isCredit: true, method: "Rewarded Ads", time: "2 hrs ago" },
    { id: 2, title: "Team Level 1 Commission", amount: 2.40, isCredit: true, method: "Affiliate Bonus", time: "6 hrs ago" },
    { id: 3, title: "Withdrawal to Binance (USDT)", amount: 25.00, isCredit: false, method: "USDT (TRC-20)", time: "Yesterday" },
    { id: 4, title: "Deposit via JazzCash", amount: 50.00, isCredit: true, method: "JazzCash", time: "3 days ago" },
  ]);

  // Modals state
  const [currentVideoAd, setCurrentVideoAd] = useState<typeof INITIAL_ADS[0] | null>(null);
  const [isDepositOpen, setIsDepositOpen] = useState(false);
  const [isWithdrawOpen, setIsWithdrawOpen] = useState(false);
  const [isAuthOpen, setIsAuthOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [copiedCode, setCopiedCode] = useState(false);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  const handleClaimAdReward = (reward: number) => {
    if (todayAdsWatched >= dailyLimit) {
      showToast("Daily ad watching limit reached! Upgrade VIP plan.");
      setCurrentVideoAd(null);
      return;
    }
    setBalance((prev) => parseFloat((prev + reward).toFixed(2)));
    setTotalEarned((prev) => parseFloat((prev + reward).toFixed(2)));
    setTodayAdsWatched((prev) => prev + 1);
    setTransactions((prev) => [
      { id: Date.now(), title: `Watched: ${currentVideoAd?.title}`, amount: reward, isCredit: true, method: "Video Ad", time: "Just now" },
      ...prev
    ]);
    setCurrentVideoAd(null);
    showToast(`🎉 Rewarded +$${reward.toFixed(2)} to wallet!`);
  };

  const handleDepositSubmit = (amt: number, method: string, tid: string) => {
    setTransactions((prev) => [
      { id: Date.now(), title: `Deposit via ${method} (Pending)`, amount: amt, isCredit: true, method, time: "Pending" },
      ...prev
    ]);
    showToast(`Deposit request of $${amt} submitted! Ref: ${tid}`);
  };

  const handleWithdrawSubmit = (amt: number, method: string, account: string) => {
    setBalance((prev) => parseFloat((prev - amt).toFixed(2)));
    setTotalWithdrawn((prev) => parseFloat((prev + amt).toFixed(2)));
    setTransactions((prev) => [
      { id: Date.now(), title: `Withdrawal to ${account}`, amount: amt, isCredit: false, method, time: "Just now" },
      ...prev
    ]);
    showToast(`Withdrawal of $${amt} to ${method} submitted!`);
  };

  const handleUpgradePlan = (plan: typeof INVESTMENT_PLANS[0]) => {
    if (balance < plan.depositPriceUsd) {
      showToast(`Insufficient balance ($${balance.toFixed(2)}). Deposit $${plan.depositPriceUsd} to upgrade!`);
      setIsDepositOpen(true);
      return;
    }
    setBalance((prev) => parseFloat((prev - plan.depositPriceUsd).toFixed(2)));
    setActivePlanId(plan.planId);
    setVipTier(plan.badge);
    setDailyLimit(plan.dailyAdsLimit);
    setTransactions((prev) => [
      { id: Date.now(), title: `Activated ${plan.name}`, amount: plan.depositPriceUsd, isCredit: false, method: "Plan Upgrade", time: "Just now" },
      ...prev
    ]);
    showToast(`🎉 Upgraded to ${plan.badge}! Daily limit is now ${plan.dailyAdsLimit} ads.`);
  };

  return (
    <div className="flex-1 flex flex-col p-4">
      {/* Toast Alert */}
      {toastMessage && (
        <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 max-w-xs w-full bg-slate-900 border border-orange-500/80 text-orange-200 px-4 py-3 rounded-2xl shadow-2xl text-xs font-semibold text-center animate-in fade-in slide-in-from-top">
          {toastMessage}
        </div>
      )}

      {/* Header */}
      <header className="flex items-center justify-between mb-4">
        <div>
          <h1 className="text-xl font-black tracking-tight text-white flex items-center gap-1.5">
            <span className="text-orange-500">Passive</span> Income
          </h1>
          <p className="text-[11px] text-slate-400">Verified Earnings & Daily Tasks</p>
        </div>
        <div className="flex items-center gap-2">
          <span className="px-2.5 py-1 rounded-full bg-amber-500/10 border border-amber-500/30 text-amber-400 font-bold text-xs">
            {vipTier}
          </span>
          {!user && (
            <button
              onClick={() => setIsAuthOpen(true)}
              className="text-xs font-bold px-3 py-1.5 rounded-xl bg-orange-500 text-white hover:bg-orange-600 transition"
            >
              Login
            </button>
          )}
        </div>
      </header>

      {/* Main Content Area */}
      {activeTab === "home" && (
        <div className="space-y-4">
          {/* Main Wallet Card */}
          <div className="rounded-3xl p-5 bg-gradient-to-br from-orange-500 via-orange-600 to-amber-600 text-white shadow-xl shadow-orange-600/20 relative overflow-hidden">
            <div className="flex justify-between items-center mb-1">
              <span className="text-xs text-orange-100 font-medium">Available Balance</span>
              <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-white/20">USD / USDT</span>
            </div>
            <div className="text-3xl font-extrabold tracking-tight mb-4">
              ${balance.toFixed(2)}
            </div>

            <div className="grid grid-cols-3 gap-2 py-3 px-3 rounded-2xl bg-black/15 backdrop-blur-sm mb-4 text-center">
              <div>
                <div className="text-[10px] text-orange-100">Total Earned</div>
                <div className="text-xs font-bold">${totalEarned.toFixed(2)}</div>
              </div>
              <div>
                <div className="text-[10px] text-orange-100">Withdrawn</div>
                <div className="text-xs font-bold">${totalWithdrawn.toFixed(2)}</div>
              </div>
              <div>
                <div className="text-[10px] text-orange-100">Today Ads</div>
                <div className="text-xs font-bold">{todayAdsWatched}/{dailyLimit}</div>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <button
                onClick={() => setIsDepositOpen(true)}
                className="py-2.5 px-3 rounded-xl bg-white text-orange-600 font-bold text-xs flex items-center justify-center gap-1.5 hover:bg-orange-50 transition active:scale-95"
              >
                <PlusCircle className="w-4 h-4 text-orange-600" /> Deposit
              </button>
              <button
                onClick={() => setIsWithdrawOpen(true)}
                className="py-2.5 px-3 rounded-xl bg-slate-900 text-white font-bold text-xs flex items-center justify-center gap-1.5 hover:bg-slate-800 transition active:scale-95"
              >
                <ArrowUpRight className="w-4 h-4" /> Withdraw
              </button>
            </div>
          </div>

          {/* Live ticker banner */}
          <div className="p-3 rounded-2xl bg-slate-800/80 border border-slate-700/60 flex items-center gap-2 text-xs text-slate-300">
            <div className="p-1 rounded-full bg-emerald-500/20 text-emerald-400 shrink-0">
              <Megaphone className="w-3.5 h-3.5" />
            </div>
            <span className="truncate">user***891 withdrew $45.00 via USDT • Instant</span>
          </div>

          {/* Daily Tasks Section */}
          <div>
            <div className="flex items-center justify-between mb-2">
              <div>
                <h3 className="text-sm font-bold text-white">Daily Rewarded Tasks</h3>
                <p className="text-[11px] text-slate-400">Watch 10-sec sponsors & get paid</p>
              </div>
              <button
                onClick={() => setActiveTab("plans")}
                className="text-xs text-orange-400 font-bold hover:underline"
              >
                Upgrade Plan
              </button>
            </div>

            <div className="space-y-2">
              {INITIAL_ADS.map((ad) => {
                const limitReached = todayAdsWatched >= dailyLimit;
                return (
                  <div
                    key={ad.id}
                    onClick={() => !limitReached && setCurrentVideoAd(ad)}
                    className={`p-3.5 rounded-2xl border transition flex items-center justify-between ${
                      limitReached
                        ? "bg-slate-900/50 border-slate-800 opacity-60 cursor-not-allowed"
                        : "bg-slate-800/90 border-slate-700 hover:border-orange-500/50 cursor-pointer active:scale-[0.99]"
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-xl bg-orange-500/10 border border-orange-500/20 flex items-center justify-center">
                        <PlayCircle className="w-6 h-6 text-orange-500" />
                      </div>
                      <div>
                        <h4 className="text-xs font-bold text-white line-clamp-1">{ad.title}</h4>
                        <span className="text-[11px] text-slate-400">{ad.sponsor} • {ad.duration}s</span>
                      </div>
                    </div>
                    <span className="text-xs font-bold px-2.5 py-1 rounded-xl bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                      {limitReached ? "Done" : `+$${ad.reward.toFixed(2)}`}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Recent Transactions */}
          <div>
            <h3 className="text-sm font-bold text-white mb-2">Recent Transactions</h3>
            <div className="space-y-2">
              {transactions.slice(0, 4).map((tx) => (
                <div key={tx.id} className="p-3 rounded-2xl bg-slate-800/50 border border-slate-700/60 flex items-center justify-between">
                  <div className="flex items-center gap-2.5">
                    <div className={`p-2 rounded-xl ${tx.isCredit ? "bg-emerald-500/10 text-emerald-400" : "bg-red-500/10 text-red-400"}`}>
                      {tx.isCredit ? <ArrowDownLeft className="w-4 h-4" /> : <ArrowUp className="w-4 h-4" />}
                    </div>
                    <div>
                      <div className="text-xs font-bold text-white line-clamp-1">{tx.title}</div>
                      <div className="text-[10px] text-slate-400">{tx.method} • {tx.time}</div>
                    </div>
                  </div>
                  <div className={`text-xs font-bold ${tx.isCredit ? "text-emerald-400" : "text-red-400"}`}>
                    {tx.isCredit ? "+" : "-"}${tx.amount.toFixed(2)}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* Plans Tab */}
      {activeTab === "plans" && (
        <div className="space-y-4">
          <div>
            <h2 className="text-lg font-bold text-white">VIP Investment Packages</h2>
            <p className="text-xs text-slate-400">Unlock higher daily task limits & guaranteed ROI</p>
          </div>

          <div className="space-y-3">
            {INVESTMENT_PLANS.map((plan) => {
              const isActive = activePlanId === plan.planId;
              const canAfford = balance >= plan.depositPriceUsd;

              return (
                <div
                  key={plan.planId}
                  className="p-4 rounded-2xl bg-slate-800/90 border border-slate-700 shadow-md flex flex-col justify-between"
                >
                  <div className="flex items-center justify-between mb-3">
                    <div>
                      <h4 className="text-sm font-bold text-white">{plan.name}</h4>
                      <p className="text-[11px] text-slate-400">Contract: {plan.validityDays} Days</p>
                    </div>
                    <span
                      className="px-2.5 py-1 rounded-xl text-xs font-extrabold"
                      style={{ backgroundColor: `${plan.color}20`, color: plan.color, border: `1px solid ${plan.color}40` }}
                    >
                      {plan.badge}
                    </span>
                  </div>

                  <div className="grid grid-cols-3 gap-2 p-2.5 rounded-xl bg-slate-900 mb-3 text-center border border-slate-800">
                    <div>
                      <div className="text-[10px] text-slate-400">Price</div>
                      <div className="text-xs font-bold text-orange-400">${plan.depositPriceUsd}</div>
                    </div>
                    <div>
                      <div className="text-[10px] text-slate-400">Daily ROI</div>
                      <div className="text-xs font-bold text-emerald-400">${plan.dailyEarningUsd.toFixed(2)}</div>
                    </div>
                    <div>
                      <div className="text-[10px] text-slate-400">Ads Limit</div>
                      <div className="text-xs font-bold text-white">{plan.dailyAdsLimit}/day</div>
                    </div>
                  </div>

                  {isActive ? (
                    <div className="w-full py-2.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 font-bold text-xs flex items-center justify-center gap-1.5">
                      <CheckCircle className="w-4 h-4" /> Currently Active Plan
                    </div>
                  ) : (
                    <button
                      onClick={() => handleUpgradePlan(plan)}
                      className={`w-full py-2.5 rounded-xl font-bold text-xs transition ${
                        canAfford
                          ? "bg-orange-500 hover:bg-orange-600 text-white shadow-md shadow-orange-500/20"
                          : "bg-slate-700 hover:bg-slate-600 text-slate-200"
                      }`}
                    >
                      {canAfford ? `Upgrade to ${plan.badge} ($${plan.depositPriceUsd})` : `Deposit $${plan.depositPriceUsd} to Activate`}
                    </button>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* Team Tab */}
      {activeTab === "team" && (
        <div className="space-y-4">
          <div>
            <h2 className="text-lg font-bold text-white">Affiliate & Team Network</h2>
            <p className="text-xs text-slate-400">Earn up to 3 levels of direct referral commissions</p>
          </div>

          {/* Referral Code Box */}
          <div className="p-4 rounded-2xl bg-slate-800/90 border border-slate-700">
            <span className="text-xs text-slate-400 font-semibold block mb-2">Your Unique Referral Code</span>
            <div className="flex items-center justify-between p-3 rounded-xl bg-slate-900 border border-slate-700">
              <span className="text-base font-extrabold text-orange-400 font-mono tracking-wider">{referralCode}</span>
              <button
                onClick={() => {
                  navigator.clipboard.writeText(referralCode);
                  setCopiedCode(true);
                  setTimeout(() => setCopiedCode(false), 2000);
                }}
                className="p-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-300"
              >
                {copiedCode ? <Check className="w-4 h-4 text-emerald-400" /> : <Copy className="w-4 h-4" />}
              </button>
            </div>
          </div>

          {/* 3 Tier commission cards */}
          <div className="grid grid-cols-3 gap-2">
            <div className="p-3 rounded-2xl bg-slate-800/80 border border-slate-700 text-center">
              <div className="text-xs font-bold text-orange-400">Level 1 (10%)</div>
              <div className="text-[11px] text-slate-400 mt-1">8 Members</div>
              <div className="text-xs font-bold text-emerald-400 mt-0.5">+$16.40</div>
            </div>
            <div className="p-3 rounded-2xl bg-slate-800/80 border border-slate-700 text-center">
              <div className="text-xs font-bold text-amber-400">Level 2 (5%)</div>
              <div className="text-[11px] text-slate-400 mt-1">14 Members</div>
              <div className="text-xs font-bold text-emerald-400 mt-0.5">+$11.20</div>
            </div>
            <div className="p-3 rounded-2xl bg-slate-800/80 border border-slate-700 text-center">
              <div className="text-xs font-bold text-blue-400">Level 3 (2%)</div>
              <div className="text-[11px] text-slate-400 mt-1">29 Members</div>
              <div className="text-xs font-bold text-emerald-400 mt-0.5">+$8.70</div>
            </div>
          </div>

          {/* Team Members List */}
          <div>
            <h3 className="text-sm font-bold text-white mb-2">Direct Affiliates</h3>
            <div className="space-y-2">
              {INITIAL_TEAM_MEMBERS.map((m, i) => (
                <div key={i} className="p-3 rounded-2xl bg-slate-800/50 border border-slate-700/60 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-orange-500/10 flex items-center justify-center text-orange-400 font-bold text-xs">
                      {m.username[0].toUpperCase()}
                    </div>
                    <div>
                      <div className="text-xs font-bold text-white">{m.username}</div>
                      <div className="text-[10px] text-slate-400">Joined: {m.date} • Level {m.level}</div>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-xs font-bold text-emerald-400">+${m.contributed.toFixed(2)}</div>
                    <div className="text-[10px] text-slate-400">{m.adsToday} ads today</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* Account Tab */}
      {activeTab === "account" && (
        <div className="space-y-4">
          <div>
            <h2 className="text-lg font-bold text-white">Profile & Account Settings</h2>
            <p className="text-xs text-slate-400">Credentials, system channels & wallet details</p>
          </div>

          <div className="p-4 rounded-2xl bg-slate-800/90 border border-slate-700 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-full bg-gradient-to-tr from-orange-500 to-amber-500 flex items-center justify-center text-white font-black text-lg">
                {user ? user.name[0].toUpperCase() : "G"}
              </div>
              <div>
                <h4 className="text-sm font-bold text-white">{user ? user.name : "Guest Investor"}</h4>
                <p className="text-xs text-slate-400">{user ? user.identifier : "Not logged in"}</p>
                <span className="inline-block mt-1 px-2 py-0.5 rounded-md bg-orange-500/20 text-orange-400 text-[10px] font-bold">
                  {vipTier}
                </span>
              </div>
            </div>

            {!user ? (
              <button
                onClick={() => setIsAuthOpen(true)}
                className="px-3.5 py-1.5 rounded-xl bg-orange-500 text-white font-bold text-xs"
              >
                Sign In
              </button>
            ) : (
              <button
                onClick={() => {
                  setUser(null);
                  showToast("Logged out successfully");
                }}
                className="px-3 py-1.5 rounded-xl border border-red-500/40 text-red-400 hover:bg-red-500/10 font-bold text-xs"
              >
                Logout
              </button>
            )}
          </div>

          <div className="p-4 rounded-2xl bg-slate-800/90 border border-slate-700 space-y-3">
            <h3 className="text-xs font-bold text-slate-300 uppercase tracking-wider">System Credentials</h3>
            <div className="flex justify-between text-xs py-1 border-b border-slate-700/60">
              <span className="text-slate-400">Account ID</span>
              <span className="font-mono text-white">PK-8842-9910</span>
            </div>
            <div className="flex justify-between text-xs py-1 border-b border-slate-700/60">
              <span className="text-slate-400">Signal Channel</span>
              <span className="font-semibold text-orange-400">CH-07 VIP</span>
            </div>
            <div className="flex justify-between text-xs py-1">
              <span className="text-slate-400">Total Lifetime Withdrawn</span>
              <span className="font-bold text-emerald-400">${totalWithdrawn.toFixed(2)} USD</span>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-2">
            <button
              onClick={() => setIsDepositOpen(true)}
              className="py-3 rounded-xl bg-orange-500 hover:bg-orange-600 text-white font-bold text-xs shadow-md shadow-orange-500/20"
            >
              Deposit Funds
            </button>
            <button
              onClick={() => setIsWithdrawOpen(true)}
              className="py-3 rounded-xl bg-slate-800 hover:bg-slate-700 text-white font-bold text-xs border border-slate-700"
            >
              Withdraw
            </button>
          </div>
        </div>
      )}

      {/* Bottom Sticky Navigation */}
      <nav className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-md bg-[#0f172a]/95 backdrop-blur-md border-t border-slate-800 p-2 z-40">
        <div className="grid grid-cols-4 gap-1">
          <button
            onClick={() => setActiveTab("home")}
            className={`flex flex-col items-center justify-center py-1.5 rounded-xl transition ${
              activeTab === "home" ? "text-orange-500" : "text-slate-400 hover:text-slate-200"
            }`}
          >
            <Home className="w-5 h-5 mb-0.5" />
            <span className="text-[10px] font-bold">Home</span>
          </button>
          <button
            onClick={() => setActiveTab("plans")}
            className={`flex flex-col items-center justify-center py-1.5 rounded-xl transition ${
              activeTab === "plans" ? "text-orange-500" : "text-slate-400 hover:text-slate-200"
            }`}
          >
            <Award className="w-5 h-5 mb-0.5" />
            <span className="text-[10px] font-bold">VIP Plans</span>
          </button>
          <button
            onClick={() => setActiveTab("team")}
            className={`flex flex-col items-center justify-center py-1.5 rounded-xl transition ${
              activeTab === "team" ? "text-orange-500" : "text-slate-400 hover:text-slate-200"
            }`}
          >
            <Users className="w-5 h-5 mb-0.5" />
            <span className="text-[10px] font-bold">Team</span>
          </button>
          <button
            onClick={() => setActiveTab("account")}
            className={`flex flex-col items-center justify-center py-1.5 rounded-xl transition ${
              activeTab === "account" ? "text-orange-500" : "text-slate-400 hover:text-slate-200"
            }`}
          >
            <User className="w-5 h-5 mb-0.5" />
            <span className="text-[10px] font-bold">Account</span>
          </button>
        </div>
      </nav>

      {/* Modals */}
      <VideoModal
        ad={currentVideoAd}
        onClose={() => setCurrentVideoAd(null)}
        onClaim={handleClaimAdReward}
      />

      <DepositModal
        isOpen={isDepositOpen}
        onClose={() => setIsDepositOpen(false)}
        onSubmit={handleDepositSubmit}
      />

      <WithdrawModal
        isOpen={isWithdrawOpen}
        balance={balance}
        onClose={() => setIsWithdrawOpen(false)}
        onSubmit={handleWithdrawSubmit}
      />

      <AuthModal
        isOpen={isAuthOpen}
        onClose={() => setIsAuthOpen(false)}
        onSuccess={(name, id) => {
          setUser({ name, identifier: id });
          setBalance((prev) => parseFloat((prev + 10.0).toFixed(2)));
          setTotalEarned((prev) => parseFloat((prev + 10.0).toFixed(2)));
          showToast(`Welcome ${name}! $10 Signup Bonus Credited.`);
        }}
      />
    </div>
  );
                      }
