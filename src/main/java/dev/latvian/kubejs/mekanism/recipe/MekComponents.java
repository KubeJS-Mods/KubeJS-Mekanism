package dev.latvian.kubejs.mekanism.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.kubejs.mekanism.util.ChemicalWrapper;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.MapJS;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.ChemicalType;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.recipe.ingredient.creator.ItemStackIngredientCreator;

import java.util.Map;

public interface MekComponents {
	RecipeComponent<ItemStackIngredient> INPUT_ITEM = new RecipeComponent<>() {
		@Override
		public ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public Class<?> componentClass() {
			return ItemStackIngredient.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, ItemStackIngredient value) {
			return value.serialize();
		}

		@Override
		public ItemStackIngredient read(RecipeJS recipe, Object from) {
			if (from instanceof ItemStackIngredient in) {
				return in;
			} else if (from instanceof JsonElement json) {
				return IngredientCreatorAccess.item().deserialize(json);
			}

			var in = InputItem.of(from);
			return ItemStackIngredientCreator.INSTANCE.from(in.ingredient, in.count);
		}

		@Override
		public boolean isInput(RecipeJS recipe, ItemStackIngredient value, ReplacementMatch match) {
			if (match instanceof ItemMatch m) {
				if (value instanceof ItemStackIngredientCreator.SingleItemStackIngredient in) {
					return m.contains(in.getInputRaw());
				} else if (value instanceof ItemStackIngredientCreator.MultiItemStackIngredient in) {
					for (var in1 : in.getIngredients()) {
						if (m.contains(((ItemStackIngredientCreator.SingleItemStackIngredient) in1).getInputRaw())) {
							return true;
						}
					}
				}
			}

			return false;
		}

		@Override
		public ItemStackIngredient replaceInput(RecipeJS recipe, ItemStackIngredient original, ReplacementMatch match, InputReplacement with) {
			if (match instanceof ItemMatch m) {
				if (original instanceof ItemStackIngredientCreator.SingleItemStackIngredient in) {
					if (m.contains(in.getInputRaw())) {
						var item = InputItem.of(with.replaceInput(recipe, match, InputItem.of(in.getInputRaw(), in.getAmountRaw())));
						return ItemStackIngredientCreator.INSTANCE.from(item.ingredient, item.count);
					}
				} else if (original instanceof ItemStackIngredientCreator.MultiItemStackIngredient in) {
					var ingredients = in.getIngredients();

					for (var in1 : ingredients) {
						if (m.contains(((ItemStackIngredientCreator.SingleItemStackIngredient) in1).getInputRaw())) {
							var repl = new ItemStackIngredient[ingredients.size()];

							for (int i = 0; i < repl.length; i++) {
								var in2 = (ItemStackIngredientCreator.SingleItemStackIngredient) ingredients.get(i);

								if (m.contains(in2.getInputRaw())) {
									var item = InputItem.of(with.replaceInput(recipe, match, InputItem.of(in2.getInputRaw(), in2.getAmountRaw())));
									repl[i] = ItemStackIngredientCreator.INSTANCE.from(item.ingredient, item.count);
								} else {
									repl[i] = in2;
								}
							}

							return ItemStackIngredientCreator.INSTANCE.createMulti(repl);
						}
					}
				}

				return original;
			}

			return original;
		}
	};

	RecipeComponent<ChemicalType> CHEMICAL_TYPE = new EnumComponent<>(ChemicalType.class, ChemicalType::getSerializedName, (c, s) -> ChemicalType.fromString(s));

	RecipeComponent<ChemicalStackIngredient<?, ?>> ANY_CHEMICAL_INPUT = new RecipeComponent<>() {
		@Override
		public ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public Class<?> componentClass() {
			return ChemicalStackIngredient.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, ChemicalStackIngredient<?, ?> value) {
			return value.serialize();
		}

		@Override
		public ChemicalStackIngredient<?, ?> read(RecipeJS recipe, Object from) {
			if (from instanceof ChemicalStackIngredient<?, ?> in) {
				return in;
			} else if (from instanceof Map<?, ?> || from instanceof JsonObject) {
				var map = MapJS.of(from);
				var cw = map == null || map.isEmpty() ? null : ChemicalWrapper.find(map);

				if (cw != null) {
					return cw.ingredient(map.get(cw.key()).toString(), map.containsKey("amount") ? ((Number) map.get("amount")).longValue() : 0L);
				}
			}

			throw new RecipeExceptionJS("Cannot read chemical stack ingredient from " + from);
		}

		@Override
		public ChemicalStackIngredient<?, ?> readFromJson(RecipeJS recipe, RecipeKey<ChemicalStackIngredient<?, ?>> key, JsonObject json) {
			ChemicalType chemicalType = SerializerHelper.getChemicalType(json);
			return IngredientCreatorAccess.getCreatorForType(chemicalType).deserialize(json.get(key.name));
		}

		@Override
		public void writeToJson(RecipeComponentValue<ChemicalStackIngredient<?, ?>> value, JsonObject json) {
			RecipeComponent.super.writeToJson(value, json);
			json.addProperty(JsonConstants.CHEMICAL_TYPE, ChemicalType.getTypeFor(value.value).getSerializedName());
		}
	};

	RecipeComponent<ChemicalStackIngredient.GasStackIngredient> GAS_INPUT = new ChemicalWrapper.InputComponent<>(ChemicalWrapper.GAS);
	RecipeComponent<ChemicalStackIngredient.InfusionStackIngredient> INFUSE_TYPE_INPUT = new ChemicalWrapper.InputComponent<>(ChemicalWrapper.INFUSE_TYPE);
	RecipeComponent<ChemicalStackIngredient.PigmentStackIngredient> PIGMENT_INPUT = new ChemicalWrapper.InputComponent<>(ChemicalWrapper.PIGMENT);
	RecipeComponent<ChemicalStackIngredient.SlurryStackIngredient> SLURRY_INPUT = new ChemicalWrapper.InputComponent<>(ChemicalWrapper.SLURRY);

	RecipeComponent<GasStack> GAS_OUTPUT = new ChemicalWrapper.OutputComponent<>(ChemicalWrapper.GAS);
	RecipeComponent<InfusionStack> INFUSE_TYPE_OUTPUT = new ChemicalWrapper.OutputComponent<>(ChemicalWrapper.INFUSE_TYPE);
	RecipeComponent<PigmentStack> PIGMENT_OUTPUT = new ChemicalWrapper.OutputComponent<>(ChemicalWrapper.PIGMENT);
	RecipeComponent<SlurryStack> SLURRY_OUTPUT = new ChemicalWrapper.OutputComponent<>(ChemicalWrapper.SLURRY);

	RecipeComponent<OutputItem> SECONDARY_OUTPUT_COMPONENT = new RecipeComponentWithParent<>() {
		@Override
		public RecipeComponent<OutputItem> parentComponent() {
			return ItemComponents.OUTPUT;
		}

		@Override
		public void writeToJson(RecipeComponentValue<OutputItem> value, JsonObject json) {
			ItemComponents.OUTPUT.writeToJson(value, json);

			if (value.value.hasChance()) {
				json.addProperty(JsonConstants.SECONDARY_CHANCE, value.value.getChance());
			}
		}

		@Override
		public OutputItem readFromJson(RecipeJS recipe, RecipeKey<OutputItem> key, JsonObject json) {
			var item = ItemComponents.OUTPUT.readFromJson(recipe, key, json);

			if (item != null && json.has(JsonConstants.SECONDARY_CHANCE)) {
				item = item.withChance(json.get(JsonConstants.SECONDARY_CHANCE).getAsDouble());
			}

			return item;
		}
	};

	RecipeComponent<FloatingLong> FLOATING_LONG = new RecipeComponent<>() {
		@Override
		public Class<?> componentClass() {
			return FloatingLong.class;
		}

		@Override
		public JsonElement write(RecipeJS recipe, FloatingLong value) {
			return new JsonPrimitive(value);
		}

		@Override
		public TypeDescJS constructorDescription(DescriptionContext ctx) {
			return TypeDescJS.NUMBER;
		}

		@Override
		public FloatingLong read(RecipeJS recipe, Object from) {
			if (from instanceof FloatingLong n) {
				return n;
			} else if (from instanceof Number n) {
				return FloatingLong.createConst(n.doubleValue());
			}

			try {
				return FloatingLong.parseFloatingLong(from instanceof JsonElement json ? json.getAsNumber().toString() : from.toString(), true);
			} catch (Exception ex) {
				throw new RecipeExceptionJS("Expected floating long to be a positive decimal number");
			}
		}
	};
}
