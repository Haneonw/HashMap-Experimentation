package hash;
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
 * Uma colisão é registrada sempre que uma nova chave distinta é inserida em um bucket que já contém pelo menos um elemento.
 *
 * Escolhemos endereçamento fechado como método de lidar com as colisões, pois não precisaremos lidar com Resize e Rehash. 
 */
public abstract class FuncaoHash {

    /**
     * Vamos medir colisões, espelhamento. Seria interessante pesquisar sobre efeito avalanche caso haja tempo e como bonus adicionar o tempo de exec, por mais que joão arthur falar q não faz mt sentido.
     */
    private int colisoes;
    private ArrayList<InfoObjeto>[] tabela;
    private int[] distribuicao;
    protected int size; // escolher um size.
    protected int numElementos; // número de elementos inseridos na tabela. útil para saber fator de carga.
    protected String nomeFunc;

    public FuncaoHash(int size){
        if(size <= 0){
            throw new IllegalArgumentException("Tamanho deve ser maior que 0");
        }
        this.colisoes = 0;
        this.size = size;
        this.distribuicao = new int[size];
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
            this.distribuicao[hash]++;
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
            this.distribuicao[hash]++;
        }
    }

    public InfoObjeto remove(int chave){
        int hash = hash(chave);
        ArrayList<InfoObjeto> lista = this.tabela[hash];
        InfoObjeto retorno = null;

        if(lista == null){
            return null;
        }
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getChave() == chave){
                retorno = lista.remove(i);
                this.distribuicao[hash]--;
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

    /**
     * 
     * @return
     */
    private double espalhamento(){
        double soma = 0;
        for(int s : this.distribuicao){
            soma += s;
        }

        double media = (double) soma / this.size;

        double somaDesvio = 0;

        for(int elmnt : this.distribuicao){
            somaDesvio += Math.pow(media - elmnt, 2);
        }

        double desvioPadrão = Math.sqrt(somaDesvio/this.size);
        if(media == 0){
            return 0.0;
        }
        return desvioPadrão / media;
    }

    private int maiorCadeia(){
        int maior = 0;
        for(ArrayList<InfoObjeto> lista : this.tabela )
        {
            if(lista != null && lista.size() > maior){
                maior = lista.size();
            }
        }

        return maior;
    }

    protected abstract int hash(int chave);

    @Override
    public String toString(){ //Medição da função hash.
        return "\n" +
        "Colisoes: " + this.colisoes + "\n" +
        "Coeficiente de variação: " +  String.format("%.2f", espalhamento()*100) + "%\n" +
        "Maior Cadeia: " + maiorCadeia();
    }

    public String getNomeFunc() {
        return nomeFunc;
    }
}
