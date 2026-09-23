/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          orange: "#FF6D00",
          orangeLight: "#FF9100",
          orangeDark: "#E65100",
          gold: "#FFB300",
          success: "#00C853",
          error: "#D50000",
          darkBg: "#0F172A",
          darkCard: "#1E293B",
          darkBorder: "#334155"
        }
      }
    },
  },
  plugins: [],
};
