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
 *
 * @author aj52izov
 */
public class Coordinate extends DataObject{

    /**
     *
     */
    protected CoordinateId id = null;
    
    /**
     * 
     */
    
    /**
     * 
     */
    protected double x = 0;
    protected double y = 0;
    protected double z = 0;
    
    /**
     *
     */
    protected long creationTime = System.currentTimeMillis();    

    
    /**
     * Initialize Coordinate with (0,0,0).
     * @methodtype constructor.
     */
    public Coordinate() {
        id = CoordinateId.getNextId();
        incWriteCount();
    }

    /**
     *
     * @methodtype constructor
     */
    public Coordinate(CoordinateId myId) {
        id = myId;
        incWriteCount();
    }

    /**
     *
     * @methodtype constructor
     */
    public Coordinate(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    /**
     *
     * @methodtype get
     */
    public String getIdAsString() {
        return String.valueOf(id.asInt());
    }

    /**
     * Initialize Coordinate with (x,y,z).
     *
     * @param x the x Coordinate.
     * @param y the y Coordinate.
     * @param z the z Coordinate.
     * @methodtype constructor.
     */
    public Coordinate(double x, double y, double z) {
        id = CoordinateId.getNextId();
        this.x = x;
        this.y = y;
        this.z = z;
        incWriteCount();
    }

    /**
     * Initialize Coordinate with (x,y,0).
     *
     * @param x the x Coodinate.
     * @param y the y Coodinate.
     * @methodtype constructor.
     */
    public Coordinate(double x, double y) {
        id = CoordinateId.getNextId();
        this.x = x;
        this.y = y;
        incWriteCount();
    }
    /**
     * Initialize Coordinate with (x,y,z).
     *
     * @param x the x Coordinate.
     * @param y the y Coordinate.
     * @param z the z Coordinate.
     * @methodtype constructor.
     */
    public Coordinate(CoordinateId myid, double x, double y, double z) {
        id = myid;
        this.x = x;
        this.y = y;
        this.z = z;
        incWriteCount();
    }

    /**
     * Initialize Coordinate with (x,y,0).
     *
     * @param x the x Coodinate.
     * @param y the y Coodinate.
     * @methodtype constructor.
     */
    public Coordinate(CoordinateId myid, double x, double y) {
        id = myid;
        this.x = x;
        this.y = y;
        incWriteCount();
    }

    /**
     * Initialize Coordinate with (x,0,0).
     *
     * @param x the x Coodinate.
     * @methodtype constructor.
     */
    public Coordinate(CoordinateId myid, double x) {
        id = myid;
        this.x = x;
        incWriteCount();
    }    
    
    
 /**
     * Initialize Coordinate with (x,0,0).
     *
     * @param x the x Coodinate.
     * @methodtype constructor.
     */
    public Coordinate(double x) {
        id = CoordinateId.getNextId();
        this.x = x;
        incWriteCount();
    }
    

    /**
     * Initialize Coordinate with existed coordinate.
     *
     * @param coordinate
     * @methodtype constructor.
     */
    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.getX();
        this.y = coordinate.getY();
        this.z = coordinate.getZ();
    }

    /**
     *
     */
    public void readFrom(ResultSet rset) throws SQLException {
        id = CoordinateId.getIdFromInt(rset.getInt("id"));
        x = rset.getDouble("x_coodinate");
        y = rset.getDouble("y_coodinate");
        z = rset.getDouble("z_coodinate");
        creationTime = rset.getLong("creation_time");
    }
    
    /**
     *
     */
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("id", id.asInt());
        rset.updateDouble("x_coodinate", x);
        rset.updateDouble("y_coodinate", y);
        rset.updateDouble("z_coodinate", z);
        rset.updateLong("creation_time", creationTime);

    }     
    
    /**
     *
     * @methodtype get
     */    
    public double getX() {
        return x;
    }

    /**
     *
     * @methodtype set
     */    
    public void setX(double x) {
        this.x = x;
    }

    /**
     *
     * @methodtype get
     */    
    public double getY() {
        return y;
    }

    /**
     *
     * @methodtype set
     */    
    public void setY(double y) {
        this.y = y;
    }

    /**
     *
     * @methodtype get
     */  
    public double getZ() {
        return z;
    }

    /**
     *
     * @methodtype set
     */       
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
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
        final Coordinate other = (Coordinate) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        return true;
    }

    /**
     * check if equal.
     *
     * @param coordinate the input coodinate to check.
     * @return return true or false.
     */
    public boolean isEqual(Coordinate coordinate) {
        if (coordinate == null) {
            return false;
        }

        if (this == coordinate) {
            return true;
        }
        if (this.equals(coordinate)) {
            return true;
        }

        if (this.x != coordinate.getX()) {
            return false;
        }
        if (this.y != coordinate.getY()) {
            return false;
        }
        if (this.z != coordinate.getZ()) {
            return false;
        }
        return true;
    }
    
    /**
     *
     * @methodtype get
     */
    public CoordinateId getId() {
        return id;
    }

    /**
     *
     * @methodtype set
     */    
    public void setId(CoordinateId id) {
        this.id = id;
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

    /**
     * calcul the cartesian distance with the coodinate.
     *
     * @param coordinate the other coordinate.
     * @return the cartesian distance.
     */
    public double getDistance(Coordinate coordinate) {
        return Math.sqrt(Math.pow(this.x - coordinate.getX(), 2) + Math.pow(this.y - coordinate.getY(), 2) + Math.pow(this.z - coordinate.getZ(), 2));
    }

    /**
     *
     */
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }

}
