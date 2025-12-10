# Park Mobile

## Visão Geral

O Park Mobile é um aplicativo Android projetado para gerenciar vagas de estacionamento de forma eficiente. O aplicativo oferece funcionalidades distintas para usuários regulares e administradores, permitindo uma gestão completa do estacionamento.

## Funcionalidades

O aplicativo é dividido em dois painéis principais, um para administradores e outro para usuários.

### Funcionalidades do Usuário

*   **Visualização de Vagas:** Usuários podem visualizar as vagas de estacionamento disponíveis.
*   **Check-in e Check-out:** Funcionalidade para registrar a entrada e saída de veículos.
*   **Histórico:** Usuários podem visualizar o histórico de utilização do estacionamento.

### Funcionalidades do Administrador

*   **Gestão de Clientes:** Administradores podem pesquisar e gerenciar informações dos clientes.
*   **Relatórios:** Acesso a relatórios detalhados sobre a ocupação do estacionamento e outras métricas.
*   **Visualização de Vagas:** Assim como os usuários, administradores podem visualizar as vagas.

## Estrutura do Projeto

O projeto segue uma arquitetura limpa, separando as responsabilidades em diferentes camadas. A estrutura de pastas principal é:

-   `app/src/main/java/com/example/parkmobile/`
    -   `data`: Contém as classes de modelo de dados (`Vaga`, `Cliente`, `Usuario`), repositórios e fontes de dados, como o `FirestoreSeeder`.
    -   `ui`: Contém toda a lógica de interface do usuário, dividida por funcionalidade (`auth`, `vagas`, `clientes`, `historico`, etc.). Cada pacote de funcionalidade geralmente contém seus `Activity`/`Fragment`, `ViewModel` e `Adapter`.
    -   `util`: Classes utilitárias que podem ser usadas em todo o aplicativo.

## Arquitetura e Padrões

*   **Linguagem:** Kotlin
*   **Arquitetura:** MVVM (Model-View-ViewModel)
*   **UI:** Android XML Layouts
*   **Componentes de Arquitetura Android:** Fragments, ViewBinding, ViewModel, LiveData.

### ViewModel e ViewModelFactory

-   **ViewModel**: Os ViewModels (ex: `VagasViewModel`, `ClienteViewModel`) são usados para armazenar e gerenciar dados relacionados à UI de forma consciente do ciclo de vida. Eles sobrevivem a mudanças de configuração (como rotação de tela) e buscam os dados dos repositórios.
-   **ViewModelFactory**: As Factories são responsáveis por instanciar os ViewModels. Isso é especialmente útil quando um ViewModel tem dependências em seu construtor (como um repositório). A Factory cria o ViewModel, fornecendo as dependências necessárias.

## Popular o Banco de Dados (Seeder)

Para facilitar o teste e a demonstração, o projeto inclui um seeder para popular o banco de dados Firestore com dados de exemplo.

### FirestoreSeeder.kt

O arquivo `@/app/src/main/java/com/example/parkmobile/data/FirestoreSeeder.kt` contém a lógica para criar:

-   Vagas de estacionamento (A01-A10, B01-B10).
-   Usuários (Administradores e Clientes) com senhas padrão.
-   Registros de histórico de estacionamento, alguns ativos e outros já finalizados.

### Como usar o Seeder

A inicialização do seeder é controlada a partir de `@/app/src/main/java/com/example/parkmobile/ui/auth/MainActivity.kt`.

1.  **Abra o arquivo `MainActivity.kt`.**
2.  Dentro do método `onCreate`, você encontrará funções comentadas:
    -   `verificarBanco()`: É chamada por padrão e apenas verifica se o banco já tem dados, sem sobrescrevê-los.
    -   `popularBanco()`: **Descomente esta linha uma vez** para popular o banco de dados com dados iniciais se ele estiver vazio. Depois de executar o app uma vez, **comente a linha novamente**.
    -   `forcarRecriacao()`: **Use com cuidado!** Descomente esta linha para limpar completamente todos os dados do Firestore e recriá-los do zero. Útil para resetar o ambiente de teste.

**Exemplo em `MainActivity.kt`:**

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // ...

    // ✅ CHAMADA DA VERIFICAÇÃO (executa sempre, só verifica)
    verificarBanco()

    // forcarRecriacao() // 👈 DESCOMENTE só para resetar TUDO

    // popularBanco()  // 👈 DESCOMENTE 1x para popular, depois COMENTE
}
```

## Configuração do Projeto

Para que o aplicativo funcione, ele precisa se conectar a um projeto Firebase. Por razões de segurança, o arquivo de configuração `google-services.json` não está incluído no repositório.

Qualquer pessoa que clonar o projeto precisará seguir estes passos para gerar seu próprio arquivo de configuração:

1.  **Clonar o Repositório**
    ```
    git clone <URL_DO_SEU_REPOSITORIO>
    ```

2.  **Criar um Projeto no Firebase**
    -   Acesse o [Firebase Console](https://console.firebase.google.com/).
    -   Clique em **"Adicionar projeto"** e siga as instruções para criar um novo projeto.

3.  **Adicionar o App Android ao Projeto Firebase**
    -   No painel do seu novo projeto, clique no ícone do Android (`</>`) para adicionar um app Android.
    -   No campo **"Nome do pacote Android"**, insira `com.example.parkmobile`. Este passo é **crucial** e o nome do pacote deve ser exatamente esse.
    -   Clique em **"Registrar app"**.

4.  **Baixar e Adicionar o `google-services.json`**
    -   Após registrar o app, o Firebase fornecerá um botão para baixar o arquivo `google-services.json`.
    -   Faça o download do arquivo.
    -   Copie o arquivo `google-services.json` que você baixou e cole-o na pasta `app/` do projeto no Android Studio.

5.  **Configurar os Serviços do Firebase**
    -   No menu do Firebase Console, vá para **Authentication**.
    -   Na aba **"Sign-in method"** (ou "Método de login"), ative o provedor **"E-mail/senha"**.
    -   Agora, vá para **Firestore Database** (ou Cloud Firestore).
    -   Clique em **"Criar banco de dados"**.
    -   Inicie no **modo de teste** para facilitar a configuração inicial.
    -   Escolha uma localização para o seu banco de dados (pode manter a padrão).

6.  **Abrir e Sincronizar no Android Studio**
    -   Abra a pasta do projeto no Android Studio.
    -   Aguarde o Gradle sincronizar. Se não acontecer automaticamente, clique em "Sync Now" na barra que aparece.

7.  **Popular o Banco de Dados (Seeder)**
    -   Siga as instruções da seção **"Como usar o Seeder"** acima para popular seu novo banco de dados Firestore com dados de teste. Isso é fundamental para que o app tenha vagas, usuários e históricos para exibir.

8.  **Executar o Aplicativo**
    -   Agora você pode compilar e executar o aplicativo em um emulador ou dispositivo físico.
