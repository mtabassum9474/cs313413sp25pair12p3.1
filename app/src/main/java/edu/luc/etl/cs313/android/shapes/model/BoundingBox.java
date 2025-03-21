package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        if (g == null || g.getShapes().isEmpty()) {
            System.err.println("ERROR: BoundingBox failed inside Group due to empty shapes!");
            return new Location(0, 0, new Rectangle(0, 0)); // Return empty bounding box
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()) {
            if (s == null) continue;
            Location bbox = s.accept(this);

            if (bbox != null && bbox.getShape() != null) {
                minX = Math.min(minX, bbox.getX());
                minY = Math.min(minY, bbox.getY());
                maxX = Math.max(maxX, bbox.getX() + ((Rectangle) bbox.getShape()).getWidth());
                maxY = Math.max(maxY, bbox.getY() + ((Rectangle) bbox.getShape()).getHeight());
            }
        }

        if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE) {
            return new Location(0, 0, new Rectangle(0, 0)); // Default empty bounding box
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        if (l == null || l.getShape() == null) {
            System.err.println("ERROR: BoundingBox failed inside Location due to null shape!");
            return new Location(0, 0, new Rectangle(0, 0)); // Return an empty bounding box
        }

        // Compute bounding box for the shape inside Location
        Location bbox = l.getShape().accept(this);

        // Ensure bounding box is not null before proceeding
        if (bbox == null || bbox.getShape() == null) {
            System.err.println("ERROR: BoundingBox failed inside Location, bbox is null!");
            return new Location(0, 0, new Rectangle(0, 0));
        }

        return new Location(l.getX() + bbox.getX(), l.getY() + bbox.getY(), bbox.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : s.getPoints()) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}