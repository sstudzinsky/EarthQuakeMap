import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

import java.util.HashMap;

/**
 * Created by Shiva on 28.07.2016.
 */
public abstract class CommonMarker extends SimplePointMarker {

    public CommonMarker(Location location) {
        super(location);
    }

    public CommonMarker(Location city, HashMap<String, Object> properties){
        super(city, properties);
    }

    public void draw(PGraphics p, float x, float y) {
        drawMarker(p, x, y);
        if (!hidden) {
            if (selected) {
                drawTitle(p, x, y);
            }
        }
    }

    public abstract void drawMarker(PGraphics p, float x, float y);
    public abstract void drawTitle (PGraphics p, float x, float y);
}
