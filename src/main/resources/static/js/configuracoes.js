const el = id => document.getElementById(id);
const cfg = () => DB.get("config", {});

// abas
document.querySelectorAll(".settings-menu button").forEach(b => b.addEventListener("click", () => {
  document.querySelectorAll(".settings-menu button").forEach(x => x.classList.toggle("active", x === b));
  document.querySelectorAll(".painel-config").forEach(p => p.classList.toggle("active", p.id === b.dataset.tab));
}));

// dados da empresa
const empresa = Object.assign({ nome: "Oficina do Breno", cnpj: "12.345.678/0001-90", telefone: "(61) 3333-4444", email: "contato@oficina.com", endereco: "Rua Exemplo, 123 - Santa Maria/DF" }, cfg().empresa);
Array.from(el("formEmpresa").elements).forEach(i => { if (i.name) i.value = empresa[i.name] || ""; });
el("formEmpresa").addEventListener("submit", e => {
  e.preventDefault();
  const dados = {};
  Array.from(e.target.elements).forEach(i => { if (i.name) dados[i.name] = i.value.trim(); });
  DB.set("config", Object.assign(cfg(), { empresa: dados }));
  toast("Dados da empresa salvos.");
});

// usuário
el("nomeUsuario").value = cfg().usuario || "Bruno Honorato";
el("formUsuario").addEventListener("submit", e => {
  e.preventDefault();
  DB.set("config", Object.assign(cfg(), { usuario: el("nomeUsuario").value.trim() || "Bruno Honorato" }));
  document.querySelectorAll("[data-user]").forEach(u => u.textContent = cfg().usuario);
  toast("Nome salvo.");
});

// backup
function exportarTudo() {
  const dados = {};
  CHAVES.concat("config").forEach(k => dados[k] = DB.get(k, k === "config" ? {} : []));
  const a = document.createElement("a");
  a.href = URL.createObjectURL(new Blob([JSON.stringify(dados, null, 2)], { type: "application/json" }));
  a.download = "backup-oficina-" + isoLocal() + ".json"; a.click();
  URL.revokeObjectURL(a.href);
}

el("arquivoBackup").addEventListener("change", e => {
  const arq = e.target.files[0];
  if (!arq) return;
  const leitor = new FileReader();
  leitor.onload = () => {
    try {
      const dados = JSON.parse(leitor.result);
      if (!confirm("Importar este backup vai substituir os dados atuais. Continuar?")) return;
      CHAVES.concat("config").forEach(k => { if (dados[k] !== undefined) DB.set(k, dados[k]); });
      localStorage.setItem("seedOk", "1");
      alert("Backup importado com sucesso.");
      location.reload();
    } catch (err) { alert("Arquivo inválido. Use um backup gerado por este sistema."); }
  };
  leitor.readAsText(arq);
});

function limparTudo() {
  if (!confirm("Isso apaga TODOS os dados do sistema (peças, clientes, OS etc.). Continuar?")) return;
  CHAVES.forEach(k => DB.set(k, []));
  localStorage.setItem("seedOk", "1");
  alert("Dados apagados.");
  location.reload();
}
