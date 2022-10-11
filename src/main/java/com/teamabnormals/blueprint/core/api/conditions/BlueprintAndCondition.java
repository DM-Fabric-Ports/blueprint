package com.teamabnormals.blueprint.core.api.conditions;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.teamabnormals.blueprint.core.Blueprint;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.function.Predicate;

/**
 * A special version of the {@link net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions} AND that stops reading if a false condition is met.
 * <p>This is useful for testing another condition only if the former conditions are met.</p>
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class BlueprintAndCondition {
	public static final ResourceLocation LOCATION = new ResourceLocation(Blueprint.MOD_ID, "and");

	public static ConditionJsonProvider and(ConditionJsonProvider... values) {
		Preconditions.checkArgument(values.length > 0, "Must register at least one value.");
		return new Serializer(values);
	}

	static {
		ResourceConditions.register(LOCATION, Serializer::read);
	}

	public static class Serializer implements ConditionJsonProvider {
		private final ResourceLocation location;
		private final ConditionJsonProvider[] conditions;

		public Serializer(ConditionJsonProvider... conditions) {
			this.location = LOCATION;
			this.conditions = conditions;
		}

		@Override
		public void writeParameters(JsonObject json) {
			JsonArray values = new JsonArray();
			for (ConditionJsonProvider condition : this.conditions) {
				values.add(condition.toJson());
			}
			json.add("values", values);
		}

		public static boolean read(JsonObject json) throws JsonSyntaxException {
			for (JsonElement elements : GsonHelper.getAsJsonArray(json, "values")) {
				if (!elements.isJsonObject()) {
					throw new JsonSyntaxException("And condition values must be an array of JsonObjects");
				}
				Predicate<JsonObject> predicate = ResourceConditions.get(new ResourceLocation(GsonHelper.getAsString(elements.getAsJsonObject(), ResourceConditions.CONDITION_ID_KEY)));
				if (predicate == null) {
					throw new JsonSyntaxException("Unknown condition: " + GsonHelper.getAsString(elements.getAsJsonObject(), ResourceConditions.CONDITION_ID_KEY));
				} else if (!predicate.test(elements.getAsJsonObject())) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ResourceLocation getConditionId() {
			return this.location;
		}
	}
}
