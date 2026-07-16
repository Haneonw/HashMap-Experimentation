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
     */
    private static final double A = (Math.sqrt(5) - 1) / 2;

    public Multiplicativa(int size){
        super(size);
        this.nomeFunc = "Multiplicativa";
    }

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
