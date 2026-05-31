# Padrões de Código

## Java / Spring Boot

### Nomenclatura
- Pacote base: `com.jeferson.biblioteca`
- Classes: PascalCase (`LivroController`, `EmprestimoService`)
- Métodos/variáveis: camelCase
- Constantes: UPPER_SNAKE_CASE
- Endpoints: kebab-case com substantivos no plural (`/api/livros`, `/api/emprestimos`)

### Convenções
- Interfaces de serviço: `LivroService.java`
- Implementações: `LivroServiceImpl.java`
- DTOs: `LivroRequest.java`, `LivroResponse.java`
- Exceções: `ResourceNotFoundException.java`, `BusinessException.java`
- Injeção via construtor (sem @Autowired em campo)
- Usar `record` para DTOs quando possível (Java 21)
- Validação com Bean Validation (`@NotBlank`, `@Email`, `@Min`)

### HTTP Status Codes
- 200: OK (GET, PUT, PATCH)
- 201: Created (POST)
- 204: No Content (DELETE)
- 400: Bad Request (validação)
- 401: Unauthorized (sem token / token inválido)
- 403: Forbidden (sem permissão / role insuficiente)
- 404: Not Found
- 409: Conflict (duplicidade)

### Resposta de Erro Padrão
```json
{
  "status": 400,
  "erro": "Mensagem descritiva",
  "detalhes": ["campo X é obrigatório"]
}
```

## Frontend (HTML/CSS/JS)

- JavaScript vanilla (ES6+), sem frameworks
- Fetch API para chamadas HTTP
- Token JWT em localStorage
- SPA com manipulação de DOM (sem reload)
- CSS simples e responsivo
- Nomes de funções em camelCase
