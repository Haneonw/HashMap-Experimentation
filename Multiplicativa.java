public class Multiplicativa extends FuncaoHash{

    //Constante 
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
