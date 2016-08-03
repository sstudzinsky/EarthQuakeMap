import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/**
 * Created by Shiva on 28.07.2016.
 */
public class OceanQuakeMarker extends EarthQuakeMarker{
    public OceanQuakeMarker(PointFeature quake){
        super(quake);
        isOnLand = false;
    }

    @Override
    public void drawQuake(PGraphics p, float x, float y) {
        float radius = getRadius();

        p.rectMode(p.CORNER);
        p.rect(x - radius, y - radius, radius * 2, radius * 2);
    }
}
