package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoPost;
import com.algaworks.junit.blog.exception.PostNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CadastroPostTest {

    @Spy
    Post post = new Post("Titulo","Conteudo",
            new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true),
            false, false);

    Ganhos ganhos = new Ganhos(BigDecimal.TEN, 1, BigDecimal.TEN);

    Long idPost = 1L;

    @InjectMocks
    private CadastroPost cadastroPost;

    @Mock
    private ArmazenamentoPost armazenamentoPost;

    @Mock
    private CalculadoraGanhos calculadoraGanhos;

    @Mock
    private GerenciadorNotificacao gerenciadorNotificacao;

    @Nested
    class CriacaoPostErro{

        @Test
        void Dado_um_post_null_Entao_deve_lanca_NullPointerException(){
            //Act & Assert
            Assertions.assertThrows(NullPointerException.class, () -> cadastroPost.criar(null));
        }
    }

    @Nested
    class CriacaoPostSucesso{

        @BeforeEach
        void init(){
            //Arrange
            Mockito.when(calculadoraGanhos.calcular(post)).thenReturn(ganhos);
            Mockito.when(armazenamentoPost.salvar(post)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Post.class));
            Mockito.doNothing().when(gerenciadorNotificacao).enviar(any());
        }


        @Test
        void Dado_um_post_Entao_deve_cadastrar_com_sucesso(){

            //Act
            var postCriado = cadastroPost.criar(post);

            //Assert
            //Verifica a ordem das chamadas
            InOrder inOrder = Mockito.inOrder(post, calculadoraGanhos,armazenamentoPost,gerenciadorNotificacao);
            inOrder.verify(post).setSlug(any());
            inOrder.verify(calculadoraGanhos).calcular(any());
            inOrder.verify(post).setGanhos(any());
            inOrder.verify(armazenamentoPost).salvar(any());

            //Verifica se foi chamado uma vez
            Mockito.verify(gerenciadorNotificacao, Mockito.times(1)).enviar(any());
            Mockito.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
            Mockito.verify(calculadoraGanhos, Mockito.times(1)).calcular(post);

            Assertions.assertEquals(ganhos, postCriado.getGanhos());

        }
    }

    @Nested
    class EdicaoPostErro{

        @Test
        void Dado_um_post_null_Entao_deve_lanca_NullPointerException(){
            //Act & Assert
            Assertions.assertThrows(NullPointerException.class, () -> cadastroPost.editar(null));
        }

        @Test
        void Dado_um_post_que_nao_existe_Entao_deve_lanca_PostNaoEncontradoException(){
            //Arrange
            post.setId(2L);
            Mockito.when(armazenamentoPost.encontrarPorId(post.getId())).thenReturn(Optional.empty());

            //Act & Assert
            Assertions.assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.editar(post));
        }
    }

    @Nested
    class EdicaoPostSucesso{

        @BeforeEach
        void init(){
            post.setId(idPost);
            Mockito.when(armazenamentoPost.encontrarPorId(idPost)).thenReturn(Optional.of(post));
            Mockito.when(armazenamentoPost.salvar(post)).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Post.class));
        }


        @Test
        void Dado_um_post_existente_que_nao_esta_pago_Entao_deve_editar_com_sucesso(){
            //Arrange
            Mockito.when(calculadoraGanhos.calcular(post)).thenReturn(ganhos);

            //Act
            var postEditado = cadastroPost.editar(post);

            //Assert
            //Verifica a ordem das chamadas
            InOrder inOrder = Mockito.inOrder(post, calculadoraGanhos,armazenamentoPost,gerenciadorNotificacao);
            inOrder.verify(armazenamentoPost).encontrarPorId(post.getId());
            inOrder.verify(post).atualizarComDados(post);
            inOrder.verify(calculadoraGanhos).calcular(any());
            inOrder.verify(post).setGanhos(any());
            inOrder.verify(armazenamentoPost).salvar(post);


            //Verifica se foi chamado uma vez
            Mockito.verify(armazenamentoPost, Mockito.times(1)).encontrarPorId(post.getId());
            Mockito.verify(calculadoraGanhos, Mockito.times(1)).calcular(post);
            Mockito.verify(armazenamentoPost, Mockito.times(1)).salvar(post);

            Assertions.assertEquals(ganhos, postEditado.getGanhos());
            Assertions.assertEquals(post.getId(), postEditado.getId());

        }

        @Test
        void Dado_um_post_existente_que_esta_pago_Entao_deve_editar_com_sucesso(){

            //Act
            post.setPago(true);
            var postEditado = cadastroPost.editar(post);

            //Assert
            //Verifica a ordem das chamadas
            InOrder inOrder = Mockito.inOrder(post, calculadoraGanhos,armazenamentoPost,gerenciadorNotificacao);
            inOrder.verify(armazenamentoPost).encontrarPorId(post.getId());
            inOrder.verify(post).atualizarComDados(post);
            inOrder.verify(armazenamentoPost).salvar(post);


            //Verifica se foi chamado uma vez
            Mockito.verify(armazenamentoPost, Mockito.times(1)).encontrarPorId(post.getId());
            Mockito.verify(armazenamentoPost, Mockito.times(1)).salvar(post);

            Assertions.assertEquals(post.getId(), postEditado.getId());

        }
    }

    @Nested
    class RemovidoPostErro{

        @Test
        void Dado_um_idPost_null_Entao_deve_lanca_NullPointerException(){
            //Act & Assert
            Assertions.assertThrows(NullPointerException.class, () -> cadastroPost.remover(null));
        }

        @Test
        void Dado_um_IdPost_que_nao_existe_Entao_deve_lanca_PostNaoEncontradoException(){
            //Arrange
            Mockito.when(armazenamentoPost.encontrarPorId(idPost)).thenReturn(Optional.empty());

            //Act & Assert
            Assertions.assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.remover(idPost));
        }

        @Test
        void Dado_um_post_publicado_Entao_deve_lanca_RegraNegocioException(){
            //Arrange
            post.setPublicado(true);
            Mockito.when(armazenamentoPost.encontrarPorId(idPost)).thenReturn(Optional.of(post));

            //Act & Assert
            Assertions.assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idPost));
        }

        @Test
        void Dado_um_post_nao_publicado_mas_pago_Entao_deve_lanca_RegraNegocioException(){
            //Arrange
            post.setPublicado(false);
            post.setPago(true);
            Mockito.when(armazenamentoPost.encontrarPorId(idPost)).thenReturn(Optional.of(post));

            //Act & Assert
            Assertions.assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idPost));
        }
    }

    @Nested
    class RemovidoPostSucesso{

        @Test
        void Dado_um_post_existente_que_nao_esta_pago_Entao_deve_editar_com_sucesso(){
            //Arrange
            Mockito.when(armazenamentoPost.encontrarPorId(idPost)).thenReturn(Optional.of(post));
            Mockito.doNothing().when(armazenamentoPost).remover(idPost);

            //Act
            cadastroPost.remover(idPost);

            //Assert
            //Verifica a ordem das chamadas
            InOrder inOrder = Mockito.inOrder(post,armazenamentoPost,gerenciadorNotificacao);
            inOrder.verify(armazenamentoPost).encontrarPorId(idPost);
            inOrder.verify(armazenamentoPost).remover(idPost);


            //Verifica se foi chamado uma vez
            Mockito.verify(armazenamentoPost, Mockito.times(1)).encontrarPorId(idPost);
            Mockito.verify(armazenamentoPost, Mockito.times(1)).remover(idPost);

        }

    }
}