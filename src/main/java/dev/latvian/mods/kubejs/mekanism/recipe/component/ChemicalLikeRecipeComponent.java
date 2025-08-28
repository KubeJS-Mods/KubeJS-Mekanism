package dev.latvian.mods.kubejs.mekanism.recipe.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.rhino.type.TypeInfo;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;

public class ChemicalLikeRecipeComponent<T> extends SimpleRecipeComponent<T> {
	public ChemicalLikeRecipeComponent(RecipeComponentType<?> type, Codec<T> codec, TypeInfo typeInfo) {
		super(type, codec, typeInfo);
	}

	@Override
	public boolean hasPriority(RecipeMatchContext cx, Object from) {
		return from instanceof Chemical || from instanceof ChemicalStack || from instanceof ChemicalIngredient || from instanceof ChemicalStackIngredient;
	}
}
