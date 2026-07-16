/**
 * Modular: explicação
 * 
 */
public class Modular extends FuncaoHash{
    public Modular(int size){
        super(size);
        this.nomeFunc = "Modular";
    }

    /**
     * explicação do método.
     */
    @Override
    protected int hash(int chave) {
        return chave % this.size;
    }
}

