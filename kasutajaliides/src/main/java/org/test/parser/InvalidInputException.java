package org.test.parser;

import java.lang.Exception;

public class InvalidInputException extends Exception{
	public InvalidInputException(String s){
		super(s);
	}
}