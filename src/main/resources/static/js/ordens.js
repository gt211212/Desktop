const nomeCli = id => (achar("clientes", id) || {}).nome || "—";
const nomeMec = id => (achar("mecanicos", id) || {}).nome || "—";
const textoVeic = v => `${v.marca} ${v.modelo}${v.ano ? " - " + v.ano : ""}${v.placa ? " (" + v.placa + ")" : ""}`;
const nomeVeic = id => { const v = achar("veiculos", id); return v ? textoVeic(v) : "—"; };
const badgeOS = s => s === "Concluída" ? "ok" : s === "Aguardando peças" ? "wait" : "blue";
const el = id => document.getElementById(id);

function carregarVeiculos(clienteId) {
  const lista = clienteId ? DB.get("veiculos").filter(v => v.clienteId === Number(clienteId)) : [];
  preencherSelect(el("f_veiculoId"), lista, v => v.id, textoVeic,
    !clienteId ? "Selecione o cliente primeiro" : lista.length ? "Selecione o veículo" : "Este cliente não tem veículos");
}

el("f_clienteId").addEventListener("change", e => carregarVeiculos(e.target.value));

setupCrud({
  key: "ordens", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Nova Ordem de Serviço", tituloEditar: "Editar Ordem de Serviço", colunas: 8,
  numeros: ["clienteId", "veiculoId", "mecanicoId", "valor"],
  padrao: () => ({ data: isoLocal() }),
  aoAbrir: item => {
    const clientes = DB.get("clientes");
    preencherSelect(el("f_clienteId"), clientes, c => c.id, c => c.nome, clientes.length ? "Selecione o cliente" : "Cadastre um cliente primeiro");
    preencherSelect(el("f_mecanicoId"), DB.get("mecanicos").filter(m => m.status === "Ativo" || (item && m.id === item.mecanicoId)),
      m => m.id, m => m.nome, "Sem mecânico definido");
    carregarVeiculos(item ? item.clienteId : "");
  },
  filtro: o => !el("filtroStatus").value || o.status === el("filtroStatus").value,
  texto: o => [pad(o.id, 4), nomeCli(o.clienteId), nomeVeic(o.veiculoId), nomeMec(o.mecanicoId), o.status].join(" "),
  linha: o => `<tr><td>#${pad(o.id, 4)}</td><td>${esc(nomeCli(o.clienteId))}</td><td>${esc(nomeVeic(o.veiculoId))}</td>
    <td>${esc(o.mecanicoId ? nomeMec(o.mecanicoId) : "-")}</td><td>${brl(o.valor)}</td>
    <td><span class="badge ${badgeOS(o.status)}">${esc(o.status)}</span></td><td>${fmtData(o.data)}</td>${acoes(o.id, true)}</tr>`,
  aoVer: o => showInfo("OS #" + pad(o.id, 4), [["Cliente", nomeCli(o.clienteId)], ["Veículo", nomeVeic(o.veiculoId)],
    ["Mecânico", o.mecanicoId ? nomeMec(o.mecanicoId) : "-"], ["Status", o.status], ["Valor", brl(o.valor)],
    ["Data", fmtData(o.data)], ["Descrição", o.descricao]]),
  podeExcluir: o => DB.get("pagamentos").some(p => p.ordemId === o.id) ? "existem pagamentos registrados nesta OS." : null
});
el("filtroStatus").addEventListener("change", () => el("busca").dispatchEvent(new Event("input")));
