/*
 * Copyright (c) 1998-2005 Regents of the University of California.
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

import java.util.EventObject;
import HTTPClient.HTTPResponse;


/**
 * Title:       WebDAVResponseEvent
 * Description: The event object sent for responses from the server
 * Copyright:   Copyright (c) 1998-2005 Regents of the University of California. All rights reserved.
 * @author      Undergraduate project team ICS 126B 1998
 * date         1998
 * @author      Yuzo Kanomata, Joachim Feise (dav-exp@ics.uci.edu)
 * date         17 March 1999
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         1 October 2001
 * Changes:     Change of package name
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         8 February 2004
 * Changes:     Added Javadoc templates
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * date         8 February 2005
 * Changes:     Added ACL-specific fields
 */
public class WebDAVResponseEvent extends EventObject
{
    public static final int URIBOX = 1;
    public static final int SELECT = 2;
    public static final int INDEX = 3;
    public static final int EXPAND = 4;
    public static final int PROPERTIES = 5;
    public static final int SAVE_AS = 6;
    public static final int EXCLUSIVE_LOCK = 10;
    public static final int SHARED_LOCK = 11;
    public static final int UNLOCK = 12;
    public static final int DELETE = 20;
    public static final int DELETE2 = 21;
    public static final int RENAME = 30;
    public static final int RENAME2 = 31;
    public static final int COPY = 32;
    public static final int MKCOL = 40;
    public static final int MKCOLBELOW = 41;
    public static final int DISPLAY = 50;
    public static final int VIEW = 51;
    public static final int EDIT = 52;
    public static final int COMMIT = 60;
    // ACLs
    public static final int ACL_OWNER = 70;
    public static final int ACL_GROUP = 71;
    public static final int ACL_SUPPORTED_PRIVILEGE_SET = 72;
    public static final int ACL_USER_PRIVILEGES = 73;
    public static final int ACL = 74;
    public static final int SUPPORTED_ACL = 75;
    public static final int INHERITED_ACL = 76;
    public static final int ACL_PRINCIPAL_COLLECTION_SET = 77;
    public static final int ACL_PRINCIPAL_NAMES = 78;
    public static final int ACL_PROPERTY_NAMES = 79;
    public static final int ACL_PRINCIPAL_PROP_SET = 80;
    public static final int PRINCIPAL_MATCH = 81;
    public static final int PRINCIPAL_PROPERTY_SEARCH = 82;
    public static final int PRINCIPAL_SEARCH_PROPERTY_SET = 83;


    /**
     * Constructor
     * @param module
     * @param hostname
     * @param Port
     * @param resource
     * @param method
     * @param response
     * @param data
     * @param node
     */
    public WebDAVResponseEvent( Object module, String hostname, int Port,
                                String resource, String method,
                                HTTPResponse response, int code, String data,
                                WebDAVTreeNode node )
    {
        super (module);

        if( GlobalData.getGlobalData().getDebugResponse() )
        {
            System.err.println( "WebDAVResponseEvent::Constructor" );
        }

        this.httpResponse = response;
        this.methodName = method;
        this.extendedCode = code;
        this.extendedData = data;
        this.resource = resource;
        this.hostName = hostname;
        this.port = Port;
        this.node = node;
    }


    /**
     * 
     * @return
     */
    public HTTPResponse getResponse()
    {
        return httpResponse;
    }


    /**
     * 
     * @return
     */
    public String getMethodName()
    {
        return methodName;
    }


    /**
     * 
     * @return
     */
    public int getExtendedCode()
    {
        return extendedCode;
    }


    /**
     * 
     * @return
     */
    public String getExtendedData()
    {
        return extendedData;
    }


    /**
     * 
     * @return
     */
    public String getHost()
    {
        return hostName;
    }


    /**
     * 
     * @return
     */
    public int getPort()
    {
        return port;
    }


    /**
     * 
     * @return
     */
    public String getResource()
    {
        return resource;
    }


    /**
     * 
     * @return
     */
    public WebDAVTreeNode getNode()
    {
        return node;
    }


    private HTTPResponse httpResponse;
    private String methodName;
    private int extendedCode;
    private String extendedData;
    private String hostName;
    private int port;
    private String resource;

    private WebDAVTreeNode node; // The node to which the expand is directed
}
