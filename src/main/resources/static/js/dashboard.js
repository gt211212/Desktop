document.addEventListener("DOMContentLoaded", () => {
  const pecas = DB.get("pecas"), ordens = DB.get("ordens"), clientes = DB.get("clientes"), pagamentos = DB.get("pagamentos");
  const el = id => document.getElementById(id);
  const cfg = DB.get("config", {});
  const primeiro = (cfg.usuario || "Bruno Honorato").split(" ")[0];

  el("saudacao").textContent = `Olá, ${primeiro}!`;
  el("dataHoje").textContent = new Date().toLocaleDateString("pt-BR", { day: "numeric", month: "long", year: "numeric" });

  const mes = isoLocal().slice(0, 7);
  el("cardPecas").textContent = pecas.reduce((t, p) => t + (p.estoque || 0), 0);
  el("cardOS").textContent = ordens.filter(o => o.status === "Em andamento").length;
  el("cardClientes").textContent = clientes.length;
  el("cardReceita").textContent = brl(pagamentos.filter(p => p.status === "Pago" && (p.data || "").startsWith(mes)).reduce((t, p) => t + p.valor, 0));

  // gráfico: OS abertas nos últimos 7 dias
  const dias = [];
  for (let i = 6; i >= 0; i--) {
    const d = new Date(); d.setDate(d.getDate() - i);
    const iso = isoLocal(d);
    dias.push({ label: d.toLocaleDateString("pt-BR", { weekday: "short", day: "2-digit" }), valor: ordens.filter(o => o.data === iso).length });
  }
  desenharBarras(el("graficoOS"), dias);

  const baixo = pecas.filter(p => p.estoque <= p.estoqueMinimo);
  el("tabelaBaixo").innerHTML = baixo.length
    ? baixo.map(p => `<tr><td>${esc(p.nome)}</td><td>${p.estoque}</td><td>${p.estoqueMinimo}</td></tr>`).join("")
    : '<tr><td colspan="3" class="empty">Nenhuma peça com estoque baixo.</td></tr>';

  const cor = s => s === "Concluída" ? "ok" : s === "Aguardando peças" ? "wait" : "blue";
  const ultimas = ordens.slice(-5).reverse();
  el("tabelaOS").innerHTML = ultimas.length
    ? ultimas.map(o => {
        const c = achar("clientes", o.clienteId), v = achar("veiculos", o.veiculoId);
        return `<tr><td>#${pad(o.id, 4)}</td><td>${esc(c ? c.nome : "—")}</td><td>${esc(v ? v.marca + " " + v.modelo : "—")}</td>
          <td><span class="badge ${cor(o.status)}">${esc(o.status)}</span></td><td>${fmtData(o.data)}</td></tr>`;
      }).join("")
    : '<tr><td colspan="5" class="empty">Nenhuma ordem de serviço cadastrada.</td></tr>';
});
