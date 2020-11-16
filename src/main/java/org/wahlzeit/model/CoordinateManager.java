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
 * A CoordinateManager manager provides access to and manages Coordinate.
 */
public class CoordinateManager extends ObjectManager {
    /**
	 * 
	 */
	protected static final CoordinateManager instance = new CoordinateManager();

	/**
	 * In-memory cache for Coordinate
	 */
	protected Map<CoordinateId, Coordinate> coordinateCache = new HashMap<CoordinateId, Coordinate>();
	
	/**
	 * 
	 */
	public static final CoordinateManager getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	public static final boolean hasCoordinate(String id) {
		return hasCoordinate(CoordinateId.getIdFromString(id));
	}
	
	/**
	 * 
	 */
	public static final boolean hasCoordinate(CoordinateId id) {
		return getCoordinate(id) != null;
	}
	
	/**
	 * 
	 */
	public static final Coordinate getCoordinate(String id) {
		return getCoordinate(CoordinateId.getIdFromString(id));
	}
	
	/**
	 * 
	 */
	public static final Coordinate getCoordinate(CoordinateId id) {
		return instance.getCoordinateFromId(id);
	}
	
	
	/**
	 * @methodtype boolean-query
	 * @methodproperties primitive
	 */
	protected boolean doHasCoordinate(CoordinateId id) {
		return coordinateCache.containsKey(id);
	}
	
	/**
	 * 
	 */
	public Coordinate getCoordinateFromId(CoordinateId id) {
		if (id.isNullId()) {
			return null;
		}

		Coordinate result = doGetCoordinateFromId(id);
		
		if (result == null) {
			try {
				PreparedStatement stmt = getReadingStatement("SELECT * FROM coordinates WHERE id = ?");
				result = (Coordinate) readObject(stmt, id.asInt());
			} catch (SQLException sex) {
				SysLog.logThrowable(sex);
			}
			if (result != null) {
				doAddCoordinate(result);
			}
		}
		
		return result;
	}
		
	/**
	 * @methodtype get
	 * @methodproperties primitive
	 */
	protected Coordinate doGetCoordinateFromId(CoordinateId id) {
		return coordinateCache.get(id);
	}
	
	/**
	 * 
	 */
	protected Coordinate createObject(ResultSet rset) throws SQLException {
		return CoordinateFactory.getInstance().createCoordinate(rset);
	}
	
	/**
	 * @methodtype command
	 *
	 * Load all persisted Coordinates. Executed when Wahlzeit is restarted.
	 */
	public void addCoordinate(Coordinate coordinate) {
		CoordinateId id = coordinate.getId();
		assertIsNewCoordinate(id);
		doAddCoordinate(coordinate);

		try {
			PreparedStatement stmt = getReadingStatement("INSERT INTO coordinates(id) VALUES(?)");
			createObject(coordinate, stmt, id.asInt());
			ServiceMain.getInstance().saveGlobals();
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * @methodtype command
	 * @methodproperties primitive
	 */
	protected void doAddCoordinate(Coordinate myCoordinate) {
		coordinateCache.put(myCoordinate.getId(), myCoordinate);
	}

	/**
	 * @methodtype command
	 */
	public void loadCoordinates(Collection<Coordinate> result) {
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM coordinates");
			readObjects(result, stmt);
			for (Iterator<Coordinate> i = result.iterator(); i.hasNext(); ) {
				Coordinate coordinate = i.next();
				if (!doHasCoordinate(coordinate.getId())) {
					doAddCoordinate(coordinate);
				} else {
					SysLog.logSysInfo("coordinate", coordinate.getId().asString(), "coordinate had already been loaded");
				}
			}
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		SysLog.logSysInfo("loaded all coordinates");
	}
	
	/**
	 * 
	 */
	public void saveCoordinate(Coordinate coordinate) {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM coordinates WHERE id = ?");
			updateObject(coordinate, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 */
	public void saveCoordinates() {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM coordinates WHERE id = ?");
			updateObjects(coordinateCache.values(), stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
		
	/**
	 * 
	 */
	public Coordinate createCoordinate() throws Exception {
		CoordinateId id = CoordinateId.getNextId();
		Coordinate result = new Coordinate(id);
		addCoordinate(result);
		return result;
	}
	
	/**
	 * @methodtype assertion
	 */
	protected void assertIsNewCoordinate(CoordinateId id) {
		if (hasCoordinate(id)) {
			throw new IllegalStateException("Coordinate already exists!");
		}
	}
}
