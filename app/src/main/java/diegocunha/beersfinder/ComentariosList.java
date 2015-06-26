package diegocunha.beersfinder;

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Funcao: ComentariosList                               ****
 * Funcionalidade: Lista para os comentarios             ****
 * Data Criacao: 21/06/2015                              ****
 ***********************************************************/
public class ComentariosList
{
    //Variaveis globais
    private String strNomeBar, strRuaBar, strUser, strComentario;

    public ComentariosList(){};

    //Recebe itens para serem adicionados na lista
    public ComentariosList(String strNomeBar, String strRuaBar, String strUser, String strComentario)
    {
        this.strNomeBar = strNomeBar;
        this.strRuaBar = strRuaBar;
        this.strUser = strUser;
        this.strComentario = strComentario;
    }

    //NomeBar
    public String getStrNomeBar()
    {
        return strNomeBar;
    }

    public void setStrNomeBar(String strNomeBar)
    {
        this.strNomeBar = strNomeBar;
    }

    //RuaBar
    public String getStrRuaBar()
    {
        return strRuaBar;
    }

    public void setStrRuaBar(String strRuaBar)
    {
        this.strRuaBar = strRuaBar;
    }

    //Usuario
    public String getStrUser()
    {
        return strUser;
    }

    public void setStrUser(String strUser)
    {
        this.strUser = strUser;
    }

    //Comentario
    public String getStrComentario()
    {
        return strComentario;
    }

    public void setStrComentario(String strComentario)
    {
        this.strComentario = strComentario;
    }
}
