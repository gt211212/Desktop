const el = id => document.getElementById(id);
const soma = (lista, f) => lista.reduce((t, x) => t + f(x), 0);
const nomeDe = (chave, id, campo = "nome") => (achar(chave, id) || {})[campo] || "—";

function montarRelatorio(tipo, ini, fim) {
  const no = d => d && (!ini || d >= ini) && (!fim || d <= fim);

  if (tipo === "estoque") {
    const pecas = DB.get("pecas"), baixo = pecas.filter(p => p.estoque <= p.estoqueMinimo);
    return {
      titulo: "Quantidade em estoque por peça (posição atual)",
      cards: [["Peças cadastradas", pecas.length], ["Com estoque baixo", baixo.length], ["Valor em estoque", brl(soma(pecas, p => p.estoque * p.preco))]],
      barras: pecas.slice(0, 8).map(p => ({ label: p.nome, valor: p.estoque })),
      cols: ["Peça com estoque baixo", "Estoque", "Mínimo", "Fornecedor"],
      linhas: baixo.map(p => [p.nome, p.estoque, p.estoqueMinimo, p.fornecedor])
    };
  }

  if (tipo === "ordens") {
    const os = DB.get("ordens").filter(o => no(o.data));
    return {
      titulo: "Ordens de serviço por status",
      cards: [["Total no período", os.length], ["Em andamento", os.filter(o => o.status === "Em andamento").length], ["Concluídas", os.filter(o => o.status === "Concluída").length]],
      barras: ["Em andamento", "Aguardando peças", "Concluída"].map(s => ({ label: s, valor: os.filter(o => o.status === s).length })),
      cols: ["Nº OS", "Cliente", "Veículo", "Status", "Valor", "Data"],
      linhas: os.map(o => ["#" + pad(o.id, 4), nomeDe("clientes", o.clienteId), nomeDe("veiculos", o.veiculoId, "modelo"), o.status, brl(o.valor), fmtData(o.data)])
    };
  }

  if (tipo === "clientes") {
    const os = DB.get("ordens").filter(o => no(o.data)), pags = DB.get("pagamentos");
    const linhas = DB.get("clientes").map(c => {
      const minhas = os.filter(o => o.clienteId === c.id);
      const pago = soma(pags.filter(p => p.status === "Pago" && minhas.some(o => o.id === p.ordemId)), p => p.valor);
      return { c, qtd: minhas.length, pago, veic: DB.get("veiculos").filter(v => v.clienteId === c.id).length };
    });
    return {
      titulo: "Ordens de serviço por cliente (no período)",
      cards: [["Clientes cadastrados", linhas.length], ["Veículos cadastrados", DB.get("veiculos").length], ["OS no período", os.length]],
      barras: linhas.filter(l => l.qtd).sort((a, b) => b.qtd - a.qtd).slice(0, 8).map(l => ({ label: l.c.nome, valor: l.qtd })),
      cols: ["Cliente", "Veículos", "OS no período", "Total pago"],
      linhas: linhas.map(l => [l.c.nome, l.veic, l.qtd, brl(l.pago)])
    };
  }

  // financeiro (padrão)
  const pags = DB.get("pagamentos").filter(p => no(p.data));
  const pagos = pags.filter(p => p.status === "Pago"), porDia = {};
  pagos.forEach(p => porDia[p.data] = (porDia[p.data] || 0) + p.valor);
  return {
    titulo: "Receita recebida por dia",
    cards: [["Receita recebida", brl(soma(pagos, p => p.valor))], ["A receber (pendente)", brl(soma(pags.filter(p => p.status === "Pendente"), p => p.valor))], ["Pagamentos no período", pags.length]],
    barras: Object.keys(porDia).sort().map(d => ({ label: fmtData(d).slice(0, 5), valor: porDia[d], texto: brl(porDia[d]) })),
    cols: ["Data", "OS", "Cliente", "Forma", "Valor", "Status"],
    linhas: pags.map(p => [fmtData(p.data), "#" + pad(p.ordemId, 4), nomeDe("clientes", (achar("ordens", p.ordemId) || {}).clienteId), p.forma, brl(p.valor), p.status])
  };
}

function gerarRelatorio() {
  const r = montarRelatorio(el("tipo").value, el("dataIni").value, el("dataFim").value);
  el("cards").innerHTML = r.cards.map(c => `<div class="card"><div class="stat-title">${esc(c[0])}</div><div class="stat-value">${esc(c[1])}</div></div>`).join("");
  el("tituloGrafico").textContent = r.titulo;
  desenharBarras(el("grafico"), r.barras);
  el("thead").innerHTML = "<tr>" + r.cols.map(c => `<th>${esc(c)}</th>`).join("") + "</tr>";
  el("tbody").innerHTML = r.linhas.length
    ? r.linhas.map(l => "<tr>" + l.map(c => `<td>${esc(c)}</td>`).join("") + "</tr>").join("")
    : `<tr><td colspan="${r.cols.length}" class="empty">Nada para mostrar neste relatório.</td></tr>`;
}

(function iniciar() {
  const hoje = new Date();
  el("dataIni").value = isoLocal(new Date(hoje.getFullYear(), hoje.getMonth(), 1));
  el("dataFim").value = isoLocal(hoje);
  gerarRelatorio();
})();
