import { useState } from "react";
import { Link } from "@tanstack/react-router";
import {
  Bell, ArrowUpRight, TrendingUp, Heart, Wallet, PiggyBank,
  Home as HomeIcon, Receipt, Target, BarChart3, User, Plus,
  CreditCard, LineChart, Building2, FileBarChart, CalendarDays, Bot, Settings,
  ArrowDownRight, Sparkles, Trophy
} from "lucide-react";
import { AddTransactionModal } from "./AddTransactionModal";
import { ThemeToggle } from "./ThemeToggle";

const goals = [
  { slug: "casa", name: "Casa", pct: 72, emoji: "🏠", color: "#1D4ED8" },
  { slug: "japao", name: "Japão", pct: 34, emoji: "🗾", color: "#22C55E" },
  { slug: "aliancas", name: "Alianças", pct: 58, emoji: "💍", color: "#F59E0B" },
  { slug: "reserva", name: "Reserva", pct: 96, emoji: "🛟", color: "#06B6D4" },
];

const tools: { label: string; icon: string; bg: string; fg: string; to?: string }[] = [
  { label: "Receitas", icon: "💰", bg: "bg-emerald-50", fg: "text-emerald-600" },
  { label: "Despesas", icon: "💸", bg: "bg-rose-50", fg: "text-rose-600" },
  { label: "Cartões", icon: "💳", bg: "bg-indigo-50", fg: "text-indigo-600" },
  { label: "Investimentos", icon: "📈", bg: "bg-blue-50", fg: "text-blue-600", to: "/investments" },
  { label: "Metas", icon: "🎯", bg: "bg-amber-50", fg: "text-amber-600" },
  { label: "Patrimônio", icon: "🏦", bg: "bg-violet-50", fg: "text-violet-600" },
  { label: "Relatórios", icon: "📊", bg: "bg-cyan-50", fg: "text-cyan-600" },
  { label: "Planejamento", icon: "📅", bg: "bg-pink-50", fg: "text-pink-600" },
  { label: "Assistente IA", icon: "🤖", bg: "bg-fuchsia-50", fg: "text-fuchsia-600" },
  { label: "Configurações", icon: "⚙️", bg: "bg-slate-100", fg: "text-slate-600" },
];

const sparkPoints = [12, 18, 15, 22, 19, 26, 24, 30, 28, 34, 32, 38];

function Sparkline({ points, stroke = "#fff", fill = "rgba(255,255,255,0.2)" }: { points: number[]; stroke?: string; fill?: string }) {
  const w = 260, h = 64;
  const max = Math.max(...points), min = Math.min(...points);
  const norm = (v: number) => h - ((v - min) / (max - min || 1)) * (h - 8) - 4;
  const step = w / (points.length - 1);
  const d = points.map((p, i) => `${i === 0 ? "M" : "L"}${(i * step).toFixed(1)},${norm(p).toFixed(1)}`).join(" ");
  const area = `${d} L${w},${h} L0,${h} Z`;
  return (
    <svg viewBox={`0 0 ${w} ${h}`} className="w-full h-16">
      <path d={area} fill={fill} />
      <path d={d} fill="none" stroke={stroke} strokeWidth={2.5} strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function ProjectionChart() {
  const data = [36, 58, 89, 140, 205, 380, 600, 890];
  const w = 320, h = 130;
  const max = Math.max(...data);
  const step = w / (data.length - 1);
  const norm = (v: number) => h - (v / max) * (h - 16) - 8;
  const d = data.map((p, i) => `${i === 0 ? "M" : "L"}${(i * step).toFixed(1)},${norm(p).toFixed(1)}`).join(" ");
  return (
    <svg viewBox={`0 0 ${w} ${h}`} className="w-full h-32">
      <defs>
        <linearGradient id="proj" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#22C55E" stopOpacity="0.35" />
          <stop offset="100%" stopColor="#22C55E" stopOpacity="0" />
        </linearGradient>
      </defs>
      <path d={`${d} L${w},${h} L0,${h} Z`} fill="url(#proj)" />
      <path d={d} fill="none" stroke="#22C55E" strokeWidth={3} strokeLinecap="round" />
      {data.map((p, i) => (
        <circle key={i} cx={i * step} cy={norm(p)} r={i === data.length - 1 ? 5 : 0} fill="#22C55E" stroke="#fff" strokeWidth={2} />
      ))}
    </svg>
  );
}

function ScoreRing({ score }: { score: number }) {
  const r = 32, c = 2 * Math.PI * r;
  const off = c - (score / 100) * c;
  return (
    <div className="relative w-20 h-20">
      <svg viewBox="0 0 80 80" className="w-20 h-20 -rotate-90">
        <circle cx="40" cy="40" r={r} stroke="hsl(var(--border))" strokeOpacity="0.25" strokeWidth="7" fill="none" stroke-width="7" style={{ stroke: "#E5E7EB" }} />
        <circle cx="40" cy="40" r={r} stroke="#22C55E" strokeWidth="7" strokeLinecap="round" fill="none" strokeDasharray={c} strokeDashoffset={off} />
      </svg>
      <div className="absolute inset-0 flex flex-col items-center justify-center">
        <span className="text-xl font-extrabold text-foreground leading-none">{score}</span>
        <span className="text-[9px] text-muted-foreground">/ 100</span>
      </div>
    </div>
  );
}

export function Dashboard() {
  const [openAdd, setOpenAdd] = useState(false);

  return (
    <div className="min-h-screen bg-background pb-28">
      <div className="mx-auto w-full max-w-[800px]">
      {/* Header */}
      <header className="px-5 pt-12 pb-4 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-11 h-11 rounded-full bg-gradient-wealth grid place-items-center text-white font-bold ring-2 ring-white shadow-card">
            L
          </div>
          <div>
            <p className="text-[15px] font-semibold text-foreground">Olá, Luan <span>👋</span></p>
            <p className="text-xs text-muted-foreground">Seu patrimônio está crescendo.</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <ThemeToggle />
          <button className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card relative">
            <Bell className="w-4.5 h-4.5 text-foreground" />
            <span className="absolute top-2.5 right-2.5 w-2 h-2 rounded-full bg-success" />
          </button>
        </div>
      </header>

      {/* Patrimônio + Saúde (desktop: 60/40 lado a lado) */}
      <div className="px-5 md:flex md:gap-4 md:items-stretch">
      <section className="md:basis-[60%] md:shrink-0">
        <div className="relative overflow-hidden rounded-3xl bg-gradient-wealth text-white p-5 shadow-elevated">
          <div className="absolute -top-16 -right-12 w-52 h-52 rounded-full bg-white/10 blur-2xl" />
          <div className="absolute -bottom-20 -left-10 w-56 h-56 rounded-full bg-[#22C55E]/30 blur-3xl" />
          <div className="relative flex items-start justify-between">
            <div>
              <p className="text-xs uppercase tracking-wider text-white/70 font-medium">Patrimônio Total</p>
              <p className="text-[34px] font-extrabold leading-tight mt-1 tracking-tight">R$ 36.209<span className="text-xl text-white/70">,79</span></p>
              <div className="mt-2 inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-[#22C55E]/25 text-[#86EFAC] text-xs font-semibold">
                <ArrowUpRight className="w-3.5 h-3.5" /> +12,4% este ano
              </div>
            </div>
            <button className="w-9 h-9 rounded-full bg-white/15 backdrop-blur grid place-items-center">
              <TrendingUp className="w-4 h-4" />
            </button>
          </div>
          <div className="relative mt-3 -mx-1">
            <Sparkline points={sparkPoints} />
          </div>
        </div>
      </section>

      {/* Saúde Financeira */}
      <section className="mt-5 md:mt-0 md:basis-[40%] md:shrink-0">
        <div className="bg-card rounded-3xl p-5 shadow-card flex items-center gap-4 h-full">
          <ScoreRing score={82} />
          <div className="flex-1 min-w-0">
            <div className="flex items-center gap-2">
              <Heart className="w-4 h-4 text-success" />
              <p className="text-sm font-bold text-foreground">Saúde Financeira</p>
              <Link
                to="/ranking"
                className="ml-auto inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-primary/10 text-primary text-[11px] font-semibold active:scale-95 transition"
              >
                <Trophy className="w-3 h-3" /> Ranking
              </Link>
            </div>
            <ul className="mt-2 space-y-1 text-[12px] text-muted-foreground">
              <li className="flex justify-between"><span>Reserva de emergência</span><span className="text-success font-semibold">Excelente</span></li>
              <li className="flex justify-between"><span>Dívidas</span><span className="text-foreground font-semibold">Controladas</span></li>
              <li className="flex justify-between"><span>Investimentos</span><span className="text-primary font-semibold">Crescendo</span></li>
            </ul>
          </div>
        </div>
      </section>
      </div>

      {/* Resumo grid 2x2 */}
      <section className="px-5 mt-5 grid grid-cols-2 md:grid-cols-4 gap-3">
        <SummaryCard label="Receitas do mês" value="R$ 3.230" icon={<ArrowUpRight className="w-4 h-4" />} tone="success" />
        <SummaryCard label="Despesas do mês" value="R$ 3.589" icon={<ArrowDownRight className="w-4 h-4" />} tone="danger" />
        <SummaryCard label="Investimentos" value="R$ 14.431" icon={<LineChart className="w-4 h-4" />} tone="primary" />
        <SummaryCard label="Reserva" value="R$ 4.815" icon={<PiggyBank className="w-4 h-4" />} tone="amber" />
      </section>

      {/* Metas Financeiras */}
      <section className="mt-7">
        <div className="px-5 flex items-center justify-between mb-3">
          <h3 className="text-base font-bold text-foreground">Metas Financeiras</h3>
          <button className="text-xs font-semibold text-primary">Ver todas</button>
        </div>
        <div className="flex md:grid md:grid-cols-4 gap-3 overflow-x-auto md:overflow-visible px-5 pb-2 scrollbar-none snap-x">
          {goals.map((g) => (
            <Link
              key={g.slug}
              to="/goals/$slug"
              params={{ slug: g.slug }}
              className="min-w-[160px] md:min-w-0 snap-start bg-card rounded-2xl p-4 shadow-card active:scale-[0.98] transition"
            >
              <div className="flex items-center justify-between">
                <span className="text-2xl">{g.emoji}</span>
                <span className="text-xs font-bold text-foreground">{g.pct}%</span>
              </div>
              <p className="mt-3 text-sm font-semibold text-foreground">Meta {g.name}</p>
              <div className="mt-2 h-1.5 w-full rounded-full bg-secondary overflow-hidden">
                <div className="h-full rounded-full" style={{ width: `${g.pct}%`, background: g.color }} />
              </div>
            </Link>
          ))}
        </div>
      </section>

      {/* Tools grid */}
      <section className="px-5 mt-6">
        <h3 className="text-base font-bold text-foreground mb-3">Ferramentas</h3>
        <div className="grid grid-cols-4 gap-3">
          {tools.map((t) => (
            t.to ? (
              <Link key={t.label} to={t.to} className="flex flex-col items-center gap-2">
                <div className={`w-14 h-14 rounded-2xl ${t.bg} grid place-items-center text-2xl shadow-card`}>
                  <span>{t.icon}</span>
                </div>
                <span className="text-[10.5px] font-medium text-foreground text-center leading-tight">{t.label}</span>
              </Link>
            ) : (
              <button key={t.label} className="flex flex-col items-center gap-2">
                <div className={`w-14 h-14 rounded-2xl ${t.bg} grid place-items-center text-2xl shadow-card`}>
                  <span>{t.icon}</span>
                </div>
                <span className="text-[10.5px] font-medium text-foreground text-center leading-tight">{t.label}</span>
              </button>
            )
          ))}
        </div>
      </section>

      {/* Projection */}
      <section className="px-5 mt-6">
        <div className="rounded-3xl bg-card p-5 shadow-card">
          <div className="flex items-center justify-between">
            <div>
              <div className="inline-flex items-center gap-1.5 text-[10px] uppercase tracking-wider text-success font-bold">
                <Sparkles className="w-3 h-3" /> Projeção Patrimonial
              </div>
              <p className="text-lg font-bold text-foreground mt-1">Seu futuro em números</p>
            </div>
          </div>
          <ProjectionChart />
          <div className="grid grid-cols-4 gap-2 mt-2">
            {[
              { l: "Hoje", v: "36k" },
              { l: "5 anos", v: "89k" },
              { l: "10 anos", v: "205k" },
              { l: "20 anos", v: "890k" },
            ].map((p) => (
              <div key={p.l} className="text-center">
                <p className="text-[10px] text-muted-foreground">{p.l}</p>
                <p className="text-sm font-bold text-foreground">R$ {p.v}</p>
              </div>
            ))}
          </div>
          <Link
            to="/simulator"
            className="mt-4 w-full h-12 rounded-2xl bg-foreground text-background font-semibold text-sm active:scale-[0.99] grid place-items-center"
          >
            Simular Cenários
          </Link>
        </div>
      </section>
      </div>

      {/* Bottom nav */}
      <nav className="fixed bottom-0 left-0 right-0 z-30">
        <div className="mx-auto max-w-[480px] px-4 pb-4">
          <div className="relative bg-card rounded-3xl shadow-elevated h-16 flex items-center justify-between px-6">
            <NavItem icon={<HomeIcon className="w-5 h-5" />} label="Início" active />
            <NavItem icon={<Receipt className="w-5 h-5" />} label="Transações" />
            <div className="w-14" />
            <NavItem icon={<LineChart className="w-5 h-5" />} label="Invest." to="/investments" />
            <NavItem icon={<User className="w-5 h-5" />} label="Perfil" to="/profile" />
            <button
              onClick={() => setOpenAdd(true)}
              className="absolute left-1/2 -translate-x-1/2 -top-6 w-14 h-14 rounded-full bg-gradient-primary text-white grid place-items-center shadow-fab active:scale-95 transition"
              aria-label="Adicionar transação"
            >
              <Plus className="w-6 h-6" strokeWidth={2.6} />
            </button>
          </div>
        </div>
      </nav>

      <AddTransactionModal open={openAdd} onClose={() => setOpenAdd(false)} />
    </div>
  );
}

function SummaryCard({ label, value, icon, tone }: { label: string; value: string; icon: React.ReactNode; tone: "success" | "danger" | "primary" | "amber" }) {
  const tones: Record<string, string> = {
    success: "bg-emerald-50 text-emerald-600",
    danger: "bg-rose-50 text-rose-600",
    primary: "bg-blue-50 text-blue-600",
    amber: "bg-amber-50 text-amber-600",
  };
  return (
    <div className="bg-card rounded-2xl p-4 shadow-card">
      <div className={`w-9 h-9 rounded-xl grid place-items-center ${tones[tone]}`}>{icon}</div>
      <p className="mt-3 text-[11px] text-muted-foreground font-medium">{label}</p>
      <p className="text-[17px] font-extrabold text-foreground tracking-tight mt-0.5">{value}</p>
    </div>
  );
}

function NavItem({ icon, label, active, to }: { icon: React.ReactNode; label: string; active?: boolean; to?: string }) {
  const cls = `flex flex-col items-center gap-0.5 ${active ? "text-primary" : "text-muted-foreground"}`;
  if (to) {
    return (
      <Link to={to} className={cls}>
        {icon}
        <span className="text-[10px] font-medium">{label}</span>
      </Link>
    );
  }
  return (
    <button className={cls}>
      {icon}
      <span className="text-[10px] font-medium">{label}</span>
    </button>
  );
}