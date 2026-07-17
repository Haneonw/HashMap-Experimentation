public class Benchmark{
    public static void main(String[] args){
        
        FuncaoHash modular = new Modular(4);
        FuncaoHash multiplicativa = new Multiplicativa(4);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(4);
        int[] chaves = {0, 1, 5, 15, 25, 11};
        FuncaoHash[] funcoes = {modular, multiplicativa, meioDoQuadrado};

        for(FuncaoHash funcao: funcoes){
            for(int chave: chaves){
                funcao.put(chave, null);
            }
            System.out.println(funcao.toString());
        }
   }
}