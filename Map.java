import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shiva on 28.07.2016.
 */
public class Map extends PApplet {
    private UnfoldingMap map;
    private CommonMarker lastSelected;

    private List<Marker> countryList;
    private List<Marker> citiesList;
    private List<Marker> earthQuaksMarkers;
    private List<PointFeature> earthQuakeList;

    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";
    private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

    static public void main(String args[]) {
        PApplet.main(new String[] { "Map" });
    }

    public void setup() {
        size(900, 700);
        map = new UnfoldingMap(this, 0, 0, 900, 700, new Google.GoogleMapProvider());
        map.setTweening(true);
        map.setZoomRange(1.5f,100);
        MapUtils.createDefaultEventDispatcher(this, map);

        List<Feature> citiesFeature = GeoJSONReader.loadData(this, cityFile);
        citiesList = new ArrayList<>();

        List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryList = MapUtils.createSimpleMarkers(countries);

        for (Feature city : citiesFeature){
            citiesList.add(new CityMarker(city));
        }

        earthQuakeList = Parsefeed.parseEarthquake(this, earthquakesURL);
        earthQuaksMarkers = new ArrayList<>();
        for (PointFeature feature : earthQuakeList){
            if (isOnLand(feature)){
                earthQuaksMarkers.add(new LandQuakeMarker(feature));
            }
            else{
                earthQuaksMarkers.add(new OceanQuakeMarker(feature));
            }
        }

        map.addMarkers(earthQuaksMarkers);
        map.addMarkers(citiesList);
    }


    @Override
    public void mouseMoved() {
        if (lastSelected != null){
            lastSelected.setSelected(false);
            lastSelected = null;
        }

        selectIfHover(earthQuaksMarkers);
        selectIfHover(citiesList);
    }

    public void selectIfHover(List<Marker> marks){
        if (lastSelected != null){
            return;
        }

        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i).isInside(map, mouseX, mouseY)){
                marks.get(i).setSelected(true);
                lastSelected = (CommonMarker) marks.get(i);
                break;
            }
        }
    }


    private boolean isOnLand(PointFeature feature){
        for (Marker country : countryList){
            if (isInCountry(feature, country)){
                return true;
            }
        }
        return false;
    }

    private boolean isInCountry(PointFeature earthquake, Marker country) {
        Location checkLoc = earthquake.getLocation();

        if(country.getClass() == MultiMarker.class) {

            for(Marker marker : ((MultiMarker)country).getMarkers()) {

                if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));

                    return true;
                }
            }
        }

        else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));

            return true;
        }
        return false;
    }


    public void draw() {
        map.draw();
    }
}