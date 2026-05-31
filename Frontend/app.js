const API = 'http://localhost:8080/api';
const app = document.getElementById('app');
const nav = document.getElementById('nav');
const usuarioInfo = document.getElementById('usuario-info');

// --- Auth helpers ---
function getToken() { return localStorage.getItem('token'); }
function getUser() { return JSON.parse(localStorage.getItem('user') || 'null'); }
function isAdmin() { const u = getUser(); return u && u.role === 'ADMIN'; }

function salvarAuth(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify({ nome: data.nome, role: data.role }));
}

function logout() {
    localStorage.clear();
    nav.style.display = 'none';
    renderLogin();
}

async function request(url, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    const token = getToken();
    if (token) headers['Authorization'] = 'Bearer ' + token;
    const res = await fetch(API + url, { ...options, headers });
    if (res.status === 204) return null;
    const body = await res.json().catch(() => null);
    if (!res.ok) throw { status: res.status, ...(body || { erro: 'Erro desconhecido' }) };
    return body;
}

// --- Navegação SPA ---
function navegarPara(pagina) {
    if (pagina === 'livros') renderLivros();
    else if (pagina === 'emprestimos') renderEmprestimos();
}

function atualizarNav() {
    const user = getUser();
    if (user) {
        nav.style.display = 'flex';
        usuarioInfo.textContent = `${user.nome} (${user.role})`;
    } else {
        nav.style.display = 'none';
    }
}

// --- Tela Login/Registro ---
function renderLogin() {
    atualizarNav();
    app.innerHTML = `
        <div class="card">
            <div class="tabs">
                <button class="tab active" id="tab-login" onclick="mostrarTab('login')">Login</button>
                <button class="tab" id="tab-registro" onclick="mostrarTab('registro')">Registrar</button>
            </div>
            <div id="msg"></div>
            <form id="form-login" onsubmit="fazerLogin(event)">
                <input type="email" id="login-email" placeholder="Email" required>
                <input type="password" id="login-senha" placeholder="Senha" required>
                <button type="submit" class="btn btn-primary">Entrar</button>
            </form>
            <form id="form-registro" style="display:none" onsubmit="fazerRegistro(event)">
                <input type="text" id="reg-nome" placeholder="Nome" required minlength="3">
                <input type="email" id="reg-email" placeholder="Email" required>
                <input type="password" id="reg-senha" placeholder="Senha (min 6)" required minlength="6">
                <select id="reg-role">
                    <option value="USUARIO">Usuário</option>
                    <option value="ADMIN">Admin</option>
                </select>
                <button type="submit" class="btn btn-primary">Registrar</button>
            </form>
        </div>`;
}

function mostrarTab(tab) {
    document.getElementById('form-login').style.display = tab === 'login' ? 'flex' : 'none';
    document.getElementById('form-registro').style.display = tab === 'registro' ? 'flex' : 'none';
    document.getElementById('tab-login').className = 'tab' + (tab === 'login' ? ' active' : '');
    document.getElementById('tab-registro').className = 'tab' + (tab === 'registro' ? ' active' : '');
}

async function fazerLogin(e) {
    e.preventDefault();
    try {
        const data = await request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email: document.getElementById('login-email').value, senha: document.getElementById('login-senha').value })
        });
        salvarAuth(data);
        atualizarNav();
        renderLivros();
    } catch (err) { mostrarMsg(err.erro || 'Erro ao fazer login', true); }
}

async function fazerRegistro(e) {
    e.preventDefault();
    try {
        const data = await request('/auth/registrar', {
            method: 'POST',
            body: JSON.stringify({ nome: document.getElementById('reg-nome').value, email: document.getElementById('reg-email').value, senha: document.getElementById('reg-senha').value, role: document.getElementById('reg-role').value })
        });
        salvarAuth(data);
        atualizarNav();
        renderLivros();
    } catch (err) { mostrarMsg(err.erro || 'Erro ao registrar', true); }
}

function mostrarMsg(texto, erro = false) {
    const el = document.getElementById('msg');
    if (el) { el.className = 'msg ' + (erro ? 'msg-erro' : 'msg-sucesso'); el.textContent = texto; }
}

// --- Tela Livros ---
async function renderLivros() {
    try {
        const livros = await request('/livros');
        let html = `<div class="card"><h2>Livros</h2><div id="msg"></div>`;
        if (isAdmin()) {
            html += `<button class="btn btn-primary" onclick="renderFormLivro()" style="margin-bottom:1rem">+ Novo Livro</button>`;
        }
        html += `<table><thead><tr><th>Título</th><th>Autor</th><th>Gênero</th><th>Qtd</th><th>Ações</th></tr></thead><tbody>`;
        livros.forEach(l => {
            html += `<tr><td>${l.titulo}</td><td>${l.autor}</td><td>${l.genero || '-'}</td><td>${l.quantidade}</td><td class="actions">`;
            if (getToken() && l.quantidade > 0) html += `<button class="btn btn-success" onclick="emprestar('${l.id}')">Emprestar</button>`;
            if (isAdmin()) {
                html += `<button class="btn btn-primary" onclick="renderFormLivro('${l.id}')">Editar</button>`;
                html += `<button class="btn btn-danger" onclick="deletarLivro('${l.id}')">Excluir</button>`;
            }
            html += `</td></tr>`;
        });
        html += `</tbody></table></div>`;
        app.innerHTML = html;
    } catch (err) { app.innerHTML = `<div class="card msg msg-erro">${err.erro || 'Erro ao carregar livros'}</div>`; }
}

async function renderFormLivro(id) {
    let livro = { titulo: '', autor: '', isbn: '', editora: '', ano: '', genero: '', quantidade: 0 };
    if (id) {
        try { livro = await request('/livros/' + id); } catch (e) {}
    }
    app.innerHTML = `
        <div class="card">
            <h2>${id ? 'Editar' : 'Novo'} Livro</h2>
            <div id="msg"></div>
            <form onsubmit="salvarLivro(event, '${id || ''}')">
                <input id="f-titulo" value="${livro.titulo}" placeholder="Título" required>
                <input id="f-autor" value="${livro.autor}" placeholder="Autor" required>
                <input id="f-isbn" value="${livro.isbn}" placeholder="ISBN" required>
                <input id="f-editora" value="${livro.editora || ''}" placeholder="Editora">
                <input id="f-ano" type="number" value="${livro.ano || ''}" placeholder="Ano">
                <input id="f-genero" value="${livro.genero || ''}" placeholder="Gênero">
                <input id="f-quantidade" type="number" value="${livro.quantidade}" placeholder="Quantidade" min="0" required>
                <div class="actions">
                    <button type="submit" class="btn btn-primary">Salvar</button>
                    <button type="button" class="btn" onclick="renderLivros()">Cancelar</button>
                </div>
            </form>
        </div>`;
}

async function salvarLivro(e, id) {
    e.preventDefault();
    const body = {
        titulo: document.getElementById('f-titulo').value,
        autor: document.getElementById('f-autor').value,
        isbn: document.getElementById('f-isbn').value,
        editora: document.getElementById('f-editora').value,
        ano: parseInt(document.getElementById('f-ano').value) || 0,
        genero: document.getElementById('f-genero').value,
        quantidade: parseInt(document.getElementById('f-quantidade').value)
    };
    try {
        await request('/livros' + (id ? '/' + id : ''), { method: id ? 'PUT' : 'POST', body: JSON.stringify(body) });
        renderLivros();
    } catch (err) { mostrarMsg(err.erro || 'Erro ao salvar', true); }
}

async function deletarLivro(id) {
    if (!confirm('Excluir este livro?')) return;
    try { await request('/livros/' + id, { method: 'DELETE' }); renderLivros(); }
    catch (err) { mostrarMsg(err.erro || 'Erro ao excluir', true); }
}

// --- Empréstimos ---
async function emprestar(livroId) {
    const data = new Date();
    data.setDate(data.getDate() + 14);
    const dataDev = data.toISOString().split('T')[0];
    try {
        await request('/emprestimos', { method: 'POST', body: JSON.stringify({ livroId, dataDevolucaoPrevista: dataDev }) });
        renderLivros();
    } catch (err) { alert(err.erro || 'Erro ao emprestar'); }
}

async function renderEmprestimos() {
    try {
        const lista = await request('/emprestimos');
        let html = `<div class="card"><h2>Meus Empréstimos</h2><table><thead><tr><th>Livro ID</th><th>Data</th><th>Devolução Prevista</th><th>Status</th><th>Ações</th></tr></thead><tbody>`;
        lista.forEach(e => {
            html += `<tr><td>${e.livroId}</td><td>${e.dataEmprestimo}</td><td>${e.dataDevolucaoPrevista}</td><td>${e.status}</td><td class="actions">`;
            if (e.status === 'ATIVO') html += `<button class="btn btn-success" onclick="devolver('${e.id}')">Devolver</button>`;
            if (isAdmin()) html += `<button class="btn btn-danger" onclick="deletarEmprestimo('${e.id}')">Excluir</button>`;
            html += `</td></tr>`;
        });
        html += `</tbody></table></div>`;
        app.innerHTML = html;
    } catch (err) { app.innerHTML = `<div class="card msg msg-erro">${err.erro || 'Erro ao carregar empréstimos'}</div>`; }
}

async function devolver(id) {
    try { await request('/emprestimos/' + id + '/devolver', { method: 'PATCH' }); renderEmprestimos(); }
    catch (err) { alert(err.erro || 'Erro ao devolver'); }
}

async function deletarEmprestimo(id) {
    if (!confirm('Excluir este empréstimo?')) return;
    try { await request('/emprestimos/' + id, { method: 'DELETE' }); renderEmprestimos(); }
    catch (err) { alert(err.erro || 'Erro ao excluir'); }
}

// --- Init ---
if (getToken()) { atualizarNav(); renderLivros(); }
else { renderLogin(); }
