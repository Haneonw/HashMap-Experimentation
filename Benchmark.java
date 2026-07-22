import hash.*;

public class Benchmark{
    public static void main(String[] args){
        int tamanho = 10;
        
        FuncaoHash modular = new Modular(tamanho);
        FuncaoHash multiplicativa = new Multiplicativa(tamanho);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(tamanho);
        FuncaoHash murmurHash3 = new MurmurHash3(tamanho);
        FuncaoHash Universal = new Universal(tamanho);
        int[] chaves = {0, 1, 5, 15, 25, 11, 13, 41, 23, 21, 1, 2, 432};
        FuncaoHash[] funcoes = {modular, multiplicativa, meioDoQuadrado, murmurHash3, Universal};

        for(FuncaoHash funcao: funcoes){
            for(int chave: chaves){
                funcao.put(chave, null);
            }
            String titulo = " " + funcao.getNomeFunc() + " ";
            System.out.println("\n" + "=".repeat(20) + titulo + "=".repeat(20));
            System.out.println(funcao);
        }
        System.out.println();
   }
}