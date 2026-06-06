import { createFileRoute } from "@tanstack/react-router";
import { Dashboard } from "@/components/patrimonio/Dashboard";

export const Route = createFileRoute("/dashboard")({
  head: () => ({
    meta: [
      { title: "Dashboard — Patrimônio+" },
      { name: "description", content: "Acompanhe seu patrimônio, metas e investimentos em um só lugar." },
    ],
  }),
  component: () => <Dashboard />,
});