const aviso = document.getElementById("aviso");
const erro = texto => { aviso.textContent = texto; aviso.className = "aviso erro"; aviso.hidden = false; };

document.getElementById("formCadastro").addEventListener("submit", e => {
  e.preventDefault();
  const nome = document.getElementById("nome").value.trim();
  const email = document.getElementById("email").value.trim().toLowerCase();
  const perfil = document.getElementById("perfil").value;
  const senha = document.getElementById("senha").value;
  const usuarios = DB.get("usuarios");

  if (senha.length < 6) return erro("A senha precisa ter pelo menos 6 caracteres.");
  if (senha !== document.getElementById("confirmar").value) return erro("As senhas não conferem.");
  if (usuarios.some(u => u.email.toLowerCase() === email)) return erro("Já existe uma conta com esse e-mail.");

  usuarios.push({ id: DB.nextId(usuarios), nome, email, senha, perfil, ativo: true });
  DB.set("usuarios", usuarios);

  // se um administrador já está logado, volta para as configurações
  location.href = localStorage.getItem("logado") === "1" ? "configuracoes.html" : "login.html?msg=criada";
});