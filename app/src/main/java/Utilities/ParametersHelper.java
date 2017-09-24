package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
        layers.add(new LayerType("COL", "ParksAndRecreation", "Trees", "none", "1", "objectid", "name", "genusspecies"));

        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Heritage Register", "none", "0", "NAME", "ADDRESS", "STATUS", "THR_ID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Crown Leases", "rings", "2", "LEASE_TY", "PLAN_REF", "VOLUME", "FOLIO"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Crown Licences", "rings", "3", "LICENCE_TY", "PLAN_REF", "STATUS"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Local Government Areas", "rings", "4", "NAME", "GAZ_DATE"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Public Land Classification", "rings", "5", "CATEGORY", "NAME", "PLAN_AUTH", "GOVERN_ACT"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Authority Land", "rings", "6", "TENURE_TY", "AUTH_TYPE", "FEAT_NAME", "PLAN_REF"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Localities", "rings", "7", "NAME", "POSTCODE"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Legislative Council Areas", "rings", "9", "NAME", "COMMENTS"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Electoral Boundaries", "rings", "10", "NAME", "ELECTOR_ID", "COMMENTS"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Marine Leases", "rings", "19", "M_LEASE_NO", "PLAN_REF"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "ASSIST Land Districts", "rings", "22", "LAND_DIST", "LD_ID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "ASSIST Parishes", "rings", "23", "PARISH", "TOWN_CITY", "PT_ID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Tasmanian Reserve Estate", "rings", "29", "RES_NAME", "RES_CLASS", "RES_STATUS", "AUTHORITY"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Ramsar Wetlands", "rings", "31", "NAME", "CATEGORY", "PLAN_AUTH"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Water Districts", "rings", "33", "NAME", "CATEGORY", "PLAN_REF", "GAZ_DATE"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Land Tenure", "rings", "34", "FEAT_NAME", "TEN_CLASS", "TENURE_ID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "State School Intake Areas", "rings", "35", "SCHOOL", "OBJECTID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Cadastral Parcels", "rings", "38", "VOLUME", "FOLIO", "PID", "FEAT_NAME"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Address Geocodes", "none", "43", "ST_NO_FROM", "STREET", "ST_TYPE", "LOCALITY"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Local Government Area Reserves", "rings", "44", "CATEGORY", "OBJECTID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Easements", "rings", "48", "EASEMNT_TY", "OBJECTID", "CID"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Aboriginal Land", "rings", "49", "VOLUME", "FOLIO", "PID", "DATA_SOURCE"));
        layers.add(new LayerType("LIST", "CadastreAndAdministrative", "Urban Locality", "rings", "65", "NAME", "POSTCODE", "PLAN_REF"));
        layers.add(new LayerType("LIST", "CadastreParcels", "Cadastral Parcels", "rings", "0", "PROP_ADD", "VOLUME", "FOLIO", "PID"));
        // layers.add(new LayerType("LIST", "COPpublic", "Road Blocks", "none", "0", "ACCESS_LEVEL", "DATE_CREATED", "DATE_LAST_UPDATED"));
        // layers.add(new LayerType("LIST", "COPpublic", "Road Closures", "paths", "1", "ACCESS_LEVEL", "DATE_CREATED", "DATE_LAST_UPDATED"));
        // layers.add(new LayerType("LIST", "COPpublic", "Exclusion Zones", "rings", "2", "ID", "DATE_CREATED", "DATE_LAST_UPDATED"));
        // layers.add(new LayerType("LIST", "EmergencyManagementPublic", "Nearby Safer Places", "none", "0", "NAME", "MAP_NAME", "FDR"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "Fire History All", "rings", "1", "FIRE_NAME", "SEVERITY", "INCD_START"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "TASVEG 3.0 Fire Attributes", "rings", "3", "FIRESENS", "FLAMMAB", "VEGCODE_D"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "Ambulance Stations", "none", "4", "NAME", "MAJOR_CATEGORY", "PROP_ADD", "MUNICIPALITY"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "Police Station", "none", "5", "NAME", "COM_TYPE1", "COM_TYPE2"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "State Emergency Service Offices", "none", "7", "NAME", "COM_TYPE1", "COMFAC_ID"));
        layers.add(new LayerType("LIST", "EmergencyManagementPublic", "Ambulance Tasmania Station Primary Response Area", "rings", "8", "REGION", "AT_STATION", "LOCATION"));
        layers.add(new LayerType("LIST", "GeologicalAndSoils", "Geological Lines 25K", "paths", "13", "TYPE", "CLASS"));
        layers.add(new LayerType("LIST", "GeologicalAndSoils", "Geological Polygons 25K", "rings", "14", "SYMBOL", "DESCRIPT", "FORMATION", "PERIOD"));
        layers.add(new LayerType("LIST", "GeologicalAndSoils", "Geological Lines 250K", "paths", "15", "TYPE", "LINECODE"));
        layers.add(new LayerType("LIST", "GeologicalAndSoils", "Geological Polygons 250K", "rings", "16", "SYMBOL", "DESCRIPT", "FORMATION", "PERIOD"));
        layers.add(new LayerType("LIST", "GeologicalAndSoils", "Soil Types", "rings", "19", "SOIL_CLASS", "SOIL_CODE", "SOIL_DESC"));

        layers.add(new LayerType("LIST", "OpenDataWFS", "Transport Segments", "paths", "42", "TRANS_TYPE", "TSEG_FEAT"));

        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Sewer Serviced Land", "rings", "0", "SERVICETYPE", "DESCRIPTION", "DATEOFISSUE"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Water Serviced Land", "rings", "1", "SERVICETYPE", "DESCRIPTION", "DATEOFISSUE"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Pressure Exclusion Boundary", "rings", "7", "OBJECTID", "DESCRIPTION"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Water Hydrant", "none", "2", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Sewer Maintenance Hole", "none", "3", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Water Main", "paths", "4", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Sewer Main", "paths", "5", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Sewer Lateral Line", "paths", "20", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_DIAMETER"));
        layers.add(new LayerType("LIST", "Infrastructure", "TasWater Water Lateral Line", "paths", "21", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_DIAMETER"));
        layers.add(new LayerType("LIST", "Infrastructure", "Water Network Structures", "none", "9", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Sewer Network Structures", "none", "10", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Recycled Water Network Structures", "none", "11", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Stormwater Network Structures", "none", "12", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Stormwater Maintenance Holes", "none", "13", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("LIST", "Infrastructure", "Sewer Pressurised Mains", "paths", "14", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Recycled Water Mains", "paths", "15", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Stormwater Gravity Mains", "paths", "16", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Stormwater Pressurised Main", "paths", "17", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("LIST", "Infrastructure", "Community Sports and Recreation Facilities", "none", "8", "NAME", "COM_TYPE1", "COM_TYPE2"));
        layers.add(new LayerType("LIST", "Infrastructure", "Metro and Tassielink Bus Routes", "paths", "18", "ROUTE_SHORT_NAME", "TRIP_HEADSIGN", "ROUTE_ID", "SERVICE_ID"));
        layers.add(new LayerType("LIST", "Infrastructure", "Metro Bus Stops", "none", "19", "STOP_NAME", "STOP_DESC", "PARENT_STATION"));

        layers.add(new LayerType("LIST", "TopographyAndRelief", "Survey Control", "none", "1", "EASTING", "NORTHING", "HEIGHT"));
        layers.add(new LayerType("LIST", "TopographyAndRelief", "5m Contours", "paths", "13", "ELEVATION", "CONTOUR_TY"));
        layers.add(new LayerType("LIST", "TopographyAndRelief", "10m Contours", "paths", "12", "ELEVATION", "CONTOUR_TY"));
        layers.add(new LayerType("LIST", "TopographyAndRelief", "Transmission Lines", "paths", "25", "NAME", "VOLTAGE"));
        layers.add(new LayerType("LIST", "TopographyAndRelief", "Tracks", "paths", "24", "TRANS_TYPE", "TSEG_FEAT"));

        layers.add(new LayerType("LIST", "PlanningOnline", "Tasmanian Planning Scheme Overlay", "rings", "3", "SCHEMECODE", "PLANSCHEME", "COMMENTS"));
        layers.add(new LayerType("LIST", "PlanningOnline", "Tasmanian Planning Zones", "rings", "4", "SCHEMECODE", "PLANSCHEME", "COMMENTS"));
        layers.add(new LayerType("LIST", "PlanningOnline", "Tasmanian Planning Reference", "rings", "7", "LINK", "DESCRIPTION"));

        layers.add(new LayerType("LIST", "NaturalEnvironment", "TASVEG 3.0", "rings", "0", "VEG_GROUP", "VEGCODE", "VEGCODE_D", "PROJECT"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Forest Groups", "rings", "1", "FORGROUP", "OBJECTID", "CURRENCY"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Threatened Native Vegetation 2014",
                "rings", "2", "SCHED_ID", "SCHED_NAME"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Estuarine Catchments", "rings", "4", "EST_CODE", "NAME", "CREATED_ON"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Riverine Catchments", "rings", "5", "RIV_CODE", "NAME", "CREATED_ON"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Geoconservation Sites - Listed", "rings", "63", "NAME", "GEOGRAPHICAL_SIGNIFICANCE", "LISTING_STATUS"));
        layers.add(new LayerType("LIST", "NaturalEnvironment", "Karst - Integrated Conservation Value", "rings", "55", "KT_NAME", "KT_NSCOR_C", "KT_LTENSEC"));

        // layers.add(new LayerType("LIST", "SearchService", "Address Geocodes", "none", "7", "ADDRESS", "LOCATION", "PROPERTY_NAME", "LOCALITY"));

        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 1", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 2", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 3", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 4", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 5", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Exploration Licence Category 6", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Mine Leases", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Landslide Zones", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Proclaimed Areas", "rings", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Mineral Occurrences", "none", null, null, null));
        layers.add(new LayerType("MRT", "GeologyRequest", "Boreholes", "none", null, null, null));

        return layers;
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("CityOfLaunceston");
        // categories.add("ABSdata");
        categories.add("CadastreAndAdministrative");
        // categories.add("CadastreParcels");
        // categories.add("ClimateChange");
        // categories.add("COPpublic");
        // categories.add("Education");
        categories.add("EmergencyManagementPublic");
        // categories.add("FloodMappingPublic");
        categories.add("GeologicalAndSoils");
        // categories.add("Indexes");
        categories.add("Infrastructure");
        // categories.add("MarineAndCoastal");
        // categories.add("MAXBiosecurityPublic");
        categories.add("NaturalEnvironment");
        categories.add("OpenDataWFS");
        // categories.add("PlacenamePoints");
        categories.add("Planning");
        categories.add("PlanningOnline");
        // categories.add("SearchService");
        categories.add("TopographyAndRelief");
        return categories;
    }

    public static HashMap<String, String> makeCategoryMap() {
        HashMap<String, String> categoryMap = new HashMap<>();
        // categoryMap.put("ABS Statistical Data", "ABSdata");
        categoryMap.put("Cadastre And Administrative", "LIST"+"CadastreAndAdministrative");
        categoryMap.put("City Of Launceston", "COL"+"ParksAndRecreation");
        // categoryMap.put("Cadastre Parcels", "CadastreParcels");
        // categoryMap.put("Climate Change", "ClimateChange");
        // categoryMap.put("Common Operating Platform Public", "COPpublic");
        // categoryMap.put("Education", "Education");
        categoryMap.put("Emergency Management Public", "LIST"+"EmergencyManagementPublic");
        // categoryMap.put("Flood Mapping Public", "FloodMappingPublic");
        categoryMap.put("Geological And Soils - non MRT", "LIST"+"GeologicalAndSoils");
        categoryMap.put("Mineral Resources Tasmania", "MRT"+"GeologyRequest");
        // categoryMap.put("Indexes", "Indexes");
        categoryMap.put("Infrastructure", "LIST"+"Infrastructure");
        //  categoryMap.put("Marine And Coastal", "MarineAndCoastal");
        // categoryMap.put("MAX Biosecurity Public", "MAXBiosecurityPublic");
        categoryMap.put("Natural Environment", "LIST"+"NaturalEnvironment");
        // categoryMap.put("Natural Values Atlas Data", "NVAdata");
        categoryMap.put("Open Data Web Feature Service", "LIST"+"OpenDataWFS");
        // categoryMap.put("Placename Points", "PlacenamePoints");
        // categoryMap.put("Planning", "Planning");
        categoryMap.put("Planning Online", "LIST"+"PlanningOnline");
        // categoryMap.put("Search Service", "SearchService");
        categoryMap.put("Topography And Relief", "LIST"+"TopographyAndRelief");
        return categoryMap;
    }

    public static Set<String> getCategorySet() {
        return makeCategoryMap().keySet();
    }

    public static HashMap<String, String> makeGeologyLayerMap() {
        HashMap<String, String> geologyLayerMap = new HashMap<>();
        geologyLayerMap.put("Boreholes", "mrtwfs:Boreholes");
        geologyLayerMap.put("Exploration Licence Category 1", "mrtwfs:LicenceCategory1");
        geologyLayerMap.put("Exploration Licence Category 2", "mrtwfs:LicenceCategory2");
        geologyLayerMap.put("Exploration Licence Category 3", "mrtwfs:LicenceCategory3");
        geologyLayerMap.put("Exploration Licence Category 4", "mrtwfs:LicenceCategory4");
        geologyLayerMap.put("Exploration Licence Category 5", "mrtwfs:LicenceCategory5");
        geologyLayerMap.put("Exploration Licence Category 6", "mrtwfs:LicenceCategory6");
        geologyLayerMap.put("Mine Leases", "mrtwfs:Leases");
        geologyLayerMap.put("Landslide Zones", "mrtwfs:LandSlidePoly");
        geologyLayerMap.put("Proclaimed Areas", "mrtwfs:ProclaimedAreasPoly");
        geologyLayerMap.put("Mineral Occurrences", "mrtwfs:MineralOccurences");
        return geologyLayerMap;
    }

    public static HashMap<String, String> makeCOLLayerMap() {
        HashMap<String, String> cOLLayerMap = new HashMap<>();
        cOLLayerMap.put("Trees", "Trees");
        return cOLLayerMap;
    }

    public static Set<String> getCOLLayers() {
        Set<String> cOLLayerSet = new HashSet<>();
        HashMap<String, String> cOLLayerMap = makeCOLLayerMap();
        for (String key:cOLLayerMap.keySet()) {
            cOLLayerSet.add(cOLLayerMap.get(key));
        }
        return cOLLayerSet;
    }

    public static Set<String> getGeologyLayers() {
        Set<String> geologyLayerSet = new HashSet<>();
        HashMap<String, String> geologyLayerMap = makeGeologyLayerMap();
        for (String key:geologyLayerMap.keySet()) {
            geologyLayerSet.add(geologyLayerMap.get(key));
        }
        return geologyLayerSet;
    }

}
