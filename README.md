# 📚 Sistema de Gerenciamento de Biblioteca

API REST + Frontend SPA para gerenciamento de acervo de livros e empréstimos, com autenticação JWT e autorização por perfis (RBAC).

## Domínio

O sistema permite:
- Cadastrar, editar, listar e remover livros do acervo
- Registrar empréstimos e devoluções com controle de estoque
- Autenticação de usuários com JWT
- Controle de acesso por perfil (ADMIN / USUARIO)

## Stack Tecnológica

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 21 + Spring Boot 4.0.6 |
| Banco de Dados | MongoDB 7 |
| Frontend | HTML + CSS + JavaScript (SPA) |
| Documentação | Swagger/OpenAPI (SpringDoc) |
| Containerização | Docker + Docker Compose |

## Pré-requisitos

- **Java 21** (JDK)
- **MongoDB 7** rodando localmente (ou Docker)
- **Docker** e **Docker Compose** (para execução containerizada)

## Executando com Docker (recomendado)

```bash
docker-compose up --build
```

Isso sobe:
- MongoDB na porta 27017
- Backend na porta 8080
- Frontend na porta 80

## Executando localmente (sem Docker)

### 1. Subir o MongoDB

```bash
docker run -d -p 27017:27017 mongo:7
```

### 2. Backend

```bash
cd Backend
./mvnw spring-boot:run
```

### 3. Frontend

Abra `Frontend/index.html` no navegador.

## Acessos

| Serviço | URL |
|---------|-----|
| Frontend | http://localhost (Docker) ou abrir `Frontend/index.html` |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

## Variáveis de Ambiente

| Variável | Descrição | Valor padrão |
|----------|-----------|--------------|
| `SPRING_DATA_MONGODB_URI` | URI de conexão com MongoDB | `mongodb://localhost:27017/biblioteca` |
| `JWT_SECRET` | Chave secreta para assinatura JWT | (chave padrão para dev) |
| `JWT_EXPIRATION` | Tempo de expiração do token (ms) | `86400000` (24h) |

## Executando Testes

```bash
cd Backend
./mvnw test
```

## Estrutura do Projeto

```
├── Backend/
│   ├── src/main/java/com/jeferson/biblioteca/
│   │   ├── config/        # SecurityConfig, SwaggerConfig
│   │   ├── controller/    # AuthController, LivroController, EmprestimoController
│   │   ├── dto/           # Request/Response DTOs
│   │   ├── exception/     # Exceções + GlobalExceptionHandler
│   │   ├── model/         # Entidades MongoDB
│   │   ├── repository/    # MongoRepository interfaces
│   │   ├── security/      # JwtService, JwtAuthFilter
│   │   └── service/       # Interfaces + Implementações
│   ├── Dockerfile
│   └── pom.xml
├── Frontend/
│   ├── index.html
│   ├── style.css
│   ├── app.js
│   ├── nginx.conf
│   └── Dockerfile
├── docker-compose.yml
├── SOLID.md
└── README.md
```

## Endpoints da API

### Autenticação (público)
- `POST /api/auth/registrar` — Registrar usuário (escolher role: ADMIN ou USUARIO)
- `POST /api/auth/login` — Login (retorna JWT)

### Livros
- `GET /api/livros` — Listar todos (público)
- `GET /api/livros/{id}` — Buscar por ID (público)
- `POST /api/livros` — Criar (ADMIN)
- `PUT /api/livros/{id}` — Atualizar (ADMIN)
- `DELETE /api/livros/{id}` — Deletar (ADMIN)

### Empréstimos (autenticado)
- `POST /api/emprestimos` — Registrar empréstimo
- `GET /api/emprestimos` — Listar (admin: todos, usuario: próprios)
- `PATCH /api/emprestimos/{id}/devolver` — Devolver livro
- `DELETE /api/emprestimos/{id}` — Deletar (ADMIN)
