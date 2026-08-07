package src.main.java.hash;
/**
 * Modular
 * 
 * Implementação do método de hash modular.
 * 
 * A implementação modular consiste usar o resto da divisão entre a chave e o tamanho da tabela.
 * Ao dividir qualquer número pelo o tamanho da tabela o resto sempre será um indice válido dentro da tabela.
 * O uso real da tabela hash consiste em usar tamanhos de tabela hash primos, já que ao usar número primos
 * no calculo modular gera uma distribuição melhor, por conta da propriedade dos números primos.
 */
public class Modular extends FuncaoHash{

    /**
     * Cria uma nova instância da função hash modular.
     *
     * @param size tamanho da tabela hash
     */
    public Modular(int size){
        super(size);
    }

    /**
     * Calcula o índice hash da chave utilizando o método modular, ou seja,
     * o resto da divisão da chave pelo tamanho da tabela.
     *
     * @param chave a chave a ser transformada
     * @return índice válido na tabela hash
     */
    @Override
    protected int hash(int chave) {
        // Dividindo a chave pelo o tamanho da tabela, retornando um indice válido. 
        return chave % this.size;
    }
}

