import hash.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Benchmark{
    public static void main(String[] args)throws IOException{
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

        // Usuário escolhe qual vai ser o tamanho fixo da tabela hash.
        System.out.print("Tamanho da tabela: ");

        ArrayList<Integer> chaves = new ArrayList<>();

        int tamanho = Integer.parseInt(entrada.readLine());   
        
        // Coleta de chaves de um arquivo.
        System.out.print("Escolha um arquivo com as chaves: ");

        String arquivo = entrada.readLine();
        
        chaves = carregarChaves(arquivo);

        // Todas as funções hash que serão testadas:
        FuncaoHash modular = new Modular(tamanho);
        FuncaoHash multiplicativa = new Multiplicativa(tamanho);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(tamanho);
        FuncaoHash murmurHash3 = new MurmurHash3(tamanho);
        FuncaoHash folding = new Folding(tamanho);
        FuncaoHash universal = new Universal(tamanho);
        FuncaoHash[] funcoes = {modular, multiplicativa, meioDoQuadrado, murmurHash3, universal, folding};

        // Inserindo as chaves em cada tabela hash.
        for(FuncaoHash funcao: funcoes){
            for(int chave: chaves){
                funcao.put(chave, null);
            }
            String titulo = " " + funcao.getNomeFunc() + " ";
            System.out.println("\n" + "=".repeat(20) + titulo + "=".repeat(20));
            System.out.println(funcao);
        }

        entrada.close();
   }

    private static ArrayList<Integer> carregarChaves(String arquivo) throws IOException
   {
        BufferedReader arq = new BufferedReader(new FileReader("dados/" + arquivo));

        ArrayList<Integer> chaves = new ArrayList<>();

        String tipo = arquivo.substring(arquivo.length() - 3);

        String linha;
        if(tipo.equals("txt")){
            while((linha = arq.readLine()) != null){
                chaves.add(Integer.parseInt(linha));
            }
        }
        else if (tipo.equals("csv")){
            arq.readLine();
            while((linha = arq.readLine()) != null){
                String[] info = linha.split(",");
                chaves.add(Integer.parseInt(info[0]));
            }
        }

        arq.close();

        return chaves;
   }
}
