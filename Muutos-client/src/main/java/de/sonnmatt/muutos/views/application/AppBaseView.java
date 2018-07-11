package de.sonnmatt.muutos.views.application;

import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarBrand;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.ConfirmCallback;
//import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;

import de.sonnmatt.muutos.URLHandler;
import de.sonnmatt.muutos.DTOs.TextResourcesDTO;
import de.sonnmatt.muutos.DTOs.UserDTO;
import de.sonnmatt.muutos.DTOs.ViewDTO;
import de.sonnmatt.muutos.enums.Params;
import de.sonnmatt.muutos.enums.Tokens;
import de.sonnmatt.muutos.interfaces.IPresenter;
import de.sonnmatt.muutos.views.resources.AppBaseViewResources;
import de.sonnmatt.muutos.views.resources.AppBaseViewVisibles;
//import de.sonnmatt.muutos.client.LoginSwitcher;
//import de.sonnmatt.muutos.client.apps.UserMgtPopup;
//import de.sonnmatt.muutos.client.cellTable.ColumnDefinition;
//import de.sonnmatt.muutos.client.cellTable.PagingCellTable;
//import de.sonnmatt.muutos.client.cellTable.PagingCellTablePopup;
//import de.sonnmatt.muutos.client.rpc.DataService;
//import de.sonnmatt.muutos.client.rpc.DataServiceAsync;
import de.sonnmatt.processor.GenerateTranslation;
//import ch.rrd.expenses.shared.dto.FieldDefinitionDTO;
//import ch.rrd.expenses.shared.dto.UserManagementDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

@GenerateTranslation("AppBaseView.ui.xml")
public class AppBaseView extends Composite implements IAppBaseView {

	private Logger logger = Logger.getLogger("Muutos");
	
	private static final String SERVER_ERROR = "An error occurred while attempting to contact the server. Please check your network connection and try again. Details: ";
	private static AppWinUiBinder uiBinder = GWT.create(AppWinUiBinder.class);
	interface AppWinUiBinder extends UiBinder<Widget, AppBaseView> { }

//	private final DataServiceAsync dataService = (DataServiceAsync) GWT.create(DataService.class);
//	private TranslationsDTO transDto;
	
	@UiField(provided=true)
	final AppBaseViewResources appRen;
	@UiField(provided=true)
	final AppBaseViewVisibles myVis;
	
	@UiField Container 		appPanel;
	@UiField NavbarBrand 	brandField;
	@UiField AnchorListItem menu1Sub1;
	@UiField AnchorListItem menu1Sub2;
//	@UiField DropDownHeader mnuSettingHead1;
//	@UiField AnchorListItem mnuSettingList11;
//	@UiField AnchorListItem mnuSettingList12;
//	@UiField DropDownHeader mnuSettingHead2;
//	@UiField AnchorListItem mnuSettingList21;
	@UiField AnchorListItem mnuSettingList22;
	@UiField Tooltip 		tipSettings;
	@UiField ScrollPanel	appDataScrollPanel;
	@UiField Container 		appData;
	@UiField Button 		btnLogout;
	@UiField Navbar 		navFooter; 
	@UiField Navbar 		navHeader; 
	
	private UserDTO localUser;
	
	public AppBaseView(TextResourcesDTO translation, ViewDTO viewDTO, UserDTO userDTO) {
		//transDto = translation;
		appRen = new AppBaseViewResources(translation);
		myVis = new AppBaseViewVisibles(viewDTO);
		initWidget(uiBinder.createAndBindUi(this));
		tipSettings.setTitle("Personal settings for " + userDTO.getFullName());
		localUser = userDTO;
		//setTranslations();
	}

	public Widget getAppBase() {
		return appPanel;
	}

//	@SuppressWarnings("unused")
//	private void setTranslations() {
//		// TODO Auto-generated method stub
//		mnuSettingHead2.setText("Benutzerverwaltung");
//	}

	@UiHandler("brandField")
	void handleBrandClick(ClickEvent event) {
		Bootbox.alert(new HTML("Brand clicked<br>Informationen zum Programm<br>&copy; 2017 by S. M&uuml;nch").toString());
	}
	
	@UiHandler({"menu1Sub1", "menu1Sub5"})
	void handleMenu1Sub1Click(ClickEvent event) {
		event.getSource();
	}
	
	@UiHandler("menu1Sub2") 
	void handleMenu1Sub2Click(ClickEvent event) {
		Bootbox.alert("menu1Sub2 clicked");
		//appData.clear();
	}

	@UiHandler("menu2Sub1") 
	void handleMenu2Sub1Click(ClickEvent event) {
		URLHandler urlHandler = new URLHandler(Window.Location.getHash());
		urlHandler.setUrlToken(Tokens.APP);
		urlHandler.addParameter(Params.FORM, "statistic");
		History.newItem(urlHandler.getUrl());
	}

	@UiHandler("mnuSettingList22") 
	void handleMenuSettingList22(ClickEvent event) {
		URLHandler urlHandler = new URLHandler(Window.Location.getHash());
		urlHandler.setUrlToken(Tokens.APP);
		urlHandler.addParameter(Params.FORM, "statistics");
		History.newItem(urlHandler.getUrl());
	}

//	@UiHandler("menu2Sub1") 
//	void handleMenu2Sub2Click(ClickEvent event) {
//		//appData.clear();
//	}
	
//	@UiHandler({"mnuSettingList21", "menu2Sub1"})
//	void handleUseradminClick(ClickEvent event) {
//		//appData.clear();
//		//LoginSwitcher appView = GWT.create(LoginSwitcher.class);
//		//appView.setUser(result);
//		//appView.switchAppView(1, this);
////		RootPanel.get("spinner").setVisible(true);
////		dataService.getUserList(localUser.getSessionId(), new AsyncCallback<UserManagementDTO>() {
////			public void onFailure(Throwable caught) {
////				Growl.growl("Error getUserList: " + caught.toString());
////				Window.alert(SERVER_ERROR + caught.getMessage());
////			}
////			
////			public void onSuccess(UserManagementDTO result) {
////				appData.clear();
////				List<FieldDefinitionDTO> columns = result.getFields();
////				List<ColumnDefinition> tableDef = new ArrayList<ColumnDefinition>();
////				ColumnDefinition colDef;
////				for(FieldDefinitionDTO column : columns) {
////					colDef = new ColumnDefinition();
////					colDef.setCaption(column.getFieldTranslation());
////					colDef.setAlignment(column.getAlignment());
////					colDef.setColumnKey(column.getDataKey());
////					colDef.setType(column.getFieldType());
////					tableDef.add(colDef);
////				}
////				
////				int height = navFooter.getAbsoluteTop() - navHeader.getOffsetHeight() - 20;
////				int width = navHeader.getOffsetWidth();
////				appPanel.setWidth((width + 45) + "px");
////				int lines = ((height - 220) / 30) - 1;
////				lines = lines < 6 ? 5 : lines;
////				PagingCellTable cellTable = new PagingCellTable(tableDef, lines);
////				cellTable.setDataList(result.getUsers());
////				cellTable.setPopup(new UserMgtPopup());
////				cellTable.setWidth((width - 0) + "px");
////				cellTable.setHeight((height - 300) + "px");
////				appData.setWidth(width + "px");
////				appData.setHeight(height + "px");
////				appData.add(cellTable);
////				
////				RootPanel.get("spinner").setVisible(false);
////				//Growl.growl("Width: " + cellTable.getOffsetWidth());
//////				UserMgtCellTable userMgt = new UserMgtCellTable();
//////				userMgt.setDataList(result.getUsers());
//////				appData.add(userMgt);
//////				int height = navFooter.getAbsoluteTop() - navHeader.getOffsetHeight() - 30;
//////				int width = navHeader.getOffsetWidth();
//////				appData.setHeight(height + "px");
//////				appData.setWidth(width + "px");
//////				RootPanel.get("spinner").setVisible(false);
////				
////				// start fixing here 
////				
////
////			}
////		});
//
//	}
	
	@Override
	public Widget getAppViewPort() {
		return appData;
	}

	@Override
	public void setAppViewPort(Widget viewPort) {
		int height = navFooter.getAbsoluteTop() - navHeader.getOffsetHeight() - 2;
		appData.clear();
		appData.add(viewPort);
		appData.setHeight(height + "px");
		appData.setWidth(navFooter.getOffsetWidth() + "px");
	}
	
	@UiHandler("btnLogout")
	void handleBtnLogoutClick(ClickEvent event) {
		Bootbox.confirm("Logoff?", new ConfirmCallback() {
			public void callback(boolean result) {
				if (result) {
					URLHandler urlHandler = new URLHandler(Window.Location.getHash());
					urlHandler.setUrlToken(Tokens.LOGOFF);
					History.newItem(urlHandler.getUrl());
				}
			}
		});
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		RootPanel.get("content").add(appPanel);
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextResourcesDTO getTextValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTextValues(TextResourcesDTO transDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popupMessage(String value, NotifyType notifyType) {
		// TODO Auto-generated method stub
		
	}

}
