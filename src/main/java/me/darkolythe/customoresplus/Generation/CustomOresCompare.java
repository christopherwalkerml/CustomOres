package me.darkolythe.customoresplus.Generation;

import java.util.Comparator;

public class CustomOresCompare implements Comparator<CustomOre> {

    /*
    This function is the compare function for sorting the ores
     */
    public int compare(CustomOre ore1, CustomOre ore2) {
        return (ore1.y - ore2.y);
    }

}
