package com.flanks255.simplygraves;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum Grave {
    WOOD("gravestone_wood", "Gravestone Wood", "gravestone_wood", Block.box(5,0,7,11,14,9)),
    ANDESITE("andesite_gravestone", "Andesite Gravestone", "gravestone_andesite", Block.box(5,0, 1, 11,7 ,15)),
    BASSALT("basselt_gravestone", "Basselt Gravestone", "gravestone_bassalt", Block.box(5,0, 1, 11,7 ,15)),
    CALCITE("calcite_gravestone", "Calcite Gravestone", "gravestone_calcite", Block.box(5,0, 1, 11,7 ,15)),
    DARK_PRISMARINE("dark_prismarine_gravestone", "Dark Prismarine Gravestone", "gravestone_dark_prismarine", Block.box(5,0, 1, 11,7 ,15)),
    DEEPSLATE("deepslate_gravestone", "Deepslate Gravestone", "gravestone_deepslate", Block.box(5,0, 1, 11,7 ,15)),
    DIORITE("diorite_gravestone", "Diorite Gravestone", "gravestone_diorite", Block.box(5,0, 1, 11,7 ,15)),
    DRIPSTONE("dripstone_gravestone", "Dripstone Gravestone", "gravestone_dripstone", Block.box(5,0, 1, 11,7 ,15)),
    GRANITE("granite_gravestone", "Granite Gravestone", "gravestone_granite", Block.box(5,0, 1, 11,7 ,15)),
    STONE("stone_gravestone", "Stone Gravestone", "gravestone_stone", Block.box(5,0, 1, 11,7 ,15)),
    END_STONE("end_stone_gravestone", "End Stone Gravestone", "gravestone_end_stone", Block.box(5,0, 1, 11,7 ,15)),
    BLACKSTONE("blackstone_gravestone", "Blackstone Gravestone", "gravestone_blackstone", Block.box(5,0, 1, 11,7 ,15));
    public final String regName;
    public final String modelName;
    public final VoxelShape shape;
    public final String lang;


    Grave(String regName, String langName, String modelName, VoxelShape shape) {
        this.lang = langName;
        this.regName = regName;
        this.modelName = modelName;
        this.shape = shape;
    }
}
