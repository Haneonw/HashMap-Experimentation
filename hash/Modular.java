package hash;
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
    public Modular(int size){
        super(size);
        this.nomeFunc = "Modular";
    }

    @Override
    protected int hash(int chave) {
        // Dividindo a chave pelo o tamanho da tabela, retornando um indice válido. 
        return chave % this.size;
    }
}

