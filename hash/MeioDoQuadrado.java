package hash;
/**
 * Meio Do Quadrado
 * 
 * Implementação da função hash utilizando o método do meio do quadrado.
 * 
 * A chave é elevada ao quadrado e uma região central dos bits do
 * resultado é utilizada para gerar o índice da tabela hash.
 * Essa técnica busca reduzir padrões existentes nas chaves originais
 * e favorecer uma distribuição mais uniforme dos elementos na tabela hash.
 */
public class MeioDoQuadrado extends FuncaoHash {
    public MeioDoQuadrado(int size){
        super(size);
        this.nomeFunc = "Meio Do Quadrado";
    }

    /**
     * Calcula o índice hash da chave utilizando o método do meio do quadrado.
     * 
     * @param chave A chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        // Passo 1: Converte a chave para um inteiro sem sinal de 64 bits,
        // evitando problemas com valores negativos durante o cálculo.
        long valor = Integer.toUnsignedLong(chave);
        
        // Passo 2: Calcula o quadrado da chave.
        long quadrado = valor * valor;

        // Passo 3: Extrai bits centrais do quadrado.
        // O deslocamento remove os 16 bits menos significativos e preservando os 32 bits seguintes.
        long meio = (quadrado >>> 16) & 0xFFFFFFFFL;

        // Passo 4: Retorna o índice hash, garantindo que esteja dentro do tamanho da tabela.
        return (int) Math.floorMod(meio, this.size);
    }
}
