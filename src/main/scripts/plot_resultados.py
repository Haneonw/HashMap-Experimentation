"""
plot_resultados.py

Gera os gráficos essenciais do experimento "Comparação Experimental entre
Funções Hash" (UFCG - EDA). Lê todos os arquivos CSV da pasta resultados/
(gerados pelo Benchmark.java) e produz 4 figuras analíticas em graficos/,
evitando repetir a mesma pergunta gráfico a gráfico por dataset.

Uso:
    python3 scripts/plot_resultados.py

Requisitos:
    pip install pandas matplotlib --break-system-packages
"""
import glob
import os
import re

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

plt.rcParams.update({
    "font.size": 11,
    "axes.grid": True,
    "grid.alpha": 0.3,
    "axes.edgecolor": "0.3",
    "figure.dpi": 150,
    "savefig.dpi": 300,
    "savefig.bbox": "tight",
})

CORES = {
    "Modular": "#4C72B0",
    "Multiplicativa": "#DD8452",
    "MeioDoQuadrado": "#55A868",
    "MurmurHash3": "#C44E52",
    "Folding": "#8172B2",
}
ORDEM_FUNCOES = ["Modular", "Multiplicativa", "MeioDoQuadrado", "Folding", "MurmurHash3"]

RESULTADOS_DIR = "resultados"
OUT_DIR = "graficos"
os.makedirs(OUT_DIR, exist_ok=True)


def cores_para(funcoes):
    """Retorna a lista de cores correspondentes a uma sequência de funções hash.

    Args:
        funcoes: iterável com os nomes das funções hash (ex.: "Modular").

    Returns:
        list: lista de códigos de cor (str) na mesma ordem de `funcoes`,
        usando "gray" como cor padrão para funções não mapeadas em CORES.
    """
    return [CORES.get(f, "gray") for f in funcoes]


# Carregar todos os resultados
def carregar_dados():
    """Carrega e consolida todos os resultados do benchmark em um único DataFrame.

    Lê todos os arquivos CSV presentes na pasta `resultados/` (gerados pelo
    Benchmark.java), concatena-os em um único DataFrame e adiciona três
    colunas derivadas:
        - "N": tamanho do dataset, extraído do nome do arquivo em "Dataset".
        - "Tipo": categoria do dataset ("Sequencial", "Aleatório",
          "ID jogos" ou "ID cidades"), inferida a partir do nome do arquivo.
        - "Funcao": coluna original convertida em tipo categórico ordenado
          conforme ORDEM_FUNCOES, para manter uma ordem consistente nos
          gráficos.

    Returns:
        pandas.DataFrame: dados consolidados de todos os CSVs, ordenados
        pela coluna "Funcao".

    Raises:
        FileNotFoundError: se nenhum arquivo CSV for encontrado em
            RESULTADOS_DIR.
    """
    arquivos = glob.glob(os.path.join(RESULTADOS_DIR, "*.csv"))
    if not arquivos:
        raise FileNotFoundError(
            f"Nenhum CSV encontrado em {RESULTADOS_DIR}/. Rode o Benchmark.java primeiro."
        )
    df = pd.concat([pd.read_csv(a) for a in arquivos], ignore_index=True)

    def extrai_n(nome):
        m = re.search(r"(\d+)", nome)
        return int(m.group(1)) if m else np.nan

    df["N"] = df["Dataset"].apply(extrai_n)
    df["Tipo"] = df["Dataset"].apply(
        lambda x: "Sequencial" if "sequencial" in x.lower()
        else "Aleatório" if "aleatorio" in x.lower()
        else "ID jogos" if "games" in x.lower()
        else "ID cidades"
    )
    df["Funcao"] = pd.Categorical(df["Funcao"], categories=ORDEM_FUNCOES, ordered=True)
    return df.sort_values("Funcao")



# Figura 1: Escalabilidade — colisões x N
def figura_escalabilidade(df, metrica, titulo, ylabel, nome_arquivo):
    """Gera um gráfico de escalabilidade de uma métrica em função de N.

    Para cada tipo de dataset ("Sequencial" e "Aleatório") que possua pelo
    menos dois tamanhos (N) distintos, plota a evolução da métrica
    escolhida em função de N (em escala logarítmica), com uma linha por
    função hash. A figura resultante é salva como PNG em `OUT_DIR`.

    Args:
        df: DataFrame consolidado retornado por `carregar_dados()`.
        metrica: nome da coluna do DataFrame a ser plotada no eixo Y
            (ex.: "Colisoes", "MaiorCadeia").
        titulo: título principal da figura.
        ylabel: rótulo do eixo Y.
        nome_arquivo: nome do arquivo de saída (sem extensão), salvo em
            `OUT_DIR/{nome_arquivo}.png`.

    Returns:
        None. Se não houver dados suficientes (menos de dois tamanhos por
        tipo de dataset), a função apenas imprime um aviso e retorna sem
        gerar a figura.
    """
    tipos = [t for t in ["Sequencial", "Aleatório"] if df[df["Tipo"] == t]["N"].nunique() >= 2]
    if not tipos:
        print(f"[aviso] dados insuficientes para '{titulo}' (precisa de >=2 tamanhos por tipo)")
        return

    fig, axes = plt.subplots(1, len(tipos), figsize=(6 * len(tipos), 4.5), sharey=True)
    axes = np.atleast_1d(axes)

    multi = 1
    if metrica == "CV":
        multi = 100

    for ax, tipo in zip(axes, tipos):
        sub = df[df["Tipo"] == tipo].sort_values("N")
        for funcao in ORDEM_FUNCOES:
            s = sub[sub["Funcao"] == funcao]
            if s.empty:
                continue
            ax.plot(s["N"], s[metrica] * multi, marker="o", label=funcao, color=CORES[funcao])
        ax.set_xscale("log")
        ax.set_xlabel("N (número de chaves, escala log)")
        ax.set_title(tipo)

    axes[0].set_ylabel(ylabel)
    axes[-1].legend(fontsize=8)
    fig.suptitle(titulo)
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/{nome_arquivo}.png")
    plt.close()


# Figura 2: Comparção CV, casos.
def figura_comparacaoCV(df):
    """Gera um gráfico de barras comparando o coeficiente de variação (CV)
    médio de cada função hash, agrupado por tipo de dataset.

    Calcula a média do CV para cada combinação de função hash e tipo de
    dataset, converte o valor para porcentagem e plota um gráfico de barras
    agrupadas, salvando o resultado como
    `OUT_DIR/4_comparaçãoCV.png`.

    Args:
        df: DataFrame consolidado retornado por `carregar_dados()`.

    Returns:
        None. A figura gerada é salva em disco e a função não retorna valor.
    """
    agrupado = df.groupby(["Funcao", "Tipo"], observed=True)["CV"].mean().unstack() * 100
    agrupado = agrupado.reindex([f for f in ORDEM_FUNCOES if f in agrupado.index])
    agrupado.plot(kind="bar", figsize=(8, 5), color=None)
    plt.ylabel("CV (%)")
    plt.xlabel("Função hash")
    plt.title("CV em cada caso")
    plt.xticks(rotation=20)
    plt.legend(title="Tipo de dado")
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/4_comparaçãoCV.png")
    plt.close()

#Figura 3: Gráficos isolados.
def figura_caso_isolado(df): 
    metricas = ["Colisoes","CV","MaiorCadeia","MenorCadeia"]
    for dataset in df["Dataset"].unique():
        dados = df[df["Dataset"] == dataset]
        fig, axes = plt.subplots(4, 1, figsize=(15, 15), constrained_layout=True)
        axes[0].bar(dados["Funcao"], dados["Colisoes"], color=cores_para(dados["Funcao"]))
        axes[0].set_title("Colisoes")
        axes[0].set_ylabel("Número de colisões")
        
        axes[1].bar(dados["Funcao"], dados["CV"] * 100, color=cores_para(dados["Funcao"]))
        axes[1].set_title("Coeficiente de variação")
        axes[1].set_ylabel("CV (%)")
        
        axes[2].bar(dados["Funcao"], dados["MaiorCadeia"], color=cores_para(dados["Funcao"]))
        axes[2].set_title("Maior Cadeia")
        axes[2].set_ylabel("Elementos")
        
        axes[3].bar(dados["Funcao"], dados["MediaCadeia"],color=cores_para(dados["Funcao"]))
        axes[3].set_title("Media Cadeia")
        axes[3].set_ylabel("Elementos")
        
        plt.savefig(f"{OUT_DIR}/{dataset}.png")
        plt.close()
        


if __name__ == "__main__":
    df = carregar_dados()

    figura_comparacaoCV(df)
    figura_escalabilidade(
        df, metrica="Colisoes",
        titulo="Escalabilidade das colisões",
        ylabel="Número de colisões",
        nome_arquivo="2_escalabilidade_colisoes",
    )
    figura_escalabilidade(
        df, metrica="MaiorCadeia",
        titulo="Escalabilidade do pior caso (maior cadeia)",
        ylabel="Tamanho da maior cadeia",
        nome_arquivo="3_escalabilidade_maior_cadeia",
    )
    figura_escalabilidade(
        df, metrica="CV",
        titulo="Escalabilidade do CV",
        ylabel="CV (%)",
        nome_arquivo="1_escalabilidade_CV",
    )
    figura_caso_isolado(df)

    print(f"Gráficos salvos em: {OUT_DIR}/")