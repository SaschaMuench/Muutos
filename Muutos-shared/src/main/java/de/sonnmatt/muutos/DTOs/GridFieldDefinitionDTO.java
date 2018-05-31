package de.sonnmatt.muutos.DTOs;

import java.io.Serializable;

import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import de.sonnmatt.muutos.enums.GridFieldTypes;

public class GridFieldDefinitionDTO implements Serializable, IsSerializable {

	private static final long serialVersionUID = -5085071382226149643L;
	private Integer fieldID;
	private Integer tableID;
	private Integer sortOrder;
	private String fieldName;
	private String fieldTranslation;
	private GridFieldTypes fieldType;
	private Integer subType;
	private Boolean visible;
	private Boolean editable;
	private Boolean needsData;
	private Boolean reCalcData;
	private String defaultValue;
	
	public GridFieldDefinitionDTO() {
	}
	
	public int getTableID() {
		return tableID;
	}
	public void setTableID(Integer tableID) {
		this.tableID = tableID;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldTranslation() {
		return fieldTranslation;
	}
	public void setFieldTranslation(String fieldTranslation) {
		this.fieldTranslation = fieldTranslation;
	}
	public GridFieldTypes getFieldType() {
		return fieldType;
	}
	public void setFieldType(GridFieldTypes fieldType) {
		this.fieldType = fieldType;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(Integer subType) {
		this.subType = subType;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	public Boolean getNeedsData() {
		return needsData;
	}
	public void setNeedsData(Boolean needsData) {
		this.needsData = needsData;
	}
	public Boolean getReCalcData() {
		return reCalcData;
	}
	public void setReCalcData(Boolean reCalcData) {
		this.reCalcData = reCalcData;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getFloatFormat() {
		//TODO: needed?
		String floatFormat = "#,##0";
		if (getSubType() > 0) {
			floatFormat += ".";
			for (int i = 1; i <= getSubType(); i++) {
				floatFormat += "0";
			}
		}
		return floatFormat;
	}

	public HorizontalAlignmentConstant getAlignment() {
		// TODO Auto-generated method stub
		return null;
	}

	public Header<?> getCaption() {
		// TODO Auto-generated method stub
		Header<?> header = new Header<String>(null) {

			@Override
			public String getValue() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return header;
	}
}
