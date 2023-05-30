package me.ht9.end.feature.module.setting;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Setting<V>
{
    private final String name;
    private final Number min;
    private final V defaultValue;
    private final Number max;
    private final Supplier<Boolean> visibility;
    private V value;
    private int enumIndex = 0;
    private int roundingScale = 0;
    private Consumer<V> onToggle;

    public <N extends Number> Setting(String name, N min, V value, N max, int roundingScale, Supplier<Boolean> visibility) {
        this.name = name;
        this.min = min;
        this.value = value;
        this.defaultValue = value;
        this.max = max;
        this.roundingScale = roundingScale;
        this.visibility = visibility;
        this.checkEnums();
    }

    public <N extends Number> Setting(String name, N min, V value, N max, int roundingScale) {
        this(name, min, value, max, roundingScale, () -> true);
    }

    public <N extends Number> Setting(String name, N min, V value, N max) {
        this(name, min, value, max, 0, () -> true);
    }

    public <N extends Number> Setting(String name, N min, V value, N max, Supplier<Boolean> visibility) {
        this(name, min, value, max, 0, visibility);
    }

    public Setting(String name, V value) {
        this(name, 0.0F, value, 0.0F, 0, () -> true);
    }

    public Setting(String name, V value, Supplier<Boolean> visibility) {
        this(name, 0.0F, value, 0.0F, 0, visibility);
    }

    public Setting<V> withOnChange(Consumer<V> action) {
        this.onToggle = action;
        return this;
    }

    private void checkEnums() {
        if (this.value instanceof Enum<?>) {
            Enum<?>[] enums = (Enum<?>[]) this.value.getClass().getEnumConstants();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i] == this.value) {
                    this.enumIndex = i;
                    break;
                }
            }
        }
    }

    public void reset() {
        this.setValue(this.defaultValue);
    }

    public Enum<?> getNextEnum() {
        if (this.value instanceof Enum<?>) {
            Enum<?>[] enums = (Enum<?>[]) this.value.getClass().getEnumConstants();
            if (++this.enumIndex >= enums.length) {
                this.enumIndex = 0;
                return enums[0];
            } else {
                return enums[this.enumIndex];
            }
        }
        throw new IllegalStateException("Setting value is not an enum.");
    }

    public String getName() {
        return this.name;
    }

    public Number getMin() {
        return this.min;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
        this.checkEnums();
        if (this.onToggle != null) {
            this.onToggle.accept(value);
        }
    }

    public V getDefaultValue() {
        return this.defaultValue;
    }

    public Number getMax() {
        return this.max;
    }

    public int getRoundingScale() {
        return this.roundingScale;
    }

    public boolean isVisible() {
        return this.visibility.get();
    }
}