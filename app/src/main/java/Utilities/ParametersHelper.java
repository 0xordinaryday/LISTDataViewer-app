package Utilities;

import java.util.ArrayList;

import exnihilum.com.au.listdataviewer.LayerType;

/**
 * Created by david on 29/07/2017.
 *
 */

public class ParametersHelper {

    private ParametersHelper() {
        // not called
    }

    public static ArrayList<LayerType> layerTypes() {
        ArrayList<LayerType> layers = new ArrayList<>();
        layers.add(new LayerType("CadastreAndAdministrative", "Local Government Areas", "rings", "4", "NAME", "GAZ_DATE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Heritage Register", "none", "0", "NAME", "ADDRESS"));
        layers.add(new LayerType("CadastreAndAdministrative", "Locality Boundaries", "rings", "7", "NAME", "POSTCODE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Urban Localities", "rings", "65", "NAME", "POSTCODE"));
        layers.add(new LayerType("CadastreParcels", "Cadastral Boundaries", "rings", "0", "PROP_ADD", "VOLUME"));
        layers.add(new LayerType("CadastreAndAdministrative", "Legislative Council Areas", "rings", "9", "NAME", "COMMENTS"));
        layers.add(new LayerType("CadastreAndAdministrative", "Electoral Boundaries", "rings", "10", "NAME", "ELECTOR_ID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Tasmanian Reserve Estate", "rings", "29", "RES_NAME", "RES_STATUS"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Lines 25K", "paths", "13", "TYPE", "CLASS"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Polygons 25K", "rings", "14", "SYMBOL", "DESCRIPT"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Lines 250K", "paths", "15", "TYPE", "GID"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Polygons 250K", "rings", "16", "SYMBOL", "DESCRIPT"));
        layers.add(new LayerType("GeologicalAndSoils", "Soil Types", "rings", "19", "SOIL_CLASS", "SOIL_CODE"));
        layers.add(new LayerType("TopographyAndRelief", "Tracks", "paths", "24", "TRANS_TYPE", "TSEG_FEAT"));
        layers.add(new LayerType("OpenDataWFS", "Transport Segments", "paths", "42", "TRANS_TYPE", "TSEG_FEAT"));
        layers.add(new LayerType("Infrastructure", "TasWater Water Hydrant", "none", "2", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("Infrastructure", "TasWater Sewer Maintenance Hole", "none", "3", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("Infrastructure", "TasWater Water Main", "paths", "4", "DIAMETER", "MATERIAL"));
        layers.add(new LayerType("Infrastructure", "TasWater Sewer Main", "paths", "5", "DIAMETER", "MATERIAL"));
        layers.add(new LayerType("TopographyAndRelief", "Survey Control", "none", "1", "EASTING", "NORTHING", "HEIGHT"));
        layers.add(new LayerType("TopographyAndRelief", "5m Contours", "paths", "13", "ELEVATION", "CONTOUR_TY"));
        layers.add(new LayerType("TopographyAndRelief", "10m Contours", "paths", "12", "ELEVATION", "CONTOUR_TY"));
        layers.add(new LayerType("TopographyAndRelief", "Transmission Lines", "paths", "25", "NAME", "VOLTAGE"));
        layers.add(new LayerType("PlanningOnline", "Tasmanian Planning Scheme Overlay", "rings", "3", "SCHEMECODE", "PLANSCHEME", "COMMENTS"));
        layers.add(new LayerType("PlanningOnline", "Tasmanian Planning Zones", "rings", "4", "SCHEMECODE", "PLANSCHEME", "COMMENTS"));
        layers.add(new LayerType("PlanningOnline", "Tasmanian Planning Reference", "rings", "7", "LINK", "DESCRIPTION"));
        return layers;
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("ABSdata");
        categories.add("CadastreAndAdministrative");
        categories.add("CadastreParcels");
        categories.add("ClimateChange");
        categories.add("COPpublic");
        categories.add("Education");
        categories.add("EmergencyManagementPublic");
        categories.add("FloodMappingPublic");
        categories.add("GeologicalAndSoils");
        categories.add("Indexes");
        categories.add("Infrastructure");
        categories.add("MarineAndCoastal");
        categories.add("MAXBiosecurityPublic");
        categories.add("NaturalEnvironment");
        categories.add("NVAdata");
        categories.add("OpenDataWFS");
        categories.add("PlacenamePoints");
        categories.add("Planning");
        categories.add("PlanningOnline");
        categories.add("SearchService");
        categories.add("TopographyAndRelief");
        return categories;
    }
}
