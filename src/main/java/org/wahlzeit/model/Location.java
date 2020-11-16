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
import org.wahlzeit.utils.StringUtil;

/**
 *An Abstract Location for creating Coodinate and related objects.
 * @author aj52izov
 */
public class Location extends DataObject{
        /**
     *
     */
    protected LocationId id = null;

    /**
     *
     */
    protected CoordinateId coodinateId = null;
    

    /**
     *
     */
    protected long creationTime = System.currentTimeMillis();

    /**
     *
     */
    public Location() {
        id = LocationId.getNextId();
        incWriteCount();
    }
    
    /**
     *
     */
    public Location(CoordinateId coordinate_Id) {
        id = LocationId.getNextId();
        coodinateId = coordinate_Id;
        incWriteCount();
    }    

    /**
     *
     * @methodtype constructor
     */
    public Location(LocationId myId, CoordinateId coordinate_Id) {
        id = myId;
        coodinateId = coordinate_Id;
        incWriteCount();
    }
    

    /**
     *
     * @methodtype constructor
     */
    public Location(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    
    /**
     *
     * @methodtype get
     */        
    public LocationId getId() {
        return id;
    }

    
    /**
     *
     * @methodtype set
     */        
    public void setId(LocationId id) {
        this.id = id;
    }

    
    /**
     *
     * @methodtype get
     */        
    public CoordinateId getCoodinateId() {
        return coodinateId;
    }

    /**
     *
     * @methodtype set
     */        
    public void setCoodinateId(CoordinateId coodinateId) {
        this.coodinateId = coodinateId;
    }

    
    /**
     *
     * @methodtype get
     */        
    public long getCreationTime() {
        return creationTime;
    }

    /**
     *
     * @methodtype set
     */        
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + this.coodinateId.asInt();
        hash = 59 * hash + (int) (this.creationTime ^ (this.creationTime >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.coodinateId != other.coodinateId) {
            return false;
        }
        if (this.creationTime != other.creationTime) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     *
     * @methodtype get
     */
    public String getIdAsString() {
        return String.valueOf(id.asInt());
    }

    /**
     *
     */
    public void readFrom(ResultSet rset) throws SQLException {
        id = LocationId.getIdFromInt(rset.getInt("id"));
        coodinateId = CoordinateId.getIdFromInt(rset.getInt("coordinate_id"));
        creationTime = rset.getLong("creation_time");
    }

    /**
     *
     */
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("id", id.asInt());
        rset.updateInt("coordinate_id", coodinateId.asInt());
        rset.updateLong("creation_time", creationTime);

    }

    /**
     *
     */
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }


    
}
