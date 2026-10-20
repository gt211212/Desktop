setupCrud({
  key: "mecanicos", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Novo Mecânico", tituloEditar: "Editar Mecânico", colunas: 6,
  linha: x => `<tr><td>${pad(x.id)}</td><td>${esc(x.nome)}</td><td>${esc(x.telefone || "-")}</td>
    <td>${esc(x.especialidade || "-")}</td>
    <td><span class="badge ${x.status === "Ativo" ? "ok" : "low"}">${esc(x.status)}</span></td>${acoes(x.id)}</tr>`,
  podeExcluir: x => DB.get("ordens").some(o => o.mecanicoId === x.id) ? "este mecânico está em ordens de serviço." : null
});
