package br.com.screenmatch.musicas.principal;

import br.com.screenmatch.musicas.model.Artista;
import br.com.screenmatch.musicas.model.Musica;
import br.com.screenmatch.musicas.model.TipoArtista;
import br.com.screenmatch.musicas.repository.ArtistaRepository;
import br.com.screenmatch.musicas.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal (ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {

        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***                    
                                        
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Pesquisar dados sobre um artista
                                    
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }


        }
    }

    private void pesquisarDadosDoArtista() {
        System.out.println("Pesquisar dados sobre qual artista? ");
        var nome = leitura.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

    private void buscarMusicasPorArtista() {
        System.out.println("Busar música de qual artista? ");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscaMusicaPorArtista(nome);
        musicas.forEach(System.out::println);

    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));

    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de qual artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
        if (artista.isPresent()){
            System.out.println("Digite o nome da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            //vinculação do artista com a música no banco, artista.get()
            musica.setArtista(artista.get());
            //persistencia do artista com a musca a ser cadastrada
            artista.get().getMusicas().add(musica);
            //salvar no banco a musica através do repositório de artista
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista não encontrado!");
        }

    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo .equalsIgnoreCase("S")) {
            System.out.println("Informe o nome do artista: ");
            var nome = leitura.nextLine();
            System.out.println("Informe o tipo do artisa: (solo, dupla ou banda)");
            var tipo = leitura.nextLine();

            //conversão do tipo para o enum
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());

            //criar construtor para salvar o artista
            Artista artista = new Artista(nome, tipoArtista);

            //criar a classe repository para salvar as instruções no banco
            repositorio.save(artista);
            System.out.println("Deseja cadastrar mais algum artista? (S/N)");
            cadastrarNovo = leitura.nextLine();
        }

    }
}



    
    
