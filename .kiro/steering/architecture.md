# Arquitetura do Projeto

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 21 + Spring Boot 4.0.6 |
| Banco | MongoDB |
| Frontend | HTML + CSS + JavaScript puro (SPA) |
| Docs API | SpringDoc OpenAPI (Swagger UI) |
| Containerização | Docker + docker-compose |

## Estrutura de Pastas

```
/
├── Backend/
│   ├── src/main/java/com/jeferson/biblioteca/
│   │   ├── config/          # SecurityConfig, SwaggerConfig, CorsConfig
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Request/Response DTOs
│   │   ├── exception/       # Exceções customizadas + GlobalExceptionHandler
│   │   ├── model/           # Entidades MongoDB (@Document)
│   │   ├── repository/      # MongoRepository interfaces
│   │   ├── security/        # JwtService, JwtAuthFilter
│   │   └── service/         # Interfaces + Implementações (Impl)
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── Frontend/
│   ├── index.html
│   ├── style.css
│   ├── app.js
│   ├── Dockerfile
│   └── nginx.conf
├── docker-compose.yml
├── README.md
├── SOLID.md
└── tasks/
```

## Padrões de Arquitetura

- **Camadas**: Controller → Service (interface) → Repository
- **Injeção de dependência**: via construtor (nunca @Autowired em campo)
- **DTOs**: separar Request e Response, nunca expor entidade diretamente
- **Exceções**: GlobalExceptionHandler com @RestControllerAdvice
- **Segurança**: Spring Security + JWT stateless

## Banco de Dados

- MongoDB com spring-boot-starter-data-mongodb
- Conexão via variável de ambiente: `SPRING_DATA_MONGODB_URI`
- Padrão local: `mongodb://localhost:27017/biblioteca`

## Endpoints Base

- Backend: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- Frontend: `http://localhost` (porta 80 via Nginx)
