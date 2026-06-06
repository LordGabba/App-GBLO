import { useEffect, useState } from "react";
import { X, Calendar, Wallet } from "lucide-react";

const categories = [
  { icon: "🍔", label: "Alimentação" },
  { icon: "🚗", label: "Transporte" },
  { icon: "🏠", label: "Casa" },
  { icon: "❤️", label: "Saúde" },
  { icon: "🎮", label: "Lazer" },
  { icon: "📚", label: "Educação" },
  { icon: "💼", label: "Trabalho" },
];

export function AddTransactionModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const [kind, setKind] = useState<"receita" | "despesa">("despesa");
  const [cat, setCat] = useState("Alimentação");
  const [value, setValue] = useState("0,00");

  useEffect(() => {
    if (open) document.body.style.overflow = "hidden";
    else document.body.style.overflow = "";
    return () => { document.body.style.overflow = ""; };
  }, [open]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center">
      <div onClick={onClose} className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-in fade-in" />
      <div className="relative w-full max-w-[480px] bg-card rounded-t-[32px] p-5 pb-8 shadow-elevated animate-in slide-in-from-bottom duration-300">
        <div className="mx-auto w-10 h-1.5 rounded-full bg-border mb-4" />
        <div className="flex items-center justify-between">
          <h3 className="text-lg font-bold text-foreground">Nova transação</h3>
          <button onClick={onClose} className="w-9 h-9 rounded-full bg-secondary grid place-items-center">
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Tipo */}
        <div className="mt-4 grid grid-cols-2 gap-2 p-1 rounded-2xl bg-secondary">
          <button
            onClick={() => setKind("receita")}
            className={`h-11 rounded-xl text-sm font-semibold transition ${kind === "receita" ? "bg-gradient-success text-white shadow-card" : "text-muted-foreground"}`}
          >Receita</button>
          <button
            onClick={() => setKind("despesa")}
            className={`h-11 rounded-xl text-sm font-semibold transition ${kind === "despesa" ? "bg-rose-500 text-white shadow-card" : "text-muted-foreground"}`}
          >Despesa</button>
        </div>

        {/* Valor */}
        <div className="mt-5 rounded-2xl bg-secondary p-5 text-center">
          <p className="text-[11px] uppercase tracking-wider text-muted-foreground font-semibold">Valor</p>
          <div className="mt-1 flex items-baseline justify-center gap-1">
            <span className="text-lg text-muted-foreground">R$</span>
            <input
              value={value}
              onChange={(e) => setValue(e.target.value)}
              inputMode="decimal"
              className="bg-transparent outline-none text-3xl font-extrabold text-foreground text-center w-40 tracking-tight"
            />
          </div>
        </div>

        {/* Fields */}
        <div className="mt-4 space-y-2">
          <Field label="Descrição" placeholder="Ex: Almoço com cliente" />
          <div className="grid grid-cols-2 gap-2">
            <FieldStatic label="Conta" value="Nubank" icon={<Wallet className="w-4 h-4 text-muted-foreground" />} />
            <FieldStatic label="Data" value="Hoje" icon={<Calendar className="w-4 h-4 text-muted-foreground" />} />
          </div>
        </div>

        {/* Categorias */}
        <p className="mt-5 text-xs font-semibold text-muted-foreground">Categoria</p>
        <div className="mt-2 flex gap-2 overflow-x-auto pb-1 scrollbar-none">
          {categories.map((c) => {
            const active = cat === c.label;
            return (
              <button
                key={c.label}
                onClick={() => setCat(c.label)}
                className={`shrink-0 flex flex-col items-center gap-1 px-3 py-2 rounded-2xl border transition ${
                  active ? "bg-primary text-primary-foreground border-primary shadow-card" : "bg-card border-border text-foreground"
                }`}
              >
                <span className="text-xl">{c.icon}</span>
                <span className="text-[10.5px] font-semibold">{c.label}</span>
              </button>
            );
          })}
        </div>

        <button
          onClick={onClose}
          className="mt-6 w-full h-14 rounded-2xl bg-gradient-primary text-white font-semibold shadow-elevated active:scale-[0.99]"
        >
          Salvar
        </button>
      </div>
    </div>
  );
}

function Field({ label, placeholder }: { label: string; placeholder: string }) {
  return (
    <label className="block">
      <span className="text-[11px] font-semibold text-muted-foreground ml-1">{label}</span>
      <input placeholder={placeholder} className="mt-1 w-full h-12 rounded-2xl bg-secondary px-4 text-sm text-foreground outline-none focus:ring-2 focus:ring-primary/30" />
    </label>
  );
}

function FieldStatic({ label, value, icon }: { label: string; value: string; icon: React.ReactNode }) {
  return (
    <div>
      <span className="text-[11px] font-semibold text-muted-foreground ml-1">{label}</span>
      <div className="mt-1 h-12 rounded-2xl bg-secondary px-4 flex items-center gap-2 text-sm text-foreground">
        {icon}{value}
      </div>
    </div>
  );
}