package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int prevColor = paint.getColor();
        paint.setColor(c.getColor()); // Change color
        c.getShape().accept(this);    // Draw inner shape
        paint.setColor(prevColor);    // Restore previous color
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style prevStyle = paint.getStyle();
        paint.setStyle(Style.FILL);  // Change to fill
        f.getShape().accept(this);   // Draw filled shape
        paint.setStyle(prevStyle);   // Restore previous style
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Style prevStyle = paint.getStyle();
        paint.setStyle(Style.STROKE); // Ensure it's outlined
        o.getShape().accept(this);    // Draw shape outline
        paint.setStyle(prevStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this); // Draw each shape in the group
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save(); // Save canvas state
        canvas.translate(l.getX(), l.getY()); // Move to new location
        l.getShape().accept(this); // Draw the shape at new location
        canvas.restore(); // Restore original position
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final float[] pts = s.getPointsAsArray(); // Ensure this method now exists
        canvas.drawLines(pts, paint);
        return null;
    }

}
