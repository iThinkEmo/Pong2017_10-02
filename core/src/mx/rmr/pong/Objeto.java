package mx.rmr.pong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Representa un objeto que forma parte del juego (pelota,raqueta)
 * Created by User on 30/01/2017.
 */

public class Objeto {
    protected Sprite sprite;
    //protected permite que la variable sea accedida
    // en las subclases para mejorar el rendimiento

    // Crea el objeto con cierta textura en la posición x, y
    public Objeto(Texture textura, float x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }
}
