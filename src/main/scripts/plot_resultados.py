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
    return [CORES.get(f, "gray") for f in funcoes]


# Carregar todos os resultados
def carregar_dados():
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
        else "Real"
    )
    df["Funcao"] = pd.Categorical(df["Funcao"], categories=ORDEM_FUNCOES, ordered=True)
    return df.sort_values("Funcao")


# Figura 1: Comparação geral entre funções (agregado)
def figura_comparacao_geral(df):
    agg = df.groupby("Funcao", observed=True).agg(
        ColisoesMedia=("Colisoes", "mean"),
        CVMedio=("CV", "mean"),
    ).reindex(ORDEM_FUNCOES).dropna(how="all")

    fig, axes = plt.subplots(1, 2, figsize=(11, 4.5))

    axes[0].bar(agg.index, agg["ColisoesMedia"], color=cores_para(agg.index))
    axes[0].set_title("Colisões médias")
    axes[0].set_ylabel("Colisões (média entre datasets)")
    axes[0].tick_params(axis="x", rotation=20)

    axes[1].bar(agg.index, agg["CVMedio"] * 100, color=cores_para(agg.index))
    axes[1].set_title("Coeficiente de Variação médio")
    axes[1].set_ylabel("CV (%)")
    axes[1].tick_params(axis="x", rotation=20)

    fig.suptitle("Comparação geral entre funções hash (média de todos os datasets testados)")
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/1_comparacao_geral.png")
    plt.close()


# Figura 2: Escalabilidade — colisões x N
def figura_escalabilidade(df, metrica, titulo, ylabel, nome_arquivo):
    tipos = [t for t in ["Sequencial", "Aleatório"] if df[df["Tipo"] == t]["N"].nunique() >= 2]
    if not tipos:
        print(f"[aviso] dados insuficientes para '{titulo}' (precisa de >=2 tamanhos por tipo)")
        return

    fig, axes = plt.subplots(1, len(tipos), figsize=(6 * len(tipos), 4.5), sharey=True)
    axes = np.atleast_1d(axes)

    for ax, tipo in zip(axes, tipos):
        sub = df[df["Tipo"] == tipo].sort_values("N")
        for funcao in ORDEM_FUNCOES:
            s = sub[sub["Funcao"] == funcao]
            if s.empty:
                continue
            ax.plot(s["N"], s[metrica], marker="o", label=funcao, color=CORES[funcao])
        ax.set_xscale("log")
        ax.set_xlabel("N (número de chaves, escala log)")
        ax.set_title(tipo)

    axes[0].set_ylabel(ylabel)
    axes[-1].legend(fontsize=8)
    fig.suptitle(titulo)
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/{nome_arquivo}.png")
    plt.close()


# Figura 4: Sintético vs. Real
def figura_sintetico_vs_real(df):
    if df["Tipo"].nunique() < 2:
        print("[aviso] dados insuficientes para comparação sintético vs. real")
        return
    agrupado = df.groupby(["Funcao", "Tipo"], observed=True)["Colisoes"].mean().unstack()
    agrupado = agrupado.reindex([f for f in ORDEM_FUNCOES if f in agrupado.index])
    agrupado.plot(kind="bar", figsize=(8, 5), color=None)
    plt.ylabel("Colisões (média)")
    plt.xlabel("Função hash")
    plt.title("Colisões médias: dados sintéticos vs. reais")
    plt.xticks(rotation=20)
    plt.legend(title="Tipo de dado")
    plt.tight_layout()
    plt.savefig(f"{OUT_DIR}/4_sintetico_vs_real.png")
    plt.close()

#Figura 5: Gráfico dos casos isolados
def caso_isolado(df): 
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

    figura_comparacao_geral(df)
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
    figura_sintetico_vs_real(df)
    caso_isolado(df)

    print(f"Gráficos salvos em: {OUT_DIR}/")