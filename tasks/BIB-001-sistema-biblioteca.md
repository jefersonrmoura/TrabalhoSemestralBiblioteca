# BIB-001: Sistema de Gerenciamento de Biblioteca

**Tipo**: Épico
**Prioridade**: Alta
**Status**: Backlog
**Criado em**: 2026-05-30

---

## Visão Geral

Desenvolver uma aplicação web completa de gerenciamento de biblioteca com duas entidades principais (Livro e Empréstimo), autenticação JWT, autorização RBAC, documentação Swagger, testes unitários, princípios SOLID e frontend SPA. O sistema permite cadastrar livros, gerenciar empréstimos e devoluções, com controle de acesso por perfil (admin/usuario).

---

## Decisões Técnicas

| Decisão | Escolha | Justificativa |
|---------|---------|---------------|
| Backend Framework | Spring Boot 4.0.6 + Java 21 | Já inicializado no projeto, robusto para APIs REST |
| Banco de Dados | MongoDB | Requisito do trabalho (NoSQL), flexível para documentos |
| Autenticação | JWT com Spring Security | Padrão da indústria, stateless, bônus A do trabalho |
| Documentação API | SpringDoc OpenAPI (Swagger UI) | Integração nativa com Spring Boot |
| Frontend | HTML + CSS + JS puro (SPA) | Requisito do trabalho, navegação assíncrona com fetch |
| Containerização | Docker + docker-compose | MongoDB + Backend + Frontend, cada um com Dockerfile |
| Testes | JUnit 5 + Mockito | Padrão Spring Boot, bônus C |
| Arquitetura | Controller → Service → Repository | Separação de responsabilidades, facilita SOLID |

---

## User Stories

### US-001: CRUD de Livros

- **Summary:** Gerenciamento completo de livros no acervo da biblioteca

#### Use Case:
- **As a** bibliotecário (admin)
- **I want to** cadastrar, listar, buscar, editar e remover livros
- **so that** o acervo da biblioteca esteja sempre atualizado

#### Acceptance Criteria:

- **Scenario 1:** Cadastrar livro com dados válidos
  - **Given:** o usuário está autenticado como admin
  - **When:** envia POST /api/livros com título, autor, isbn, editora, ano, genero e quantidade
  - **Then:** retorna 201 com o livro criado incluindo id gerado

- **Scenario 2:** Listar todos os livros
  - **Given:** existem livros cadastrados
  - **When:** envia GET /api/livros
  - **Then:** retorna 200 com a lista de livros

- **Scenario 3:** Buscar livro por ID
  - **Given:** existe um livro com id válido
  - **When:** envia GET /api/livros/{id}
  - **Then:** retorna 200 com os dados do livro

- **Scenario 4:** Buscar livro inexistente
  - **Given:** o id informado não existe
  - **When:** envia GET /api/livros/{id}
  - **Then:** retorna 404

- **Scenario 5:** Atualizar livro
  - **Given:** o usuário está autenticado como admin
  - **When:** envia PUT /api/livros/{id} com dados atualizados
  - **Then:** retorna 200 com o livro atualizado

- **Scenario 6:** Deletar livro
  - **Given:** o usuário está autenticado como admin
  - **When:** envia DELETE /api/livros/{id}
  - **Then:** retorna 204

#### Notas Técnicas:
- Model: `Livro.java` com campos id, titulo, autor, isbn, editora, ano, genero, quantidade
- Repository: `LivroRepository.java` extends MongoRepository
- Service: `LivroService.java` com interface + implementação
- Controller: `LivroController.java` com anotações Swagger
- DTO: `LivroRequest.java`, `LivroResponse.java`

---

### US-002: CRUD de Empréstimos

- **Summary:** Gerenciamento de empréstimos e devoluções de livros

#### Use Case:
- **As a** usuário da biblioteca
- **I want to** registrar empréstimos e devoluções de livros
- **so that** haja controle de quem pegou qual livro e quando deve devolver

#### Acceptance Criteria:

- **Scenario 1:** Registrar empréstimo
  - **Given:** o usuário está autenticado
  - **and Given:** o livro tem quantidade disponível > 0
  - **When:** envia POST /api/emprestimos com livroId e dataDevolucaoPrevista
  - **Then:** retorna 201 com o empréstimo criado e decrementa quantidade do livro

- **Scenario 2:** Registrar empréstimo sem estoque
  - **Given:** o livro tem quantidade disponível = 0
  - **When:** envia POST /api/emprestimos com livroId
  - **Then:** retorna 400 com mensagem "Livro não disponível"

- **Scenario 3:** Listar empréstimos
  - **Given:** o usuário é admin
  - **When:** envia GET /api/emprestimos
  - **Then:** retorna 200 com todos os empréstimos

- **Scenario 4:** Listar meus empréstimos
  - **Given:** o usuário está autenticado (role usuario)
  - **When:** envia GET /api/emprestimos
  - **Then:** retorna 200 apenas com seus próprios empréstimos

- **Scenario 5:** Devolver livro
  - **Given:** existe um empréstimo ativo
  - **When:** envia PATCH /api/emprestimos/{id}/devolver
  - **Then:** retorna 200, marca status como DEVOLVIDO e incrementa quantidade do livro

- **Scenario 6:** Deletar empréstimo
  - **Given:** o usuário é admin
  - **When:** envia DELETE /api/emprestimos/{id}
  - **Then:** retorna 204

#### Notas Técnicas:
- Model: `Emprestimo.java` com campos id, livroId, usuarioId, nomeUsuario, dataEmprestimo, dataDevolucaoPrevista, dataDevolucao, status (ATIVO/DEVOLVIDO)
- Repository: `EmprestimoRepository.java` extends MongoRepository
- Service: `EmprestimoService.java` com interface + implementação
- Controller: `EmprestimoController.java`
- DTO: `EmprestimoRequest.java`, `EmprestimoResponse.java`

---

### US-003: Autenticação JWT ✅ DONE

- **Summary:** Registro e login de usuários com token JWT

#### Use Case:
- **As a** visitante
- **I want to** me registrar e fazer login
- **so that** eu possa acessar as funcionalidades protegidas do sistema

#### Acceptance Criteria:

- **Scenario 1:** Registrar novo usuário
  - **Given:** o email não está cadastrado
  - **When:** envia POST /api/auth/registrar com nome, email e senha
  - **Then:** retorna 201 com dados do usuário (sem senha)

- **Scenario 2:** Registrar com email duplicado
  - **Given:** o email já está cadastrado
  - **When:** envia POST /api/auth/registrar
  - **Then:** retorna 409 com mensagem "Email já cadastrado"

- **Scenario 3:** Login com credenciais válidas
  - **Given:** o usuário existe
  - **When:** envia POST /api/auth/login com email e senha corretos
  - **Then:** retorna 200 com token JWT válido

- **Scenario 4:** Login com credenciais inválidas
  - **Given:** email ou senha incorretos
  - **When:** envia POST /api/auth/login
  - **Then:** retorna 401 com mensagem "Credenciais inválidas"

- **Scenario 5:** Acessar endpoint protegido sem token
  - **Given:** nenhum token no header Authorization
  - **When:** envia requisição a endpoint protegido
  - **Then:** retorna 401

- **Scenario 6:** Token expirado
  - **Given:** o token JWT expirou
  - **When:** envia requisição com token expirado
  - **Then:** retorna 401

#### Notas Técnicas:
- Model: `Usuario.java` com campos id, nome, email, senha (hash), role
- Repository: `UsuarioRepository.java`
- Service: `AuthService.java`, `JwtService.java`
- Controller: `AuthController.java`
- Config: `SecurityConfig.java`, `JwtAuthFilter.java`
- DTO: `RegistroRequest.java`, `LoginRequest.java`, `AuthResponse.java`
- Dependências: spring-boot-starter-security, jjwt


### US-004: Autorização RBAC

- **Summary:** Controle de acesso baseado em perfis (admin e usuario)

#### Use Case:
- **As a** administrador do sistema
- **I want to** restringir ações sensíveis apenas a admins
- **so that** usuários comuns não possam alterar dados críticos

#### Acceptance Criteria:

- **Scenario 1:** Admin acessa endpoint restrito
  - **Given:** o usuário autenticado tem role ADMIN
  - **When:** envia requisição a endpoint restrito (DELETE, PUT em livros)
  - **Then:** a requisição é processada normalmente

- **Scenario 2:** Usuário comum acessa endpoint restrito
  - **Given:** o usuário autenticado tem role USUARIO
  - **When:** envia DELETE /api/livros/{id}
  - **Then:** retorna 403 Forbidden

- **Scenario 3:** Role presente no JWT
  - **Given:** o usuário faz login
  - **When:** o token é gerado
  - **Then:** o payload do JWT contém o campo "role"

#### Notas Técnicas:
- Enum: `Role.java` (ADMIN, USUARIO)
- Anotação `@PreAuthorize` nos controllers para endpoints restritos
- Primeiro usuário registrado é ADMIN, demais são USUARIO
- SecurityConfig com authorizeHttpRequests por role

---

### US-005: Documentação Swagger/OpenAPI

- **Summary:** Documentação interativa de todos os endpoints da API

#### Use Case:
- **As a** desenvolvedor ou avaliador
- **I want to** acessar a documentação Swagger da API
- **so that** eu possa entender e testar os endpoints disponíveis

#### Acceptance Criteria:

- **Scenario 1:** Acessar Swagger UI
  - **Given:** a aplicação está rodando
  - **When:** acessa http://localhost:8080/swagger-ui.html
  - **Then:** exibe a interface Swagger com todos os endpoints

- **Scenario 2:** Endpoints documentados
  - **Given:** a documentação está acessível
  - **When:** visualiza qualquer endpoint
  - **Then:** mostra descrição, parâmetros, body de request e exemplos de response

- **Scenario 3:** Autenticação no Swagger
  - **Given:** o Swagger UI está aberto
  - **When:** clica em "Authorize"
  - **Then:** permite inserir Bearer token para testar endpoints protegidos

#### Notas Técnicas:
- Dependência: springdoc-openapi-starter-webmvc-ui
- Config: `SwaggerConfig.java` com SecurityScheme para JWT
- Anotações `@Operation`, `@ApiResponse` nos controllers
- `@Schema` nos DTOs

---

### US-006: Testes Unitários

- **Summary:** Testes da camada de serviço com cenários de sucesso e erro

#### Use Case:
- **As a** desenvolvedor
- **I want to** ter testes unitários na camada de serviço
- **so that** eu possa garantir que as regras de negócio funcionam corretamente

#### Acceptance Criteria:

- **Scenario 1:** Teste de criação de livro com dados válidos
  - **Given:** dados válidos de um livro
  - **When:** chama livroService.criar()
  - **Then:** retorna o livro salvo com id gerado

- **Scenario 2:** Teste de busca de livro inexistente
  - **Given:** id que não existe no banco
  - **When:** chama livroService.buscarPorId()
  - **Then:** lança exceção ResourceNotFoundException

- **Scenario 3:** Teste de empréstimo com livro disponível
  - **Given:** livro com quantidade > 0
  - **When:** chama emprestimoService.criar()
  - **Then:** cria empréstimo e decrementa quantidade

- **Scenario 4:** Teste de empréstimo sem estoque
  - **Given:** livro com quantidade = 0
  - **When:** chama emprestimoService.criar()
  - **Then:** lança exceção BusinessException

#### Notas Técnicas:
- Framework: JUnit 5 + Mockito (já incluído no starter-test)
- Arquivos: `LivroServiceTest.java`, `EmprestimoServiceTest.java`
- Mock dos repositories com `@Mock` e `@InjectMocks`
- Executar com: `./mvnw test`

---

### US-007: Princípios SOLID

- **Summary:** Aplicação de pelo menos 3 princípios SOLID no backend

#### Use Case:
- **As a** avaliador
- **I want to** ver princípios SOLID aplicados no código
- **so that** a qualidade arquitetural do projeto seja demonstrada

#### Acceptance Criteria:

- **Scenario 1:** Single Responsibility Principle (SRP)
  - **Given:** a arquitetura do projeto
  - **When:** analisa as classes
  - **Then:** cada classe tem uma única responsabilidade (Controller só roteia, Service só tem lógica, Repository só persiste)

- **Scenario 2:** Open/Closed Principle (OCP)
  - **Given:** a camada de serviço
  - **When:** precisa adicionar nova validação
  - **Then:** pode estender sem modificar código existente (via interfaces)

- **Scenario 3:** Dependency Inversion Principle (DIP)
  - **Given:** os controllers e services
  - **When:** analisa as dependências
  - **Then:** dependem de abstrações (interfaces) e não de implementações concretas

- **Scenario 4:** Interface Segregation Principle (ISP)
  - **Given:** as interfaces de serviço
  - **When:** analisa os métodos
  - **Then:** interfaces são específicas e coesas (não forçam implementação de métodos desnecessários)

#### Notas Técnicas:
- Criar interfaces: `LivroService.java`, `EmprestimoService.java`, `AuthService.java`
- Implementações: `LivroServiceImpl.java`, `EmprestimoServiceImpl.java`, `AuthServiceImpl.java`
- Criar arquivo `SOLID.md` na raiz explicando cada princípio aplicado
- Injeção via construtor (não @Autowired em campo)

---

### US-008: Frontend SPA

- **Summary:** Página web com navegação assíncrona consumindo a API

#### Use Case:
- **As a** usuário da biblioteca
- **I want to** acessar uma interface web para gerenciar livros e empréstimos
- **so that** eu possa usar o sistema sem precisar de ferramentas técnicas

#### Acceptance Criteria:

- **Scenario 1:** Tela de Login
  - **Given:** o usuário acessa a aplicação
  - **When:** a página carrega
  - **Then:** exibe formulário de login com opção de registro

- **Scenario 2:** Navegação sem reload
  - **Given:** o usuário está logado
  - **When:** navega entre seções (Livros, Empréstimos)
  - **Then:** o conteúdo muda sem recarregar a página

- **Scenario 3:** Listagem de livros
  - **Given:** o usuário está na seção Livros
  - **When:** a seção carrega
  - **Then:** exibe lista de livros via fetch à API

- **Scenario 4:** Cadastro de livro (admin)
  - **Given:** o usuário é admin
  - **When:** preenche formulário e submete
  - **Then:** livro é criado via POST assíncrono e lista atualiza

- **Scenario 5:** Realizar empréstimo
  - **Given:** o usuário está logado
  - **When:** clica em "Emprestar" em um livro disponível
  - **Then:** empréstimo é registrado via POST e quantidade atualiza

- **Scenario 6:** Devolver livro
  - **Given:** o usuário tem empréstimo ativo
  - **When:** clica em "Devolver"
  - **Then:** empréstimo é marcado como devolvido via PATCH

#### Notas Técnicas:
- Pasta: `Frontend/` na raiz do projeto
- Arquivos: `index.html`, `style.css`, `app.js`
- SPA com manipulação de DOM (innerHTML/createElement)
- Token JWT armazenado em localStorage
- Fetch com header Authorization: Bearer
- Nginx para servir os arquivos estáticos (Dockerfile)

---

### US-009: Docker e Docker Compose

- **Summary:** Containerização completa do projeto com docker-compose

#### Use Case:
- **As a** avaliador ou desenvolvedor
- **I want to** subir toda a aplicação com um único comando
- **so that** não precise configurar ambiente manualmente

#### Acceptance Criteria:

- **Scenario 1:** Subir com docker-compose
  - **Given:** Docker está instalado
  - **When:** executa `docker-compose up`
  - **Then:** MongoDB, Backend e Frontend sobem e se comunicam

- **Scenario 2:** Backend conecta ao MongoDB
  - **Given:** os containers estão rodando
  - **When:** o backend inicia
  - **Then:** conecta ao MongoDB via variável de ambiente

- **Scenario 3:** Frontend acessível
  - **Given:** os containers estão rodando
  - **When:** acessa http://localhost
  - **Then:** exibe a aplicação frontend

- **Scenario 4:** Backend acessível
  - **Given:** os containers estão rodando
  - **When:** acessa http://localhost:8080/swagger-ui.html
  - **Then:** exibe o Swagger

#### Notas Técnicas:
- `Backend/Dockerfile`: multi-stage build (Maven build + JRE runtime)
- `Frontend/Dockerfile`: Nginx servindo arquivos estáticos
- `docker-compose.yml` na raiz com serviços: mongodb, backend, frontend
- Variáveis de ambiente: SPRING_DATA_MONGODB_URI
- Rede interna entre containers
- Volume para persistência do MongoDB


---

## Ordem de Execução

```
US-003 (Auth/JWT) ──→ US-004 (RBAC)
       │                    │
       ▼                    ▼
US-001 (CRUD Livros) ──→ US-002 (CRUD Empréstimos)
       │                    │
       ▼                    ▼
US-005 (Swagger) ──→ US-007 (SOLID) ──→ US-006 (Testes)
                                              │
                                              ▼
                                    US-008 (Frontend SPA)
                                              │
                                              ▼
                                    US-009 (Docker)
```

**Sequência recomendada:**
1. US-003 — Auth/JWT (base para tudo)
2. US-004 — RBAC (complementa auth)
3. US-001 — CRUD Livros (primeira entidade)
4. US-002 — CRUD Empréstimos (depende de Livros)
5. US-005 — Swagger (documenta o que já existe)
6. US-007 — SOLID (refatorar para interfaces, já aplicar durante dev)
7. US-006 — Testes (testar services já prontos)
8. US-008 — Frontend SPA (consome API pronta)
9. US-009 — Docker (empacota tudo)

---

## Critérios de Aceite do Épico

- [ ] API REST funcional com CRUD completo para Livro e Empréstimo
- [ ] MongoDB como banco de dados (string de conexão via env var)
- [ ] Swagger UI acessível com todos os endpoints documentados
- [ ] JWT implementado com registro, login e proteção de rotas
- [ ] RBAC com perfis admin e usuario funcionando
- [ ] Testes unitários com 2+ cenários de sucesso e 2+ de erro
- [ ] 3+ princípios SOLID aplicados e documentados em SOLID.md
- [ ] Frontend SPA com navegação assíncrona (sem reload)
- [ ] docker-compose subindo MongoDB + Backend + Frontend
- [ ] README.md completo com instruções de execução

---

## Riscos

| Risco | Mitigação |
|-------|-----------|
| Spring Boot 4.x pode ter breaking changes vs documentação online | Usar documentação oficial e release notes do Spring Boot 4 |
| MongoDB connection issues no Docker | Usar healthcheck no docker-compose e depends_on com condition |
| CORS entre Frontend (Nginx) e Backend | Configurar CorsFilter no Spring Security |
| Token JWT expirando durante uso do frontend | Implementar refresh ou expiração longa (24h) para demo |
| Tempo curto para apresentação (01/06) | Priorizar requisitos obrigatórios, bônus são incrementais |

---

## Contratos de API

### POST /api/auth/registrar

- **Auth**: Público
- **Request Body**:
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123"
}
```
- **Validações**:

| Campo | Regra |
|-------|-------|
| nome | @NotBlank, min 3 caracteres |
| email | @NotBlank, @Email |
| senha | @NotBlank, min 6 caracteres |

- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 201 | Registro com sucesso | `{"id": "...", "nome": "...", "email": "...", "role": "USUARIO"}` |
| 400 | Validação falhou | `{"status": 400, "erro": "Dados inválidos", "detalhes": [...]}` |
| 409 | Email já existe | `{"status": 409, "erro": "Email já cadastrado"}` |

---

### POST /api/auth/login

- **Auth**: Público
- **Request Body**:
```json
{
  "email": "joao@email.com",
  "senha": "senha123"
}
```
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Login com sucesso | `{"token": "eyJ...", "nome": "João", "role": "ADMIN"}` |
| 401 | Credenciais inválidas | `{"status": 401, "erro": "Credenciais inválidas"}` |

---

### GET /api/livros

- **Auth**: Público (listagem aberta)
- **Request Body**: nenhum
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Sucesso | `[{"id": "...", "titulo": "...", "autor": "...", "isbn": "...", "editora": "...", "ano": 2020, "genero": "...", "quantidade": 5}]` |

---

### GET /api/livros/{id}

- **Auth**: Público
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Encontrado | `{"id": "...", "titulo": "...", ...}` |
| 404 | Não encontrado | `{"status": 404, "erro": "Livro não encontrado"}` |

---

### POST /api/livros

- **Auth**: ADMIN
- **Request Body**:
```json
{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "978-0132350884",
  "editora": "Prentice Hall",
  "ano": 2008,
  "genero": "Tecnologia",
  "quantidade": 3
}
```
- **Validações**:

| Campo | Regra |
|-------|-------|
| titulo | @NotBlank |
| autor | @NotBlank |
| isbn | @NotBlank |
| quantidade | @Min(0) |

- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 201 | Criado | `{"id": "...", "titulo": "...", ...}` |
| 400 | Validação falhou | `{"status": 400, "erro": "Dados inválidos", "detalhes": [...]}` |
| 403 | Não é admin | `{"status": 403, "erro": "Acesso negado"}` |

---

### PUT /api/livros/{id}

- **Auth**: ADMIN
- **Request Body**: mesmo do POST
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Atualizado | `{"id": "...", "titulo": "...", ...}` |
| 404 | Não encontrado | `{"status": 404, "erro": "Livro não encontrado"}` |
| 403 | Não é admin | `{"status": 403, "erro": "Acesso negado"}` |

---

### DELETE /api/livros/{id}

- **Auth**: ADMIN
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 204 | Deletado | nenhum |
| 404 | Não encontrado | `{"status": 404, "erro": "Livro não encontrado"}` |
| 403 | Não é admin | `{"status": 403, "erro": "Acesso negado"}` |

---

### POST /api/emprestimos

- **Auth**: Autenticado (qualquer role)
- **Request Body**:
```json
{
  "livroId": "abc123",
  "dataDevolucaoPrevista": "2026-06-15"
}
```
- **Validações**:

| Campo | Regra |
|-------|-------|
| livroId | @NotBlank |
| dataDevolucaoPrevista | @NotNull, @Future |

- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 201 | Criado | `{"id": "...", "livroId": "...", "usuarioId": "...", "nomeUsuario": "...", "dataEmprestimo": "2026-05-30", "dataDevolucaoPrevista": "2026-06-15", "status": "ATIVO"}` |
| 400 | Livro sem estoque | `{"status": 400, "erro": "Livro não disponível"}` |
| 404 | Livro não existe | `{"status": 404, "erro": "Livro não encontrado"}` |

---

### GET /api/emprestimos

- **Auth**: Autenticado
- **Comportamento por role**:
  - ADMIN: retorna todos os empréstimos
  - USUARIO: retorna apenas os próprios empréstimos
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Sucesso | `[{"id": "...", "livroId": "...", "nomeUsuario": "...", "status": "ATIVO", ...}]` |

---

### PATCH /api/emprestimos/{id}/devolver

- **Auth**: Autenticado (dono do empréstimo ou ADMIN)
- **Request Body**: nenhum
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 200 | Devolvido | `{"id": "...", "status": "DEVOLVIDO", "dataDevolucao": "2026-05-30", ...}` |
| 400 | Já devolvido | `{"status": 400, "erro": "Empréstimo já devolvido"}` |
| 404 | Não encontrado | `{"status": 404, "erro": "Empréstimo não encontrado"}` |
| 403 | Não autorizado | `{"status": 403, "erro": "Acesso negado"}` |

---

### DELETE /api/emprestimos/{id}

- **Auth**: ADMIN
- **Responses**:

| Status | Caso | Body |
|--------|------|------|
| 204 | Deletado | nenhum |
| 404 | Não encontrado | `{"status": 404, "erro": "Empréstimo não encontrado"}` |
| 403 | Não é admin | `{"status": 403, "erro": "Acesso negado"}` |

