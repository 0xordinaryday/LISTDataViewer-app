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
        layers.add(new LayerType("CadastreAndAdministrative", "Heritage Register", "none", "0", "NAME", "ADDRESS", "STATUS", "THR_ID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Crown Leases", "rings", "2", "LEASE_TY", "PLAN_REF", "VOLUME", "FOLIO"));
        layers.add(new LayerType("CadastreAndAdministrative", "Crown Licences", "rings", "3", "LICENCE_TY", "PLAN_REF", "STATUS"));
        layers.add(new LayerType("CadastreAndAdministrative", "Local Government Areas", "rings", "4", "NAME", "GAZ_DATE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Public Land Classification", "rings", "5", "CATEGORY", "NAME", "PLAN_AUTH", "GOVERN_ACT"));
        layers.add(new LayerType("CadastreAndAdministrative", "Authority Land", "rings", "6", "TENURE_TY", "AUTH_TYPE", "FEAT_NAME", "PLAN_REF"));
        layers.add(new LayerType("CadastreAndAdministrative", "Localities", "rings", "7", "NAME", "POSTCODE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Legislative Council Areas", "rings", "9", "NAME", "COMMENTS"));
        layers.add(new LayerType("CadastreAndAdministrative", "Electoral Boundaries", "rings", "10", "NAME", "ELECTOR_ID", "COMMENTS"));
        layers.add(new LayerType("CadastreAndAdministrative", "Marine Leases", "rings", "19", "M_LEASE_NO", "PLAN_REF"));
        layers.add(new LayerType("CadastreAndAdministrative", "ASSIST Land Districts", "rings", "22", "LAND_DIST", "LD_ID"));
        layers.add(new LayerType("CadastreAndAdministrative", "ASSIST Parishes", "rings", "23", "PARISH", "TOWN_CITY", "PT_ID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Tasmanian Reserve Estate", "rings", "29", "RES_NAME", "RES_CLASS", "RES_STATUS", "AUTHORITY"));
        layers.add(new LayerType("CadastreAndAdministrative", "Ramsar Wetlands", "rings", "31", "NAME", "CATEGORY", "PLAN_AUTH"));
        layers.add(new LayerType("CadastreAndAdministrative", "Water Districts", "rings", "33", "NAME", "CATEGORY", "PLAN_REF", "GAZ_DATE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Land Tenure", "rings", "34", "FEAT_NAME", "TEN_CLASS", "TENURE_ID"));
        layers.add(new LayerType("CadastreAndAdministrative", "State School Intake Areas", "rings", "35", "SCHOOL", "OBJECTID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Cadastral Parcels", "rings", "38", "VOLUME", "FOLIO", "PID", "FEAT_NAME"));
        layers.add(new LayerType("CadastreAndAdministrative", "Address Geocodes", "none", "43", "ST_NO_FROM", "STREET", "ST_TYPE", "LOCALITY"));
        layers.add(new LayerType("CadastreAndAdministrative", "Local Government Area Reserves", "rings", "44", "CATEGORY", "OBJECTID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Easements", "rings", "48", "EASEMNT_TY", "OBJECTID", "CID"));
        layers.add(new LayerType("CadastreAndAdministrative", "Aboriginal Land", "rings", "49", "VOLUME", "FOLIO", "PID", "DATA_SOURCE"));
        layers.add(new LayerType("CadastreAndAdministrative", "Urban Locality", "rings", "65", "NAME", "POSTCODE", "PLAN_REF"));
        layers.add(new LayerType("CadastreParcels", "Cadastral Parcels", "rings", "0", "PROP_ADD", "VOLUME", "FOLIO", "PID"));
        layers.add(new LayerType("COPpublic", "Road Blocks", "none", "0", "ACCESS_LEVEL", "DATE_CREATED", "DATE_LAST_UPDATED"));
        layers.add(new LayerType("COPpublic", "Road Closures", "paths", "1", "ACCESS_LEVEL", "DATE_CREATED", "DATE_LAST_UPDATED"));
        layers.add(new LayerType("COPpublic", "Exclusion Zones", "rings", "2", "ID", "DATE_CREATED", "DATE_LAST_UPDATED"));
        layers.add(new LayerType("EmergencyManagementPublic", "Nearby Safer Places", "none", "0", "NAME", "MAP_NAME", "FDR"));
        layers.add(new LayerType("EmergencyManagementPublic", "Fire History All", "rings", "1", "FIRE_NAME", "SEVERITY", "INCD_START"));
        layers.add(new LayerType("EmergencyManagementPublic", "TASVEG 3.0 Fire Attributes", "rings", "3", "FIRESENS", "FLAMMAB", "VEGCODE_D"));
        layers.add(new LayerType("EmergencyManagementPublic", "Ambulance Stations", "none", "4", "NAME", "MAJOR_CATEGORY", "PROP_ADD", "MUNICIPALITY"));
        layers.add(new LayerType("EmergencyManagementPublic", "Police Station", "none", "5", "NAME", "COM_TYPE1", "COM_TYPE2"));
        // layers.add(new LayerType("EmergencyManagementPublic", "Fire Station", "none", "6", "ADDRESS", "STATION_TY", "BRIGADE"));
        layers.add(new LayerType("EmergencyManagementPublic", "State Emergency Service Offices", "none", "7", "NAME", "COM_TYPE1", "COMFAC_ID"));
        layers.add(new LayerType("EmergencyManagementPublic", "Ambulance Tasmania Station Primary Response Area", "rings", "8", "REGION", "AT_STATION", "LOCATION"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Lines 25K", "paths", "13", "TYPE", "CLASS"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Polygons 25K", "rings", "14", "SYMBOL", "DESCRIPT", "FORMATION", "PERIOD"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Lines 250K", "paths", "15", "TYPE", "LINECODE"));
        layers.add(new LayerType("GeologicalAndSoils", "Geological Polygons 250K", "rings", "16", "SYMBOL", "DESCRIPT", "FORMATION", "PERIOD"));
        layers.add(new LayerType("GeologicalAndSoils", "Soil Types", "rings", "19", "SOIL_CLASS", "SOIL_CODE", "SOIL_DESC"));

        layers.add(new LayerType("TopographyAndRelief", "Tracks", "paths", "24", "TRANS_TYPE", "TSEG_FEAT"));
        layers.add(new LayerType("OpenDataWFS", "Transport Segments", "paths", "42", "TRANS_TYPE", "TSEG_FEAT"));

        layers.add(new LayerType("Infrastructure", "TasWater Water Hydrant", "none", "2", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("Infrastructure", "TasWater Sewer Maintenance Hole", "none", "3", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("Infrastructure", "TasWater Water Main", "paths", "4", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "TasWater Sewer Main", "paths", "5", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "TasWater Sewer Lateral Line", "paths", "20", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_DIAMETER"));
        layers.add(new LayerType("Infrastructure", "TasWater Water Lateral Line", "paths", "21", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_DIAMETER"));
        layers.add(new LayerType("Infrastructure", "Water Network Structures", "none", "9", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Sewer Network Structures", "none", "10", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Recycled Water Network Structures", "none", "11", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Stormwater Network Structures", "none", "12", "NAME", "SUBTYPECD", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Stormwater Maintenance Holes", "none", "13", "OBJECTID", "ASSETID"));
        layers.add(new LayerType("Infrastructure", "Sewer Pressurised Mains", "paths", "14", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Recycled Water Mains", "paths", "15", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Stormwater Gravity Mains", "paths", "16", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Stormwater Pressurised Main", "paths", "17", "DIAMETER", "MATERIAL", "D_MATERIAL", "D_SUBTYPEC"));
        layers.add(new LayerType("Infrastructure", "Community Sports and Recreation Facilities", "none", "8", "NAME", "COM_TYPE1", "COM_TYPE2"));
        layers.add(new LayerType("Infrastructure", "Metro and Tassielink Bus Routes", "paths", "18", "ROUTE_SHORT_NAME", "TRIP_HEADSIGN", "ROUTE_ID", "SERVICE_ID"));
        layers.add(new LayerType("Infrastructure", "Metro Bus Stops", "none", "19", "STOP_NAME", "STOP_DESC", "PARENT_STATION"));



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
        // categories.add("ABSdata");
        categories.add("CadastreAndAdministrative");
        categories.add("CadastreParcels");
        // categories.add("ClimateChange");
        categories.add("COPpublic");
        // categories.add("Education");
        categories.add("EmergencyManagementPublic");
        // categories.add("FloodMappingPublic");
        categories.add("GeologicalAndSoils");
        // categories.add("Indexes");
        categories.add("Infrastructure");
        // categories.add("MarineAndCoastal");
        // categories.add("MAXBiosecurityPublic");
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
