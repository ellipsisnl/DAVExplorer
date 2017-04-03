/*
 * Copyright (C) 2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.ics.DAVExplorer;


import java.util.Vector;


/**
 * Title:       Property search node
 * Description: Describes one entry in the property search datamodel
 * Copyright:   Copyright (c) 2005 Regents of the University of California. All rights reserved.
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         11 Feb 2005
 */
public class ACLPropertySearchNode
{
    /**
     * Constructor
     * 
     * @param properties
     *      the properties to apply the search to
     * @param match
     *      the match string used in the search
     */
    public ACLPropertySearchNode( Vector properties, String match )
    {
        this.properties = new Vector( properties );
        this.match = match;
    }


    /**
     * Set the properties to apply the search to.
     *  
     * @param properties
     *  the properties to apply the search to
     */
    public void setProperty( Vector properties )
    {
        properties = new Vector( properties );
    }


    /**
     * Set the match string.
     *  
     * @param match
     *      the new match string
     */
    public void setMatch( String match )
    {
        this.match = match;
    }


    /**
     * Returns the search properties.
     *  
     * @return
     *      the properties to apply the search to
     */
    public Vector getProperties()
    {
        return properties;
    }


    /**
     * Returns the match string.
     *  
     * @return
     *      the string use in the search comparison
     */
    public String getMatch()
    {
        return match;
    }


    protected Vector properties;
    protected String match;
}
