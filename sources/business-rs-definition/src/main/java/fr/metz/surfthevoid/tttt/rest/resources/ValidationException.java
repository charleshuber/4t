package fr.metz.surfthevoid.tttt.rest.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 6296828042739307519L;
	private Type type;
	private Errors errors;
	
	public ValidationException(Type type, Errors errors){
		this.type = type;
		this.errors = errors;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Errors getErrors() {
		return errors;
	}
	public void setErrors(Errors errors) {
		this.errors = errors;
	}
	public Boolean hasErrors(){
		return errors != null && errors.hasErrors();
	}
	
	public static class Errors implements Serializable {
		private static final long serialVersionUID = 500707749913161248L;
		private Map<String, List<String>> fieldsValidationMessages;
		private List<String> globalValidationMessages;
		
		public Errors(){}
		
		public Boolean hasErrors(){
			return (fieldsValidationMessages != null && fieldsValidationMessages.size() > 0) 
					|| (globalValidationMessages != null && globalValidationMessages.size() > 0);
		}
		
		public Map<String, List<String>> getFieldsValidationMessages() {
			return fieldsValidationMessages;
		}
		public void setFieldsValidationMessages(Map<String, List<String>> fieldsValidationMessages) {
			this.fieldsValidationMessages = fieldsValidationMessages;
		}
		public List<String> getGlobalValidationMessages() {
			return globalValidationMessages;
		}
		public void setGlobalValidationMessages(List<String> globalValidationMessages) {
			this.globalValidationMessages = globalValidationMessages;
		}
		public void addFieldError(String fieldName, String error){
			if(StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(error)) {
				return;
			}
			if(this.fieldsValidationMessages == null){
				this.fieldsValidationMessages = new HashMap<>();
			}
			if(this.fieldsValidationMessages.get(fieldName) == null){
				this.fieldsValidationMessages.put(fieldName, new ArrayList<>());
			}
			this.fieldsValidationMessages.get(fieldName).add(error);
		}
		public void addGlobalError(String error){
			if(StringUtils.isEmpty(error)) {
				return;
			}
			if(this.globalValidationMessages == null){
				this.globalValidationMessages = new ArrayList<>();
			}
			this.globalValidationMessages.add(error);
		}
	}
	
	public static enum Type {
		BAD_REQUEST,
		NO_CONTENT,
		INVALID_INPUT,
		INVALID_STATE,
		INVALID_RIGHT, 
		CONFLICT, 
		INTERNAL_ERROR
	}
}