package net.panuska.tlappy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import net.panuska.tlappy.mobile.R;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class IntroActivity extends TlappyActivity {
    private static final String TAG = IntroActivity.class.getSimpleName();

    private LinearLayout mainMenu;
    private AsyncTask intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        view = (SurfaceView) findViewById(R.id.intro_content);
        mainMenu = (LinearLayout) findViewById(R.id.mainMenu);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu.setVisibility(mainMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) { // support for Android TV controls; make menu visible
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) { // when pressing the DPAD center key
            LinearLayout buttonLayout = (LinearLayout) mainMenu.getChildAt(1);
            for (int i = 0; i < buttonLayout.getChildCount(); i++) {
                if (buttonLayout.getChildAt(i).isFocused()) {   // if there is any button focused
                    return super.dispatchKeyEvent(event);       // press the button
                }
            }                                                   // otherwise show / hide the menu
            mainMenu.setVisibility(mainMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        intro = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Main.getInstance(view);  //todo this should not be necessary -> move vram into Intro class
                new Intro().intro();
                return null;
            }
        }.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {                         // when getting back from pause
            Device.vram.refresh();
        }
    }

    public void showOptions(View view) {
        startActivity(new Intent(this, OptionsActivity.class));
        hideMenuAndFinishIntro();
    }

    public void playGame(View view) {
        startActivity(new Intent(this, FullscreenActivity.class).putExtra(SCENE_NUMBER, retrieveSceneNumber() + 1));
        hideMenuAndFinishIntro();
    }

    public void selectScreen(View view) {
        startActivity(new Intent(this, SelectSceneActivity.class));
        hideMenuAndFinishIntro();
    }

    private void hideMenuAndFinishIntro() {
        mainMenu.setVisibility(View.GONE);
        Main.setState(Main.EXIT_GAME);
        intro.cancel(true);
        Device.music.stop();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mainMenu.getVisibility() == View.VISIBLE) {
            mainMenu.setVisibility(View.GONE);
        } else {
            mainMenu.setVisibility(View.VISIBLE);
        }
    }

    public void exitGame(View view) {
        hideMenuAndFinishIntro();
        Device.destroy();
        super.onBackPressed();
    }
}
