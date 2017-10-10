package Utilities;

import android.graphics.Color;

import java.util.HashMap;

/**
 * Created by David on 5/10/2017.
 *
 */

public class ColorMappingHelper {

    private ColorMappingHelper() {
        // never called
    }

    public static HashMap<String, Integer> makePlanningOnlineColorMap(int alphaValue) {
        HashMap<String, Integer> planningOnlineColorMap = new HashMap<>();
        planningOnlineColorMap.put("10.0 General Residential", Color.argb(alphaValue, 255, 0, 0));
        planningOnlineColorMap.put("11.0 Inner Residential", Color.argb(alphaValue, 128, 0, 0));
        planningOnlineColorMap.put("12.0 Low Density Residential", Color.argb(alphaValue, 242, 121, 121));
        planningOnlineColorMap.put("13.0 Rural Living", Color.argb(alphaValue, 255, 191, 202));
        planningOnlineColorMap.put("14.0 Environmental Living", Color.argb(alphaValue, 128, 128, 0));
        planningOnlineColorMap.put("15.0 Urban Mixed Use", Color.argb(alphaValue, 191, 191, 191));
        planningOnlineColorMap.put("16.0 Village", Color.argb(alphaValue, 255, 170, 0));
        planningOnlineColorMap.put("17.0 Community Purpose", Color.argb(alphaValue, 255, 255, 191));
        planningOnlineColorMap.put("18.0 Recreation", Color.argb(alphaValue, 0, 255, 0));
        planningOnlineColorMap.put("19.0 Open Space", Color.argb(alphaValue, 0, 128, 0));
        planningOnlineColorMap.put("20.0 Local Business", Color.argb(alphaValue, 172, 214, 230));
        planningOnlineColorMap.put("21.0 General Business", Color.argb(alphaValue, 69, 109, 230));
        planningOnlineColorMap.put("22.0 Central Business", Color.argb(alphaValue, 0, 0, 204));
        planningOnlineColorMap.put("23.0 Commercial", Color.argb(alphaValue, 131, 109, 242));
        planningOnlineColorMap.put("24.0 Light Industrial", Color.argb(alphaValue, 255, 0, 255));
        planningOnlineColorMap.put("25.0 General Industrial", Color.argb(alphaValue, 128, 0, 117));
        planningOnlineColorMap.put("26.0 Rural Resource", Color.argb(alphaValue, 255, 228, 191));
        planningOnlineColorMap.put("27.0 Significant Agricultural", Color.argb(alphaValue, 204, 133, 61));
        planningOnlineColorMap.put("28.0 Utilities", Color.argb(alphaValue, 255, 255, 0));
        planningOnlineColorMap.put("29.0 Environmental Mangement", Color.argb(alphaValue, 0, 128, 128));
        planningOnlineColorMap.put("30.0 Major Tourism", Color.argb(alphaValue, 230, 230, 255));
        return planningOnlineColorMap;
    }

    public static HashMap<String, Integer> makeAuthorityColorMap(int alphaValue) {
        HashMap<String, Integer> authorityColorMap = new HashMap<>();
        authorityColorMap.put("ABT Railway Ministerial Corp", Color.argb(alphaValue, 122, 179, 121));
        authorityColorMap.put("Aboriginal Land", Color.argb(alphaValue, 215, 176, 158));
        authorityColorMap.put("Aurora Energy Pty Ltd", Color.argb(alphaValue, 221, 224, 135));
        authorityColorMap.put("Commonwealth of Australia", Color.argb(alphaValue, 255, 211, 127));
        authorityColorMap.put("Dept of Primary Industries, Parks, Water and Environment (Crown Land Services)", Color.argb(alphaValue, 225, 225, 225));
        authorityColorMap.put("Dept of Primary Industries, Parks, Water and Environment (Future Potential Production Forest)", Color.argb(alphaValue, 0, 97, 0));
        authorityColorMap.put("Dept of Education", Color.argb(alphaValue, 190, 255, 232));
        authorityColorMap.put("Dept of Defence", Color.argb(alphaValue, 217, 112, 145));
        authorityColorMap.put("Dept of Health and Human Services", Color.argb(alphaValue, 116, 194, 168));
        authorityColorMap.put("Dept of Justice and Indust. Rel.", Color.argb(alphaValue, 152, 217, 168));
        authorityColorMap.put("Dept of Police and Public Safety", Color.argb(alphaValue, 190, 210, 255));
        authorityColorMap.put("Dept of Premier and Cabinet", Color.argb(alphaValue, 86, 153, 133));
        authorityColorMap.put("Dept of Treasury and Finance", Color.argb(alphaValue, 94, 168, 94));
        authorityColorMap.put("Forestry Tasmania", Color.argb(alphaValue, 115, 178, 115));
        authorityColorMap.put("Housing Tasmania", Color.argb(alphaValue, 209, 207, 153));
        authorityColorMap.put("Hydro Electric Corporation", Color.argb(alphaValue, 223, 115, 255));
        authorityColorMap.put("Inland Fisheries Commission", Color.argb(alphaValue, 225, 204, 255));
        authorityColorMap.put("Local Government Authority", Color.argb(alphaValue, 233, 255, 190));
        authorityColorMap.put("Marine and Safety Tasmania", Color.argb(alphaValue, 122, 142, 245));
        authorityColorMap.put("Parks and Wildlife Service", Color.argb(alphaValue, 245, 245, 135));
        authorityColorMap.put("Port Arthur HS Management Authority", Color.argb(alphaValue, 135, 150, 150));
        authorityColorMap.put("State Fire Commission", Color.argb(alphaValue, 255, 85, 0));
        authorityColorMap.put("State Growth (Dept of Infrastructure Energy and Resources)", Color.argb(alphaValue, 0, 255, 0));
        authorityColorMap.put("State Growth (Economic Development)", Color.argb(alphaValue, 255, 170, 0));
        authorityColorMap.put("TasNetworks", Color.argb(alphaValue, 255, 190, 232));
        authorityColorMap.put("TasWater", Color.argb(alphaValue, 0, 38, 255));
        authorityColorMap.put("Tasmanian Ambulance Service", Color.argb(alphaValue, 255, 224, 190));
        authorityColorMap.put("Tasmanian Irrigation Pty Ltd", Color.argb(alphaValue, 200, 227, 132));
        authorityColorMap.put("Tasmanian Ports Corporation Pty Ltd", Color.argb(alphaValue, 197, 0, 255));
        authorityColorMap.put("Transend Networks Pty Ltd", Color.argb(alphaValue, 255, 190, 232));
        authorityColorMap.put("University of Tasmania", Color.argb(alphaValue, 255, 255, 115));
        return authorityColorMap;
    }

    public static HashMap<String, Integer> makeSoilsColorMap(int alphaValue) {
        HashMap<String, Integer> soilsColorMap = new HashMap<>();
        soilsColorMap.put("SOIL_CODE: A", Color.argb(alphaValue, 170, 169, 173));
        soilsColorMap.put("SOIL_CODE: A-Pm", Color.argb(alphaValue, 170, 169, 173));
        soilsColorMap.put("SOIL_CODE: An", Color.argb(alphaValue, 114, 198, 102));
        soilsColorMap.put("SOIL_CODE: Ar", Color.argb(alphaValue, 181, 86, 77));
        soilsColorMap.put("SOIL_CODE: Ar-M1", Color.argb(alphaValue, 181, 86, 77));
        soilsColorMap.put("SOIL_CODE: ArLv", Color.argb(alphaValue, 181, 86, 77));
        soilsColorMap.put("SOIL_CODE: As", Color.argb(alphaValue, 254, 188, 127));
        soilsColorMap.put("SOIL_CODE: BD", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: BD-Ps", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: BDKp", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: BL", Color.argb(alphaValue, 97, 0, 0));
        soilsColorMap.put("SOIL_CODE: Bb", Color.argb(alphaValue, 203, 127, 153));
        soilsColorMap.put("SOIL_CODE: Bb-Bd1", Color.argb(alphaValue, 203, 127, 153));
        soilsColorMap.put("SOIL_CODE: Bb1", Color.argb(alphaValue, 203, 127, 153));
        soilsColorMap.put("SOIL_CODE: Bd", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Bd-Bld1", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Bd-Pd1", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Bd1", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Bd1-Bfs2", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Bd1-Pd1", Color.argb(alphaValue, 214, 167, 168));
        soilsColorMap.put("SOIL_CODE: Be", Color.argb(alphaValue, 191, 251, 249));
        soilsColorMap.put("SOIL_CODE: Bfs1", Color.argb(alphaValue, 27, 122, 90));
        soilsColorMap.put("SOIL_CODE: Bfs2", Color.argb(alphaValue, 27, 122, 90));
        soilsColorMap.put("SOIL_CODE: Bgb", Color.argb(alphaValue, 176, 105, 103));
        soilsColorMap.put("SOIL_CODE: Bi", Color.argb(alphaValue, 221, 104, 108));
        soilsColorMap.put("SOIL_CODE: Bk", Color.argb(alphaValue, 226, 165, 50));
        soilsColorMap.put("SOIL_CODE: Bl", Color.argb(alphaValue, 109, 42, 111));
        soilsColorMap.put("SOIL_CODE: Bl-Ca", Color.argb(alphaValue, 109, 42, 111));
        soilsColorMap.put("SOIL_CODE: Bl-Ps", Color.argb(alphaValue, 109, 42, 111));
        soilsColorMap.put("SOIL_CODE: Blb1", Color.argb(alphaValue, 253, 197, 200));
        soilsColorMap.put("SOIL_CODE: Blb1-RBb1", Color.argb(alphaValue, 253, 197, 200));
        soilsColorMap.put("SOIL_CODE: Bld1", Color.argb(alphaValue, 210, 204, 240));
        soilsColorMap.put("SOIL_CODE: Bld1-Bd", Color.argb(alphaValue, 210, 204, 240));
        soilsColorMap.put("SOIL_CODE: Bld1-Pd1", Color.argb(alphaValue, 210, 204, 240));
        soilsColorMap.put("SOIL_CODE: Bld1-Pss", Color.argb(alphaValue, 210, 204, 240));
        soilsColorMap.put("SOIL_CODE: Bms", Color.argb(alphaValue, 112, 200, 98));
        soilsColorMap.put("SOIL_CODE: Bms1", Color.argb(alphaValue, 112, 200, 98));
        soilsColorMap.put("SOIL_CODE: Bms1-Pss", Color.argb(alphaValue, 112, 200, 98));
        soilsColorMap.put("SOIL_CODE: Bo", Color.argb(alphaValue, 153, 171, 157));
        soilsColorMap.put("SOIL_CODE: Bo-Ps", Color.argb(alphaValue, 153, 171, 157));
        soilsColorMap.put("SOIL_CODE: Bp", Color.argb(alphaValue, 42, 80, 52));
        soilsColorMap.put("SOIL_CODE: Br", Color.argb(alphaValue, 186, 114, 66));
        soilsColorMap.put("SOIL_CODE: Br-Ca", Color.argb(alphaValue, 186, 114, 66));
        soilsColorMap.put("SOIL_CODE: Br-Ps", Color.argb(alphaValue, 186, 114, 66));
        soilsColorMap.put("SOIL_CODE: Br-Ta", Color.argb(alphaValue, 186, 114, 66));
        soilsColorMap.put("SOIL_CODE: Bss", Color.argb(alphaValue, 73, 137, 1));
        soilsColorMap.put("SOIL_CODE: Bu", Color.argb(alphaValue, 208, 34, 33));
        soilsColorMap.put("SOIL_CODE: Ca", Color.argb(alphaValue, 213, 184, 126));
        soilsColorMap.put("SOIL_CODE: Ca-Ea", Color.argb(alphaValue, 213, 184, 126));
        soilsColorMap.put("SOIL_CODE: Ca-Gl", Color.argb(alphaValue, 213, 184, 126));
        soilsColorMap.put("SOIL_CODE: Ca-Ps", Color.argb(alphaValue, 213, 184, 126));
        soilsColorMap.put("SOIL_CODE: Ca-Ta", Color.argb(alphaValue, 213, 184, 126));
        soilsColorMap.put("SOIL_CODE: Cc", Color.argb(alphaValue, 128, 149, 74));
        soilsColorMap.put("SOIL_CODE: Cl", Color.argb(alphaValue, 66, 143, 193));
        soilsColorMap.put("SOIL_CODE: Cm", Color.argb(alphaValue, 234, 185, 118));
        soilsColorMap.put("SOIL_CODE: Cm-Bd", Color.argb(alphaValue, 234, 185, 118));
        soilsColorMap.put("SOIL_CODE: Cm-Ps", Color.argb(alphaValue, 234, 185, 118));
        soilsColorMap.put("SOIL_CODE: Cs", Color.argb(alphaValue, 250, 226, 90));
        soilsColorMap.put("SOIL_CODE: Cy", Color.argb(alphaValue, 73, 41, 2));
        soilsColorMap.put("SOIL_CODE: Da", Color.argb(alphaValue, 32, 68, 6));
        soilsColorMap.put("SOIL_CODE: Dl", Color.argb(alphaValue, 248, 229, 186));
        soilsColorMap.put("SOIL_CODE: DlLv", Color.argb(alphaValue, 248, 229, 186));
        soilsColorMap.put("SOIL_CODE: Dw", Color.argb(alphaValue, 62, 46, 35));
        soilsColorMap.put("SOIL_CODE: Ea", Color.argb(alphaValue, 122, 158, 156));
        soilsColorMap.put("SOIL_CODE: Ea-Ar", Color.argb(alphaValue, 122, 158, 156));
        soilsColorMap.put("SOIL_CODE: Ea-F", Color.argb(alphaValue, 122, 158, 156));
        soilsColorMap.put("SOIL_CODE: Ea-Ps", Color.argb(alphaValue, 122, 158, 156));
        soilsColorMap.put("SOIL_CODE: Ea-Wk", Color.argb(alphaValue, 122, 158, 156));
        soilsColorMap.put("SOIL_CODE: Ec", Color.argb(alphaValue, 205, 183, 106));
        soilsColorMap.put("SOIL_CODE: FG", Color.argb(alphaValue, 166, 116, 0));
        soilsColorMap.put("SOIL_CODE: Fl", Color.argb(alphaValue, 164, 178, 106));
        soilsColorMap.put("SOIL_CODE: GBb", Color.argb(alphaValue, 252, 106, 173));
        soilsColorMap.put("SOIL_CODE: Gl", Color.argb(alphaValue, 65, 183, 161));
        soilsColorMap.put("SOIL_CODE: Gl-Ca", Color.argb(alphaValue, 65, 183, 161));
        soilsColorMap.put("SOIL_CODE: Gl-Ps", Color.argb(alphaValue, 65, 183, 161));
        soilsColorMap.put("SOIL_CODE: Gl-Qu", Color.argb(alphaValue, 65, 183, 161));
        soilsColorMap.put("SOIL_CODE: Hg", Color.argb(alphaValue, 169, 178, 125));
        soilsColorMap.put("SOIL_CODE: Hl", Color.argb(alphaValue, 220, 145, 29));
        soilsColorMap.put("SOIL_CODE: Ho", Color.argb(alphaValue, 225, 105, 10));
        soilsColorMap.put("SOIL_CODE: In", Color.argb(alphaValue, 160, 146, 71));
        soilsColorMap.put("SOIL_CODE: K", Color.argb(alphaValue, 170, 140, 142));
        soilsColorMap.put("SOIL_CODE: Kb", Color.argb(alphaValue, 178, 164, 249));
        soilsColorMap.put("SOIL_CODE: Kl", Color.argb(alphaValue, 28, 133, 90));
        soilsColorMap.put("SOIL_CODE: L", Color.argb(alphaValue, 200, 116, 191));
        soilsColorMap.put("SOIL_CODE: Le", Color.argb(alphaValue, 194, 254, 192));
        soilsColorMap.put("SOIL_CODE: Lf", Color.argb(alphaValue, 122, 51, 26));
        soilsColorMap.put("SOIL_CODE: Lg", Color.argb(alphaValue, 231, 228, 109));
        soilsColorMap.put("SOIL_CODE: Lp", Color.argb(alphaValue, 215, 90, 72));
        soilsColorMap.put("SOIL_CODE: Lw", Color.argb(alphaValue, 249, 80, 77));
        soilsColorMap.put("SOIL_CODE: M1", Color.argb(alphaValue, 77, 116, 133));
        soilsColorMap.put("SOIL_CODE: M1-Pd1", Color.argb(alphaValue, 77, 116, 133));
        soilsColorMap.put("SOIL_CODE: M1-Qu", Color.argb(alphaValue, 77, 116, 133));
        soilsColorMap.put("SOIL_CODE: M10", Color.argb(alphaValue, 16, 163, 147));
        soilsColorMap.put("SOIL_CODE: M2", Color.argb(alphaValue, 116, 242, 127));
        soilsColorMap.put("SOIL_CODE: M3", Color.argb(alphaValue, 144, 125, 57));
        soilsColorMap.put("SOIL_CODE: M4", Color.argb(alphaValue, 218, 122, 113));
        soilsColorMap.put("SOIL_CODE: M5", Color.argb(alphaValue, 94, 91, 71));
        soilsColorMap.put("SOIL_CODE: M6", Color.argb(alphaValue, 124, 237, 191));
        soilsColorMap.put("SOIL_CODE: M7", Color.argb(alphaValue, 232, 40, 151));
        soilsColorMap.put("SOIL_CODE: M8", Color.argb(alphaValue, 120, 106, 31));
        soilsColorMap.put("SOIL_CODE: M9", Color.argb(alphaValue, 128, 129, 68));
        soilsColorMap.put("SOIL_CODE: MEa", Color.argb(alphaValue, 57, 153, 167));
        soilsColorMap.put("SOIL_CODE: MEa2", Color.argb(alphaValue, 68, 97, 177));
        soilsColorMap.put("SOIL_CODE: Mi", Color.argb(alphaValue, 232, 213, 198));
        soilsColorMap.put("SOIL_CODE: Mq", Color.argb(alphaValue, 218, 102, 108));
        soilsColorMap.put("SOIL_CODE: Mq+Bk", Color.argb(alphaValue, 218, 102, 108));
        soilsColorMap.put("SOIL_CODE: Mq-Ps", Color.argb(alphaValue, 218, 102, 108));
        soilsColorMap.put("SOIL_CODE: Ne", Color.argb(alphaValue, 142, 90, 58));
        soilsColorMap.put("SOIL_CODE: Ne-Ps", Color.argb(alphaValue, 142, 90, 58));
        soilsColorMap.put("SOIL_CODE: Nl", Color.argb(alphaValue, 113, 127, 42));
        soilsColorMap.put("SOIL_CODE: Nr", Color.argb(alphaValue, 178, 191, 179));
        soilsColorMap.put("SOIL_CODE: Nt", Color.argb(alphaValue, 181, 191, 180));
        soilsColorMap.put("SOIL_CODE: On", Color.argb(alphaValue, 133, 60, 17));
        soilsColorMap.put("SOIL_CODE: Pcs", Color.argb(alphaValue, 124, 156, 119));
        soilsColorMap.put("SOIL_CODE: Pcs-Pd1", Color.argb(alphaValue, 145, 165, 157));
        soilsColorMap.put("SOIL_CODE: Pd1", Color.argb(alphaValue, 136, 140, 165));
        soilsColorMap.put("SOIL_CODE: Pd1-Bd1", Color.argb(alphaValue, 136, 140, 165));
        soilsColorMap.put("SOIL_CODE: Pd1-Bld1", Color.argb(alphaValue, 136, 140, 165));
        soilsColorMap.put("SOIL_CODE: Pd1-Pcs", Color.argb(alphaValue, 136, 140, 165));
        soilsColorMap.put("SOIL_CODE: Pd1-Pss", Color.argb(alphaValue, 136, 140, 165));
        soilsColorMap.put("SOIL_CODE: Pd2", Color.argb(alphaValue, 106, 99, 140));
        soilsColorMap.put("SOIL_CODE: Pm", Color.argb(alphaValue, 246, 180, 122));
        soilsColorMap.put("SOIL_CODE: Pm1", Color.argb(alphaValue, 247, 150, 59));
        soilsColorMap.put("SOIL_CODE: Pm2", Color.argb(alphaValue, 246, 127, 19));
        soilsColorMap.put("SOIL_CODE: Pm2-Pd1", Color.argb(alphaValue, 246, 127, 19));
        soilsColorMap.put("SOIL_CODE: Pm3", Color.argb(alphaValue, 246, 127, 19));
        soilsColorMap.put("SOIL_CODE: Ps", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Ps-Bo", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Ps-Ca", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Ps-Ea", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Ps-Mq", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Ps-Wk", Color.argb(alphaValue, 61, 85, 119));
        soilsColorMap.put("SOIL_CODE: Pss", Color.argb(alphaValue, 89, 125, 113));
        soilsColorMap.put("SOIL_CODE: Pss(F)", Color.argb(alphaValue, 89, 125, 113));
        soilsColorMap.put("SOIL_CODE: Pss1", Color.argb(alphaValue, 89, 125, 113));
        soilsColorMap.put("SOIL_CODE: Qu", Color.argb(alphaValue, 78, 110, 9));
        soilsColorMap.put("SOIL_CODE: Qu-Bl", Color.argb(alphaValue, 206, 137, 156));
        soilsColorMap.put("SOIL_CODE: RBb", Color.argb(alphaValue, 216, 49, 116));
        soilsColorMap.put("SOIL_CODE: RBb1", Color.argb(alphaValue, 166, 86, 87));
        soilsColorMap.put("SOIL_CODE: Rb", Color.argb(alphaValue, 254, 151, 152));
        soilsColorMap.put("SOIL_CODE: RbLv", Color.argb(alphaValue, 254, 151, 152));
        soilsColorMap.put("SOIL_CODE: Ri", Color.argb(alphaValue, 106, 133, 64));
        soilsColorMap.put("SOIL_CODE: Ro", Color.argb(alphaValue, 172, 68, 230));
        soilsColorMap.put("SOIL_CODE: Ro-Dw", Color.argb(alphaValue, 172, 68, 230));
        soilsColorMap.put("SOIL_CODE: Ro-Pss1", Color.argb(alphaValue, 172, 68, 230));
        soilsColorMap.put("SOIL_CODE: Rs", Color.argb(alphaValue, 167, 234, 131));
        soilsColorMap.put("SOIL_CODE: Rv", Color.argb(alphaValue, 230, 114, 120));
        soilsColorMap.put("SOIL_CODE: Rw", Color.argb(alphaValue, 206, 135, 93));
        soilsColorMap.put("SOIL_CODE: Sh", Color.argb(alphaValue, 29, 118, 30));
        soilsColorMap.put("SOIL_CODE: Sp", Color.argb(alphaValue, 75, 252, 102));
        soilsColorMap.put("SOIL_CODE: Su", Color.argb(alphaValue, 94, 132, 0));
        soilsColorMap.put("SOIL_CODE: Tm", Color.argb(alphaValue, 75, 78, 15));
        soilsColorMap.put("SOIL_CODE: Tt", Color.argb(alphaValue, 178, 108, 6));
        soilsColorMap.put("SOIL_CODE: Vl", Color.argb(alphaValue, 82, 61, 4));
        soilsColorMap.put("SOIL_CODE: Wa", Color.argb(alphaValue, 246, 142, 43));
        soilsColorMap.put("SOIL_CODE: Wbs", Color.argb(alphaValue, 0, 166, 76));
        soilsColorMap.put("SOIL_CODE: Wk", Color.argb(alphaValue, 125, 135, 72));
        soilsColorMap.put("SOIL_CODE: Wk-Ps", Color.argb(alphaValue, 125, 135, 72));
        soilsColorMap.put("SOIL_CODE: Wr", Color.argb(alphaValue, 182, 78, 88));
        soilsColorMap.put("SOIL_CODE: Wy", Color.argb(alphaValue, 206, 203, 114));
        soilsColorMap.put("SOIL_CODE: YT", Color.argb(alphaValue, 4, 227, 136));
        return soilsColorMap;
    }

}
