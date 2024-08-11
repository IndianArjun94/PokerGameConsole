package Game.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lwjgl.opengl.GL20;

public class Render extends ApplicationAdapter {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration application = new Lwjgl3ApplicationConfiguration();
        application.setTitle("Window");
        application.setWindowedMode(1280, 720);
        application.useVsync(false);
        application.setForegroundFPS(120);
        new Lwjgl3Application(new Render(), application);
    }

    ShapeRenderer shape;
    int x = 100;
    int y = 100;

    @Override
    public void create() {
//        TODO creation code blah blah blah
        shape = new ShapeRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen each time we render

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(x, y, 50);
        shape.end();

        x+=2;

    }

}
