import { createFileRoute } from "@tanstack/react-router";
import { LoginScreen } from "@/components/patrimonio/LoginScreen";

export const Route = createFileRoute("/")({
  head: () => ({
    meta: [
      { title: "Patrimônio+ — Controle financeiro e patrimônio" },
      { name: "description", content: "Controle suas finanças, acompanhe seu patrimônio e alcance suas metas." },
      { property: "og:title", content: "Patrimônio+" },
      { property: "og:description", content: "Controle suas finanças, acompanhe seu patrimônio e alcance suas metas." },
    ],
  }),
  component: Index,
});

function Index() {
  return <LoginScreen />;
}
