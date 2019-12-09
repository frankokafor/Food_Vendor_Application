package com.frankokafor.food_vendor.messages;

public class MessageResponses {

	public static String CODE_OK = "success";
	public static String CODE_ERROR = "error";
	public static String MESSAGE_UPDATE = "Successfully updated";
	public static String MESSAGE_CREATE = "Successfully created";
	public static String MESSAGE_ERROR = "Error occured, please contact admin";
	public static String MESSAGE_UPLOAD = "Successfully uploaded";
	public static String MESSAGE_DELETE = "Successfully Deleted";
	public static String MESSAGE = "message";
	public static String CODE = "code";
	private String code;
	private String message;

	public String getCode() {
		return code;
	}

	public MessageResponses(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static MessageResponses response(String code, String message) {
		return new MessageResponses(code, message);
	}

	public static MessageResponses isCreating(int code) {
		MessageResponses mr;

		if (code == 0) {
			mr = MessageResponses.response(MessageResponses.CODE_OK, MessageResponses.MESSAGE_CREATE);
		} else {
			mr = MessageResponses.response(MessageResponses.CODE_OK, MessageResponses.MESSAGE_UPDATE);
		}
		return mr;
	}
}
