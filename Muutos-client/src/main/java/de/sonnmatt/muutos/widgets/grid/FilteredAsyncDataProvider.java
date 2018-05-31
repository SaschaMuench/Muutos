package de.sonnmatt.muutos.widgets.grid;

import java.util.ArrayList;
import java.util.List;

import de.sonnmatt.muutos.widgets.grid.IFilter;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public class FilteredAsyncDataProvider<T> extends AsyncDataProvider<T> {

	private String filterString;

	public final IFilter<T> filter;

	public FilteredAsyncDataProvider(IFilter<T> filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filterString;
	}

	public void setFilter(String filter) {
		this.filterString = filter;
		//refresh();
	}

	public void resetFilter() {
		filterString = null;
		//refresh();
	}

	public boolean hasFilter() {
		return !(filterString == null || "".equals(filterString));
	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @Override protected void updateRowData(HasData display, int start, List
	 * values) { if (!hasFilter() || filter == null) { // we don't need to
	 * filter, so call base class super.updateRowData(display, start, values); }
	 * else { int end = start + values.size(); Range range =
	 * display.getVisibleRange(); int curStart = range.getStart(); int curLength
	 * = range.getLength(); int curEnd = curStart + curLength; if (start ==
	 * curStart || (curStart < end && curEnd > start)) { int realStart =
	 * curStart < start ? start : curStart; int realEnd = curEnd > end ? end :
	 * curEnd; int realLength = realEnd - realStart; List<T> resulted = new
	 * ArrayList<T>(realLength); for (int i = realStart - start; i < realStart -
	 * start + realLength; i++) { if (filter.isValid((T) values.get(i),
	 * getFilter())) { resulted.add((T) values.get(i)); } }
	 * display.setRowData(realStart, resulted);
	 * display.setRowCount(resulted.size()); } } }
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void updateRowData(HasData display, int start, List values) {
		if (!hasFilter() || filter == null) { // we don't need to filter, so
												// call base class
			display.setRowCount(values.size());
			super.updateRowData(display, start, values);
		} else {
			int end = start + values.size();
			List resulted = new ArrayList(values.size());
			for (int i = start; i < end; i++) {
				if (filter.isValid((T) values.get(i), getFilter())) {
					resulted.add((T) values.get(i));
				}
			}
			display.setRowData(start, resulted);
			display.setRowCount(resulted.size());
		}

	}

	@Override
	protected void onRangeChanged(HasData<T> display) {
		// TODO Auto-generated method stub
		
	}
}