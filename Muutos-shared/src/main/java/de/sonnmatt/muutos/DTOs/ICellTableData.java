package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

public interface ICellTableData extends Serializable {

	public static final ProvidesKey<ICellTableData> KEY_PROVIDER = new ProvidesKey<ICellTableData>() {
		public Object getKey(ICellTableData item) {
			return item == null ? null : item.getId();
		}
	};

	public abstract int compareTo(ICellTableData o);

	public abstract boolean equals(Object o);

	public abstract int getId();

	public abstract String getColumnData(Integer column);
	
}