package dev.latvian.kubejs.mekanism.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.MapJS;
import mekanism.api.JsonConstants;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.ChemicalType;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

// @formatter:off
public record ChemicalWrapper<C extends Chemical<C>, S extends ChemicalStack<C>, I extends ChemicalStackIngredient<C, S>>(
		String key,
		ChemicalType type,
		ResourceKey<? extends Registry<C>> registry,
		IChemicalStackIngredientCreator<C, S, I> creator,
		long defaultAmount,
		Function<ResourceLocation, C> chemicalFromId,
		Class<S> stackType,
		Class<I> ingredientType
) {
	public static final ChemicalWrapper<Gas, GasStack, ChemicalStackIngredient.GasStackIngredient> GAS = new ChemicalWrapper<>(
			JsonConstants.GAS,
			ChemicalType.GAS,
			MekanismAPI.gasRegistryName(),
			IngredientCreatorAccess.gas(),
			1000L,
			Gas::getFromRegistry,
			GasStack.class,
			ChemicalStackIngredient.GasStackIngredient.class
	);

	public static final ChemicalWrapper<InfuseType, InfusionStack, ChemicalStackIngredient.InfusionStackIngredient> INFUSE_TYPE = new ChemicalWrapper<>(
			JsonConstants.INFUSE_TYPE,
			ChemicalType.INFUSION,
			MekanismAPI.infuseTypeRegistryName(),
			IngredientCreatorAccess.infusion(),
			10L,
			InfuseType::getFromRegistry,
			InfusionStack.class,
			ChemicalStackIngredient.InfusionStackIngredient.class
	);

	public static final ChemicalWrapper<Pigment, PigmentStack, ChemicalStackIngredient.PigmentStackIngredient> PIGMENT = new ChemicalWrapper<>(
			JsonConstants.PIGMENT,
			ChemicalType.PIGMENT,
			MekanismAPI.pigmentRegistryName(),
			IngredientCreatorAccess.pigment(),
			1000L,
			Pigment::getFromRegistry,
			PigmentStack.class,
			ChemicalStackIngredient.PigmentStackIngredient.class
	);

	public static final ChemicalWrapper<Slurry, SlurryStack, ChemicalStackIngredient.SlurryStackIngredient> SLURRY = new ChemicalWrapper<>(
			JsonConstants.SLURRY,
			ChemicalType.SLURRY,
			MekanismAPI.slurryRegistryName(),
			IngredientCreatorAccess.slurry(),
			1000L,
			Slurry::getFromRegistry,
			SlurryStack.class,
			ChemicalStackIngredient.SlurryStackIngredient.class
	);
// @formatter:on

	public static final ChemicalWrapper<?, ?, ?>[] VALUES = {GAS, INFUSE_TYPE, PIGMENT, SLURRY};

	@Nullable
	public static ChemicalWrapper<?, ?, ?> find(Map<?, ?> map) {
		for (var wrapper : VALUES) {
			if (map.containsKey(wrapper.key)) {
				return wrapper;
			}
		}

		return null;
	}

	public I ingredient(String id, long amount) {
		if (amount <= 0) {
			amount = defaultAmount;
		}

		if (id.startsWith("#")) {
			var tag = TagKey.create(registry, new ResourceLocation(id.substring(1)));
			return creator.from(tag, amount);
		}

		return creator.from(chemicalFromId.apply(new ResourceLocation(id)), amount);
	}

	public S stack(String id, long amount) {
		if (amount <= 0) {
			amount = defaultAmount;
		}

		return (S) chemicalFromId.apply(new ResourceLocation(id)).getStack(amount);
	}

	public record InputComponent<C extends Chemical<C>, S extends ChemicalStack<C>, I extends ChemicalStackIngredient<C, S>>(ChemicalWrapper<C, S, I> wrapper) implements RecipeComponent<I> {
		@Override
		public ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public Class<?> componentClass() {
			return wrapper.ingredientType;
		}

		@Override
		public JsonElement write(RecipeJS recipe, I value) {
			return value.serialize();
		}

		@Override
		public TypeDescJS constructorDescription(DescriptionContext ctx) {
			return TypeDescJS.object().add(wrapper.key, TypeDescJS.STRING).add(JsonConstants.AMOUNT, TypeDescJS.NUMBER).or(TypeDescJS.STRING);
		}

		@Override
		public I read(RecipeJS recipe, Object from) {
			if (wrapper.ingredientType.isInstance(from)) {
				return (I) from;
			} else if (from instanceof CharSequence) {
				return wrapper.ingredient(from.toString(), wrapper.defaultAmount);
			} else if (from instanceof Map<?, ?> || from instanceof JsonObject) {
				var map = MapJS.of(from);

				if (map != null) {
					return wrapper.ingredient(map.get(wrapper.key()).toString(), map.containsKey(JsonConstants.AMOUNT) ? ((Number) map.get(JsonConstants.AMOUNT)).longValue() : 0L);
				}
			}

			return null;
		}
	}

	public record OutputComponent<C extends Chemical<C>, S extends ChemicalStack<C>, I extends ChemicalStackIngredient<C, S>>(ChemicalWrapper<C, S, I> wrapper) implements RecipeComponent<S> {
		@Override
		public ComponentRole role() {
			return ComponentRole.OUTPUT;
		}

		@Override
		public Class<?> componentClass() {
			return wrapper.stackType;
		}

		@Override
		public TypeDescJS constructorDescription(DescriptionContext ctx) {
			return TypeDescJS.object().add(wrapper.key, TypeDescJS.STRING).add(JsonConstants.AMOUNT, TypeDescJS.NUMBER).or(TypeDescJS.STRING);
		}

		@Override
		public JsonElement write(RecipeJS recipe, S value) {
			JsonObject json = new JsonObject();
			json.addProperty(wrapper.key, value.getTypeRegistryName().toString());
			json.addProperty(JsonConstants.AMOUNT, value.getAmount());
			return json;
		}

		@Override
		public S read(RecipeJS recipe, Object from) {
			if (wrapper.ingredientType.isInstance(from)) {
				return (S) from;
			} else if (from instanceof JsonObject || from instanceof Map<?, ?>) {
				var json = MapJS.json(from);
				var amount = json.has(JsonConstants.AMOUNT) ? json.get(JsonConstants.AMOUNT).getAsLong() : 0L;
				return wrapper.stack(json.get(wrapper.key).getAsString(), amount);
			} else if (from instanceof CharSequence) {
				return wrapper.stack(from.toString(), wrapper.defaultAmount);
			} else {
				return null;
			}
		}
	}
}
