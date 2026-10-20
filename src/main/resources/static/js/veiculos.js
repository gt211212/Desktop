const nomeCliente = id => (achar("clientes", id) || {}).nome || "—";
setupCrud({
  key: "veiculos", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Novo Veículo", tituloEditar: "Editar Veículo", colunas: 7,
  numeros: ["clienteId", "ano"],
  aoAbrir: () => preencherSelect(document.getElementById("f_clienteId"), DB.get("clientes"),
    c => c.id, c => c.nome, DB.get("clientes").length ? "Selecione o cliente" : "Cadastre um cliente primeiro"),
  texto: x => [nomeCliente(x.clienteId), x.marca, x.modelo, x.placa, x.ano].join(" "),
  linha: x => `<tr><td>${pad(x.id)}</td><td>${esc(nomeCliente(x.clienteId))}</td><td>${esc(x.marca)}</td>
    <td>${esc(x.modelo)}</td><td>${esc(x.ano || "-")}</td><td>${esc(x.placa || "-")}</td>${acoes(x.id)}</tr>`,
  podeExcluir: x => DB.get("ordens").some(o => o.veiculoId === x.id) ? "este veículo está em ordens de serviço." : null
});
