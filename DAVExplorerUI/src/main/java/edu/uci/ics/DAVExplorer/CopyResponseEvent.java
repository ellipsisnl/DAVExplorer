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
 * Title:       CopyResponse Event
 * Description: This class creates an event object which carries the path
 *              and node to the receiving listener.
 * Copyright:   Copyright (c) 1998-2004 Regents of the University of California. All rights reserved.
 * @author      Undergraduate project team ICS 126B 1998
 * @date        1998
 * @author      Yuzo Kanomata, Joachim Feise (dav-exp@ics.uci.edu)
 * @date        7 April 1999
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        1 October 2001
 * Changes:     Change of package name
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        08 February 2004
 * Changes:     Added Javadoc templates
 */

package edu.uci.ics.DAVExplorer;

import java.util.EventObject;

/**
 * 
 */
public class CopyResponseEvent extends EventObject
{
    WebDAVTreeNode Node;


    /**
     * Constructor
     * @param module
     * @param n 
     */
    public CopyResponseEvent(Object module, WebDAVTreeNode n)
    {
        super(module);
        Node = n;
    }


    /**
     * 
     * @return
     */
    public WebDAVTreeNode getNode()
    {
        return Node;
    }
}
