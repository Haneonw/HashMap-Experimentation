# HashMap-Experimentation

Comparação experimental entre funções hash (Modular, Multiplicativa, Meio do
Quadrado, MurmurHash3, Folding) quanto a colisões, distribuição e desempenho,
usando encadeamento separado.

## Organização

hash/ → implementações das funções hash
dados/ → datasets de entrada (sequenciais, aleatórios, reais)
resultados/ → CSVs gerados pelo benchmark (1 por dataset)
scripts/ → plot_resultados.py (geração dos gráficos)
graficos/ → saída dos gráficos (não versionado)
Benchmark.java → executa o benchmark e gera os CSVs

## Como rodar

1. Compilar e rodar o benchmark para cada dataset:
```bash
   javac hash/*.java Benchmark.java
   java Benchmark
```
   → gera um novo CSV em `resultados/`

2. Repetir para cada dataset que faltar

3. Gerar os gráficos:
```bash
   python3 scripts/plot_resultados.py
```
   → lê todos os CSVs de `resultados/` automaticamente e salva as figuras em `graficos/`