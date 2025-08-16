package dev.latvian.mods.kubejs.mekanism;

import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.util.TickDuration;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import mekanism.api.MekanismAPITags;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.api.datamaps.chemical.ChemicalSolidTag;
import mekanism.api.datamaps.chemical.attribute.ChemicalFuel;
import mekanism.api.datamaps.chemical.attribute.ChemicalRadioactivity;
import mekanism.api.datamaps.chemical.attribute.CooledCoolant;
import mekanism.api.datamaps.chemical.attribute.HeatedCoolant;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@ReturnsSelf
public class KubeChemicalBuilder extends BuilderBase<Chemical> {
	@ReturnsSelf
	public static class Default extends KubeChemicalBuilder {
		public Default(ResourceLocation id) {
			super(id);
		}

		public Default texture(ResourceLocation texture) {
			chemicalBuilder = ChemicalBuilder.builder(texture);
			return this;
		}
	}

	public static class Liquid extends KubeChemicalBuilder {
		public Liquid(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.builder();
		}
	}

	public static class CleanSlurry extends KubeChemicalBuilder {
		public CleanSlurry(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.cleanSlurry();
		}
	}

	public static class DirtySlurry extends KubeChemicalBuilder {
		public DirtySlurry(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.dirtySlurry();
		}
	}

	public static class InfuseType extends KubeChemicalBuilder {
		public InfuseType(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.infuseType();
		}
	}

	public static class Pigment extends KubeChemicalBuilder {
		public Pigment(ResourceLocation id) {
			super(id);
			chemicalBuilder = ChemicalBuilder.pigment();
		}
	}

	protected ChemicalBuilder chemicalBuilder;
	public transient ChemicalFuel fuel;
	public transient HeatedCoolant heatedCoolant;
	public transient CooledCoolant cooledCoolant;
	public transient ChemicalRadioactivity radiation;
	public transient ChemicalSolidTag ore;

	public KubeChemicalBuilder(ResourceLocation id) {
		super(id);
	}

	private ChemicalBuilder chemicalBuilder() {
		if (chemicalBuilder == null) {
			chemicalBuilder = ChemicalBuilder.builder();
		}

		return chemicalBuilder;
	}

	public KubeChemicalBuilder fuel(TickDuration burnTicks, long energyDensity) {
		if (burnTicks.ticks() > 0 && energyDensity > 0L) {
			fuel = new ChemicalFuel((int) burnTicks.ticks(), energyDensity);
		}

		return this;
	}

	public KubeChemicalBuilder heatedCoolant(Holder<Chemical> chemical, double thermalEnthalpy, double conductivity, double temperature) {
		if (thermalEnthalpy > 0D && conductivity > 0D) {
			heatedCoolant = new HeatedCoolant(chemical, thermalEnthalpy, conductivity, temperature);
		}

		return this;
	}

	public KubeChemicalBuilder heatedCoolant(Holder<Chemical> chemical, double thermalEnthalpy, double conductivity) {
		return heatedCoolant(chemical, thermalEnthalpy, conductivity, 100_000);
	}

	public KubeChemicalBuilder cooledCoolant(Holder<Chemical> chemical, double thermalEnthalpy, double conductivity) {
		if (thermalEnthalpy > 0D && conductivity > 0D) {
			cooledCoolant = new CooledCoolant(chemical, thermalEnthalpy, conductivity);
		}

		return this;
	}

	public KubeChemicalBuilder radiation(double radioactivity) {
		if (radioactivity > 0D) {
			radiation = new ChemicalRadioactivity(radioactivity);
		}

		return this;
	}

	public KubeChemicalBuilder tint(KubeColor tint) {
		chemicalBuilder().tint(tint.kjs$getRGB());
		return this;
	}

	public KubeChemicalBuilder ore(TagKey<Item> oreTag) {
		ore = new ChemicalSolidTag(oreTag);
		return this;
	}

	public KubeChemicalBuilder gaseous() {
		tag(new ResourceLocation[]{MekanismAPITags.Chemicals.GASEOUS.location()});
		return this;
	}

	@Override
	public Chemical createObject() {
		return new Chemical(chemicalBuilder());
	}
}
