/**
 * Implementação da função hash utilizando o método do meio do quadrado.
 * 
 * A chave é elevada ao quadrado e uma região central dos bits do resultado é utilizada para gerar o índice da tabela hash.
 * Essa técnica tenta reduzir padrões existentes nas chaves originais e favorecer uma distribuição mais uniforme dos elementos.
 */
public class MeioDoQuadrado extends FuncaoHash{
    public MeioDoQuadrado(int size){

        super(size);

    }

    /**
     * Calcula o índice hash para uma determinada chave.
     * 
     * @param chave A chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        // Passo 1: Converte a chave para um valor long para evitar overflow ao calcular o quadrado.
        long valor = Integer.toUnsignedLong(chave);
        
        // Passo 2: Calcula o quadrado da chave.
        long quadrado = valor * valor;

        // Passo 3: Extrai bits centrais do quadrado, deslocando 16 bits à direita e descartando os 16 bits menos significativos e preservando os 32 bits seguintes.
        long meio = (int)((quadrado >>> 16) & 0xFFFFFFFFL);

        // Passo 4: Retorna o índice hash, garantindo que esteja dentro do tamanho da tabela.
        return (int) Math.floorMod(meio, this.size);
    }
}
