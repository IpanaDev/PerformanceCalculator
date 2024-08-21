package ui;

import calculators.priority.Priority;
import cars.Overall;
import performance.ValueFilter;

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
