import { createFileRoute, Link } from "@tanstack/react-router";
import { ArrowLeft, Crown, Medal, Trophy } from "lucide-react";

export const Route = createFileRoute("/ranking")({
  head: () => ({
    meta: [
      { title: "Ranking — Patrimônio+" },
      { name: "description", content: "Veja o ranking mensal de saúde financeira entre você e seus amigos." },
    ],
  }),
  component: RankingPage,
});

const friends = [
  { name: "Luan (você)", score: 82, you: true, emoji: "🦁" },
  { name: "Marina", score: 94, emoji: "🌸" },
  { name: "Rafael", score: 88, emoji: "🚀" },
  { name: "Bianca", score: 76, emoji: "🌟" },
  { name: "Pedro", score: 71, emoji: "🎧" },
  { name: "Júlia", score: 65, emoji: "🍀" },
  { name: "Caio", score: 58, emoji: "⚡" },
];

function RankingPage() {
  const sorted = [...friends].sort((a, b) => b.score - a.score);
  const medalFor = (i: number) => {
    if (i === 0) return <Crown className="w-4 h-4 text-amber-500" />;
    if (i === 1) return <Medal className="w-4 h-4 text-slate-400" />;
    if (i === 2) return <Medal className="w-4 h-4 text-orange-500" />;
    return null;
  };

  return (
    <div className="min-h-screen bg-background pb-10">
      <header className="px-5 pt-12 pb-4 flex items-center gap-3">
        <Link to="/dashboard" className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card">
          <ArrowLeft className="w-4 h-4 text-foreground" />
        </Link>
        <div>
          <p className="text-[15px] font-bold text-foreground">Ranking mensal</p>
          <p className="text-xs text-muted-foreground">Saúde financeira entre amigos</p>
        </div>
      </header>

      <section className="px-5">
        <div className="rounded-3xl bg-gradient-wealth text-white p-5 shadow-elevated relative overflow-hidden">
          <div className="absolute -top-12 -right-10 w-40 h-40 rounded-full bg-white/10 blur-2xl" />
          <div className="relative flex items-center gap-3">
            <Trophy className="w-6 h-6" />
            <div>
              <p className="text-[11px] uppercase tracking-wider text-white/70 font-semibold">Junho 2026</p>
              <p className="text-lg font-bold">Você está em #{sorted.findIndex((f) => f.you) + 1}</p>
            </div>
          </div>
        </div>
      </section>

      <section className="px-5 mt-5 space-y-2">
        {sorted.map((f, i) => (
          <div
            key={f.name}
            className={`flex items-center gap-3 p-4 rounded-2xl shadow-card ${
              f.you ? "bg-primary/10 ring-1 ring-primary/30" : "bg-card"
            }`}
          >
            <span className="w-7 text-center text-sm font-extrabold text-muted-foreground">{i + 1}</span>
            <div className="w-10 h-10 rounded-full bg-secondary grid place-items-center text-lg">{f.emoji}</div>
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                <p className="text-sm font-semibold text-foreground truncate">{f.name}</p>
                {medalFor(i)}
              </div>
              <div className="mt-1.5 h-1.5 w-full rounded-full bg-secondary overflow-hidden">
                <div className="h-full rounded-full bg-success" style={{ width: `${f.score}%` }} />
              </div>
            </div>
            <span className="text-base font-extrabold text-foreground">{f.score}</span>
          </div>
        ))}
      </section>
    </div>
  );
}