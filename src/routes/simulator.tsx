import { createFileRoute, Link } from "@tanstack/react-router";
import { useMemo, useState } from "react";
import { ArrowLeft, Calculator, TrendingUp, Eraser } from "lucide-react";

export const Route = createFileRoute("/simulator")({
  head: () => ({
    meta: [
      { title: "Simulador de Juros Compostos — Patrimônio+" },
      { name: "description", content: "Simule cenários de juros compostos e veja a evolução mês a mês." },
    ],
  }),
  component: SimulatorPage,
});

const fmt = (n: number) =>
  n.toLocaleString("pt-BR", { style: "currency", currency: "BRL", minimumFractionDigits: 2 });

type Row = {
  mes: number;
  juros: number;
  totalInvestido: number;
  totalJuros: number;
  totalAcumulado: number;
};

function SimulatorPage() {
  const [valorInicial, setValorInicial] = useState("");
  const [valorMensal, setValorMensal] = useState("");
  const [taxa, setTaxa] = useState("8");
  const [taxaTipo, setTaxaTipo] = useState<"anual" | "mensal">("anual");
  const [periodo, setPeriodo] = useState("1");
  const [periodoTipo, setPeriodoTipo] = useState<"ano" | "mes">("ano");
  const [result, setResult] = useState<{ rows: Row[]; meses: number } | null>(null);

  const parse = (s: string) => {
    const n = parseFloat(s.replace(/\./g, "").replace(",", "."));
    return isNaN(n) ? 0 : n;
  };

  const calcular = () => {
    const pv = parse(valorInicial);
    const pmt = parse(valorMensal);
    const t = parse(taxa);
    const p = parse(periodo);
    const meses = Math.max(1, Math.round(periodoTipo === "ano" ? p * 12 : p));
    const iMensal = taxaTipo === "anual" ? Math.pow(1 + t / 100, 1 / 12) - 1 : t / 100;
    const rows: Row[] = [];
    let saldo = pv;
    let totalInv = pv;
    let totalJuros = 0;
    for (let m = 0; m < meses; m++) {
      saldo += pmt;
      totalInv += pmt;
      const juros = saldo * iMensal;
      saldo += juros;
      totalJuros += juros;
      rows.push({
        mes: m,
        juros,
        totalInvestido: totalInv,
        totalJuros,
        totalAcumulado: saldo,
      });
    }
    setResult({ rows, meses });
  };

  const limpar = () => {
    setValorInicial("");
    setValorMensal("");
    setTaxa("8");
    setPeriodo("1");
    setResult(null);
  };

  const resumo = useMemo(() => {
    if (!result) return null;
    const last = result.rows[result.rows.length - 1];
    return { final: last.totalAcumulado, investido: last.totalInvestido, juros: last.totalJuros };
  }, [result]);

  return (
    <div className="min-h-screen bg-background pb-16">
      <header className="px-5 pt-12 pb-4 max-w-[800px] mx-auto flex items-center gap-3">
        <Link to="/dashboard" className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card">
          <ArrowLeft className="w-4 h-4 text-foreground" />
        </Link>
        <div>
          <p className="text-[15px] font-bold text-foreground">Simular Cenários</p>
          <p className="text-xs text-muted-foreground">Calculadora de juros compostos</p>
        </div>
      </header>

      <section className="px-5 max-w-[800px] mx-auto">
        <div className="rounded-3xl bg-card shadow-card p-5">
          <div className="flex items-center gap-2 mb-4">
            <div className="w-9 h-9 rounded-xl bg-primary/10 text-primary grid place-items-center">
              <Calculator className="w-4 h-4" />
            </div>
            <p className="text-sm font-bold text-foreground">Simulador de Juros Compostos</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Field label="Valor inicial">
              <Prefix>R$</Prefix>
              <input
                inputMode="decimal"
                placeholder="0,00"
                value={valorInicial}
                onChange={(e) => setValorInicial(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground px-3 h-11"
              />
            </Field>
            <Field label="Valor mensal">
              <Prefix>R$</Prefix>
              <input
                inputMode="decimal"
                placeholder="0,00"
                value={valorMensal}
                onChange={(e) => setValorMensal(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground px-3 h-11"
              />
            </Field>
            <Field label="Taxa de juros">
              <Prefix>%</Prefix>
              <input
                inputMode="decimal"
                value={taxa}
                onChange={(e) => setTaxa(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground px-3 h-11"
              />
              <select
                value={taxaTipo}
                onChange={(e) => setTaxaTipo(e.target.value as "anual" | "mensal")}
                className="bg-secondary/60 text-foreground text-xs h-11 px-2 border-l border-border outline-none"
              >
                <option value="anual">anual</option>
                <option value="mensal">mensal</option>
              </select>
            </Field>
            <Field label="Período">
              <input
                inputMode="numeric"
                value={periodo}
                onChange={(e) => setPeriodo(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground px-3 h-11"
              />
              <select
                value={periodoTipo}
                onChange={(e) => setPeriodoTipo(e.target.value as "ano" | "mes")}
                className="bg-secondary/60 text-foreground text-xs h-11 px-2 border-l border-border outline-none"
              >
                <option value="ano">ano(s)</option>
                <option value="mes">mês(es)</option>
              </select>
            </Field>
          </div>

          <div className="flex items-center justify-between mt-5 gap-3 flex-wrap">
            <button
              onClick={calcular}
              className="h-11 px-6 rounded-2xl bg-primary text-primary-foreground font-semibold text-sm active:scale-[0.99]"
            >
              Calcular
            </button>
            <button
              onClick={limpar}
              className="text-xs font-semibold text-muted-foreground hover:text-foreground inline-flex items-center gap-1"
            >
              <Eraser className="w-3 h-3" /> Limpar
            </button>
          </div>
        </div>

        {result && resumo && (
          <>
            <div className="grid grid-cols-3 gap-3 mt-5">
              <SummaryCard label="Valor final" value={fmt(resumo.final)} accent="text-primary" />
              <SummaryCard label="Total investido" value={fmt(resumo.investido)} accent="text-foreground" />
              <SummaryCard label="Total em juros" value={fmt(resumo.juros)} accent="text-success" />
            </div>

            <div className="rounded-3xl bg-card shadow-card mt-5 overflow-hidden">
              <div className="p-4 flex items-center gap-2 border-b border-border">
                <div className="w-9 h-9 rounded-xl bg-success/15 text-success grid place-items-center">
                  <TrendingUp className="w-4 h-4" />
                </div>
                <div>
                  <p className="text-sm font-bold text-foreground">Tabela</p>
                  <p className="text-[11px] text-muted-foreground">Evolução mês a mês ({result.meses} meses)</p>
                </div>
              </div>
              <div className="max-h-[480px] overflow-auto">
                <table className="w-full text-[11px]">
                  <thead className="sticky top-0 bg-secondary/80 backdrop-blur">
                    <tr className="text-muted-foreground">
                      <th className="text-left font-semibold px-3 py-2">Mês</th>
                      <th className="text-right font-semibold px-3 py-2">Juros</th>
                      <th className="text-right font-semibold px-3 py-2">Total Investido</th>
                      <th className="text-right font-semibold px-3 py-2">Total Juros</th>
                      <th className="text-right font-semibold px-3 py-2">Total Acumulado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {result.rows.map((r) => (
                      <tr key={r.mes} className="border-t border-border/60">
                        <td className="px-3 py-2 text-foreground font-medium">{r.mes}</td>
                        <td className="px-3 py-2 text-right text-foreground">{fmt(r.juros)}</td>
                        <td className="px-3 py-2 text-right text-foreground">{fmt(r.totalInvestido)}</td>
                        <td className="px-3 py-2 text-right text-success font-semibold">{fmt(r.totalJuros)}</td>
                        <td className="px-3 py-2 text-right text-primary font-bold">{fmt(r.totalAcumulado)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </>
        )}
      </section>
    </div>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <p className="text-[11px] font-semibold text-muted-foreground mb-1.5">{label}</p>
      <div className="flex items-center rounded-xl border border-border bg-background overflow-hidden">
        {children}
      </div>
    </div>
  );
}

function Prefix({ children }: { children: React.ReactNode }) {
  return (
    <span className="bg-secondary/60 text-muted-foreground text-xs font-semibold h-11 px-3 grid place-items-center border-r border-border">
      {children}
    </span>
  );
}

function SummaryCard({ label, value, accent }: { label: string; value: string; accent: string }) {
  return (
    <div className="rounded-2xl bg-card shadow-card p-3">
      <p className="text-[10px] uppercase tracking-wider text-muted-foreground font-semibold">{label}</p>
      <p className={`text-sm font-bold mt-1 ${accent}`}>{value}</p>
    </div>
  );
}