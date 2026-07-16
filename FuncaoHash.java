/**
 * FuncaoHash
 * 
 * Classe abstrata para servir de base para cada função hash.
 * 
 * Todas as funções hash compartilham uma mesma estrutura e alguns comportamentos
 * comuns, como atributos e implementações de métodos como equals() e toString().
 * Entretanto, cada função possui sua própria implementação. Por esse motivo, foi 
 * utilizada uma classe abstrata em vez de uma interface.
 */
public abstract class FuncaoHash {

    /**
     * Vamos medir colisões, espelhamento. Seria interessante pesquisar sobre efeito avalanche caso haja tempo e como bonus adicionar o tempo de exec, por mais que joão arthur falar q não faz mt sentido.
     */
    private int colisoes;
    private int espelhamento; // necessidade de decidir como vamos medir isso. 
    private String nomeFunc;

    public abstract void add();

    public abstract void remove();

    public abstract void acessar(int chave);

    @Override
    public String toString(){ //Informação textual das colisões de cada função hash.
        return
        "Função Hash: " + this.nomeFunc + "\n" + 
        "Colisoes: " + this.colisoes;
    }
}
