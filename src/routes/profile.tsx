import { useEffect, useState } from "react";
import { createFileRoute, Link } from "@tanstack/react-router";
import { ArrowLeft, Check, Copy, UserPlus, Users, Pencil, X, Camera } from "lucide-react";
import { ThemeToggle } from "@/components/patrimonio/ThemeToggle";

export const Route = createFileRoute("/profile")({
  head: () => ({
    meta: [
      { title: "Perfil — Patrimônio+" },
      { name: "description", content: "Seu código de amizade e gerenciamento de amigos no Patrimônio+." },
    ],
  }),
  component: ProfilePage,
});

function generateCode() {
  let c = "";
  for (let i = 0; i < 9; i++) c += Math.floor(Math.random() * 10);
  return c;
}

function formatCode(c: string) {
  return c.replace(/(\d{3})(\d{3})(\d{3})/, "$1 $2 $3");
}

function ProfilePage() {
  const [code, setCode] = useState("");
  const [friendCode, setFriendCode] = useState("");
  const [copied, setCopied] = useState(false);
  const [friends, setFriends] = useState<string[]>([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [name, setName] = useState("Luan Silva");
  const [email, setEmail] = useState("luan@email.com");
  const [photo, setPhoto] = useState<string | null>(null);
  const [editOpen, setEditOpen] = useState(false);
  const [draftName, setDraftName] = useState("");
  const [draftEmail, setDraftEmail] = useState("");
  const [draftPhoto, setDraftPhoto] = useState<string | null>(null);

  useEffect(() => {
    let c = localStorage.getItem("patrimonio:my-code");
    if (!c) {
      c = generateCode();
      localStorage.setItem("patrimonio:my-code", c);
    }
    setCode(c);
    try {
      const f = JSON.parse(localStorage.getItem("patrimonio:friends") || "[]");
      if (Array.isArray(f)) setFriends(f);
    } catch {}
    const savedName = localStorage.getItem("patrimonio:name");
    const savedEmail = localStorage.getItem("patrimonio:email");
    const savedPhoto = localStorage.getItem("patrimonio:photo");
    if (savedName) setName(savedName);
    if (savedEmail) setEmail(savedEmail);
    if (savedPhoto) setPhoto(savedPhoto);
  }, []);

  const openEdit = () => {
    setDraftName(name);
    setDraftEmail(email);
    setDraftPhoto(photo);
    setEditOpen(true);
  };

  const onPickPhoto = (file?: File | null) => {
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => setDraftPhoto(String(reader.result));
    reader.readAsDataURL(file);
  };

  const saveProfile = () => {
    const n = draftName.trim() || "Sem nome";
    setName(n);
    setEmail(draftEmail.trim());
    setPhoto(draftPhoto);
    localStorage.setItem("patrimonio:name", n);
    localStorage.setItem("patrimonio:email", draftEmail.trim());
    if (draftPhoto) localStorage.setItem("patrimonio:photo", draftPhoto);
    else localStorage.removeItem("patrimonio:photo");
    setEditOpen(false);
  };

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(code);
      setCopied(true);
      setTimeout(() => setCopied(false), 1500);
    } catch {}
  };

  const handleAdd = () => {
    setError("");
    setSuccess("");
    const clean = friendCode.replace(/\D/g, "");
    if (clean.length !== 9) {
      setError("O código deve ter 9 dígitos.");
      return;
    }
    if (clean === code) {
      setError("Você não pode adicionar o seu próprio código.");
      return;
    }
    if (friends.includes(clean)) {
      setError("Esse amigo já foi adicionado.");
      return;
    }
    const next = [...friends, clean];
    setFriends(next);
    localStorage.setItem("patrimonio:friends", JSON.stringify(next));
    setFriendCode("");
    setSuccess("Amigo adicionado com sucesso!");
  };

  const onChangeFriend = (v: string) => {
    const digits = v.replace(/\D/g, "").slice(0, 9);
    const parts = [digits.slice(0, 3), digits.slice(3, 6), digits.slice(6, 9)].filter(Boolean);
    setFriendCode(parts.join(" "));
  };

  return (
    <div className="min-h-screen bg-background pb-10">
      <div className="mx-auto w-full max-w-[800px] px-5">
      <header className="pt-12 pb-4 flex items-center gap-3">
        <Link to="/dashboard" className="w-10 h-10 rounded-full bg-card grid place-items-center shadow-card">
          <ArrowLeft className="w-4 h-4 text-foreground" />
        </Link>
        <div className="flex-1">
          <p className="text-[15px] font-bold text-foreground">Perfil</p>
          <p className="text-xs text-muted-foreground">Gerencie sua conta e amigos</p>
        </div>
        <ThemeToggle />
      </header>

      <section>
        <div className="rounded-3xl bg-card p-5 shadow-card flex items-center gap-4">
          {photo ? (
            <img src={photo} alt={name} className="w-16 h-16 rounded-full object-cover shadow-card" />
          ) : (
            <div className="w-16 h-16 rounded-full bg-gradient-wealth grid place-items-center text-white text-2xl font-extrabold shadow-card">
              {name.charAt(0).toUpperCase()}
            </div>
          )}
          <div className="flex-1 min-w-0">
            <p className="text-base font-bold text-foreground truncate">{name}</p>
            <p className="text-xs text-muted-foreground truncate">{email}</p>
          </div>
          <button
            onClick={openEdit}
            className="h-9 px-3 rounded-full bg-primary/10 text-primary text-xs font-semibold inline-flex items-center gap-1 active:scale-95 transition"
          >
            <Pencil className="w-3.5 h-3.5" /> Editar
          </button>
        </div>
      </section>

      {/* Seu código */}
      <section className="mt-5">
        <div className="rounded-3xl bg-gradient-wealth text-white p-5 shadow-elevated relative overflow-hidden">
          <div className="absolute -bottom-16 -right-10 w-48 h-48 rounded-full bg-white/10 blur-2xl" />
          <p className="text-[11px] uppercase tracking-wider text-white/70 font-semibold">Seu código</p>
          <p className="text-xs text-white/80 mt-1">Compartilhe com amigos para se conectarem com você.</p>
          <div className="relative mt-4 flex items-center justify-between gap-3 bg-white/10 backdrop-blur rounded-2xl px-4 py-3">
            <span className="text-2xl font-extrabold tracking-[0.25em]">{code ? formatCode(code) : "— — —"}</span>
            <button
              onClick={handleCopy}
              className="w-10 h-10 rounded-full bg-white/20 grid place-items-center active:scale-95 transition"
              aria-label="Copiar código"
            >
              {copied ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
            </button>
          </div>
        </div>
      </section>

      {/* Adicionar amigo */}
      <section className="mt-4">
        <div className="rounded-3xl bg-card p-5 shadow-card">
          <div className="flex items-center gap-2">
            <UserPlus className="w-4 h-4 text-primary" />
            <p className="text-sm font-bold text-foreground">Adicionar amigo</p>
          </div>
          <p className="text-xs text-muted-foreground mt-1">Digite o código de 9 dígitos do seu amigo.</p>
          <div className="mt-3 flex items-center gap-2">
            <input
              inputMode="numeric"
              value={friendCode}
              onChange={(e) => onChangeFriend(e.target.value)}
              placeholder="000 000 000"
              maxLength={11}
              className="flex-1 h-12 rounded-2xl bg-secondary px-4 text-base font-semibold tracking-[0.2em] text-foreground placeholder:text-muted-foreground/60 outline-none focus:ring-2 focus:ring-primary/40"
            />
            <button
              onClick={handleAdd}
              className="h-12 px-5 rounded-2xl bg-primary text-primary-foreground font-semibold text-sm active:scale-95 transition"
            >
              Adicionar
            </button>
          </div>
          {error && <p className="text-xs text-rose-500 mt-2 font-medium">{error}</p>}
          {success && <p className="text-xs text-success mt-2 font-medium">{success}</p>}
        </div>
      </section>

      {/* Amigos */}
      <section className="mt-4">
        <div className="rounded-3xl bg-card p-5 shadow-card">
          <div className="flex items-center gap-2 mb-3">
            <Users className="w-4 h-4 text-foreground" />
            <p className="text-sm font-bold text-foreground">Seus amigos</p>
            <span className="ml-auto text-xs text-muted-foreground">{friends.length}</span>
          </div>
          {friends.length === 0 ? (
            <p className="text-xs text-muted-foreground">Você ainda não adicionou amigos. Use o campo acima para começar.</p>
          ) : (
            <ul className="space-y-2">
              {friends.map((f) => (
                <li key={f} className="flex items-center gap-3 p-3 rounded-2xl bg-secondary/60">
                  <div className="w-9 h-9 rounded-full bg-card grid place-items-center text-sm font-bold text-foreground">
                    {f.slice(0, 1)}
                  </div>
                  <span className="text-sm font-semibold tracking-widest text-foreground">{formatCode(f)}</span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </section>
      </div>

      {editOpen && (
        <div className="fixed inset-0 z-40 bg-black/50 grid place-items-end sm:place-items-center" onClick={() => setEditOpen(false)}>
          <div className="w-full sm:max-w-md bg-card rounded-t-3xl sm:rounded-3xl p-5 shadow-elevated" onClick={(e) => e.stopPropagation()}>
            <div className="flex items-center justify-between mb-4">
              <p className="text-base font-bold text-foreground">Editar perfil</p>
              <button onClick={() => setEditOpen(false)} className="w-8 h-8 rounded-full bg-secondary grid place-items-center">
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="flex flex-col items-center gap-2">
              <label className="relative cursor-pointer">
                {draftPhoto ? (
                  <img src={draftPhoto} alt="" className="w-20 h-20 rounded-full object-cover" />
                ) : (
                  <div className="w-20 h-20 rounded-full bg-gradient-wealth grid place-items-center text-white text-2xl font-extrabold">
                    {(draftName || "?").charAt(0).toUpperCase()}
                  </div>
                )}
                <span className="absolute -bottom-1 -right-1 w-8 h-8 rounded-full bg-primary grid place-items-center text-white shadow-card">
                  <Camera className="w-4 h-4" />
                </span>
                <input
                  type="file"
                  accept="image/*"
                  className="hidden"
                  onChange={(e) => onPickPhoto(e.target.files?.[0])}
                />
              </label>
              <p className="text-[11px] text-muted-foreground">Toque para trocar a foto</p>
            </div>

            <div className="mt-4 space-y-3">
              <div>
                <label className="text-[11px] font-semibold text-muted-foreground uppercase tracking-wider">Nome de exibição</label>
                <input
                  value={draftName}
                  onChange={(e) => setDraftName(e.target.value)}
                  className="mt-1 w-full h-11 rounded-2xl bg-secondary px-4 text-sm text-foreground outline-none focus:ring-2 focus:ring-primary/40"
                  placeholder="Seu nome"
                />
              </div>
              <div>
                <label className="text-[11px] font-semibold text-muted-foreground uppercase tracking-wider">E-mail</label>
                <input
                  value={draftEmail}
                  onChange={(e) => setDraftEmail(e.target.value)}
                  className="mt-1 w-full h-11 rounded-2xl bg-secondary px-4 text-sm text-foreground outline-none focus:ring-2 focus:ring-primary/40"
                  placeholder="voce@email.com"
                />
              </div>
              {draftPhoto && (
                <button
                  onClick={() => setDraftPhoto(null)}
                  className="text-xs text-rose-500 font-semibold"
                >
                  Remover foto
                </button>
              )}
            </div>

            <button
              onClick={saveProfile}
              className="mt-5 w-full h-12 rounded-2xl bg-primary text-primary-foreground font-bold text-sm active:scale-[0.99]"
            >
              Salvar alterações
            </button>
          </div>
        </div>
      )}
    </div>
  );
}