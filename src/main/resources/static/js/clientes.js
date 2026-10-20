setupCrud({
  key: "clientes", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Novo Cliente", tituloEditar: "Editar Cliente", colunas: 6,
  linha: x => `<tr><td>${pad(x.id)}</td><td>${esc(x.nome)}</td><td>${esc(x.telefone)}</td>
    <td>${esc(x.documento || "-")}</td><td>${esc(x.email || "-")}</td>${acoes(x.id)}</tr>`,
  podeExcluir: x => {
    if (DB.get("veiculos").some(v => v.clienteId === x.id)) return "este cliente tem veículos cadastrados.";
    if (DB.get("ordens").some(o => o.clienteId === x.id)) return "este cliente tem ordens de serviço.";
  }
});
