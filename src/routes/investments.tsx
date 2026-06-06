import { createFileRoute, Link } from "@tanstack/react-router";
import { ArrowLeft, TrendingUp, LineChart, Wallet } from "lucide-react";

export const Route = createFileRoute("/investments")({
  head: () => ({
    meta: [
      { title: "Investimentos — Patrimônio+" },
      { name: "description", content: "Histórico completo dos seus aportes, juros e patrimônio acumulado." },
    ],
  }),
  component: InvestmentsPage,
});

type Row = {
  data: string;
  invMes: number;
  patrimonio: number;
  pct?: number;
  parc?: number;
  juros: number;
  totalInvestido: number;
  totalJuros: number;
  totalAcumulado: number;
};

const history2023: Row[] = [
  { data: "junho", invMes: 341.75, patrimonio: 2429.46, juros: 0, totalInvestido: 0, totalJuros: 0, totalAcumulado: 0 },
  { data: "julho", invMes: 220.0, patrimonio: 2647.46, juros: 0, totalInvestido: 0, totalJuros: 0, totalAcumulado: 0 },
  { data: "agosto", invMes: 340.0, patrimonio: 1510.33, juros: 0, totalInvestido: 0, totalJuros: 0, totalAcumulado: 0 },
  { data: "setembro", invMes: 360.0, patrimonio: 3147.46, juros: 0, totalInvestido: 0, totalJuros: 0, totalAcumulado: 0 },
  { data: "outubro", invMes: 259.04, patrimonio: 0, parc: 0, juros: 0, totalInvestido: 0, totalJuros: 0, totalAcumulado: 0 },
  { data: "novembro", invMes: 50.0, patrimonio: 3727.46, parc: 1, juros: 0, totalInvestido: 300, totalJuros: 0, totalAcumulado: 300 },
  { data: "dezembro", invMes: 300.0, patrimonio: 4527.46, parc: 2, juros: 3.07, totalInvestido: 600, totalJuros: 3.07, totalAcumulado: 603.07 },
];

const history2024: Row[] = [
  { data: "janeiro", invMes: 300, patrimonio: 235.89, pct: -1819.3, parc: 3, juros: 6.17, totalInvestido: 900, totalJuros: 9.24, totalAcumulado: 909.24 },
  { data: "fevereiro", invMes: 300, patrimonio: 722.87, pct: 67.37, parc: 4, juros: 9.31, totalInvestido: 1200, totalJuros: 18.55, totalAcumulado: 1218.55 },
  { data: "março", invMes: 300, patrimonio: 1169.10, pct: 38.17, parc: 5, juros: 12.47, totalInvestido: 1500, totalJuros: 31.03, totalAcumulado: 1531.03 },
  { data: "abril", invMes: 600, patrimonio: 1778.33, pct: 34.26, parc: 6, juros: 15.67, totalInvestido: 1800, totalJuros: 46.7, totalAcumulado: 1846.7 },
  { data: "maio", invMes: 300, patrimonio: 2143.06, pct: 17.02, parc: 7, juros: 18.9, totalInvestido: 2100, totalJuros: 65.6, totalAcumulado: 2165.6 },
  { data: "junho", invMes: 300, patrimonio: 2462.39, pct: 12.97, parc: 8, juros: 22.17, totalInvestido: 2400, totalJuros: 87.77, totalAcumulado: 2487.77 },
  { data: "julho", invMes: 1200, patrimonio: 3688.22, pct: 33.24, parc: 9, juros: 25.47, totalInvestido: 2700, totalJuros: 113.24, totalAcumulado: 2813.24 },
  { data: "agosto", invMes: 500, patrimonio: 4226.67, pct: 12.74, parc: 10, juros: 28.8, totalInvestido: 3000, totalJuros: 142.04, totalAcumulado: 3142.04 },
  { data: "setembro", invMes: 610, patrimonio: 4874.97, pct: 13.30, parc: 11, juros: 32.16, totalInvestido: 3300, totalJuros: 174.2, totalAcumulado: 3474.2 },
  { data: "outubro", invMes: 500, patrimonio: 5409.89, pct: 9.89, parc: 12, juros: 35.56, totalInvestido: 3600, totalJuros: 209.77, totalAcumulado: 3809.77 },
  { data: "novembro", invMes: 1000, patrimonio: 6460.87, pct: 16.27, parc: 13, juros: 39, totalInvestido: 3900, totalJuros: 248.77, totalAcumulado: 4148.77 },
  { data: "dezembro", invMes: 900, patrimonio: 7417.12, pct: 12.89, parc: 14, juros: 42.47, totalInvestido: 4200, totalJuros: 291.24, totalAcumulado: 4491.24 },
];

const history2025: Row[] = [
  { data: "janeiro", invMes: 650, patrimonio: 8129.62, pct: 8.76, parc: 15, juros: 59.15, totalInvestido: 7921.12, totalJuros: 59.15, totalAcumulado: 7980.27 },
  { data: "fevereiro", invMes: 1601.29, patrimonio: 9810.77, pct: 17.14, parc: 16, juros: 63.64, totalInvestido: 8425.12, totalJuros: 122.78, totalAcumulado: 8547.9 },
  { data: "março", invMes: 971.41, patrimonio: 10837.38, pct: 9.47, parc: 17, juros: 68.16, totalInvestido: 8929.12, totalJuros: 190.94, totalAcumulado: 9120.06 },
  { data: "abril", invMes: 1098.78, patrimonio: 11129.40, pct: 2.62, parc: 18, juros: 72.72, totalInvestido: 9433.12, totalJuros: 263.67, totalAcumulado: 9696.79 },
  { data: "maio", invMes: 881.10, patrimonio: 12203.72, pct: 8.80, parc: 19, juros: 77.32, totalInvestido: 9937.12, totalJuros: 340.99, totalAcumulado: 10278.11 },
  { data: "junho", invMes: 992.40, patrimonio: 13324.58, pct: 8.41, parc: 20, juros: 81.96, totalInvestido: 10441.12, totalJuros: 422.95, totalAcumulado: 10864.07 },
  { data: "julho", invMes: 789.60, patrimonio: 14217.66, pct: 6.28, parc: 21, juros: 86.63, totalInvestido: 10945.12, totalJuros: 509.58, totalAcumulado: 11454.7 },
  { data: "agosto", invMes: 2027.62, patrimonio: 16367.87, pct: 13.14, parc: 22, juros: 91.34, totalInvestido: 11449.12, totalJuros: 600.92, totalAcumulado: 12050.04 },
  { data: "setembro", invMes: 766.37, patrimonio: 17153.12, pct: 4.58, parc: 23, juros: 96.09, totalInvestido: 11953.12, totalJuros: 697.01, totalAcumulado: 12650.13 },
  { data: "outubro", invMes: 1491.29, patrimonio: 18764.01, pct: 7.89, parc: 24, juros: 100.87, totalInvestido: 12457.12, totalJuros: 797.89, totalAcumulado: 13255.01 },
  { data: "novembro", invMes: 1361.49, patrimonio: 20255.73, pct: 7.36, parc: 25, juros: 105.7, totalInvestido: 12961.12, totalJuros: 903.58, totalAcumulado: 13864.7 },
  { data: "dezembro", invMes: 2466.00, patrimonio: 22666.30, pct: 10.64, parc: 26, juros: 110.56, totalInvestido: 13465.12, totalJuros: 1014.14, totalAcumulado: 14479.26 },
];

const years = [
  { ano: 2023, aporte: 670, media: 567.5, projecao: "12% a.a", meta: 0, rows: history2023 },
  { ano: 2024, aporte: 670, media: 1258.11, projecao: "12% a.a", meta: 20000, rows: history2024 },
  { ano: 2025, aporte: 670, media: 1258.11, projecao: "12% a.a", meta: 20000, rows: history2025 },
];

const fmt = (n: number) =>
  n.toLocaleString("pt-BR", { style: "currency", currency: "BRL", minimumFractionDigits: 2 });

function InvestmentsPage() {
  const last = history2025[history2025.length - 1];
  const totalInvestido = history2024[history2024.length - 1].totalInvestido + last.totalInvestido;
  const totalJuros = last.totalJuros + history2024[history2024.length - 1].totalJuros;
  return (
    <div className="min-h-screen bg-background pb-16">
      <header className="px-5 pt-12 pb-4 flex items-center gap-3">
        <Link to="/dashboard" className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card">
          <ArrowLeft className="w-4 h-4 text-foreground" />
        </Link>
        <div>
          <p className="text-[15px] font-bold text-foreground">Investimentos</p>
          <p className="text-xs text-muted-foreground">Histórico mensal de aportes e rendimentos</p>
        </div>
      </header>

      <section className="px-5">
        <div className="rounded-3xl bg-gradient-wealth text-white p-5 shadow-elevated relative overflow-hidden">
          <div className="absolute -bottom-16 -right-10 w-48 h-48 rounded-full bg-white/10 blur-2xl" />
          <p className="text-[11px] uppercase tracking-wider text-white/70 font-semibold">Patrimônio atual</p>
          <p className="text-3xl font-extrabold mt-1">{fmt(last.patrimonio)}</p>
          <div className="mt-3 grid grid-cols-2 gap-3 text-xs">
            <div className="bg-white/10 rounded-2xl p-3">
              <p className="text-white/70">Total investido</p>
              <p className="text-sm font-bold mt-0.5">{fmt(totalInvestido)}</p>
            </div>
            <div className="bg-white/10 rounded-2xl p-3">
              <p className="text-white/70">Total juros</p>
              <p className="text-sm font-bold mt-0.5">{fmt(totalJuros)}</p>
            </div>
          </div>
        </div>
      </section>

      {years.map((y) => (
        <section key={y.ano} className="px-5 mt-5">
          <div className="rounded-3xl bg-card shadow-card overflow-hidden">
            <div className="p-4 flex items-center justify-between border-b border-border">
              <div className="flex items-center gap-2">
                <div className="w-9 h-9 rounded-xl bg-primary/10 text-primary grid place-items-center">
                  <LineChart className="w-4 h-4" />
                </div>
                <div>
                  <p className="text-sm font-bold text-foreground">Ano {y.ano}</p>
                  <p className="text-[11px] text-muted-foreground">
                    Aporte {y.aporte} · Média {fmt(y.media)} · Meta {y.meta ? fmt(y.meta) : "—"}
                  </p>
                </div>
              </div>
              <span className="inline-flex items-center gap-1 px-2 py-1 rounded-full bg-success/15 text-success text-[10px] font-bold">
                <TrendingUp className="w-3 h-3" /> {y.projecao}
              </span>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-[11px]">
                <thead>
                  <tr className="bg-secondary/60 text-muted-foreground">
                    <th className="text-left font-semibold px-3 py-2">Mês</th>
                    <th className="text-right font-semibold px-3 py-2">Inv. mês</th>
                    <th className="text-right font-semibold px-3 py-2">Patrimônio</th>
                    <th className="text-right font-semibold px-3 py-2">%</th>
                    <th className="text-right font-semibold px-3 py-2">Juros</th>
                    <th className="text-right font-semibold px-3 py-2">Acumulado</th>
                  </tr>
                </thead>
                <tbody>
                  {y.rows.map((r, i) => (
                    <tr key={i} className="border-t border-border/60">
                      <td className="px-3 py-2 capitalize font-medium text-foreground">{r.data}</td>
                      <td className="px-3 py-2 text-right text-foreground">{fmt(r.invMes)}</td>
                      <td className="px-3 py-2 text-right text-foreground font-semibold">{fmt(r.patrimonio)}</td>
                      <td className={`px-3 py-2 text-right font-semibold ${r.pct === undefined ? "text-muted-foreground" : r.pct >= 0 ? "text-success" : "text-rose-500"}`}>
                        {r.pct === undefined ? "—" : `${r.pct.toFixed(2)}%`}
                      </td>
                      <td className="px-3 py-2 text-right text-foreground">{fmt(r.juros)}</td>
                      <td className="px-3 py-2 text-right text-primary font-bold">{fmt(r.totalAcumulado)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </section>
      ))}

      <section className="px-5 mt-5">
        <div className="rounded-3xl bg-card p-4 shadow-card flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-success/15 text-success grid place-items-center">
            <Wallet className="w-5 h-5" />
          </div>
          <p className="text-xs text-muted-foreground">
            Dados importados do seu controle pessoal. Continue aportando mensalmente para alcançar suas metas.
          </p>
        </div>
      </section>
    </div>
  );
}