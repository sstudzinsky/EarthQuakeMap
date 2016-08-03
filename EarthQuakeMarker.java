import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PGraphics;

import java.util.HashMap;

/**
 * Created by Shiva on 28.07.2016.
 */
public abstract class EarthQuakeMarker extends CommonMarker implements Comparable<EarthQuakeMarker> {
    // Greater than or equal to this threshold is a moderate earthquake
    public static final float THRESHOLD_MODERATE = 5;
    // Greater than or equal to this threshold is a light earthquake
    public static final float THRESHOLD_LIGHT = 4;

    // Greater than or equal to this threshold is an intermediate depth
    public static final float THRESHOLD_INTERMEDIATE = 70;
    // Greater than or equal to this threshold is a deep depth
    public static final float THRESHOLD_DEEP = 300;

    // Shows, is the earthQuake on the ocean, or on the earth;
    protected boolean isOnLand;
    private float radius;

    public EarthQuakeMarker(Feature quake){
        super(((PointFeature)quake).getLocation(), quake.getProperties());

        HashMap<String, Object> properties = quake.getProperties();
        float magnitude = Float.parseFloat(properties.get("magnitude").toString());
        radius = magnitude * 2;
        properties.put("radius", radius);
        setProperties(properties);
    }


    public void chooseColor(PGraphics p){
        float depth = getDepth();

        int DEEP_COLOR = p.color(112,115,154);
        int MEDIUM_COLOR = p.color(160,138,125);
        int LIGHT_COLOR = p.color(82,160,66);

        if (depth > THRESHOLD_DEEP)
            p.fill(DEEP_COLOR);
        else if (depth > THRESHOLD_INTERMEDIATE)
            p.fill(MEDIUM_COLOR);
        else
            p.fill(LIGHT_COLOR);
    }
    public double setCircle(PGraphics p){
        float kmPerMile = 1.6f;

        double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
        double km = (miles * kmPerMile);
        return km;
    }

    @Override
    public void drawTitle(PGraphics p, float x, float y) {
        String title = getTitle();
        float textWidth = p.textWidth(title);

        p.pushStyle();

        p.rectMode(p.CORNER);
        p.fill(255,231,200);
        p.rect(x, y - 22, textWidth + 6, 22);

        p.fill(0);
        p.textAlign(p.LEFT, p.TOP);
        p.text(title, x + 3, y - 17);

        p.popStyle();
    }
    @Override
    public void drawMarker(PGraphics p, float x, float y) {
        p.pushStyle();

        chooseColor(p);
        drawQuake(p, x, y);

        // Add sign 'X' if the Earthquake's age is less than 1 day

        String age = getStringProperty("age");
        if (age.equals("Past Day") || age.equals("Past Hour")){

            p.stroke(2);

            p.line(x - radius, y - radius, x + radius, y + radius);
            p.line(x + radius, y - radius, x - radius, y + radius);

        }

        p.popStyle();
    }
    @Override
    public String toString(){
        return getTitle();
    }

    public int compareTo(EarthQuakeMarker other){
        Float thisMagnitude = this.getMagnitude();
        Float otherMagnitude = other.getMagnitude();

        return (thisMagnitude.compareTo(otherMagnitude));
    }

    // Abstract method for drowing earthquake
    public abstract void drawQuake(PGraphics p, float x, float y);

    // Getters
    public float getMagnitude(){
        return Float.parseFloat(properties.get("magnitude").toString());
    }
    public float getRadius(){
        return Float.parseFloat(properties.get("radius").toString());
    }
    public float getDepth() {
        return Float.parseFloat(getProperty("depth").toString());
    }
    public String getTitle() {
        return (String) getProperty("title");
    }
}
