import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/**
 * Created by Shiva on 28.07.2016.
 */
public class LandQuakeMarker extends EarthQuakeMarker {
    public LandQuakeMarker(PointFeature quake){
        super(quake);
        isOnLand = true;
    }

    public String getCountry(){
        return getStringProperty("country");
    }

    @Override
    public void drawQuake(PGraphics p, float x, float y) {
        float radius = getRadius();
        p.ellipse(x, y, radius * 2, radius * 2);
    }
}
