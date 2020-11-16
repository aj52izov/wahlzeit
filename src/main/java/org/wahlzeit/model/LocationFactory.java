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
public class LocationFactory {
    
    
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	private static LocationFactory instance = null;
	
	/**
	 * Public singleton access method.
	 */
	public static synchronized LocationFactory getInstance() {
		if (instance == null) {
			SysLog.logSysInfo("setting generic LocationFactory");
			setInstance(new LocationFactory());
		}
		
		return instance;
	}
	
	/**
	 * Method to set the singleton instance of LocationFactory.
	 */
	protected static synchronized void setInstance(LocationFactory locationFactory) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initialize CoordinateFactory twice");
		}
		
		instance = locationFactory;
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
	protected LocationFactory() {
		// do nothing
	}

	/**
	 * @methodtype factory
	 */
	public Location createLocation() {
		return new Location();
	}
	
	/**
	 * 
	 */
	public Location createLocation(CoordinateId id) {
		return new Location(id);
	}  

    /**
     *
     * @methodtype constructor
     */
    public Location createLocation(LocationId myId, CoordinateId coordinate_Id) {
        return new Location(myId, coordinate_Id);
    }
    
	
	/**
	 * 
	 */
	public Location createLocation(ResultSet rs) throws SQLException {
		return new Location(rs);
	} 
        
    
}
