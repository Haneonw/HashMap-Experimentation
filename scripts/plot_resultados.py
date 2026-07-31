"""
plot_resultados.py

Gera os gráficos do experimento "Comparação Experimental entre Funções Hash"
(UFCG - EDA). Lê todos os arquivos CSV da pasta resultados/ (gerados pelo
Benchmark.java) e produz figuras em estilo científico na pasta graficos/.

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
ORDEM_FUNCOES = ["Modular", "Multiplicativa", "MeioDoQuadrado", "MurmurHash3", "Folding"]

RESULTADOS_DIR = "resultados"
OUT_DIR = "graficos"
os.makedirs(OUT_DIR, exist_ok=True)


def cores_para(funcoes):
    return [CORES.get(f, "gray") for f in funcoes]


def ordenar_funcoes(df):
    presentes = [f for f in ORDEM_FUNCOES if f in df["Funcao"].values]
    df["Funcao"] = pd.Categorical(df["Funcao"], categories=presentes, ordered=True)
    return df.sort_values("Funcao")

def carregar_dados():
    arquivos = glob.glob(os.path.join(RESULTADOS_DIR, "*.csv"))
    if not arquivos:
        raise FileNotFoundError(
            f"Nenhum CSV encontrado em {RESULTADOS_DIR}/. Rode o Benchmark.java primeiro."
        )
    dfs = [pd.read_csv(a) for a in arquivos]
    df = pd.concat(dfs, ignore_index=True)

    def extrai_n(nome):
        m = re.search(r"(\d+)", nome)
        return int(m.group(1)) if m else np.nan

    df["N"] = df["Dataset"].apply(extrai_n)
    df["Tipo"] = df["Dataset"].apply(
        lambda x: "Sequencial" if "sequencial" in x.lower()
        else "Aleatório" if "aleatorio" in x.lower()
        else "Real"
    )
    return df


# Gráfico 1: Colisões por função, para um dataset específico
def grafico_colisoes_barra(df, dataset_nome):
    sub = ordenar_funcoes(df[df["Dataset"] == dataset_nome].copy())
    fig, ax = plt.subplots(figsize=(7, 4.5))
    ax.bar(sub["Funcao"], sub["Colisoes"], color=cores_para(sub["Funcao"]))
    ax.set_ylabel("Número de colisões")
    ax.set_xlabel("Função hash")
    ax.set_title(f"Colisões por função — {dataset_nome}")
    plt.xticks(rotation=20)
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/colisoes_{dataset_nome.replace('.', '_')}.png")
    plt.close()


# Gráfico 2: Coeficiente de Variação por função
def grafico_cv_barra(df, dataset_nome):
    sub = ordenar_funcoes(df[df["Dataset"] == dataset_nome].copy())
    fig, ax = plt.subplots(figsize=(7, 4.5))
    ax.bar(sub["Funcao"], sub["CV"] * 100, color=cores_para(sub["Funcao"]))
    ax.set_ylabel("Coeficiente de Variação (%)")
    ax.set_xlabel("Função hash")
    ax.set_title(f"Dispersão da distribuição — {dataset_nome}\n(quanto menor, mais uniforme)")
    plt.xticks(rotation=20)
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/cv_{dataset_nome.replace('.', '_')}.png")
    plt.close()


# Gráfico 3: Escalabilidade — Colisões x N (escala log)
def grafico_escalabilidade(df, tipo_dataset):
    sub = df[df["Tipo"] == tipo_dataset].sort_values("N")
    if sub["N"].nunique() < 2:
        return  # precisa de pelo menos 2 tamanhos distintos pra fazer sentido
    fig, ax = plt.subplots(figsize=(7, 4.5))
    for funcao in ORDEM_FUNCOES:
        s = sub[sub["Funcao"] == funcao]
        if s.empty:
            continue
        ax.plot(s["N"], s["Colisoes"], marker="o", label=funcao, color=CORES[funcao])
    ax.set_xscale("log")
    ax.set_xlabel("Número de chaves (N, escala log)")
    ax.set_ylabel("Número de colisões")
    ax.set_title(f"Escalabilidade das colisões — dados {tipo_dataset.lower()}s")
    ax.legend()
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/escalabilidade_{tipo_dataset.lower()}.png")
    plt.close()


# Gráfico 4: Maior cadeia por função (pior caso)
def grafico_maior_cadeia(df, dataset_nome):
    sub = ordenar_funcoes(df[df["Dataset"] == dataset_nome].copy())
    fig, ax = plt.subplots(figsize=(7, 4.5))
    ax.bar(sub["Funcao"], sub["MaiorCadeia"], color=cores_para(sub["Funcao"]))
    ax.set_ylabel("Tamanho da maior cadeia")
    ax.set_xlabel("Função hash")
    ax.set_title(f"Pior caso de encadeamento — {dataset_nome}")
    plt.xticks(rotation=20)
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/maior_cadeia_{dataset_nome.replace('.', '_')}.png")
    plt.close()


# Gráfico 5: Sintético (seq/aleatório) vs. real — colisões médias
def grafico_sintetico_vs_real(df):
    if df["Tipo"].nunique() < 2:
        return
    agrupado = df.groupby(["Funcao", "Tipo"])["Colisoes"].mean().unstack()
    agrupado = agrupado.reindex([f for f in ORDEM_FUNCOES if f in agrupado.index])
    agrupado.plot(kind="bar", figsize=(8, 5))
    plt.ylabel("Média de colisões")
    plt.xlabel("Função hash")
    plt.title("Colisões médias: dados sintéticos vs. reais")
    plt.xticks(rotation=20)
    plt.legend(title="Tipo de dado")
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/sintetico_vs_real.png")
    plt.close()


if __name__ == "__main__":
    df = carregar_dados()

    for dataset in df["Dataset"].unique():
        grafico_colisoes_barra(df, dataset)
        grafico_cv_barra(df, dataset)
        grafico_maior_cadeia(df, dataset)

    for tipo in df["Tipo"].unique():
        grafico_escalabilidade(df, tipo)

    grafico_sintetico_vs_real(df)

    print(f"Gráficos salvos em: {OUT_DIR}/")
