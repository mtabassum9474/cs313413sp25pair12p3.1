package edu.luc.etl.cs313.android.shapes.android;

import edu.luc.etl.cs313.android.shapes.model.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view for drawing shapes.
 */
public class DrawWidget extends View {

    private final Paint paint = new Paint(); // Reuse the paint object

    public DrawWidget(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawWidget(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawWidget(final Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (canvas == null) {
            return; // Prevent NullPointerException
        }

        // Get the shape to draw
        final Shape shape = Fixtures.complexGroup; // Ensure Fixtures is correctly implemented

        try {
            // Compute bounding box
            final Location bbox = shape.accept(new BoundingBox());

            // Set up paint
            paint.setColor(android.graphics.Color.RED);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);

            // Draw a placeholder rectangle to confirm onDraw works
            canvas.drawRect(50, 50, 200, 200, paint);

            // Draw the actual shape
            shape.accept(new Draw(canvas, paint));
        } catch (Exception e) {
            // Handle any unexpected exceptions
            paint.setColor(android.graphics.Color.RED);
            paint.setTextSize(40);
            canvas.drawText("ERROR: " + e.getMessage(), 50, 100, paint);
        }
    }
}
