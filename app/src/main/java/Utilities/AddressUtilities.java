package Utilities;

import java.util.HashMap;

import exnihilum.com.au.listdataviewer.Address;

public class AddressUtilities {

    public static String[] makeStreetTypeList() {
        return new String[]{"ALLEY", "ARCADE", "AVENUE",
                "BOULEVARD", "BYPASS", "CIRCUIT", "CLOSE", "CORNER", "COURT",
                "CRESCENT", "CUL-DE-SAC", "DRIVE", "ESPLANADE", "GREEN", "GROVE",
                "HIGHWAY", "JUNCTION", "LANE", "LINK", "MEWS", "PARADE", "PLACE",
                "RIDGE", "ROAD", "SQUARE", "STREET", "TERRACE", "WAY"};
    }

    public static String[] makeStreetContractionsList() {
        return new String[]{"ALLY", "AL", "ARC",
                "AVE", "AV", "BVD", "BVDE", "BYPA", "BY", "BYP", "CCT", "CL",
                "CRN", "CT", "CRES", "CR", "CDS", "DR", "ESP", "GRN", "GR", "HWY",
                "JNC", "JC", "JN", "LN", "LNE", "LNK", "MEWS", "PDE", "PD", "PL",
                "PLC", "RDGE", "RD", "SQ", "ST", "STR", "TCE", "WY", "W"};
    }

    public static HashMap<String, String> makeContractions() {
        HashMap<String, String> contractions = new HashMap<>();
        contractions.put("ALLY", "ALLEY");
        contractions.put("AL", "ALLEY");
        contractions.put("ARC", "ARCADE");
        contractions.put("AVE", "AVENUE");
        contractions.put("AV", "AVENUE");
        contractions.put("BVD", "BOULEVARD");
        contractions.put("BVDE", "BOULEVARD");
        contractions.put("BYPA", "BYPASS");
        contractions.put("BY", "BYPASS");
        contractions.put("BYP", "BYPASS");
        contractions.put("CCT", "CIRCUIT");
        contractions.put("CL", "CLOSE");
        contractions.put("CRN", "CORNER");
        contractions.put("CT", "COURT");
        contractions.put("CRES", "CRESCENT");
        contractions.put("CR", "CRESCENT");
        contractions.put("CDS", "CUL-DE-SAC");
        contractions.put("DR", "DRIVE");
        contractions.put("ESP", "ESPLANADE");
        contractions.put("GRN", "GREEN");
        contractions.put("GR", "GROVE");
        contractions.put("HWY", "HIGHWAY");
        contractions.put("JNC", "JUNCTION");
        contractions.put("JC", "JUNCTION");
        contractions.put("JN", "JUNCTION");
        contractions.put("LN", "LANE");
        contractions.put("LNE", "LANE");
        contractions.put("LNK", "LINK");
        contractions.put("MEWS", "MEWS");
        contractions.put("PDE", "PARADE");
        contractions.put("PD", "PARADE");
        contractions.put("PL", "PLACE");
        contractions.put("PLC", "PLACE");
        contractions.put("RDGE", "RIDGE");
        contractions.put("RD", "ROAD");
        contractions.put("SQ", "SQUARE");
        contractions.put("ST", "STREET");
        contractions.put("STR", "STREET");
        contractions.put("TCE", "TERRACE");
        contractions.put("WY", "WAY");
        contractions.put("W", "WAY");
        return contractions;
    }

    public static String generateGeocodeQuery(Address address) {

        // %22 is "
        // %3D is =
        // %27 is '

        String locality = "";
        String streetName = "";
        String streetType = "";
        String streetNumber = "";

        if (!address.getLocality().isEmpty()) {
            locality = "%22LOCALITY%22+%3D+%27" + address.getLocality() + "%27";
        }
        if (!address.getStreetName().isEmpty()) {
            if (locality.isEmpty()) {
                streetName = "%22STREET%22+%3D+%27" + address.getStreetName() + "%27+";
            } else {
                streetName = "AND+%22STREET%22+%3D+%27" + address.getStreetName() + "%27+";
            }
        }
        if (!address.getStreetType().isEmpty()) {
            streetType = "AND+%22ST_TYPE%22+%3D+%27" + address.getStreetType() + "%27+";
        }
        if (!address.getStreetNumber().isEmpty()) {
            streetNumber = "AND+%22ST_NO_FROM%22+%3D+" + address.getStreetNumber();
        }

        return "https://services.thelist.tas.gov.au/arcgis/rest/services/Public/" +
                "CadastreAndAdministrative/MapServer/43/query?where=" +
                locality + streetName + streetNumber + streetType +
                "&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&" +
                "spatialRel=esriSpatialRelIntersects&relationParam=&outFields=ST_NO_FROM%2C+" +
                "STREET%2C+ST_TYPE%2C+LOCALITY%2C+POSTCODE&returnGeometry=" +
                "true&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=4326&" +
                "returnIdsOnly=false&returnCountOnly=false&orderByFields=&" +
                "groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&" +
                "gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=json";
    }


}
