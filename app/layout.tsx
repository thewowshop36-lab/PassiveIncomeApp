import type { Metadata, Viewport } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Passive Income - Daily Rewarded Tasks & Investments",
  description: "Watch sponsored video ads, earn daily guaranteed ROI, deposit via JazzCash / EasyPaisa / USDT and withdraw instantly.",
};

export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  maximumScale: 1,
  themeColor: "#FF6D00",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-[#0b0f19] text-gray-100 min-h-screen flex justify-center">
        <main className="w-full max-w-md min-h-screen relative pb-24 shadow-2xl bg-[#0f172a] flex flex-col">
          {children}
        </main>
      </body>
    </html>
  );
}
