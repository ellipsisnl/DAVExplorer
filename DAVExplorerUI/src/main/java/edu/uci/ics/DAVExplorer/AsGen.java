/*
 * Copyright (c) 1998-2004 Regents of the University of California.
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

/**
 * Title:       AsGen
 * Description: This class is used to generate namespace aliases within
 *              an xml document.
 * Copyright:   Copyright (c) 1998-2004 Regents of the University of California. All rights reserved.
 * @author      Robert Emmery
 * @date        28 February 1998
 * @author      Yuzo Kanomata, Joachim Feise (dav-exp@ics.uci.edu)
 * @date        17 March 1999
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        29 September 2001
 * Changes:     Now storing the aliases in a linked list
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        1 October 2001
 * Changes:     Change of package name
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        08 February 2004
 * Changes:     Added Javadoc templates
 */

package edu.uci.ics.DAVExplorer;

/**
 * 
 */
class AsNode
{
    /**
     * Constructor
     * @param schema
     * @param alias
     * @param next 
     */
    public AsNode( String schema, String alias, AsNode next )
    {
        m_schema = schema;
        m_alias = alias;
        if( next != null )
            next.m_next = this;
        m_prev = next;
    }


    /**
     * 
     * @return
     */
    public String getAlias()
    {
        return m_alias;
    }


    /**
     * 
     * @return
     */
    public String getSchema()
    {
        return m_schema;
    }


    /**
     * 
     * @return
     */
    public boolean isAttributeSet()
    {
        return m_attributeSet;
    }


    /**
     * 
     */
    public void setAttribute()
    {
        m_attributeSet = true;
    }


    /**
     *
     * @return
     */
    public AsNode getNext()
    {
        return m_next;
    }


    /**
     *
     * @return
     */
    public AsNode getPrev()
    {
        return m_prev;
    }

    AsNode m_next = null;
    AsNode m_prev = null;
    String m_schema;
    String m_alias;
    boolean m_attributeSet = false;
};


/**
 * 
 */
public class AsGen
{
    /**
     * Constructor
     */
    public AsGen()
    {
    }


    /**
     * Copy constructor
     */
    protected AsGen( AsNode node )
    {
        if( m_first == null )
            m_first = node;
        if( m_last == null )
            m_last = node;
        m_current = node;
    }


    /**
     * 
     * @param schema
     */
    public void createNamespace( String schema )
    {
        AsNode node = new AsNode( schema, getNextAs(), m_last );
        m_last = node;
        if( m_first == null )
            m_first = node;
        m_current = node;
    }


    /**
     * 
     * @return
     */
    public AsGen getNext()
    {
        if( m_current == null )
            return null;
        else
            m_current = m_current.getNext();
        return new AsGen( m_current );
    }


    /**
     * 
     * @return
     */
    public AsGen getPrev()
    {
        if( m_current == null )
            return null;
        else
            m_current = m_current.getPrev();
        return new AsGen( m_current );
    }


    /**
     * 
     * @return
     */
    public AsGen getFirst()
    {
        if( m_first == null )
            return null;
        else
            return new AsGen( m_first );
    }


    /**
     * 
     * @return
     */
    public String getAlias()
    {
        if( m_current == null )
            return null;
        else
            return m_current.getAlias();
    }


    /**
     * 
     * @return
     */
    public String getSchema()
    {
        if( m_current == null )
            return null;
        else
            return m_current.getSchema();
    }


    /**
     * 
     * @return
     */
    public boolean isAttributeSet()
    {
        if( m_current == null )
            return false;
        else
            return m_current.isAttributeSet();
    }


    /**
     * 
     */
    public void setAttribute()
    {
        // set when the namespace is declared
        if( m_current != null )
            m_current.setAttribute();
    }


    /**
     * 
     */
    public static void clear()
    {
        m_current = m_first = m_last = null;
        m_lastGenerated = "@";
    }


    /**
     * 
     * @return
     */
    private String getNextAs()
    {
        String str = m_lastGenerated;

        int len = str.length();
        byte[] byte_str = str.getBytes();
        if (str.endsWith("Z"))
        {
            byte_str[len-1] = (byte)'A';
            boolean found = false;
            boolean append = true;
            int i = len-2;
            while( (i>=0) && (!found) )
            {
                if (byte_str[i] != 'Z') {
                    append = false;
                    found = true;
                    byte_str[i]++;
                }
                else
                {
                    byte_str[i] = (byte)'A';
                }
                i--;
            }
            str = new String(byte_str);
            if (append)
                str += 'A';
        }
        else
        {
            byte_str[len-1]++;
            str = new String(byte_str);
        }
        m_lastGenerated = str;
        return str;
    }


    private static AsNode m_first;
    private static AsNode m_last;
    private static AsNode m_current;
    private static String m_lastGenerated = "@";
}
