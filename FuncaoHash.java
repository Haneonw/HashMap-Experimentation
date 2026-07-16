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
 * Escolhemos encadeamento fechado como método de lidar com as colisões, pois não precisaremos lidar com Resize e Rehash. 
 */
public abstract class FuncaoHash {

    /**
     * Vamos medir colisões, espelhamento. Seria interessante pesquisar sobre efeito avalanche caso haja tempo e como bonus adicionar o tempo de exec, por mais que joão arthur falar q não faz mt sentido.
     */
    private int colisoes;
    private int espelhamento; // necessidade de decidir como vamos medir isso.
    private ArrayList<InfoObjeto>[] tabela;
    protected int size; // escolher um size.
    protected String nomeFunc;

    public FuncaoHash(int size){
        this.colisoes = 0;
        this.espelhamento = 0;
        this.size = size;
        this.tabela = new ArrayList[size];
    }

    public void put(int chave, String valor){
        int hash = hash(chave);
        ArrayList<InfoObjeto> lista = this.tabela[hash];

        InfoObjeto obj = new InfoObjeto(chave, valor);
        
        if(lista == null)
        {
            lista = new ArrayList<InfoObjeto>();
            lista.add(obj);
            this.tabela[hash] = lista;
        }
        else
        {
            for(int i = 0; i < lista.size(); i++){
                if(lista.get(i).getChave() == chave){
                    lista.set(i, obj);
                    return;
                }
            }
            this.colisoes++;
            lista.add(obj);
        }
    }

    public InfoObjeto remove(int chave){
        int hash = hash(chave);
        ArrayList<InfoObjeto> lista = this.tabela[hash];
        InfoObjeto retorno = null;

        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getChave() == chave){
                retorno = lista.remove(i);
                break;
            }
        }

        return retorno;
    }

    public InfoObjeto get(int chave){
        int hash = hash(chave);
        ArrayList<InfoObjeto> lista = this.tabela[hash];
        InfoObjeto retorno = null;
        if(lista != null)
        {
            for(InfoObjeto s : lista)
            {
                if(s.getChave() == chave){
                    retorno = s;
                    break;
                }
            }
        }

        return retorno;
    }

    protected abstract int hash(int chave);

    @Override
    public String toString(){ //Medição da função hash.
        return
        "Função Hash: " + this.nomeFunc + "\n" + 
        "Colisoes: " + this.colisoes + "\n" +
        "Espalhamento: " + this.espelhamento + "\n";
    }
}
