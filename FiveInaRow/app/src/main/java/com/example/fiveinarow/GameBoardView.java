package com.example.fiveinarow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GameBoardView extends View {

    Paint brush;
    float numTileOneSide;
    float size;
    float tileSize;

    // from parent
    FiveInARowGame game;

    public GameBoardView(Context context, FiveInARowGame game) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // initialize
        this.game = game;
        brush = new Paint();
        brush.setStrokeWidth(3);
        brush.setColor(Color.DKGRAY);
    }

    void initDims() {
        size = Math.min(getWidth(), getHeight());
        numTileOneSide = game.getBoard().length;
        tileSize =  size / numTileOneSide;
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                registerMove(event.getX(), event.getY());
            default:
                return true;
        }
    }

    void registerMove(float xPos, float yPos) {
        int x = (int) (xPos / tileSize);
        int y = (int) (yPos / tileSize);
        if (x < numTileOneSide && y < numTileOneSide) {
            game.makeMove(x, y, game.nextPlayer());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (size == 0) initDims();
        // how to draw a board?
        // game != null
        int[][] board = game.getBoard();
        for (int i = 0; i < numTileOneSide; i++) {
            for (int j = 0; j < numTileOneSide; j++) {
                float x = i * tileSize;
                float y = j * tileSize;
                brush.setColor(Color.DKGRAY);
                brush.setStyle(Paint.Style.STROKE);
                canvas.drawRect(x, y, x + tileSize, y + tileSize, brush);
                brush.setStyle(Paint.Style.FILL);
                brush.setColor(Color.GRAY);
                canvas.drawRect(x, y, x + tileSize, y + tileSize, brush);
                if (board[i][j] == FiveInARowGame.BLACK) {
                    brush.setColor(Color.BLACK);
                    brush.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x + tileSize / 2, y + tileSize / 2, tileSize / 2, brush);
                } else if (board[i][j] == FiveInARowGame.WHITE) {
                    brush.setColor(Color.WHITE);
                    brush.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x + tileSize / 2, y + tileSize / 2, tileSize / 2, brush);
                } else {
                    (new Exception("wtf")).printStackTrace();
                }
            }
        }
    }
}
