package hash;
/** 
 * MurmurHash3
 * 
 * Implementação da função de hash MurmurHash3 adaptada para inteiros.
 * 
 * Esta implementação é baseada na versão original de Austin Appleby, preservando as etapas de mistura 
 * e finalização, mas foi simplificada para lidar apenas com inteiros. A função de hash 
 * é projetada para distribuir uniformemente os valores de entrada em um espaço de hash.
 */

public class MurmurHash3 extends FuncaoHash {
    public MurmurHash3(int size) {
        super(size);
    }

    // Constantes de mistura específicas para a versão MurmurHash3
    private final int C1 = 0xcc9e2d51;
    private final int C2 = 0x1b873593;

    //Utiliza uma semente fixa tendo em vista que o objetivo é apenas comparar a função de hash com as outras implementações
    private final int seed = 0;

    /**
     * Calcula o índice hash da chave utilizando o algoritmo MurmurHash3.
     * 
     * @param chave A chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        // Passo 1: Inicializa o hash com a semente fixa.
        int hash = seed;
        // Passo 2: Processa a chave como um bloco de 4 bytes (um inteiro).
        int bloco = chave;

        // Passo 3: Mistura o bloco usando as constantes C1 e C2, aplicando rotações e multiplicações.
        bloco *= C1;
        bloco = Integer.rotateLeft(bloco, 15);
        bloco *= C2;

        // Passo 4: Atualiza o hash com o bloco misturado.
        hash ^= bloco;
        hash = Integer.rotateLeft(hash, 13);
        hash = hash * 5 + 0xe6546b64;

        hash ^= 4; // tamanho da chave em bytes (int = 4 bytes)

        // Passo 5: Finalização do hash para garantir uma boa distribuição.
        hash ^= (hash >>> 16);
        hash *= 0x85ebca6b;
        hash ^= (hash >>> 13);
        hash *= 0xc2b2ae35;
        hash ^= (hash >>> 16);

        // Passo 6: Retorna o índice hash, garantindo que esteja dentro do tamanho da tabela.
        return Math.floorMod(hash, this.size);
    }
}