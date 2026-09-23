export interface InvestmentPlan {
  planId: number;
  name: string;
  badge: string;
  depositPriceUsd: number;
  dailyAdsLimit: number;
  dailyEarningUsd: number;
  monthlyRoiPercent: number;
  validityDays: number;
  color: string;
}

export const INVESTMENT_PLANS: InvestmentPlan[] = [
  { planId: 1, name: "Plan 1 - Bronze Starter", badge: "VIP 1", depositPriceUsd: 20, dailyAdsLimit: 5, dailyEarningUsd: 1.00, monthlyRoiPercent: 150, validityDays: 60, color: "#CD7F32" },
  { planId: 2, name: "Plan 2 - Silver Classic", badge: "VIP 2", depositPriceUsd: 50, dailyAdsLimit: 8, dailyEarningUsd: 2.50, monthlyRoiPercent: 150, validityDays: 60, color: "#9E9E9E" },
  { planId: 3, name: "Plan 3 - Gold Advance", badge: "VIP 3", depositPriceUsd: 100, dailyAdsLimit: 12, dailyEarningUsd: 5.00, monthlyRoiPercent: 150, validityDays: 60, color: "#FFB300" },
  { planId: 4, name: "Plan 4 - Platinum Pro", badge: "VIP 4", depositPriceUsd: 200, dailyAdsLimit: 18, dailyEarningUsd: 11.00, monthlyRoiPercent: 165, validityDays: 60, color: "#42A5F5" },
  { planId: 5, name: "Plan 5 - Diamond Executive", badge: "VIP 5", depositPriceUsd: 500, dailyAdsLimit: 25, dailyEarningUsd: 30.00, monthlyRoiPercent: 180, validityDays: 60, color: "#26C6DA" },
  { planId: 6, name: "Plan 6 - Crown Royal", badge: "VIP 6", depositPriceUsd: 1000, dailyAdsLimit: 35, dailyEarningUsd: 65.00, monthlyRoiPercent: 195, validityDays: 60, color: "#AB47BC" },
  { planId: 7, name: "Plan 7 - Ruby Master", badge: "VIP 7", depositPriceUsd: 2000, dailyAdsLimit: 50, dailyEarningUsd: 140.00, monthlyRoiPercent: 210, validityDays: 60, color: "#E91E63" },
  { planId: 8, name: "Plan 8 - Sapphire Prestige", badge: "VIP 8", depositPriceUsd: 3500, dailyAdsLimit: 70, dailyEarningUsd: 260.00, monthlyRoiPercent: 225, validityDays: 60, color: "#3F51B5" },
  { planId: 9, name: "Plan 9 - Emerald Elite", badge: "VIP 9", depositPriceUsd: 5000, dailyAdsLimit: 90, dailyEarningUsd: 400.00, monthlyRoiPercent: 240, validityDays: 60, color: "#00897B" },
  { planId: 10, name: "Plan 10 - Imperial Titan", badge: "VIP 10", depositPriceUsd: 10000, dailyAdsLimit: 120, dailyEarningUsd: 850.00, monthlyRoiPercent: 255, validityDays: 60, color: "#FF6D00" },
];

export const INITIAL_ADS = [
  { id: "ad_1", title: "Crypto Arbitrage Trading AI", sponsor: "Binance Global", reward: 0.05, duration: 10, category: "Crypto Trading" },
  { id: "ad_2", title: "Automated Forex Copy Trading", sponsor: "OctaTrade", reward: 0.05, duration: 10, category: "Forex Strategy" },
  { id: "ad_3", title: "NextGen Solar Energy Hub", sponsor: "EcoWatt Corp", reward: 0.05, duration: 10, category: "Clean Energy" },
  { id: "ad_4", title: "E-Commerce Logistics Suite", sponsor: "DropFast Global", reward: 0.05, duration: 10, category: "Supply Chain" },
  { id: "ad_5", title: "AI Social Marketing Automation", sponsor: "AgentScale AI", reward: 0.05, duration: 10, category: "AI Tools" },
];

export const INITIAL_TEAM_MEMBERS = [
  { username: "farhan_investor", level: 1, date: "20 Sep 2026", contributed: 12.80, adsToday: 10 },
  { username: "ali_crypto99", level: 1, date: "18 Sep 2026", contributed: 24.50, adsToday: 8 },
  { username: "hamza_trader", level: 2, date: "15 Sep 2026", contributed: 6.40, adsToday: 5 },
  { username: "usman_pro", level: 2, date: "12 Sep 2026", contributed: 18.00, adsToday: 9 },
  { username: "sana_tech", level: 3, date: "10 Sep 2026", contributed: 4.20, adsToday: 6 },
];
