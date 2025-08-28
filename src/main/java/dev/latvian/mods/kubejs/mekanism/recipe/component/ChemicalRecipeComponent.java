package dev.latvian.mods.kubejs.mekanism.recipe.component;

import dev.latvian.mods.kubejs.mekanism.MekanismChemicalWrapper;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.common.Mekanism;
import net.minecraft.core.Holder;

public class ChemicalRecipeComponent extends ChemicalLikeRecipeComponent<Holder<Chemical>> {
	public static final RecipeComponentType<Holder<Chemical>> CHEMICAL = RecipeComponentType.unit(Mekanism.rl("chemical"), ChemicalRecipeComponent::new);

	private ChemicalRecipeComponent(RecipeComponentType<?> type) {
		super(type, Chemical.HOLDER_CODEC, MekanismChemicalWrapper.CHEMICAL_HOLDER_TYPE_INFO);
	}

	@Override
	public boolean matches(RecipeMatchContext cx, Holder<Chemical> value, ReplacementMatchInfo match) {
		return match.match() instanceof ChemicalIngredient m && value.getKey() != MekanismAPI.EMPTY_CHEMICAL_KEY && m.test(value);
	}

	@Override
	public boolean isEmpty(Holder<Chemical> value) {
		return value.getKey() == MekanismAPI.EMPTY_CHEMICAL_KEY;
	}

	@Override
	public void buildUniqueId(UniqueIdBuilder builder, Holder<Chemical> value) {
		builder.append(value.getKey());
	}
}
