package exnihilum.com.au.listdataviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Utilities.AddressUtilities;

public class AddressParser {

    private static Address parsedAddress = new Address();
    private static ArrayList<String> tokensList;
    private String rawAddressString;

    public AddressParser(String rawAddressString) {
        this.rawAddressString = rawAddressString;
    }

    private Address parseAddress(String rawAddressString) {
        String[] tokens = rawAddressString.trim().replaceAll(",", "").toUpperCase().split(" ");
        tokensList = new ArrayList<>(Arrays.asList(tokens));

        Integer containsRoad = parseStreetType();
        parseLocality(containsRoad);
        if (containsRoad > 0) {
            parseStreetName(containsRoad);
            parseStreetNumber(containsRoad);
        }
        return parsedAddress;
    }

    private static void parseStreetName(Integer position) {
        Integer index = 0;
        StringBuilder streetName = new StringBuilder();
        for (String entry : tokensList) {
            if (!containsDigit(entry) && index < position && !entry.toUpperCase().equals("UNIT")) {
                streetName.append(entry).append(" ");
            }
            index ++;
        }
        parsedAddress.setStreetName(streetName.toString().trim());
    }

    private static void parseStreetNumber(Integer position) {
        Integer index = 0;
        for (String entry : tokensList) {
            if (isInteger(entry) && index < position) {
                parsedAddress.setStreetNumber(entry);
            }
            index ++;
        }
    }

    private static void parseLocality(Integer position) {
        // passed in position is where the road/rd term was in the tokenised input string
        StringBuilder locality = new StringBuilder();
        Integer index = 0;
        for (String entry : tokensList) {
            if (position == 0 || index > position) {
                locality.append(entry).append(" ");
            }
            index++;
        }
        parsedAddress.setLocality(locality.toString().trim());
    }

    private static Integer parseStreetType() {
        String[] contractionsArray = AddressUtilities.makeStreetContractionsList();
        String[] streetTypeArray = AddressUtilities.makeStreetTypeList();
        ArrayList<String> contractions = new ArrayList<>(Arrays.asList(contractionsArray));
        ArrayList<String> streetTypes = new ArrayList<>(Arrays.asList(streetTypeArray));
        HashMap<String, String> contractionMap = AddressUtilities.makeContractions();
        int index = 0;
        for (String entry : tokensList) {
            if (streetTypes.contains(entry)) {
                parsedAddress.setStreetType(entry);
                return index;
            } else if (contractions.contains(entry)) {
                String streetType = contractionMap.get(entry);
                parsedAddress.setStreetType(streetType);
                return index;
            }
            index++;
        }
        return 0;
    }

    private static void printAddress() {
        System.out.println(parsedAddress.toString());
    }

    // helper methods to test if a string is an integer - i.e. is *only* a number
    private static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    // helper method to see if a string contains a digit (number)
    private static boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }

}
