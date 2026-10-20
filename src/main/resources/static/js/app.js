/* =====================================================
   app.js – funções compartilhadas por todas as páginas
   ===================================================== */

const CHAVES = ["pecas", "fornecedores", "clientes", "veiculos", "mecanicos", "ordens", "pagamentos"];

/* ---------- Login: bloqueia páginas internas ---------- */
(function () {
  const pagina = location.pathname.split("/").pop() || "index.html";
  if (pagina !== "login.html" && localStorage.getItem("logado") !== "1") {
    location.href = "login.html";
  }
})();

/* ---------- Banco de dados (localStorage) ---------- */
const DB = {
  get(chave, padrao = []) {
    try { const v = JSON.parse(localStorage.getItem(chave)); return v ?? padrao; }
    catch (e) { return padrao; }
  },
  set(chave, valor) { localStorage.setItem(chave, JSON.stringify(valor)); },
  nextId(lista) { return lista.reduce((m, x) => Math.max(m, Number(x.id) || 0), 0) + 1; }
};

/* ---------- Utilitários ---------- */
const esc = s => String(s ?? "").replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
const brl = n => Number(n || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
const pad = (n, t = 3) => String(n).padStart(t, "0");
const isoLocal = (d = new Date()) => d.getFullYear() + "-" + pad(d.getMonth() + 1, 2) + "-" + pad(d.getDate(), 2);
const fmtData = iso => iso ? iso.split("-").reverse().join("/") : "-";
const achar = (chave, id) => DB.get(chave).find(x => x.id === Number(id));

function toast(msg) {
  const t = document.createElement("div");
  t.className = "toast"; t.textContent = msg;
  document.body.appendChild(t);
  setTimeout(() => t.remove(), 2600);
}

function openModal(id) { document.getElementById(id).classList.add("active"); }
function closeModal(id) { document.getElementById(id).classList.remove("active"); }

function showInfo(titulo, linhas) {
  const m = document.createElement("div");
  m.className = "modal active";
  m.innerHTML = `<div class="modal-box"><div class="modal-head"><h2>${esc(titulo)}</h2>
    <button class="btn danger small" type="button">✕</button></div>
    <dl class="info-list">${linhas.map(l => `<dt>${esc(l[0])}</dt><dd>${esc(l[1])}</dd>`).join("")}</dl></div>`;
  m.querySelector("button").onclick = () => m.remove();
  m.addEventListener("click", e => { if (e.target === m) m.remove(); });
  document.body.appendChild(m);
}

function preencherSelect(sel, itens, valor, texto, placeholder) {
  sel.innerHTML = (placeholder ? `<option value="">${esc(placeholder)}</option>` : "") +
    itens.map(i => `<option value="${esc(valor(i))}">${esc(texto(i))}</option>`).join("");
}

function acoes(id, comVer) {
  return `<td class="actions">${comVer ? `<button class="btn small" data-act="ver" data-id="${id}">Ver</button>` : ""}
    <button class="btn small secondary" data-act="editar" data-id="${id}">Editar</button>
    <button class="btn small danger" data-act="excluir" data-id="${id}">Excluir</button></td>`;
}

function desenharBarras(el, itens) {
  const max = Math.max(0, ...itens.map(i => i.valor));
  if (!itens.length || max === 0) { el.innerHTML = '<div class="sem-dados">Sem dados para mostrar.</div>'; return; }
  el.innerHTML = '<div class="bars">' + itens.map(i => `
    <div class="bar-col"><div class="bar-area"><div style="display:flex;flex-direction:column;align-items:center;justify-content:flex-end;height:100%;width:100%">
      <span class="bar-val">${esc(i.texto ?? i.valor)}</span>
      <div class="bar" style="height:${Math.max(3, (i.valor / max) * 85)}%"></div></div></div>
    <div class="bar-label" title="${esc(i.label)}">${esc(i.label)}</div></div>`).join("") + "</div>";
}

/* ---------- Dados de exemplo (só na primeira vez) ---------- */
(function seed() {
  if (localStorage.getItem("seedOk")) return;
  if (!localStorage.getItem("pecas")) DB.set("pecas", [
    { id: 1, nome: "Filtro de óleo", codigo: "789001", marca: "Mann-Filter", fornecedor: "Mann-Filter", estoque: 3, estoqueMinimo: 10, preco: 35.9, localizacao: "A-02" },
    { id: 2, nome: "Pastilha de freio", codigo: "789002", marca: "Bosch", fornecedor: "Bosch", estoque: 12, estoqueMinimo: 8, preco: 120, localizacao: "B-01" },
    { id: 3, nome: "Bateria", codigo: "789003", marca: "Heliar", fornecedor: "Heliar", estoque: 5, estoqueMinimo: 10, preco: 450, localizacao: "C-03" },
    { id: 4, nome: "Correia dentada", codigo: "789004", marca: "Dayco", fornecedor: "Dayco", estoque: 8, estoqueMinimo: 5, preco: 89.9, localizacao: "D-02" }
  ]);
  if (!localStorage.getItem("fornecedores")) DB.set("fornecedores",
    ["Mann-Filter", "Bosch", "Heliar", "Dayco"].map((e, i) => ({ id: i + 1, empresa: e, cnpj: "", telefone: "", email: "" })));
  localStorage.setItem("seedOk", "1");
})();

/* ---------- CRUD genérico (cadastrar, editar, excluir, buscar) ---------- */
function setupCrud(o) {
  const form = document.getElementById(o.formId);
  const corpo = document.getElementById(o.bodyId);
  const modal = document.getElementById(o.modalId);
  const busca = document.getElementById("busca");
  const lista = () => DB.get(o.key);

  function render() {
    const q = (busca ? busca.value : "").toLowerCase();
    let itens = lista();
    if (o.filtro) itens = itens.filter(o.filtro);
    if (q) itens = itens.filter(x => (o.texto ? o.texto(x) : Object.values(x).join(" ")).toLowerCase().includes(q));
    corpo.innerHTML = itens.length ? itens.map(o.linha).join("")
      : `<tr><td colspan="${o.colunas}" class="empty">Nenhum registro encontrado.</td></tr>`;
    if (o.aoRenderizar) o.aoRenderizar(lista());
  }

  function abrir(item) {
    form.reset();
    if (o.aoAbrir) o.aoAbrir(item);
    form.elements._id.value = item ? item.id : "";
    if (item) {
      Array.from(form.elements).forEach(el => { if (el.name && el.name !== "_id") el.value = item[el.name] ?? ""; });
    }
    modal.querySelector("h2").textContent = item ? o.tituloEditar : o.tituloNovo;
    openModal(o.modalId);
  }

  form.addEventListener("submit", e => {
    e.preventDefault();
    const itens = lista();
    const idAtual = form.elements._id.value;
    const dados = {};
    Array.from(form.elements).forEach(el => {
      if (!el.name || el.name === "_id") return;
      let v = el.value.trim();
      if ((o.numeros || []).includes(el.name)) v = v === "" ? "" : Number(v);
      dados[el.name] = v;
    });
    const erro = o.validar ? o.validar(dados) : null;
    if (erro) { alert(erro); return; }
    let antigo = null;
    if (idAtual) {
      const i = itens.findIndex(x => x.id === Number(idAtual));
      antigo = itens[i];
      itens[i] = Object.assign({}, antigo, dados);
    } else {
      itens.push(Object.assign({ id: DB.nextId(itens) }, o.padrao ? o.padrao() : {}, dados));
    }
    DB.set(o.key, itens);
    if (o.aoSalvar) o.aoSalvar(dados, antigo);
    closeModal(o.modalId);
    render();
    toast(idAtual ? "Registro atualizado." : "Registro cadastrado.");
  });

  corpo.addEventListener("click", e => {
    const b = e.target.closest("[data-act]");
    if (!b) return;
    const item = lista().find(x => x.id === Number(b.dataset.id));
    if (!item) return;
    if (b.dataset.act === "editar") abrir(item);
    if (b.dataset.act === "ver" && o.aoVer) o.aoVer(item);
    if (b.dataset.act === "excluir") {
      const bloqueio = o.podeExcluir ? o.podeExcluir(item) : null;
      if (bloqueio) { alert("Não é possível excluir: " + bloqueio); return; }
      if (!confirm("Deseja realmente excluir este registro?")) return;
      DB.set(o.key, lista().filter(x => x.id !== item.id));
      render();
      toast("Registro excluído.");
    }
  });

  document.querySelectorAll("[data-novo]").forEach(b => b.addEventListener("click", () => abrir(null)));
  if (busca) busca.addEventListener("input", render);
  render();
  return { render, abrir };
}

/* ---------- Topbar: nome do usuário, busca e sair ---------- */
document.addEventListener("DOMContentLoaded", () => {
  const cfg = DB.get("config", {});
  document.querySelectorAll("[data-user]").forEach(el => el.textContent = cfg.usuario || "Bruno Honorato");

  const sair = document.getElementById("sair");
  if (sair) sair.addEventListener("click", () => localStorage.removeItem("logado"));

  const global = document.getElementById("buscaGlobal"), pagina = document.getElementById("busca");
  if (global) {
    if (!pagina) global.style.visibility = "hidden";
    else global.addEventListener("input", () => { pagina.value = global.value; pagina.dispatchEvent(new Event("input")); });
  }

  document.addEventListener("click", e => { if (e.target.classList.contains("modal")) e.target.classList.remove("active"); });
  document.addEventListener("keydown", e => {
    if (e.key === "Escape") document.querySelectorAll(".modal.active").forEach(m => m.classList.remove("active"));
  });
});
