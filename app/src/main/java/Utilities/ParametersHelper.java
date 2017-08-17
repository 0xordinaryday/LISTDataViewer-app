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
        layers.add(new LayerType("TopographyAndRelief", "10m Contours", "paths", "12", "ELEVATION", "OBJECTID"));
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
        return layers;
    }
}
