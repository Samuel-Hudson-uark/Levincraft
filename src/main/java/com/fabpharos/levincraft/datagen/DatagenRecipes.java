package com.fabpharos.levincraft.datagen;

import com.fabpharos.levincraft.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class DatagenRecipes extends RecipeProvider {
    public DatagenRecipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.BAUXITE_ORE_ITEM.get()),
                        Registration.ALUMINUM_INGOT.get(), 1.0f, 100)
                .unlockedBy("has_ore", has(Registration.BAUXITE_ORE_ITEM.get()))
                .save(consumer, "aluminum_ingot1");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.RAW_BAUXITE.get()),
                        Registration.ALUMINUM_INGOT.get(), 0.0f, 100)
                .unlockedBy("has_chunk", has(Registration.RAW_BAUXITE.get()))
                .save(consumer, "aluminum_ingot2");
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Registration.DURALUMIN_BLEND.get()),
                        Registration.DURALUMIN_INGOT.get(), 0.0f, 400)
                .unlockedBy("has_chunk", has(Registration.DURALUMIN_BLEND.get()))
                .save(consumer, "duralumin_ingot");
        ShapelessRecipeBuilder.shapeless(Registration.DURALUMIN_BLEND.get(), 2)
                .requires(Items.RAW_COPPER, 1)
                .requires(Registration.RAW_BAUXITE.get(), 8)
                .unlockedBy("has_chunk", has(Registration.RAW_BAUXITE.get()))
                .save(consumer, "duralumin_blend");;
        makeMetalConversionRecipies(Registration.ALUMINUM_NUGGET.get(), Registration.ALUMINUM_INGOT.get(), Registration.ALUMINUM_BLOCK_ITEM.get(), consumer);
        makeMetalConversionRecipies(Registration.DURALUMIN_NUGGET.get(), Registration.DURALUMIN_INGOT.get(), Registration.DURALUMIN_BLOCK_ITEM.get(), consumer);
    }

    private <I extends Item> void makeMetalConversionRecipies(I nugget, I ingot, I block, Consumer<FinishedRecipe> consumer) {
        //Nuggets to Ingot
        ShapelessRecipeBuilder.shapeless(ingot)
                .requires(nugget, 9)
                .group("levincraft")
                .unlockedBy(nugget.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(nugget))
                .save(consumer, nugget.getRegistryName().getPath() + "s_to_ingot");
        //Ingots to Block
        ShapelessRecipeBuilder.shapeless(block)
                .requires(ingot, 9)
                .group("levincraft")
                .unlockedBy(ingot.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ingot))
                .save(consumer, ingot.getRegistryName().getPath() + "s_to_block");
        //Block to Ingots
        ShapelessRecipeBuilder.shapeless(ingot, 9)
                .requires(block)
                .group("levincraft")
                .unlockedBy(block.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(block))
                .save(consumer, block.getRegistryName().getPath() + "_to_ingots");
        //Ingots to Nuggets
        ShapelessRecipeBuilder.shapeless(nugget, 9)
                .requires(ingot)
                .group("levincraft")
                .unlockedBy(ingot.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(ingot))
                .save(consumer, ingot.getRegistryName().getPath() + "_to_nuggets");
    }
}
