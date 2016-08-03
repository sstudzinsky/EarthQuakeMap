import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PGraphics;

/**
 * Created by Shiva on 28.07.2016.
 */
public class CityMarker extends CommonMarker{
    private final static int TRIANGLE_SIZE = 7;


    // Constructors
    public CityMarker (Location location){
        super(location);
    }

    public CityMarker (Feature city){
        super(((PointFeature)city).getLocation(), city.getProperties());
    }



    // Draw overrided Markers
    @Override
    public void drawMarker(PGraphics p, float x, float y) {
        int redColor = p.color(244, 23, 1);

        p.pushStyle();
        p.fill(redColor);
        p.triangle(x - TRIANGLE_SIZE/2, y - TRIANGLE_SIZE, x + TRIANGLE_SIZE/2, y - TRIANGLE_SIZE, x, y);
        p.popStyle();
    }
    @Override
    public void drawTitle(PGraphics p, float x, float y) {
        int bgColor = p.color(255,231,200);

        String name = getCity() + " " + getCountry();
        String pop = "Pop: " + getPopulation() + " Million";

        p.pushStyle();

        // Rectangle-bg drawing
        p.rectMode(p.CORNER);
        p.fill(bgColor);
        p.rect(x, y - 39 - TRIANGLE_SIZE, Math.max(p.textWidth(name), p.textWidth(pop)) + 6, 39);

        // Text drawing
        p.fill(0);
        p.textAlign(p.LEFT, p.TOP);
        p.textSize(12);
        p.text(name, x + 3, y - 34 - TRIANGLE_SIZE);
        p.text(pop, x + 3, y - 17 - TRIANGLE_SIZE);

        p.popStyle();
    }

    // Getters
    public String getCity(){
        return getStringProperty("name");
    }
    public String getCountry(){
        return getStringProperty("country");
    }
    public float getPopulation(){
        return Float.parseFloat(getStringProperty("population"));
    }
}
