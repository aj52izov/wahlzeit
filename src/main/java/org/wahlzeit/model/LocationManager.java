/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.model;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.wahlzeit.main.*;
import org.wahlzeit.services.*;

/**
 * A Location manager provides access to and manages Location.
 */

public class LocationManager  extends ObjectManager{
    
 /**
	 * 
	 */
	protected static final LocationManager instance = new LocationManager();

	/**
	 * In-memory cache for Location
	 */
	protected Map<LocationId, Location> locationCache = new HashMap<LocationId, Location>();
	
	/**
	 * 
	 */
	public static final LocationManager getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	public static final boolean hasLocation(String id) {
		return hasLocation(LocationId.getIdFromString(id));
	}
	
	/**
	 * 
	 */
	public static final boolean hasLocation(LocationId id) {
		return getLocation(id) != null;
	}
	
	/**
	 * 
	 */
	public static final Location getLocation(String id) {
		return getLocation(LocationId.getIdFromString(id));
	}
	
	/**
	 * 
	 */
	public static final Location getLocation(LocationId id) {
		return instance.getLocationFromId(id);
	}
	
	
	/**
	 * @methodtype boolean-query
	 * @methodproperties primitive
	 */
	protected boolean doHasLocation(LocationId id) {
		return locationCache.containsKey(id);
	}
	
	/**
	 * 
	 */
	public Location getLocationFromId(LocationId id) {
		if (id.isNullId()) {
			return null;
		}

		Location result = doGetLocationFromId(id);
		
		if (result == null) {
			try {
				PreparedStatement stmt = getReadingStatement("SELECT * FROM locations WHERE id = ?");
				result = (Location) readObject(stmt, id.asInt());
			} catch (SQLException sex) {
				SysLog.logThrowable(sex);
			}
			if (result != null) {
				doAddLocation(result);
			}
		}
		
		return result;
	}
		
	/**
	 * @methodtype get
	 * @methodproperties primitive
	 */
	protected Location doGetLocationFromId(LocationId id) {
		return locationCache.get(id);
	}
	
	/**
	 * 
	 */
	protected Location createObject(ResultSet rset) throws SQLException {
		return LocationFactory.getInstance().createLocation(rset);
	}
	
	/**
	 * @methodtype command
	 *
	 * Load all persisted Locations. Executed when Wahlzeit is restarted.
	 */
	public void addLocation(Location Location) {
		LocationId id = Location.getId();
		assertIsNewLocation(id);
		doAddLocation(Location);

		try {
			PreparedStatement stmt = getReadingStatement("INSERT INTO locations(id) VALUES(?)");
			createObject(Location, stmt, id.asInt());
			ServiceMain.getInstance().saveGlobals();
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * @methodtype command
	 * @methodproperties primitive
	 */
	protected void doAddLocation(Location myLocation) {
		locationCache.put(myLocation.getId(), myLocation);
	}

	/**
	 * @methodtype command
	 */
	public void loadLocations(Collection<Location> result) {
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM locations");
			readObjects(result, stmt);
			for (Iterator<Location> i = result.iterator(); i.hasNext(); ) {
				Location Location = i.next();
				if (!doHasLocation(Location.getId())) {
					doAddLocation(Location);
				} else {
					SysLog.logSysInfo("Location", Location.getId().asString(), "location had already been loaded");
				}
			}
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		SysLog.logSysInfo("loaded all locations");
	}
	
	/**
	 * 
	 */
	public void saveLocation(Location Location) {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
			updateObject(Location, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 */
	public void saveLocations() {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
			updateObjects(locationCache.values(), stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * @methodtype command
	 */
	public Persistent findLocationByCoordinate(CoordinateId coordinateId) {
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM locations WHERE coordinate_id = ?");
			return readObject(stmt, coordinateId.asString());
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
	}
                return null;
        }   
	/**
	 * 
	 */
	protected void updateDependents(Persistent obj) throws SQLException {
		Location location = (Location) obj;
                Coordinate coodinate = new CoordinateManager().doGetCoordinateFromId(location.getCoodinateId());
                Persistent obj1 = new Coordinate(coodinate);
		PreparedStatement stmt = getReadingStatement("DELETE FROM coordinates WHERE id = ?");
		deleteObject(obj1, stmt);
                PhotoManager photoManager = new PhotoManager();
                Photo photo = (Photo)photoManager.findPhotosByLocation(location.getId());
                photo.setLocationId(0);
                photoManager.savePhoto(photo);
	}
		
	/**
	 * 
	 */
	public Location createLocation(CoordinateId coordinateId) throws Exception {
		LocationId id = LocationId.getNextId();
		Location result = new Location(id, coordinateId);
		addLocation(result);
		return result;
	}
	
	/**
	 * @methodtype assertion
	 */
	protected void assertIsNewLocation(LocationId id) {
		if (hasLocation(id)) {
			throw new IllegalStateException("location already exists!");
		}
	}
        
}
