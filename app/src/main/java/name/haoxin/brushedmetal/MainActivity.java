package name.haoxin.brushedmetal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean showQuickAction;
    private Timer fpsUpdateTimer;
    private TimerTask fpsUpdateTask;
    private TextView fpsTextView;
    private Handler fpsUpdateHandler;
    private CustomGLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initQuickButton();
        glView = (CustomGLSurfaceView) findViewById(R.id.glview);
        fpsTextView = (TextView) findViewById(R.id.fpsText);
        fpsUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 1) {
                    fpsTextView.setText("FPS: " + glView.getFps());
                }
            }
        };
        fpsUpdateTimer = new Timer();
    }

    private void initQuickButton() {


        final FloatingActionButton screenshot = (FloatingActionButton) findViewById(R.id.screenshot);
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                glView.capture(new CaptureCallBack() {
                    @Override
                    public void saveCapture(Bitmap bitmap) {
                        if (Save.saveImage(MainActivity.this, bitmap)) {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainActivity.this, "Image saved.", Toast.LENGTH_LONG).show();
//                                }
//                            });
                            Snackbar.make(view, "Screenshot Saved", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        } else {
                            Snackbar.make(view, "Cannot Save The Screenshot", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                });
            }
        });

        final FloatingActionButton renderOnRequest = (FloatingActionButton) findViewById(R.id.renderOnRequest);
        renderOnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                glView.switchRenderMode();
                if (glView.isRenderOnRequest()) {
                    Snackbar.make(view, "Render When Dirty: ON", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Render When Dirty: OFF", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        final FloatingActionButton fps = (FloatingActionButton) findViewById(R.id.fps);
        fps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (glView.switchFpsDisplay()) {
                    fpsUpdateTimer.purge();
                    fpsUpdateTask = new TimerTask() {
                        @Override
                        public void run() {
                            Message msg = fpsUpdateHandler.obtainMessage();
                            msg.arg1 = 1;
                            msg.sendToTarget();
                        }
                    };
                    fpsUpdateTimer.schedule(fpsUpdateTask, 0, 1000);
                    fpsTextView.setVisibility(View.VISIBLE);
                } else {
                    fpsUpdateTask.cancel();
                    fpsTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        final FloatingActionButton quickButton = (FloatingActionButton) findViewById(R.id.quickButton);
        quickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showQuickAction) {
                    ((RelativeLayout) findViewById(R.id.quickGroup)).setVisibility(View.INVISIBLE);
                    quickButton.setAlpha(0.3f);
                } else {
                    ((RelativeLayout) findViewById(R.id.quickGroup)).setVisibility(View.VISIBLE);
                    quickButton.setAlpha(1.0f);
                }
                showQuickAction = !showQuickAction;
            }
        });
    }

}
