package src.main.java.hash;

/**
* Multiplicativa
* 
* Implementação do método da multiplicação seguindo a aplicação da fórmula
* proposta por Donald Knuth, a fórmula:
* 
* indiceHash = floor(m * parteFracionaria(k * A))
* 
* onde:
* k = chave
* m = tamanho da tabela
* A = constante real entre 0 e 1 
* parteFracionária(x) = x - floor(x) 
*/
public class Multiplicativa extends FuncaoHash{
    
    /**
     * Constante sugerida por Knuth: A = (sqrt(5) - 1) / 2 ≈ 0.6180339887
     * 
     * Esse valor tem relação com à razão áurea e possui propriedades
     * matemátticas que favorecem uma distribuição mais uniforme das chaves.
     */
    private static final double A = (Math.sqrt(5) - 1) / 2;

    /**
     * Cria uma nova instância da função hash multiplicativa.
     *
     * @param size tamanho da tabela hash
     */
    public Multiplicativa(int size){
        super(size);
    }

    /**
     * Calcula o índice hash da chave utilizando o método da multiplicação
     * de Knuth, multiplicando a chave pela constante {@link #A}, extraindo
     * a parte fracionária do resultado e escalando-a pelo tamanho da
     * tabela.
     *
     * @param chave a chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        //passo 1: multiplica a chave pela constante A
        double produto = chave * A;

        //passo 2: extrai apenas parte fracionária de chave * A
        double parteFracionária = produto - Math.floor(produto);

        //passo 3 e 4: multiplica pelo tamanho da tabela e arredonda para baixo
        int indice = (int) Math.floor(this.size * parteFracionária);
        
        return indice;
    }
}
