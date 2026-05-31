# Princípios SOLID Aplicados

## S — Single Responsibility Principle (SRP)

Cada classe tem uma única responsabilidade:

| Classe | Responsabilidade |
|--------|-----------------|
| `LivroController` | Receber requisições HTTP e delegar ao service |
| `LivroServiceImpl` | Regras de negócio de livros |
| `LivroRepository` | Persistência de dados no MongoDB |
| `JwtService` | Geração e validação de tokens JWT |
| `JwtAuthFilter` | Interceptar requisições e autenticar via token |
| `GlobalExceptionHandler` | Tratamento centralizado de exceções |

**Arquivos:** Todos os controllers, services e repositories do projeto.

---

## O — Open/Closed Principle (OCP)

As classes estão abertas para extensão e fechadas para modificação através do uso de interfaces:

- `LivroService` (interface) → `LivroServiceImpl` (implementação)
- `EmprestimoService` (interface) → `EmprestimoServiceImpl` (implementação)
- `AuthService` (interface) → `AuthServiceImpl` (implementação)

Para adicionar uma nova regra de negócio (ex: validação customizada de empréstimo), basta criar uma nova implementação ou decorar a existente sem alterar a interface.

**Arquivos:**
- `service/LivroService.java` e `service/LivroServiceImpl.java`
- `service/EmprestimoService.java` e `service/EmprestimoServiceImpl.java`
- `service/AuthService.java` e `service/AuthServiceImpl.java`

---

## I — Interface Segregation Principle (ISP)

As interfaces de serviço são coesas e específicas. Cada interface expõe apenas os métodos relevantes ao seu domínio:

- `LivroService`: apenas operações CRUD de livros (criar, listar, buscarPorId, atualizar, deletar)
- `EmprestimoService`: apenas operações de empréstimo (criar, listar, devolver, deletar)
- `AuthService`: apenas operações de autenticação (registrar, login)

Nenhuma classe é forçada a implementar métodos que não utiliza.

**Arquivos:**
- `service/LivroService.java`
- `service/EmprestimoService.java`
- `service/AuthService.java`

---

## D — Dependency Inversion Principle (DIP)

As classes de alto nível (controllers) dependem de abstrações (interfaces), não de implementações concretas:

```java
// LivroController depende da interface, não da implementação
public class LivroController {
    private final LivroService livroService; // interface
}
```

A injeção de dependência é feita via construtor, e o Spring resolve a implementação concreta em tempo de execução.

**Arquivos:**
- `controller/LivroController.java` → depende de `LivroService` (interface)
- `controller/EmprestimoController.java` → depende de `EmprestimoService` (interface)
- `controller/AuthController.java` → depende de `AuthService` (interface)
- `service/EmprestimoServiceImpl.java` → depende de `LivroRepository` (interface do Spring Data)
