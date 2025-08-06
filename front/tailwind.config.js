/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'gray-fatec': '#3E3E3E',
      },
    },
  },
  plugins: [
    require('@tailwindcss/line-clamp'),
  ],
}