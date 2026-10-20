setupCrud({
  key: "fornecedores", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Novo Fornecedor", tituloEditar: "Editar Fornecedor", colunas: 6,
  linha: x => `<tr><td>${pad(x.id)}</td><td>${esc(x.empresa)}</td><td>${esc(x.cnpj || "-")}</td>
    <td>${esc(x.telefone || "-")}</td><td>${esc(x.email || "-")}</td>${acoes(x.id)}</tr>`,
  // se o nome da empresa mudar, atualiza as peças que usam esse fornecedor
  aoSalvar: (novo, antigo) => {
    if (!antigo || antigo.empresa === novo.empresa) return;
    DB.set("pecas", DB.get("pecas").map(p => p.fornecedor === antigo.empresa ? Object.assign(p, { fornecedor: novo.empresa }) : p));
  },
  podeExcluir: x => DB.get("pecas").some(p => p.fornecedor === x.empresa) ? "existem peças vinculadas a este fornecedor." : null
});
