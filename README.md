# API - Automação Predial Integrada

## 📋 Sobre o Projeto

Este repositório contém o back-end do sistema **API (Automação Predial Integrada)**. A API centraliza o controle do prédio, tornando-o mais eficiente, seguro e econômico ao gerenciar inteligentemente:

- 🌡️ **Clima** - Controle de temperatura e ar-condicionado
- 💡 **Iluminação** - Gerenciamento inteligente de luzes
- ⚡ **Energia** - Monitoramento e otimização do consumo energético
- 🚪 **Acesso** - Controle de entrada e saída de pessoas

## 🚀 Tecnologias Utilizadas

- **Java** com Spring Boot
- **Maven** como gerenciador de dependências
- **Docker** para containerização
- **Docker Compose** para orquestração de containers

## 📁 Estrutura do Projeto

```
.
├── .mvn/wrapper/          # Maven Wrapper
├── src/                   # Código fonte da aplicação
├── .gitattributes         # Configurações do Git
├── .gitignore            # Arquivos ignorados pelo Git
├── Dockerfile            # Configuração do container Docker
├── docker-compose.yml    # Orquestração de containers
├── mvnw                  # Maven Wrapper (Unix)
├── mvnw.cmd              # Maven Wrapper (Windows)
└── pom.xml               # Configurações do Maven e dependências
```

## 🛠️ Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior (ou use o Maven Wrapper incluído)
- Docker e Docker Compose (opcional, para execução containerizada)

## 🔧 Instalação e Execução

### Execução Local com Maven

```bash
# Clone o repositório
git clone <url-do-repositorio>
cd <nome-do-repositorio>

# Execute a aplicação usando Maven Wrapper
./mvnw spring-boot:run

# Ou no Windows
mvnw.cmd spring-boot:run
```

### Execução com Docker

```bash
# Build e execução com Docker Compose
docker-compose up -d

# Para parar os containers
docker-compose down
```

### Build do Projeto

```bash
# Gerar o arquivo JAR
./mvnw clean package

# Ou no Windows
mvnw.cmd clean package
```

## 🏗️ Entidades Principais

- **ArCondicionado** - Gerenciamento de sistemas de climatização
- *(Outras entidades conforme o desenvolvimento do projeto)*

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Fazer fork do projeto
2. Criar uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abrir um Pull Request

## 📄 Licença

*(Adicione informações sobre a licença do projeto)*

## 📧 Contato

*(Adicione informações de contato ou links relevantes)*

---

⚡ Desenvolvido para tornar prédios mais inteligentes, eficientes e sustentáveis.
