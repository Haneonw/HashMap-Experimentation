import hash.*;

public class Benchmark{
    public static void main(String[] args){
        
        FuncaoHash modular = new Modular(4);
        FuncaoHash multiplicativa = new Multiplicativa(4);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(4);
        FuncaoHash murmurHash3 = new MurmurHash3(4);
        FuncaoHash Universal = new Universal(4);
        int[] chaves = {0, 1, 5, 15, 25,11 ,13 ,41 ,23 ,21 ,1 ,2 ,432};
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