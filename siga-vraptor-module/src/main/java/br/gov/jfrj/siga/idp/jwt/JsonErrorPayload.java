package br.gov.jfrj.siga.idp.jwt;

import java.util.List;

public class JsonErrorPayload {

	private String message;
	private List<String> details;

	public JsonErrorPayload(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}
	
	public void setDetails(List<String> details) {
		this.details = details;
	}

}
