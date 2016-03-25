package name.haoxin.brushedmetal;

import android.graphics.Bitmap;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * Created by hx on 16/3/24.
 */
public class CustomGLRenderer implements GLSurfaceView.Renderer {
    private boolean takeScreenShot = false;
    private boolean calculateFps = false;
    private FpsListener fpsListener = new FpsListener();
    private CaptureCallBack captureCallBack;
    private int width, height;
    private ArcBall arcBall;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        arcBall = null;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        if (takeScreenShot) {
            captureCallBack.saveCapture(createBitmapFromGLSurface(gl));
            takeScreenShot = false;
        }

        if (calculateFps) {
            fpsListener.makeFPS();
        }
    }

    public void capture(CaptureCallBack captureCallBack) {
        this.captureCallBack = captureCallBack;
        takeScreenShot = true;
    }

    private Bitmap createBitmapFromGLSurface(GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[width * height];
        int bitmapSource[] = new int[width * height];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < height; i++) {
                offset1 = i * width;
                offset2 = (height - i - 1) * width;
                for (int j = 0; j < width; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, width, height, Bitmap.Config.ARGB_8888);
    }

    public void rotateArcBall(float[] touchFromVector, float[] touchToVector) {
        arcBall.rotate(touchFromVector, touchToVector);
    }

    public boolean switchFpsDisplay() {
        calculateFps = !calculateFps;
        if (calculateFps) {
            fpsListener.reset();
        }
        return calculateFps;
    }

    public String getFps() {
        return fpsListener.getFPS();
    }
}
