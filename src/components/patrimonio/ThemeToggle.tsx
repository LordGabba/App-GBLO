import { Moon, Sun } from "lucide-react";
import { useTheme } from "@/hooks/use-theme";

export function ThemeToggle() {
  const { theme, toggle } = useTheme();
  const isDark = theme === "dark";
  return (
    <button
      onClick={toggle}
      role="switch"
      aria-checked={isDark}
      aria-label="Alternar tema"
      className={`relative w-14 h-8 rounded-full transition-colors shadow-card ${
        isDark ? "bg-primary" : "bg-secondary"
      }`}
    >
      <span
        className={`absolute top-1 left-1 w-6 h-6 rounded-full bg-white grid place-items-center shadow-card transition-transform ${
          isDark ? "translate-x-6" : ""
        }`}
      >
        {isDark ? (
          <Moon className="w-3.5 h-3.5 text-primary" />
        ) : (
          <Sun className="w-3.5 h-3.5 text-amber-500" />
        )}
      </span>
    </button>
  );
}
