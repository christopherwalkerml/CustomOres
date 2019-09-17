package me.darkolythe.customoresplus.Generation;

import java.util.ArrayList;
import java.util.List;

public class OreData {

    public short oreid;
    public String type;
    public Integer ymin;
    public Integer ymax;
    public Integer frequency;
    public Integer veinsize;
    public String id;
    public String textures;
    public List<String> blocks = new ArrayList<>();
    public String colour;
    public boolean blastResistant;
    public List<String> worlds = new ArrayList<>();
    public Integer requiredtier;
}