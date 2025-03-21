package edu.luc.etl.cs313.android.shapes.model;

public class Polygon implements Shape {

    private final Point[] points; // ✅ Declare the points array

    public Polygon(Point... points) {
        this.points = points; // ✅ Initialize the points array
    }

    public Point[] getPoints() {
        return points;
    }

    // ✅ Corrected method to work with arrays instead of lists
    public float[] getPointsAsArray() {
        if (points == null || points.length == 0) return new float[0]; // Fixed check

        float[] pts = new float[points.length * 2];
        for (int i = 0; i < points.length; i++) {
            pts[i * 2] = points[i].getX(); // Corrected array access
            pts[i * 2 + 1] = points[i].getY();
        }
        return pts;
    }

    @Override
    public <Result> Result accept(Visitor<Result> v) {
        return v.onPolygon(this);
    }
}