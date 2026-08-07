package src.main.java.hash;

/**
 * InfoObjeto
 *
 * Representa um par chave/valor armazenado em um bucket da tabela hash.
 * Cada instância guarda a chave inserida e a informação (valor) associada
 * a ela.
 */
public class InfoObjeto {
    private int chave;
    private String info;

    /**
     * Cria um novo objeto contendo a chave e o valor a serem armazenados
     * na tabela hash.
     *
     * @param chave a chave a ser armazenada
     * @param info  o valor associado à chave
     */
    InfoObjeto(int chave, String info){
        this.chave = chave;
        this.info = info;
    }

    /**
     * Retorna a chave armazenada neste objeto.
     *
     * @return a chave armazenada
     */
    public int getChave() {
        return chave;
    }

    /**
     * Retorna o valor (informação) associado à chave armazenada neste
     * objeto.
     *
     * @return o valor associado à chave
     */
    public String getInfo() {
        return info;
    }
}
