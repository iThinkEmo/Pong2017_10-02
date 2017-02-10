package mx.rmr.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by User on 30/01/2017.
 */

public class Pelota extends Objeto {
    private float DX = 4; // Incremento en x, define el desplazamiento en cada frame
    private float DY = 4; // Incremento en y

    private final float ALTO_MARGEN = 20; // Grosor de la pared arriba-abajo
    private final float ANCHO_RAQUETA = 20; // Grosor de la raqueta
    private final float ALTO_RAQUETA = 100; // Alto de la raqueta

    public Pelota(Texture textura, float x, float y){
        super(textura, x, y);
    }

    // Se llama desde el método render del juego
    public void dibujar(SpriteBatch batch){
        sprite.draw(batch);
    }

    // Movimiento de la pelota en la cancha
    public void mover(Raqueta raqueta){
        float xp = sprite.getX();
        float yp = sprite.getY();

        // Prueba límites DERECHA, la raqueta de la máquina
        if (xp>=PantallaJuego.ANCHO){
            DX = -DX; //Invierte el sentido
        }
        // Prueba colisión con la raqueta del jugador
        float xr = raqueta.sprite.getX(); // esta raqueta debe llegar como parámetro
        float yr = raqueta.sprite.getY();
        if(xp>=xr && xp<=xr+ANCHO_RAQUETA && yp>=yr && yp<=(yr+ALTO_RAQUETA-ALTO_MARGEN)){
            DX = -DX;
        }
        // Acelerar la pelota (buscar un mejor algoritmo :)
        if(DX>=0){
            DX += Gdx.graphics.getDeltaTime()/5; //Este factor puede depender del nivel
        }
        if(DY>=0){
            DY += Gdx.graphics.getDeltaTime()/5;
        }
        // Prueba límites ARRIBA-ABAJO
        if (yp>=PantallaJuego.ALTO || yp<=0){
            DY = -DY;
        }

        // Poner la pelota en la nueva posición
        sprite.setX(xp+DX);
        sprite.setY(yp+DY);
    }

    public void reset() {
        sprite.setPosition(PantallaJuego.ANCHO/2, PantallaJuego.ALTO/2);
        DX = 4;
        DY = 4;
    }
}
