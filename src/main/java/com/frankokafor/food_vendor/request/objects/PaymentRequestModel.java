package com.frankokafor.food_vendor.request.objects;

public class PaymentRequestModel {

	private double total;
	private String currency;
	private String method;
	private String intent;
	private String description;
	private String cancleUri;
	private String successUri;
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCancleUri() {
		return cancleUri;
	}
	public void setCancleUri(String cancleUri) {
		this.cancleUri = cancleUri;
	}
	public String getSuccessUri() {
		return successUri;
	}
	public void setSuccessUri(String successUri) {
		this.successUri = successUri;
	}
	
	
}
