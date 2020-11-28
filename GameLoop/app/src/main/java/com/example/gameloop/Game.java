package com.example.gameloop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.EventListener;
import java.util.Random;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;

    public Game(Context context) {
        super(context);

        // get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawMap(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(20);
        canvas.drawText("UPS: " + averageUPS, 100, 20, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.teal_200);
        paint.setColor(color);
        paint.setTextSize(20);
        canvas.drawText("FPS: " + averageFPS, 100, 40, paint);
        canvas.drawText("ncx: " + ncx + " ncy: " + ncy, 100, 60, paint);
    }

    public void drawMap ( Canvas canvas ) {
        Paint paint = new Paint();
        Rect rect = new Rect();
        for ( int x=0; x<nMapWidth; x++ ) {
            for ( int y=0; y<nMapHeight; y++ ) {

                int i = y*nCellHeight, j = x*nCellWidth;
                rect.top = i ; rect.left = j ;
                rect.bottom = i+nCellHeight ; rect.right = j+nCellWidth ;

                switch ( nMapArr[y*nMapWidth+x] ) {
                    case 9 :
                        paint.setColor(color_green);
                        canvas.drawRect(rect, paint);
                        break;
                    case 3 :
                        paint.setColor(color_cyan);
                        canvas.drawRect(rect, paint);
                        break;
                    default:
                        break;
                }
            }
        }
        // drawing bat
        rect.top = nScreenHeight-nCellWidth; rect.left = (int)fBatPos-nBatLength;
        rect.bottom = rect.top+(nCellHeight/2);rect.right = (int)fBatPos+nBatLength;
        paint.setColor(color_red);
        canvas.drawRect(rect, paint);
        // drawing ball
        canvas.drawCircle(fBallPosX, fBallPosY,10, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                nTouchedOnX = (int)event.getX(); nTouchedOnY = (int)event.getY();
                return true;
            case MotionEvent.ACTION_MOVE :
                nTouchedOnX = (int)event.getX(); nTouchedOnY = (int)event.getY();
                setBatPosition ( event.getX() );
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void setBatPosition(float x) {
        fBatPos = x;
        if ( fBatPos < (float)(nBatLength+nCellWidth) ) {
            fBatPos = (float)(nBatLength+nCellWidth);
        } else if ( fBatPos > (float)(nScreenWidth-nBatLength-nCellWidth) ) {
            fBatPos = (float)(nScreenWidth-nBatLength-nCellWidth);
        }
    }

    public void update() {
        float fBallOldX = fBallPosX;
        float fBallOldY = fBallPosY;

        fBallPosX += fBallDX * 5.0f;
        fBallPosY += fBallDY * 5.0f;

        int nCOX = (int)fBallOldX / nCellHeight;
        int nCOY = (int)fBallOldY / nCellWidth;

        int nCNX = (int)fBallPosX / nCellHeight;
        int nCNY = (int)fBallPosY / nCellWidth;
        ncx = nCNX; ncy = nCNY;

        int nNewCell = nMapArr[nCNY*nMapWidth+nCNX];
        int nOldCell = nMapArr[nCOY*nMapWidth+nCOX];

        if ( nNewCell == 9 ) {
            if ( nCNX != nCOX ) fBallDX *= -1.0f;
            if ( nCNY != nCOY ) fBallDY *= -1.0f;
        }

        //int color = ((A&0xff)<<24 | (R&0xff)<<16 | (G&0xff)<<8 | (B&0xff));
        //drawPixel(i,j,color);
    }
    int ncx, ncy;

    int color_red = ContextCompat.getColor(getContext(), R.color.red);
    int color_black = ContextCompat.getColor(getContext(), R.color.black);
    int color_white = ContextCompat.getColor(getContext(), R.color.white);
    int color_green = ContextCompat.getColor(getContext(), R.color.green);
    int color_blue = ContextCompat.getColor(getContext(), R.color.blue);
    int color_cyan = ContextCompat.getColor(getContext(), R.color.cyan);


    float fBallPosX, fBallPosY;
    float fBallDX, fBallDY;

    int nScreenWidth;
    int nScreenHeight;
    int nMapWidth;
    int nMapHeight;
    int nCellWidth;
    int nCellHeight;
    int[] nMapArr;
    float fBatPos;
    int nBatLength;
    int nTouchedOnX;
    int nTouchedOnY;

    public void createPicture(Canvas canvas) {
        nScreenWidth = canvas.getWidth();
        nScreenHeight = canvas.getHeight();
        nMapWidth = 15;
        nMapHeight = 30;
        nCellWidth = nScreenWidth/nMapWidth;
        nCellHeight = nScreenHeight/nMapHeight;
        prepareBall();
        prepareMap();
        prepareBat();
    }

    private void prepareBall() {
        Random random = new Random();
        float fAngle = ( (float)random.nextInt(200) / 199.0f ) * 3.1516f * 2.0f ;
        fBallDX = (float)Math.cos(fAngle);
        fBallDY = (float)Math.sin(fAngle);
        fBallPosX = nScreenWidth/2; fBallPosY = nScreenHeight/2;
    }

    private void prepareBat() {
        fBatPos = (float)(nScreenWidth/2);
        nBatLength = nCellWidth;
    }

    public void prepareMap () {
        nMapArr = new int[nMapWidth*nMapHeight];
        for ( int x=0; x<nMapWidth; x++ ) {
            for (int y = 0; y < nMapHeight; y++) {
                if (x==0||y==0||x==nMapWidth-1||y==nMapHeight-1) nMapArr[y*nMapWidth+x] = 9;
                else nMapArr[y*nMapWidth+x] = 0;
            }
        }
        int limit = nMapWidth-4, start = 2*nMapWidth+2;
        while ( limit-->0 ) {
            nMapArr[start+limit] = 3;
        }
    }
}
