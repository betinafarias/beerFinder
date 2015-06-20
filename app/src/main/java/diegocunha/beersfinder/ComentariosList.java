package diegocunha.beersfinder;

/**
 * Created by SnowGhost on 19/06/2015.
 */
public class ComentariosList
{
    private String strNomeBar, strRuaBar, strUser, strComentario;

    public ComentariosList(){};

    public ComentariosList(String strNomeBar, String strRuaBar, String strUser, String strComentario)
    {
        this.strNomeBar = strNomeBar;
        this.strRuaBar = strRuaBar;
        this.strUser = strUser;
        this.strComentario = strComentario;
    }

    public String getStrNomeBar()
    {
        return strNomeBar;
    }

    public void setStrNomeBar(String strNomeBar)
    {
        this.strNomeBar = strNomeBar;
    }

    public String getStrRuaBar()
    {
        return strRuaBar;
    }

    public void setStrRuaBar(String strRuaBar)
    {
        this.strRuaBar = strRuaBar;
    }

    public String getStrUser()
    {
        return strUser;
    }

    public void setStrUser(String strUser)
    {
        this.strUser = strUser;
    }

    public String getStrComentario()
    {
        return strComentario;
    }

    public void setStrComentario(String strComentario)
    {
        this.strComentario = strComentario;
    }
}
