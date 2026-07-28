import hash.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class Benchmark{
    public static void main(String[] args)throws IOException{
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        int tamanho = 0;

        // Usuário escolhe qual vai ser o tamanho fixo da tabela hash.
        while (true) {
            try {
                System.out.print("Tamanho da tabela: ");
                tamanho = Integer.parseInt(entrada.readLine());

                if (tamanho > 0)
                    break;

            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro positivo.");
            }
        }
        
        ArrayList<Integer> chaves = new ArrayList<>();
        
        // Coleta de chaves de um arquivo.
        System.out.print("Escolha um arquivo com as chaves: ");

        String arquivo = entrada.readLine();
        
        chaves = carregarChaves(arquivo);

        if (chaves == null) return;

        // Todas as funções hash que serão testadas:
        FuncaoHash modular = new Modular(tamanho);
        FuncaoHash multiplicativa = new Multiplicativa(tamanho);
        FuncaoHash meioDoQuadrado = new MeioDoQuadrado(tamanho);
        FuncaoHash murmurHash3 = new MurmurHash3(tamanho);
        FuncaoHash folding = new Folding(tamanho);
        FuncaoHash[] funcoes = {modular, multiplicativa, meioDoQuadrado, murmurHash3, folding};

        // Inserindo as chaves em cada tabela hash.
        for(FuncaoHash funcao: funcoes){
            for(int chave: chaves){
                funcao.put(chave, null);
            }
            String titulo = " " + funcao.getNomeFunc() + " ";
            System.out.println("\n" + "=".repeat(20) + titulo + "=".repeat(20));
            System.out.println(funcao);
        }

        String nome = arquivo.substring(0, arquivo.lastIndexOf('.'));

        PrintWriter writer = new PrintWriter(new FileWriter("resultados/resultados_" + nome +".csv"));

        writer.println("Funcao,Dataset,TamanhoTabela,Colisoes,CV,MaiorCadeia,MediaCadeia");

        for (FuncaoHash f : funcoes) {

            // executa benchmark

            writer.println(
                f.getNomeFunc() + "," +
                arquivo + "," +
                tamanho + "," +
                f.getColisoes() + "," +
                f.getCoeficienteVariacao() + "," +
                f.getMaiorCadeia() + "," +
                f.getMediaCadeia()
            );
        }

        writer.close();
   }

    private static ArrayList<Integer> carregarChaves(String arquivo) throws IOException{
        FileReader arq;
        try{
            arq = new FileReader("dados/" + arquivo);
        }catch (FileNotFoundException e){
            System.out.println("Arquivo não encontrado.");
            return null;
        }

        BufferedReader leitorArq = new BufferedReader(arq);

        ArrayList<Integer> chaves = new ArrayList<>();

        String linha;
        try{
            if(arquivo.endsWith(".txt")){
                while((linha = leitorArq.readLine()) != null){
                    chaves.add(Integer.parseInt(linha));
                }
            }
            else if (arquivo.endsWith(".csv")){
                leitorArq.readLine();
                while((linha = leitorArq.readLine()) != null){
                    String[] info = linha.split(",");
                    chaves.add(Integer.parseInt(info[0]));
                }
            } 
            else {
                System.out.println("Formato não suportado.");
                fecha(arq,leitorArq);
                return null;
            }
        }catch (Exception e){

            System.out.println("Formato do arquivo inválido.");
            fecha(arq,leitorArq);
            return null;
        }
        fecha(arq,leitorArq);

        return chaves;
   }

    private static void fecha(FileReader arq, BufferedReader leitor){
        try{
            arq.close();
            leitor.close();
        }catch(Exception e){}
   }
}
