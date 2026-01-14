package me.ipana.ui;

import me.ipana.calculator.priority.Priority;
import me.ipana.car.Overall;
import me.ipana.parts.ValueFilter;

public class SearchOptions {

    public Overall overall;
    public ValueFilter valueFilter;
    public Priority priority;

    public SearchOptions(Overall overall, ValueFilter valueFilter, Priority priority) {
        this.overall = overall;
        this.valueFilter = valueFilter;
        this.priority = priority;
    }
}
