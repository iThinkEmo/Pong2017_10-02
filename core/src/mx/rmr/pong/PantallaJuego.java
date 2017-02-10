package mx.rmr.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport. Viewport;

/**
 * Created by User on 24/01/2017.
 */
public class PantallaJuego implements Screen {

    private final Pong pong;
    // Tamaño del mundo
    public static final float ANCHO = 800;
    public static final float ALTO = 600;
    // Cámara
    private OrthographicCamera camara;
    private Viewport vista;
    // Administra los trazos sobre la pantalla
    private SpriteBatch batch;
    // Texturas
    private Texture texturaBarraHorizontal;
    private Texture texturaRaqueta;
    private Texture texturaCuadro;
    // Objetos en el juego
    private Pelota pelota;
    private Raqueta raquetaComputadora;
    private Raqueta raquetaJugador;

    // Puntos del jugador y computadora
    private int puntosJugador = 0;
    private int puntosMaquina = 0;
    private Texto texto; // Para poner mensajes en la pantalla

    // Estado del juego
    private Estado estado = Estado.JUGANDO;

    public PantallaJuego(Pong pong) {
        this.pong = pong;
    }

    @Override
    public void show() {
        // Al mostrarse esta pantalla
        // Crear la camara
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
        batch = new SpriteBatch();
        cargarTexturas();
        crearObjetos();
        // Indica quién atiende los eventos del touch
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearObjetos() {
        pelota = new Pelota (texturaCuadro, ANCHO/2, ALTO/2);
        raquetaComputadora = new Raqueta(texturaRaqueta, ANCHO-texturaRaqueta.getWidth(), ALTO/2);
        raquetaJugador = new Raqueta(texturaRaqueta,0,ALTO/2);
        texto = new Texto();
    }

    private void cargarTexturas() {
        texturaBarraHorizontal = new Texture("1200x20.png");
        texturaRaqueta = new Texture("20x100.png");
        texturaCuadro = new Texture("20x20.png");
    }

    @Override
    public void render(float delta) {
        if (estado==Estado.JUGANDO){
            // Actualizar los objetos en la pantalla
            pelota.mover(raquetaJugador);
            raquetaComputadora.seguirPelota(pelota);
        }

        // Prueba si pierde el jugador
        if (estado== Estado.JUGANDO && pelota.sprite.getX()<=0){
            puntosMaquina++;
            if (puntosMaquina>=5){
                // Perdió el jugador
                estado = Estado.PERDIO;
            }
            // Reinicia
            pelota.sprite.setPosition(ANCHO/2,ALTO/2);
        }

        // Dibuja en la pantalla aprox. 60 veces por segundo
        // Borrar la pantalla
        Gdx.gl.glClearColor(0,0,0,1); // Blanco opaco
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        // Aquí dibujaremos los elementos del juego
        batch.draw(texturaBarraHorizontal,0,0);
        batch.draw(texturaBarraHorizontal,0,ALTO-texturaBarraHorizontal.getHeight());
        // Linea central
        for(float y=0; y<ALTO; y+=2*texturaCuadro.getHeight()){
            batch.draw(texturaCuadro,ANCHO/2-texturaCuadro.getWidth()/2,y);
        }
        // Dibujamos la pelota
        pelota.dibujar(batch); // Llarmarlo dentro de batch.begin()-batch.end()
        // Raquetas
        raquetaComputadora.dibujar(batch);
        raquetaJugador.dibujar(batch);
        //batch.draw(texturaRaqueta,0,ALTO/2);
        //batch.draw(texturaRaqueta,ANCHO-texturaRaqueta.getWidth(),ALTO/2);

        //Marcador
        texto.mostrarMensaje(batch, Integer.toString(puntosJugador),
                ANCHO/2-ANCHO/6, 3*ALTO/4);
        texto.mostrarMensaje(batch, Integer.toString(puntosMaquina),
                ANCHO/2+ANCHO/6, 3*ALTO/4);

        // Perdió, reiniciar
        if (estado!=Estado.JUGANDO){
            texto.mostrarMensaje(batch, "Lo siento, perdiste", ANCHO/2,ALTO/2);
            texto.mostrarMensaje(batch, "Tap para continuar", 3*ANCHO/4, ALTO/4);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    // UNA clase para capturar los eventos del touch (Teclado y mouse también)
    class ProcesadorEntrada implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // Revisar si hace tap para reiniciar el juego
            if(estado==Estado.PERDIO){
                puntosMaquina=0;
                puntosJugador=0;
                pelota.reset();

                estado = Estado.JUGANDO;
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            Vector3 v = new Vector3(screenX,screenY,0);
            camara.unproject(v); //Transforma de un sistema a otro
            raquetaJugador.sprite.setY(v.y);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
