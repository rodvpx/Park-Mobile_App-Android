# Padrões do Projeto ParkMobile

Este documento descreve os padrões de UI/UX e de código adotados no projeto ParkMobile para garantir consistência e facilitar o desenvolvimento.

## 1. Paleta de Cores

As cores do aplicativo são centralizadas no arquivo `app/src/main/res/values/colors.xml`. Isso permite uma fácil manutenção e a implementação de temas (como o modo escuro).

Nossa paleta principal é:

-   `md_theme_primary` (#00668B): Azul escuro, usado como a cor primária principal para elementos como a `Toolbar`.
-   `md_theme_onPrimary` (#FFFFFF): Branco, para textos e ícones sobre a cor primária.
-   `md_theme_secondary` (#4D616C): Cinza-azulado, para elementos secundários.
-   `md_theme_onSecondary` (#FFFFFF): Branco, para textos e ícones sobre a cor secundária.
-   `md_theme_background` (#FDFCFF): Branco suave, para o fundo geral do app.
-   `md_theme_surface` (#FDFCFF): Cor para superfícies de componentes como `CardView`.
-   `md_theme_error` (#BA1A1A): Vermelho, para indicar erros.

## 2. Componentes da UI

Para manter a consistência visual, padronizamos alguns componentes da UI que são reutilizados em várias telas.

### 2.1. Toolbar Superior

A `Toolbar` superior é padronizada através do layout `app/src/main/res/layout/toolbar_sup_padrao.xml`.

**Uso:**

Para incluir a `Toolbar` em uma tela, use a tag `<include>`:

```xml
<include
    android:id="@+id/toolbar"
    layout="@layout/toolbar_sup_padrao" />
```

**Características:**

-   **Título Centralizado:** Possui um `TextView` (`@id/toolbar_title`) posicionado no centro para exibir o título da tela. O texto é controlado pela `Activity` ou `Fragment` correspondente.
-   **Cor de Fundo:** Utiliza `?attr/colorPrimary`, garantindo que a cor se adapte ao tema do aplicativo.
-   **Tema:** O tema da Toolbar é `@style/ThemeOverlay.MaterialComponents.Dark.ActionBar`, o que torna os ícones e o texto do menu (se houver) brancos por padrão.
-   **Ícones e Ações:** A adição de ícones de menu (como o menu hambúrguer ou ícones de ação) e a lógica para lidar com seus cliques são implementadas na `Activity` ou `Fragment` correspondente, no arquivo Kotlin. Isso mantém a separação entre a aparência (XML) e o comportamento (Kotlin).

### 2.2. Botões da Tela Principal

Os botões nas telas principais (como "Entrar" e "Cadastrar") utilizam um estilo comum para manter a aparência uniforme.

**Estilo:** `@style/BotaoTelaPrincipal`

Este estilo é definido em `app/src/main/res/values/themes.xml` e aplica as seguintes propriedades:

-   **Altura:** `80dp`
-   **Margens Laterais:** `45dp`
-   **Tamanho do Texto:** `25sp`
-   **Tamanho do Ícone:** `35dp`
-   **Cantos Arredondados:** `16dp`

**Uso:**

```xml
<Button
    android:id="@+id/bt_entrar"
    style="@style/BotaoTelaPrincipal"
    android:text="Entrar"
    app:icon="@drawable/ic_profile" />
```

### 2.3. Botão Geral

Para ações secundárias ou botões dentro de formulários (como "Salvar", "Avançar", etc.), utilizamos o estilo `BotaoGeral`.

**Estilo:** `@style/BotaoGeral`

Definido em `app/src/main/res/values/themes.xml`, este estilo possui as seguintes características:

-   **Altura:** `60dp`
-   **Margens Laterais:** `32dp`
-   **Tamanho do Texto:** `16sp`
-   **Estilo do Texto:** `bold`
-   **Cantos Arredondados:** `15dp`

**Uso:**

```xml
<Button
    android:id="@+id/bt_salvar"
    style="@style/BotaoGeral"
    android:text="Salvar Alterações" />
```

### 2.4. Campos de Formulário

Para garantir que os campos de entrada de texto sejam consistentes, utilizamos um estilo padrão para `TextInputLayout`.

**Estilo:** `@style/CampoFormulario`

Baseado em `Widget.MaterialComponents.TextInputLayout.OutlinedBox`, este estilo definido em `app/src/main/res/values/themes.xml` aplica:

-   **Margens Laterais:** `32dp`

**Uso:**

```xml
<com.google.android.material.textfield.TextInputLayout
    style="@style/CampoFormulario"
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    android:hint="Nome Completo">

    <com.google.android.material.textfield.TextInputEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</com.google.android.material.textfield.TextInputLayout>
```

A aplicação destes estilos garante uma UI coesa e facilita a manutenção do código.
