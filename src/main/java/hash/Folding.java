package src.main.java.hash;

/**
 * Folding
 *
 * Implementação da função hash pelo método do dobramento (folding).
 *
 * A chave é dividida em blocos de tamanho fixo, com número de dígitos igual
 * ao do maior índice válido da tabela. Os blocos são somados entre si,
 * alternando-se blocos invertidos e não invertidos, e o resultado da soma
 * é reduzido ao intervalo da tabela por meio do operador módulo.
 */
public class Folding extends FuncaoHash{
    
    private final int dig;
    private final int base;

    /**
     * Cria uma nova instância da função hash de dobramento (folding).
     *
     * Calcula a quantidade de dígitos do maior índice válido da tabela e
     * define, a partir disso, o tamanho dos blocos (base) utilizados para
     * dividir a chave durante o cálculo do hash.
     *
     * @param size tamanho da tabela hash
     */
    public Folding(int size){
        super(size);

        this.dig = Integer.toString(size - 1).length();
        this.base = (int) Math.pow(10, this.dig);
    }

    /**
     * Calcula o índice hash da chave utilizando o método do dobramento.
     *
     * A chave (em módulo) é dividida em blocos de {@code dig} dígitos. A
     * cada iteração, o bloco é somado a um acumulador, sendo invertido
     * (dígitos em ordem reversa) a cada outro bloco processado. Ao final,
     * o índice é obtido pelo resto da divisão da soma acumulada pelo
     * tamanho da tabela.
     *
     * @param chave a chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        chave = Math.abs(chave);

        int soma = 0;

        boolean inverte = false;

        while (chave > 0) {
            int bloco = chave % base;
            if(inverte){
                String inv = Integer.toString(bloco);
                
                String invertido = new StringBuilder(inv).reverse().toString();

                bloco = Integer.parseInt(invertido);
            }

            soma += bloco;
            inverte = !inverte;
            chave /= base;
        }

        return soma % size;
    }
}
