import { useMemo } from "react";
import { createFileRoute, Link } from "@tanstack/react-router";
import { ArrowLeft, ArrowDownRight, ArrowUpRight, Target } from "lucide-react";

export const Route = createFileRoute("/goals/")({
  head: () => ({
    meta: [
      { title: "Meta — Patrimônio+" },
      { name: "description", content: "Movimentações, evolução e progresso desta meta." },
    ],
  }),
  component: GoalDetailPage,
});

const GOALS: Record<string, { name: string; emoji: string; target: number; color: string }> = {
  casa: { name: "Casa", emoji: "🏠", target: 80000, color: "#1D4ED8" },
  japao: { name: "Japão", emoji: "🗾", target: 25000, color: "#22C55E" },
  aliancas: { name: "Alianças", emoji: "💍", target: 12000, color: "#F59E0B" },
  reserva: { name: "Reserva", emoji: "🛟", target: 20000, color: "#06B6D4" },
};

type Tx = { id: number; date: Date; value: number; type: "in" | "out" };

function mulberry32(seed: number) {
  return function () {
    let t = (seed += 0x6d2b79f5);
    t = Math.imul(t ^ (t >>> 15), t | 1);
    t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

function hashSlug(s: string) {
  let h = 0;
  for (let i = 0; i < s.length; i++) h = (h * 31 + s.charCodeAt(i)) | 0;
  return Math.abs(h) || 1;
}

function genTransactions(slug: string, target: number): Tx[] {
  const rand = mulberry32(hashSlug(slug));
  const now = new Date();
  const txs: Tx[] = [];
  for (let i = 0; i < 20; i++) {
    const daysAgo = Math.floor(rand() * 300) + i * 5;
    const date = new Date(now);
    date.setDate(now.getDate() - daysAgo);
    const isOut = rand() < 0.25;
    const base = target / 25;
    const value = Math.round((base * (0.3 + rand() * 1.4)) / 10) * 10;
    txs.push({ id: i, date, value, type: isOut ? "out" : "in" });
  }
  return txs.sort((a, b) => a.date.getTime() - b.date.getTime());
}

function fmt(n: number) {
  return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function fmtDate(d: Date) {
  return d.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "2-digit" });
}

function EvolutionChart({ data, color, target }: { data: { x: number; y: number }[]; color: string; target: number }) {
  const w = 760, h = 180, pad = 8;
  const max = Math.max(target, ...data.map((d) => d.y));
  const step = data.length > 1 ? (w - pad * 2) / (data.length - 1) : 0;
  const norm = (v: number) => h - pad - (v / max) * (h - pad * 2);
  const d = data
    .map((p, i) => `${i === 0 ? "M" : "L"}${(pad + i * step).toFixed(1)},${norm(p.y).toFixed(1)}`)
    .join(" ");
  const area = `${d} L${pad + (data.length - 1) * step},${h - pad} L${pad},${h - pad} Z`;
  const targetY = norm(target);
  return (
    <svg viewBox={`0 0 ${w} ${h}`} className="w-full h-44">
      <defs>
        <linearGradient id="goalArea" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor={color} stopOpacity="0.35" />
          <stop offset="100%" stopColor={color} stopOpacity="0" />
        </linearGradient>
      </defs>
      <line x1={pad} x2={w - pad} y1={targetY} y2={targetY} stroke={color} strokeOpacity="0.4" strokeDasharray="4 4" />
      <path d={area} fill="url(#goalArea)" />
      <path d={d} fill="none" stroke={color} strokeWidth={3} strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function GoalDetailPage() {
  const { slug } = Route.useParams();
  const goal = GOALS[slug] || { name: slug, emoji: "🎯", target: 10000, color: "#1D4ED8" };
  const txs = useMemo(() => genTransactions(slug, goal.target), [slug, goal.target]);

  const totals = useMemo(() => {
    let added = 0, removed = 0;
    const series: { x: number; y: number }[] = [];
    let running = 0;
    txs.forEach((t, i) => {
      if (t.type === "in") { added += t.value; running += t.value; }
      else { removed += t.value; running -= t.value; }
      series.push({ x: i, y: Math.max(0, running) });
    });
    return { added, removed, balance: added - removed, series };
  }, [txs]);

  const pct = Math.min(100, Math.round((totals.balance / goal.target) * 100));

  return (
    <div className="min-h-screen bg-background pb-16">
      <div className="mx-auto w-full max-w-[800px] px-5">
        <header className="pt-12 pb-4 flex items-center gap-3">
          <Link to="/dashboard" className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card">
            <ArrowLeft className="w-4 h-4 text-foreground" />
          </Link>
          <div className="flex-1">
            <p className="text-[15px] font-bold text-foreground flex items-center gap-2">
              <span className="text-xl">{goal.emoji}</span> Meta {goal.name}
            </p>
            <p className="text-xs text-muted-foreground">Acompanhe todas as movimentações desta caixa.</p>
          </div>
        </header>

        {/* Resumo */}
        <section>
          <div className="rounded-3xl p-5 shadow-elevated text-white relative overflow-hidden" style={{ background: `linear-gradient(135deg, ${goal.color}, #22C55E)` }}>
            <div className="flex items-start justify-between">
              <div>
                <p className="text-[11px] uppercase tracking-wider text-white/80 font-semibold">Saldo atual</p>
                <p className="text-3xl font-extrabold mt-1">{fmt(totals.balance)}</p>
                <p className="text-xs text-white/80 mt-1">Meta: {fmt(goal.target)}</p>
              </div>
              <div className="w-12 h-12 rounded-2xl bg-white/15 grid place-items-center">
                <Target className="w-5 h-5" />
              </div>
            </div>
            <div className="mt-4 h-2 rounded-full bg-white/20 overflow-hidden">
              <div className="h-full bg-white" style={{ width: `${pct}%` }} />
            </div>
            <p className="text-[11px] mt-1 text-white/80">{pct}% concluído</p>
          </div>
        </section>

        {/* Totais */}
        <section className="mt-4 grid grid-cols-2 gap-3">
          <div className="bg-card rounded-2xl p-4 shadow-card">
            <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 grid place-items-center">
              <ArrowUpRight className="w-4 h-4" />
            </div>
            <p className="mt-3 text-[11px] text-muted-foreground font-medium">Total adicionado</p>
            <p className="text-[17px] font-extrabold text-foreground mt-0.5">{fmt(totals.added)}</p>
          </div>
          <div className="bg-card rounded-2xl p-4 shadow-card">
            <div className="w-9 h-9 rounded-xl bg-rose-50 text-rose-600 grid place-items-center">
              <ArrowDownRight className="w-4 h-4" />
            </div>
            <p className="mt-3 text-[11px] text-muted-foreground font-medium">Total retirado</p>
            <p className="text-[17px] font-extrabold text-foreground mt-0.5">{fmt(totals.removed)}</p>
          </div>
        </section>

        {/* Gráfico */}
        <section className="mt-5">
          <div className="bg-card rounded-3xl p-5 shadow-card">
            <p className="text-sm font-bold text-foreground">Evolução da meta</p>
            <p className="text-[11px] text-muted-foreground">Do início até hoje</p>
            <EvolutionChart data={totals.series} color={goal.color} target={goal.target} />
            <div className="flex items-center justify-between text-[11px] text-muted-foreground">
              <span>Início</span>
              <span>Linha tracejada = meta</span>
              <span>Hoje</span>
            </div>
          </div>
        </section>

        {/* Movimentações */}
        <section className="mt-5">
          <p className="text-base font-bold text-foreground mb-3">Movimentações</p>
          <ul className="space-y-2">
            {[...txs].reverse().map((t) => {
              const isIn = t.type === "in";
              return (
                <li
                  key={t.id}
                  className={`flex items-center gap-3 rounded-2xl p-3 shadow-card ${
                    isIn ? "bg-emerald-50 dark:bg-emerald-500/10" : "bg-rose-50 dark:bg-rose-500/10"
                  }`}
                >
                  <div className={`w-10 h-10 rounded-xl grid place-items-center ${
                    isIn ? "bg-emerald-500/15 text-emerald-600" : "bg-rose-500/15 text-rose-600"
                  }`}>
                    {isIn ? <ArrowUpRight className="w-4 h-4" /> : <ArrowDownRight className="w-4 h-4" />}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-semibold text-foreground">{isIn ? "Adicionado" : "Retirado"}</p>
                    <p className="text-[11px] text-muted-foreground">{fmtDate(t.date)}</p>
                  </div>
                  <span className={`text-sm font-extrabold ${isIn ? "text-emerald-600" : "text-rose-600"}`}>
                    {isIn ? "+" : "−"} {fmt(t.value)}
                  </span>
                </li>
              );
            })}
          </ul>
        </section>
      </div>
    </div>
  );
}
