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

import com.ms.xml.om.Element;

/**
 * Title:       ACL Restriction dialog
 * Description: Show ACL restrictions as reported from a server.
 * Copyright:   Copyright (c) 2005 Regents of the University of California. All rights reserved.
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        14 Feb 2005
 */
public class ACLRestrictionDialog extends PropDialog
{
    /**
     * Constructor
     * Removes unused buttons from the dialog created by the superclass.
     * 
     * @param properties
     *      the ACL properties to show
     * @param resource
     *      the resource the ACLs apply to
     * @param hostname
     *      the server name
     */
    public ACLRestrictionDialog( Element properties, String resource,
            String hostname )
    {
        init( new ACLPropModel(properties), resource, hostname, "View ACL Restrictions", null, false );
        buttonPanel.remove(addButton);
        buttonPanel.remove(deleteButton);
        buttonPanel.remove(saveButton);
        pack();
        setSize( getPreferredSize() );
        GlobalData.getGlobalData().center( this );
        show();
    }
}
