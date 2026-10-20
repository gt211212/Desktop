const baixo = p => p.estoque <= p.estoqueMinimo;

function atualizarFiltroFornecedores() {
  const sel = document.getElementById("filtroFornecedor"), atual = sel.value;
  const nomes = [...new Set(DB.get("fornecedores").map(f => f.empresa).concat(DB.get("pecas").map(p => p.fornecedor)))].filter(Boolean);
  preencherSelect(sel, nomes, n => n, n => n, "Todos os fornecedores");
  sel.value = atual;
}

const estoque = setupCrud({
  key: "pecas", formId: "formCadastro", bodyId: "tabela", modalId: "modalCadastro",
  tituloNovo: "Cadastrar Peça", tituloEditar: "Editar Peça", colunas: 10,
  numeros: ["estoque", "estoqueMinimo", "preco"],
  aoAbrir: item => {
    const nomes = DB.get("fornecedores").map(f => f.empresa);
    if (item && item.fornecedor && !nomes.includes(item.fornecedor)) nomes.push(item.fornecedor);
    preencherSelect(document.getElementById("f_fornecedor"), nomes, n => n, n => n,
      nomes.length ? "Selecione o fornecedor" : "Cadastre um fornecedor primeiro");
  },
  filtro: p => {
    const f = document.getElementById("filtroFornecedor").value, s = document.getElementById("filtroStatus").value;
    return (!f || p.fornecedor === f) && (!s || (s === "baixo") === baixo(p));
  },
  texto: p => [p.nome, p.codigo, p.fornecedor, p.marca].join(" "),
  linha: p => `<tr><td>${esc(p.codigo)}</td><td><strong>${esc(p.nome)}</strong></td><td>${esc(p.marca)}</td>
    <td>${esc(p.fornecedor)}</td><td>${p.estoque}</td><td>${p.estoqueMinimo}</td><td>${brl(p.preco)}</td>
    <td>${esc(p.localizacao)}</td>
    <td><span class="badge ${baixo(p) ? "wait" : "ok"}">${baixo(p) ? "Estoque baixo" : "Estoque OK"}</span></td>${acoes(p.id, true)}</tr>`,
  aoVer: p => showInfo("Detalhes da peça", [["Nome", p.nome], ["Código", p.codigo], ["Marca", p.marca],
    ["Fornecedor", p.fornecedor], ["Estoque", p.estoque], ["Estoque mínimo", p.estoqueMinimo],
    ["Preço", brl(p.preco)], ["Localização", p.localizacao]]),
  aoRenderizar: todas => {
    atualizarFiltroFornecedores();
    document.getElementById("totalPecas").textContent = todas.length;
    document.getElementById("estoqueBaixo").textContent = todas.filter(baixo).length;
    document.getElementById("totalFornecedores").textContent = new Set(todas.map(p => p.fornecedor)).size;
    document.getElementById("valorEstoque").textContent = brl(todas.reduce((t, p) => t + p.estoque * p.preco, 0));
  }
});

["filtroFornecedor", "filtroStatus"].forEach(id => document.getElementById(id).addEventListener("change", estoque.render));

function limparFiltros() {
  document.getElementById("busca").value = "";
  document.getElementById("filtroFornecedor").value = "";
  document.getElementById("filtroStatus").value = "";
  estoque.render();
}

function exportarDados() {
  const blob = new Blob([JSON.stringify(DB.get("pecas"), null, 2)], { type: "application/json" });
  const a = document.createElement("a");
  a.href = URL.createObjectURL(blob); a.download = "estoque-oficina.json"; a.click();
  URL.revokeObjectURL(a.href);
}
