package de.sonnmatt.muutos.widgets.grid;

import com.google.gwt.view.client.ProvidesKey;

public interface IGridData {

	public static final ProvidesKey<IGridData> KEY_PROVIDER = new ProvidesKey<IGridData>() {
		public Object getKey(IGridData item) {
			return item == null ? null : item.getId();
		}
	};

	public abstract int compareTo(IGridData o);

	public abstract boolean equals(Object o);

	public abstract int getId();

	public abstract String getColumnData(Integer column);

	public abstract Class<?> getColumnFormat(int i);

	public abstract IGridData getIntegerColumnData(int i);

}