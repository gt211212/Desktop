const el = id => document.getElementById(id);
const clienteDaOS = id => { const o = achar("ordens", id); return o ? (achar("clientes", o.clienteId) || {}).nome || "—" : "—"; };
const badgePag = s => s === "Pago" ? "ok" : s === "Cancelado" ? "low" : "wait";

el("f_ordemId").addEventListener("change", e => {
  const o = achar("ordens", e.target.value), v = el("f_valor");
  if (o && !Number(v.value)) v.value = o.valor || "";
});

setupCrud({
  key: "pagamentos", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Novo Pagamento", tituloEditar: "Editar Pagamento", colunas: 7,
  numeros: ["ordemId", "valor"],
  padrao: () => ({ data: isoLocal() }),
  validar: d => d.valor > 0 ? null : "Informe um valor maior que zero.",
  aoAbrir: () => {
    const ordens = DB.get("ordens");
    preencherSelect(el("f_ordemId"), ordens, o => o.id, o => `#${pad(o.id, 4)} – ${clienteDaOS(o.id)}`,
      ordens.length ? "Selecione a OS" : "Cadastre uma OS primeiro");
  },
  texto: p => [pad(p.ordemId, 4), clienteDaOS(p.ordemId), p.forma, p.status].join(" "),
  linha: p => `<tr><td>#${pad(p.ordemId, 4)}</td><td>${esc(clienteDaOS(p.ordemId))}</td><td>${brl(p.valor)}</td>
    <td>${esc(p.forma)}</td><td><span class="badge ${badgePag(p.status)}">${esc(p.status)}</span></td>
    <td>${fmtData(p.data)}</td>${acoes(p.id)}</tr>`,
  aoRenderizar: todos => {
    const soma = s => todos.filter(p => p.status === s).reduce((t, p) => t + p.valor, 0);
    el("recebido").textContent = brl(soma("Pago"));
    el("pendente").textContent = brl(soma("Pendente"));
    el("transacoes").textContent = todos.length;
  }
});
