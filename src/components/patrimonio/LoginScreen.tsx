import { useState } from "react";
import { useNavigate } from "@tanstack/react-router";
import { Mail, Lock, Eye, EyeOff, ShieldCheck, TrendingUp } from "lucide-react";

export function LoginScreen() {
  const navigate = useNavigate();
  const [showPwd, setShowPwd] = useState(false);
  const [email, setEmail] = useState("luan@patrimonio.app");
  const [pwd, setPwd] = useState("••••••••");

  function submit(e: React.FormEvent) {
    e.preventDefault();
    navigate({ to: "/dashboard" });
  }

  return (
    <div className="min-h-screen w-full bg-background flex flex-col">
      {/* Top brand panel */}
      <div className="relative overflow-hidden bg-gradient-wealth text-white px-6 pt-14 pb-12 rounded-b-[36px] shadow-elevated">
        <div className="absolute -top-20 -right-16 w-64 h-64 rounded-full bg-white/10 blur-2xl" />
        <div className="absolute -bottom-24 -left-10 w-72 h-72 rounded-full bg-[#22C55E]/30 blur-3xl" />
        <div className="relative flex items-center gap-3">
          <div className="w-12 h-12 rounded-2xl bg-white/15 backdrop-blur flex items-center justify-center ring-1 ring-white/25">
            <TrendingUp className="w-6 h-6" strokeWidth={2.2} />
          </div>
          <div className="leading-tight">
            <h1 className="text-2xl font-extrabold tracking-tight">Patrimônio<span className="text-[#86EFAC]">+</span></h1>
            <p className="text-xs text-white/75">wealth · finance · goals</p>
          </div>
        </div>
        <p className="relative mt-8 text-[15px] text-white/90 leading-relaxed max-w-[18rem]">
          Controle suas finanças, acompanhe seu patrimônio e alcance suas metas.
        </p>
      </div>

      {/* Form */}
      <form onSubmit={submit} className="flex-1 px-6 pt-8 pb-8 flex flex-col">
        <h2 className="text-xl font-bold text-foreground">Entrar</h2>
        <p className="text-sm text-muted-foreground mt-1">Bem-vindo de volta 👋</p>

        <div className="mt-6 space-y-3">
          <label className="block">
            <span className="text-xs font-medium text-muted-foreground ml-1">E-mail</span>
            <div className="mt-1 flex items-center gap-2 rounded-2xl bg-secondary px-4 h-13 py-3 ring-1 ring-transparent focus-within:ring-primary/40 transition">
              <Mail className="w-4 h-4 text-muted-foreground" />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground placeholder:text-muted-foreground"
                placeholder="voce@email.com"
              />
            </div>
          </label>

          <label className="block">
            <span className="text-xs font-medium text-muted-foreground ml-1">Senha</span>
            <div className="mt-1 flex items-center gap-2 rounded-2xl bg-secondary px-4 py-3 ring-1 ring-transparent focus-within:ring-primary/40 transition">
              <Lock className="w-4 h-4 text-muted-foreground" />
              <input
                type={showPwd ? "text" : "password"}
                value={pwd}
                onChange={(e) => setPwd(e.target.value)}
                className="flex-1 bg-transparent outline-none text-sm text-foreground"
                placeholder="••••••••"
              />
              <button type="button" onClick={() => setShowPwd((v) => !v)} className="text-muted-foreground">
                {showPwd ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
              </button>
            </div>
          </label>
        </div>

        <div className="flex justify-end mt-3">
          <button type="button" className="text-xs font-semibold text-primary">Esqueci minha senha</button>
        </div>

        <button
          type="submit"
          className="mt-6 h-14 rounded-2xl bg-gradient-primary text-primary-foreground font-semibold text-[15px] shadow-elevated active:scale-[0.99] transition"
        >
          Entrar
        </button>

        <button type="button" className="mt-3 text-sm text-muted-foreground">
          Não tem conta? <span className="text-primary font-semibold">Criar conta</span>
        </button>

        <div className="flex items-center gap-3 my-6">
          <div className="flex-1 h-px bg-border" />
          <span className="text-xs text-muted-foreground">Ou continue com</span>
          <div className="flex-1 h-px bg-border" />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <SocialButton label="Google" />
          <SocialButton label="Facebook" color="#1877F2" />
        </div>

        <div className="mt-auto pt-8 flex items-center justify-center gap-2 text-[11px] text-muted-foreground">
          <ShieldCheck className="w-3.5 h-3.5 text-success" />
          Seus dados protegidos com criptografia.
        </div>
      </form>
    </div>
  );
}

function SocialButton({ label, color }: { label: string; color?: string }) {
  return (
    <button
      type="button"
      className="h-12 rounded-2xl bg-card border border-border flex items-center justify-center gap-2 text-sm font-medium text-foreground shadow-card active:scale-[0.99]"
    >
      <span
        className="w-5 h-5 rounded-full flex items-center justify-center text-[10px] font-bold text-white"
        style={{ backgroundColor: color ?? "#EA4335" }}
      >
        {label[0]}
      </span>
      {label}
    </button>
  );
}