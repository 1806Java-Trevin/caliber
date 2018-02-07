package com.revature.category.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QCStatus implements Serializable{
	@JsonProperty("Superstar")
	Superstar,
	@JsonProperty("Good")
	Good,
	@JsonProperty("Average")
	Average,
	@JsonProperty("Poor")
	Poor,
	@JsonProperty("Undefined")
	Undefined
}