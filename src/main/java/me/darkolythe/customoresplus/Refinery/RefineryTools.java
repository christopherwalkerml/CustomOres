package me.darkolythe.customoresplus.Refinery;

import me.darkolythe.customoresplus.CustomOresPlus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import static java.lang.Math.*;

public class RefineryTools {

    public CustomOresPlus main;
    public RefineryTools(CustomOresPlus plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    public boolean itemstackEquals(ItemStack item1, ItemStack item2) {
        if (item1 != null && item2 != null) {
            ItemStack i1 = item1.clone();
            ItemStack i2 = item2.clone();
            i1.setAmount(1);
            i2.setAmount(1);
            if (i1.equals(i2)) {
                return true;
            }
        } else if (item1 == item2) {
            return true;
        }
        return false;
    }

    /*
    This creates the armour stand models for tier 1 and tier II refineries
     */
    public void drawRefinery(byte tier, World world, Refinery refinery, byte rotation) {
        if (tier == 1) {
            double[] vals = {0.5, -0.68, 0.8};
            double[] angles = {0, 0, 0};
            ArmorStand ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.CRAFTING_TABLE, 1));

            vals = new double[] {0.282, -1.38, 0.5};
            angles = new double[] {0, 90, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.ANVIL, 1));

            vals = new double[] {0.23, -1.12, 0.5};
            angles = new double[] {90, 90, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.COBBLESTONE_SLAB, 1));

            vals = new double[] {0.5, -1.12, 0.73};
            angles = new double[] {90, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.COBBLESTONE_SLAB, 1));

            vals = new double[] {0.73, -1.12, 0.5};
            angles = new double[] {90, 270, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.COBBLESTONE_SLAB, 1));

            vals = new double[] {0.5, -1.12, 0.27};
            angles = new double[] {90, 180, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.COBBLESTONE_SLAB, 1));

            vals = new double[] {0.58, -0.3, 0.5};
            angles = new double[] {90, 62, 37};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setHelmet(new ItemStack(Material.MAGMA_BLOCK, 1));

            vals = new double[] {0.5, 0.2, 0.15};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setItemInHand(new ItemStack(Material.COAL_BLOCK, 1));

            vals = new double[] {0.54, -0.4, 0.48};
            angles = new double[] {34, 62, 53};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setHelmet(new ItemStack(Material.COAL_BLOCK, 1));

            vals = new double[] {0.88, 0.28, 0.41};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setItemInHand(new ItemStack(Material.MAGMA_BLOCK, 1));

            vals = new double[] {0.86, -0.14, 0.6};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[2], true);
            ar.setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
            ar.setRightArmPose(new EulerAngle(toRadians(71), toRadians(344), toRadians(350)));
        } else if (tier == 2) {
            double[] vals = {0.282, -0.281, 0.78};
            double[] angles = {0, 0, 0};
            ArmorStand ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.718, -0.281, 0.78};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.718, -0.281, 0.344};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.282, -0.281, 0.344};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.5, -1.38, 0.562};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[0], false);
            ar.setHelmet(new ItemStack(Material.CRAFTING_TABLE, 1));

            vals = new double[] {0.22, -0.281, 0.282};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.22, -0.281, 0.718};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.656, -0.281, 0.282};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.656, -0.281, 0.718};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.586, -1.38, 0.5};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.ANVIL, 1));

            vals = new double[] {0.196, -1.38, 0.5};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[2], false);
            ar.setHelmet(new ItemStack(Material.ANVIL, 1));

            vals = new double[] {0.31, -1.38, 0.31};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICKS, 1));

            vals = new double[] {0.69, -1.38, 0.31};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICKS, 1));

            vals = new double[] {0.69, -1.38, 0.69};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICKS, 1));

            vals = new double[] {0.31, -1.38, 0.69};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, false, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICKS, 1));

            vals = new double[] {0.7835, 0.11, 0.828};
            angles = new double[] {90, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.348, 0.11, 0.828};
            angles = new double[] {90, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.7835, 0.11, 0.042};
            angles = new double[] {90, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.348, 0.11, 0.042};
            angles = new double[] {90, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.172, 0.11, 0.216};
            angles = new double[] {90, 90, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.172, 0.11, 0.782};
            angles = new double[] {90, 90, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.172, 0.11, 0.5};
            angles = new double[] {90, 90, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], false);
            ar.setHelmet(new ItemStack(Material.STONE_BRICK_SLAB, 1));

            vals = new double[] {0.4, -0.3, 0.4};
            angles = new double[] {30, 65, 78};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setHelmet(new ItemStack(Material.MAGMA_BLOCK, 1));

            vals = new double[] {0.65, 0.3, 0.5};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setItemInHand(new ItemStack(Material.COAL_BLOCK, 1));

            vals = new double[] {0.6, -0.35, 0.6};
            angles = new double[] {22, 55, 54};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setHelmet(new ItemStack(Material.COAL_BLOCK, 1));

            vals = new double[] {0.7, 0, 0.1};
            angles = new double[] {0, 0, 0};
            ar = displaceValue(vals, angles, rotation, world, true, refinery.blocklocs[1], true);
            ar.setItemInHand(new ItemStack(Material.MAGMA_BLOCK, 1));
            ar.setRightArmPose(new EulerAngle(toRadians(315), toRadians(48), toRadians(36)));
        }
    }

    /*
    This function takes care of rotating the armour stands properly for the refineries
     */
    private ArmorStand displaceValue(double[] locs, double[] angle, byte rotation, World world, boolean small, Integer[] blocklocs, boolean ishand) {
        int rot = abs((rotation - 4) % 4);

        double[] nums = new double[2];
        if (rot == 0) {
            nums[0] = locs[0];
            nums[1] = locs[2];
        } else if (rot == 1) {
            nums[0] = 1 - locs[2];
            nums[1] = locs[0];
        } else if (rot == 2) {
            nums[0] = 1 - locs[0];
            nums[1] = 1 - locs[2];
        } else {
            nums[0] = locs[2];
            nums[1] = 1 - locs[0];
        }
        Location loc = new Location(world, blocklocs[0] + nums[0], blocklocs[1] + locs[1], blocklocs[2] + nums[1]);
        if (ishand) {
            loc.setYaw((rot * 90) % 360);
        }
        ArmorStand ar = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        if (!ishand) {
            ar.setHeadPose(new EulerAngle(toRadians(angle[0]), toRadians((angle[1] + (rot * 90)) % 360), toRadians(angle[2])));
        } else {
            ar.setHeadPose(new EulerAngle(toRadians(angle[0]), toRadians(angle[1]), toRadians(angle[2])));
        }
        baseStand(ar, small);

        return ar;
    }

    /*
    This function sets all the base stats for the refinery models
     */
    private void baseStand(ArmorStand stand, boolean small) {
        stand.setMarker(true);
        stand.setInvulnerable(true);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(small);
    }
}
