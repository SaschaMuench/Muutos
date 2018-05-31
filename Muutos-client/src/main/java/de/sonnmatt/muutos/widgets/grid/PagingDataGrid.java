package de.sonnmatt.muutos.widgets.grid;

import java.util.Comparator;
import java.util.List;

import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;

import com.google.gwt.core.client.AsyncProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.sonnmatt.muutos.DTOs.GridFieldDefinitionDTO;
import de.sonnmatt.muutos.widgets.grid.FilteredListDataProvider;
import de.sonnmatt.muutos.widgets.grid.IFilter;

public class PagingDataGrid<T extends IGridData> extends Composite {

	interface generalPagingDataGridUiBinder extends UiBinder<Widget, PagingDataGrid<IGridData>> { }
	private static generalPagingDataGridUiBinder uiBinder = GWT.create(generalPagingDataGridUiBinder.class);
	
	@UiField DataGrid<IGridData> grid = new DataGrid<IGridData>();
	@UiField Pagination gridPagination;
	@UiField TextBox gridSearch;

	private SimplePager gridPager = new SimplePager();
	private List<GridFieldDefinitionDTO> columns;
	
/*	public PagingCellTable() {
		initWidget(uiBinder.createAndBindUi(this));
		initTable(cellTable, cellTablePager, cellTablePagination, cellTableProvider);
		cellTablePager.setPageSize(10);
	}
*/

	@SuppressWarnings("unchecked")
	public PagingDataGrid(List<GridFieldDefinitionDTO> columns, int pageSize) {
		this.columns = columns;
		initWidget(uiBinder.createAndBindUi((PagingDataGrid<IGridData>) this));
		initTable(grid, gridPager, gridPagination, gridProvider);
		gridPager.setPageSize(pageSize);
	}
	
	AsyncDataProvider<IGridData> gridAsnycProvider = new AsyncDataProvider<IGridData>() {

		@Override
		protected void onRangeChanged(HasData<IGridData> display) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private final FilteredListDataProvider<IGridData> gridProvider = new FilteredListDataProvider<IGridData>(new IFilter<IGridData>() {
        @Override
        public boolean isValid(IGridData value, String filter) {
            if (filter==null || value==null)
                return true;
            Boolean b = false;
            for (Integer j = 0; j <= columns.size(); j++) {
            	b = b || value.getColumnData(j).toLowerCase().contains(filter.toLowerCase());
            }
            return b;
        }
    });

	public void setDataList(List<? extends IGridData> data) {
		gridProvider.getList().addAll(data);
		gridProvider.flush();
		gridPagination.rebuild(gridPager);
	}

	@UiHandler("gridSearch")
	public void onEnterSearchData(KeyUpEvent event) {
		gridProvider.setFilter(gridSearch.getText());
		gridProvider.refresh();
		gridPagination.rebuild(gridPager);
	}

	private void initTable(final AbstractCellTable<IGridData> grid, final SimplePager pager, final Pagination pagination,
			final ListDataProvider<IGridData> dataProvider) {

		ListHandler<IGridData> sortHandler = new ListHandler<IGridData>(dataProvider.getList());
		for (int i = 0; i <= columns.size(); i++) {
			TextColumn<IGridData> textColumn = generateTextColumn(sortHandler, i);
			grid.addColumn(textColumn, columns.get(i).getCaption());
		}

		grid.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(final RangeChangeEvent event) {
				pagination.rebuild(pager);
			}
		});
		
		final SingleSelectionModel<IGridData> selectionModel = new SingleSelectionModel<IGridData>();
		grid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				IGridData data = selectionModel.getSelectedObject();
				//CellTables.this.driver.edit(person);
				//Growl.growl("Selectd: " + data.getId());
				//ToDo
				//UserMgtPopup popupUser = new UserMgtPopup();
				
				//RootPanel.get("content").add(popupUser);
				//popupUser.show();
				
			}
		});

		grid.setEmptyTableWidget(new HTML("No Data to Display"));
		grid.getColumn(0).setDefaultSortAscending(true);
		grid.addColumnSortHandler(sortHandler);
		
		pager.setDisplay(grid);
		pagination.clear();
		dataProvider.addDataDisplay(grid);
	}

	/**
	 * @param sortHandler
	 * @param i
	 * @return
	 */
	private TextColumn<IGridData> generateTextColumn(ListHandler<IGridData> sortHandler, final int i) {
		TextColumn<IGridData> textColumn = new TextColumn<IGridData>() {
			@Override
			public String getValue(IGridData object) {
				return object.getColumnData(i);
			}
			@Override
			public HorizontalAlignmentConstant getHorizontalAlignment() {
				return columns.get(i).getAlignment();
			}
			
		};
		
		textColumn.setSortable(true);
		sortHandler.setComparator(textColumn, new Comparator<IGridData>() {
			public int compare(IGridData o1, IGridData o2) {
				if (o1.getColumnFormat(i)==Integer.class)
					return o1.getIntegerColumnData(i).compareTo(o2.getIntegerColumnData(i));
				else
					return o1.getColumnData(i).compareTo(o2.getColumnData(i));
			}
		});
		return textColumn;
	}



}
