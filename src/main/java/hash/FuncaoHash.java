package src.main.java.hash;
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
 * Uma colisão é registrada sempre que uma nova chave distinta é inserida em um bucket que já
 * contém pelo menos um elemento.
 *
 * Foi utilizado encadeamento separado, também denominado endereçamento fechado,
 * como método de lidar com as colisões, pois não precisaremos lidar com Resize e Rehash. 
 */
public abstract class FuncaoHash {


    private int colisoes;
    private ArrayList<InfoObjeto>[] tabela;
    private int[] distribuicao;
    protected int size; // escolher um size.
    protected int numElementos; // número de elementos inseridos na tabela. útil para saber fator de carga.

    /**
     * Cria uma nova função hash com uma tabela de tamanho fixo.
     *
     * @param size tamanho da tabela hash, deve ser maior que 0
     * @throws IllegalArgumentException se {@code size} for menor ou igual a 0
     */
    public FuncaoHash(int size){
        if(size <= 0){
            throw new IllegalArgumentException("Tamanho deve ser maior que 0");
        }
        this.colisoes = 0;
        this.size = size;
        this.distribuicao = new int[size];
        this.tabela = new ArrayList[size];
    }

    /**
     * Insere um par chave/valor na tabela hash.
     *
     * Caso a chave já exista na tabela, o valor associado é atualizado e
     * nenhuma colisão é contabilizada. Caso a chave seja nova e o bucket
     * calculado já contenha algum elemento, uma colisão é registrada.
     *
     * @param chave a chave a ser inserida
     * @param valor o valor a ser associado à chave
     */
    public void put(int chave, String valor){
        int hash = hash(chave);
        ArrayList<InfoObjeto> lista = this.tabela[hash];

        InfoObjeto obj = new InfoObjeto(chave, valor);
                
        if(lista == null) {
            lista = new ArrayList<InfoObjeto>();
            lista.add(obj);
            this.tabela[hash] = lista;
            this.distribuicao[hash]++;
            this.numElementos++;
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
            this.numElementos++;
        }
    }

    /**
     * Remove o elemento associado à chave informada, caso ele exista.
     *
     * @param chave a chave do elemento a ser removido
     * @return o {@link InfoObjeto} removido, ou {@code null} caso a chave
     * não seja encontrada na tabela
     */
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
                this.numElementos--;
                break;
            }
        }

        return retorno;
    }

    /**
     * Busca o elemento associado à chave informada.
     *
     * @param chave a chave a ser buscada
     * @return o {@link InfoObjeto} correspondente à chave, ou {@code null}
     *         caso a chave não esteja presente na tabela
     */
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
     * Calcula o coeficiente de variação (desvio padrão dividido pela média)
     * da distribuição de elementos entre os buckets da tabela hash.
     *
     * Quanto menor o coeficiente de variação, mais uniforme é a distribuição
     * das chaves entre os buckets da tabela.
     *
     * @return o coeficiente de variação da distribuição, ou {@code 0.0} caso
     * a média de elementos por bucket seja igual a 0
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

    /**
     * Retorna o número total de colisões ocorridas na tabela hash.
     *
     * @return quantidade de colisões registradas
     */
    public int getColisoes(){
        return this.colisoes;
    }

    /**
     * Retorna o coeficiente de variação da distribuição de elementos entre
     * os buckets da tabela hash, utilizado como medida de espalhamento.
     *
     * @return o coeficiente de variação da distribuição
     */
    public double getCoeficienteVariacao(){
        return espalhamento();
    }

    /**
     * Retorna o número de elementos atualmente inseridos na tabela hash.
     *
     * @return quantidade de elementos inseridos
     */
    public int getNumElementos(){
        return this.numElementos;
    }

    /**
     * Retorna o fator de carga da tabela hash, ou seja, a razão entre o
     * número de elementos inseridos e o tamanho da tabela.
     *
     * @return o fator de carga da tabela
     */
    public double getFatorCarga(){
        return (double) this.numElementos / this.size;
    }

    /**
     * Retorna o tamanho da maior cadeia (bucket com mais elementos) presente
     * na tabela hash.
     *
     * @return o tamanho da maior cadeia de elementos
     */
    public int getMaiorCadeia(){
        return maiorCadeia();
    }

    /**
     * Calcula o tamanho da maior cadeia (bucket com mais elementos) entre
     * todos os buckets da tabela hash.
     *
     * @return o tamanho da maior cadeia encontrada
     */
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

    /**
     * Calcula a média de elementos por cadeia (bucket) não vazia da tabela
     * hash.
     *
     * @return a média de elementos das cadeias não vazias
     */
    private int mediaCadeia(){
        int soma = 0;
        int cadeias = 0;
        for(ArrayList<InfoObjeto> lista : this.tabela )
        {
            if(lista != null){
                soma += lista.size();
                cadeias++; 
            }
        }

        return soma/cadeias;
    }

    public int getMediaCadeia(){
        return mediaCadeia();
    }

    /**
     * Calcula o índice da tabela hash correspondente a uma chave.
     *
     * Cada subclasse concreta deve implementar seu próprio algoritmo de
     * espalhamento (hashing), retornando sempre um índice válido dentro dos
     * limites da tabela.
     *
     * @param chave a chave a ser transformada em um índice
     * @return um índice válido dentro do tamanho da tabela hash
     */
    protected abstract int hash(int chave);

    /**
     * Retorna uma representação textual com as métricas de desempenho da
     * função hash, incluindo número de elementos, colisões, coeficiente de
     * variação, fator de carga, maior cadeia e média das cadeias.
     *
     * @return uma {@code String} formatada com as métricas medidas
     */
    @Override
    public String toString(){ //Medição da função hash.
        return "\n" +
        "Função: " + getNomeFunc() + "\n" +
        "Elementos: " + getNumElementos() + "\n" +
        "Colisões: " + getColisoes() + "\n" +
        "Coeficiente de variação: " +  String.format("%.2f", getCoeficienteVariacao() * 100) + "%\n" +
        "Fator de carga: " + String.format("%.2f", getFatorCarga()) + "\n" +
        "Maior Cadeia: " + getMaiorCadeia() + "\n" +
        "Média Cadeia: " + getMediaCadeia() + "\n";
    }

    /**
     * Retorna o nome da função hash, obtido a partir do nome simples da
     * subclasse concreta em execução.
     *
     * @return o nome da função hash
     */
    public String getNomeFunc() {
        return getClass().getSimpleName();
    }
}
