package io.github.umajack;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.InputProcessor;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    public final static float SCALE = 32f;
	public final static float INV_SCALE = 1.f/SCALE;
    public final static float VP_WIDTH = 2560 * INV_SCALE;
	public final static float VP_HEIGHT = 1600 * INV_SCALE;

    private ExtendViewport viewport;
    private OrthographicCamera camera;

    private AssetManager assetManager;
    private SpriteBatch batch;
    private Texture card;
    private BitmapFont font;
    private String drawMessage = "";


    // Button area definition
    private float buttonX = 200, buttonY = 250, buttonWidth = 120, buttonHeight = 60;

    private Blackjack blackjackGame;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);

        assetManager = new AssetManager();
        assetManager.load("cards/card.png", Texture.class);
        assetManager.finishLoading();
        card = assetManager.get("cards/card.png", Texture.class);
        batch = new SpriteBatch();
        font = new com.badlogic.gdx.graphics.g2d.BitmapFont();


        Gdx.input.setInputProcessor(this);

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player());
        blackjackGame = new Blackjack(players);
    }

    Vector3 tp = new Vector3();
	boolean dragging;
	@Override 
    public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override 
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        camera.unproject(tp.set(screenX, screenY, 0));
        // Check if the click is inside the button area
        float x = tp.x;
        float y = tp.y;
        if (x >= buttonX && x <= buttonX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
            float card = blackjackGame.hit();
            drawMessage = "Player 1 drew card " + card;
            return true;
        }
        dragging = true;
        return true;
	}

	@Override 
    public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		camera.unproject(tp.set(screenX, screenY, 0));
		return true;
	}

	@Override 
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		camera.unproject(tp.set(screenX, screenY, 0));
		dragging = false;
		return true;
	}

    @Override 
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); 
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        // Draw the card as before
        batch.draw(card, 140, 210, 60,100);
        // Draw the button as a colored rectangle (using card texture as a placeholder)
        batch.setColor(0.2f, 0.6f, 1f, 1f); // blue-ish
        batch.draw(card, buttonX, buttonY, buttonWidth, buttonHeight);
        batch.setColor(1f, 1f, 1f, 1f); // reset color
        // Draw the message if set
        if (!drawMessage.isEmpty()) {
            font.draw(batch, drawMessage, 100, 400);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        if (font != null) font.dispose();
    }

    @Override 
    public boolean keyDown (int keycode) {
		return false;
	}

	@Override 
    public boolean keyUp (int keycode) {
		return false;
	}

	@Override 
    public boolean keyTyped (char character) {
		return false;
	}

	@Override 
    public boolean scrolled (float x, float y) {
		return false;
	}

}
