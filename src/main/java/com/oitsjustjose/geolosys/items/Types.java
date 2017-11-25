package com.oitsjustjose.geolosys.items;

import net.minecraft.util.IStringSerializable;

public class Types
{
    public enum Ingot implements IStringSerializable
    {
        COPPER(0, "copper"),
        TIN(1, "tin"),
        SILVER(2, "silver"),
        LEAD(3, "lead"),
        ALUMINUM(4, "aluminum"),
        NICKEL(5, "nickel"),
        PLATINUM(6, "platinum"),
        ZINC(7, "zinc");

        private static final Ingot[] META_LOOKUP = new Ingot[values().length];
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Ingot(int meta, String name)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public static Ingot byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.serializedName;
        }

        static
        {
            for (Ingot type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }

    public enum Cluster implements IStringSerializable
    {
        IRON(0, "iron"),
        GOLD(1, "gold"),
        COPPER(2, "copper"),
        TIN(3, "tin"),
        SILVER(4, "silver"),
        LEAD(5, "lead"),
        ALUMINUM(6, "aluminum"),
        NICKEL(7, "nickel"),
        PLATINUM(8, "platinum"),
        URANIUM(9, "uranium"),
        ZINC(10, "zinc"),
        YELLORIUM(11, "yellorium"),
        OSMIUM(12, "osmium");

        private static final Cluster[] META_LOOKUP = new Cluster[values().length];
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Cluster(int meta, String name)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public static Cluster byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.serializedName;
        }

        static
        {
            for (Cluster type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }
}
