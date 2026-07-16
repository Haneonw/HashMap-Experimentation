import java.util.ArrayList;

/**
 * FuncaoHash
 * 
 * Classe abstrata para servir de base para cada função hash.
 * 
 * Todas as funções hash compartilham uma mesma estrutura e alguns comportamentos
 * comuns, como atributos e implementações de métodos como equals() e toString().
 * Entretanto, cada função possui sua própria implementação do hash. Por esse motivo, foi 
 * utilizada uma classe abstrata em vez de uma interface.
 * 
 */
public abstract class FuncaoHash {

    /**
     * Vamos medir colisões, espelhamento. Seria interessante pesquisar sobre efeito avalanche caso haja tempo e como bonus adicionar o tempo de exec, por mais que joão arthur falar q não faz mt sentido.
     */
    private int colisoes;
    private int espelhamento; // necessidade de decidir como vamos medir isso.
    private ArrayList<Integer>[] tabela;
    protected int size; 
    protected String nomeFunc;

    public FuncaoHash(int size){
        this.colisoes = 0;
        this.espelhamento = 0;
        this.size = size;
        this.tabela = new ArrayList[size];
    }

    public void put(int chave, int valor){
        int hash = hash(chave);
    }

    public void remove(int chave){

    }

    public int get(int chave){
        int hash = hash(chave);
        return 0;
    }

    protected abstract int hash(int chave);

    @Override
    public String toString(){ //Informação textual das colisões de cada função hash.
        return
        "Função Hash: " + this.nomeFunc + "\n" + 
        "Colisoes: " + this.colisoes + "\n" +
        "Espalhamento: " + this.espelhamento + "\n";
    }
}
