/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.*;

/**
 *An Abstract Factory for creating photos and related objects.
 * @author aj52izov
 */
public class CoordinateFactory {
    
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	private static CoordinateFactory instance = null;
	
	/**
	 * Public singleton access method.
	 */
	public static synchronized CoordinateFactory getInstance() {
		if (instance == null) {
			SysLog.logSysInfo("setting generic CoordinateFactory");
			setInstance(new CoordinateFactory());
		}
		
		return instance;
	}
	
	/**
	 * Method to set the singleton instance of CoordinateFactory.
	 */
	protected static synchronized void setInstance(CoordinateFactory coordinateFactory) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initialize CoordinateFactory twice");
		}
		
		instance = coordinateFactory;
	}
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	public static void initialize() {
		getInstance(); // drops result due to getInstance() side-effects
	}
	
	/**
	 * 
	 */
	protected CoordinateFactory() {
		// do nothing
	}

	/**
	 * @methodtype factory
	 */
	public Coordinate createCoordinate() {
		return new Coordinate();
	}
	
	/**
	 * 
	 */
	public Coordinate createCoordinate(CoordinateId id) {
		return new Coordinate(id);
	}
	
	/**
	 * 
	 */
	public Coordinate createCoordinate(ResultSet rs) throws SQLException {
		return new Coordinate(rs);
	} 
    
}
