package de.sonnmatt.muutos.widgets.grid;

public interface IFilter<T> {
	boolean isValid(T value, String filter);
}
