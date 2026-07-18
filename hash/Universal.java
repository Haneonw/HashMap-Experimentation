package hash;
import java.util.Random;
/**
 * Universal
 * 
 * Implementação do método do hash Universal.
 * 
 * Explicação :p
 *
 * h (k) = ((a ⋅ k + b) % p) % m
 * 
 * onde:
 * k = chave.
 * m = tamanho da tabela.
 * p = maior que qualquer chave possível.
 * a: inteiro aleatório entre 1 e p−1.
 * b: inteiro aleatório entre 0 e p−1.
 */
public class Universal extends FuncaoHash {

    private final int a;
    private final int b;
    private static final int p = 1000033; //Primo muito maior do que qualquer chave possível.

    public Universal(int size) {
        super(size);

        this.nomeFunc = "Universal";

        Random rand = new Random();

        // Definir a, inteiro aleatório entre 1 e p−1.
        a = rand.nextInt(p - 1) + 1;
        // Definir b, inteiro aleatório entre 0 e p−1.
        b = rand.nextInt(p);
    }

    @Override
    protected int hash(int chave) {
        // Passo 1: Multiplicar a chave pelo a definido e somar b.
        long valor = ((long)a * chave + b);

        // Passo 2: Fazer o resto entre o resultado e o primo escolhido.
        valor = valor % p;

        // Passo 3: Pega o valor gerado pela a equação e dividir pelo o tamanho da tabela, garantido que gera um indice valido.
        return (int)(valor % size);
    }
}