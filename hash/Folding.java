package hash;

public class Folding extends FuncaoHash{
    
    private final int dig;
    private final int base;

    public Folding(int size){
        super(size);
        this.nomeFunc = "Folding";

        this.dig = Integer.toString(size - 1).length();
        this.base = (int) Math.pow(10, this.dig);
    }

    @Override
    protected int hash(int chave) {
        chave = Math.abs(chave);

        int soma = 0;

        boolean inverte = false;

        while (chave > 0) {
            int bloco = chave % base;
            if(inverte){
                String inv = Integer.toString(bloco);
                
                String invertido = new StringBuilder(inv).reverse().toString();

                bloco = Integer.parseInt(invertido);
            }

            soma += bloco;
            inverte = !inverte;
            chave /= base;
        }

        return soma % size;
    }
}
