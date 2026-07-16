public class Multiplicativa extends FuncaoHash{

    //Constante 
    private static final double A = (Math.sqrt(5) - 1 / 2);

    public Multiplicativa(int size){
        super(size);
        this.nomeFunc = "Multiplicativa";
    }

    @Override
    protected int hash(int chave) {
        //passo 1: k * A
        double produto = chave * A;

        //passo 2 parte fracionária 
        double fracao = produto - Math.floor(produto);

        //passo 3 e 4: m * fracao(kA), depois a parte inteira
        int indice = (int) Math.floor(this.size * fracao);
        
        return indice;
    }
}
