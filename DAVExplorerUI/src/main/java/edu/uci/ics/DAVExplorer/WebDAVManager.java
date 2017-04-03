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
 * Title:       Login Dialog
 * Description: This is the WebDAV method wrapper class. It is implemented as
 *              a bean, which listens for a request event. Once the event
 *              occurs, appropriate Method from the WebDAV class library is
 *              called.
 * Copyright:   Copyright (c) 1998-2004 Regents of the University of California. All rights reserved.
 * @author      Robert Emmery (dav-exp@ics.uci.edu)
 * @date        25 March 1998
 * @author      Yuzo Kanomata, Joachim Feise (dav-exp@ics.uci.edu)
 * @date        14 April 1999
 * Changes:     Added notification for IO exceptions during connect
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        12 January 2001
 * Changes:     Added support for https (SSL)
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        1 October 2001
 * Changes:     Change of package name
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        19 November 2001
 * Changes:     Added handling of untrusted certificates by asking user
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        25 June 2002
 * Changes:     Special handling of PUT to support files > 2GB
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        17 March 2003
 * Changes:     Integrated Brian Johnson's applet changes.
 *              Added better error reporting.
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        3 November 2003
 * Changes:     Added support for proxy server in applet settings (it always worked through
 *              the "Edit Proxy Info" menu entry.)
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        10 November 2003
 * Changes:     Clearing the proxy setting when proxy info is removed
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        17 November 2003
 * Changes:     Setting the cursor to an hourglass when connecting.
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        08 February 2004
 * Changes:     Added Javadoc templates
 */

package edu.uci.ics.DAVExplorer;

import java.io.IOException;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.NVPair;
import HTTPClient.ModuleException;
import java.util.Vector;
import java.util.StringTokenizer;
import java.awt.Cursor;
//import com.sun.net.ssl.KeyManager;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;


/**
 * 
 */
public class WebDAVManager
{
    public HTTPResponse Response;
    private WebDAVConnection Con;
    private String Hostname = null;
    private int Port;
    private String ProxyHostname = null;
    private int ProxyPort;
    private String MethodName;
    private String ResourceName;
    private NVPair[] Headers;
    private byte[] Body;
    private int ExtraInfo;
    private String ExtraData;
    private Vector Listeners = new Vector();

    private boolean logging = false;
    private String logFilename = null;


    /**
     * Constructor
     */
    public WebDAVManager()
    {
    }


    /**
     * 
     * @param e
     */
    public void sendRequest(WebDAVRequestEvent e)
    {
        String ProxyTempHost = null;
        int ProxyTempPort = 0;
        String proxy = null;
        if( GlobalData.getGlobalData().isAppletMode() )
        {
            proxy = GlobalData.getGlobalData().getProxy();
        }
        if( proxy == null )
            proxy = GlobalData.getGlobalData().ReadConfigEntry("proxy");
        boolean useProxy = false;
        // find out if proxy is used
        if( proxy != null && (proxy.length() > 0 || proxy.startsWith(GlobalData.WebDAVPrefix)) )
        {
            if( proxy.startsWith(GlobalData.WebDAVPrefix) )
                proxy = proxy.substring(GlobalData.WebDAVPrefix.length());
            StringTokenizer str = new StringTokenizer( proxy, "/" );
            if( !str.hasMoreTokens() )
            {
                GlobalData.getGlobalData().errorMsg("Invalid proxy name.");
                return;
            }
            proxy = str.nextToken();

            str = new StringTokenizer( proxy, ":" );
            if( !str.hasMoreTokens() )
            {
                GlobalData.getGlobalData().errorMsg("Invalid proxy name.");
                return;
            }
            useProxy = true;
            ProxyTempHost = str.nextToken();
            if( str.hasMoreTokens() )
            {
                try
                {
                    ProxyTempPort = Integer.parseInt( str.nextToken() );
                }
                catch (Exception ex)
                {
                    GlobalData.getGlobalData().errorMsg("Invalid proxy port number.");
                    Port = 0;
                    return;
                }
            }
        }

        WebDAVTreeNode tn = e.getNode();
        String TempHost = e.getHost();
        int TempPort = e.getPort();

        // prevent selecting the same server twice when first request is not yet
        // finished
        if( ((TempHost!=null) && (TempHost.length()>0) && !TempHost.equals(Hostname)) ||
            (TempPort!=Port) ||
            ((ProxyTempHost!=null) && (ProxyTempHost.length()>0) && !ProxyTempHost.equals(ProxyHostname)) ||
            (ProxyTempPort!=ProxyPort) )
        {
            try
            {
                if( useProxy )
                {
                    ProxyHostname = ProxyTempHost;
                    ProxyPort = ProxyTempPort;
                    HTTPConnection.setProxyServer( ProxyHostname, ProxyPort );
                }
                else
                    // disable proxy
                    HTTPConnection.setProxyServer( null, 0 );
                
                Hostname = TempHost;
                GlobalData.getGlobalData().getMainFrame().setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
                if (TempPort != 0)
                {
                    Port = TempPort;
                    if( GlobalData.getGlobalData().getSSL() )
                    {
                        Con = new WebDAVConnection( "https", Hostname, Port );
                    }
                    else
                    {
                        Con = new WebDAVConnection(Hostname, Port);
                    }
                }
                else
                {
                    Port = 0;
                    if( GlobalData.getGlobalData().getSSL() )
                    {
                        Con = new WebDAVConnection( "https", Hostname, 443 );
                    }
                    else
                    {
                        Con = new WebDAVConnection(Hostname);
                    }
                }
                Con.setLogging( logging, logFilename );
                GlobalData.getGlobalData().getMainFrame().setCursor( Cursor.getDefaultCursor() );
            }
            catch( HTTPClient.ProtocolNotSuppException httpException )
            {
                GlobalData.getGlobalData().getMainFrame().setCursor( Cursor.getDefaultCursor() );
                GlobalData.getGlobalData().errorMsg( "Error: Protocol not supported.\n" + httpException.toString() );
            }
        }
        if( Con == null )
        {
            // user hit enter twice? Creating a new WebDAVConnection can take
            // a while, especially if a proxy is involved
            // so we just return here
            return;
        }

        String user = e.getUser();
        String pass = e.getPass();

        if (user.length() > 0)
        {
            try
            {
                Con.addDigestAuthorization(Hostname,user, pass);
                Con.addBasicAuthorization(Hostname,user,pass);
            }
            catch (Exception exc)
            {
                System.out.println(exc);
            }
        }

        MethodName = e.getMethod();
        ResourceName = e.getResource();
        Headers = e.getHeaders();
        Body = e.getBody();
        ExtraInfo = e.getExtraInfo();
        ExtraData = e.getExtraData();
        try {
            if( e.getMethod().equals("PUT") )
            {
                // special handling of PUT to allow for files > 2GB
                Response = Con.Put( ResourceName, ExtraData, Headers );
            }
            else
            {
                Response = Con.Generic(MethodName, ResourceName, Body, Headers);
            }

            WebDAVResponseEvent webdavResponse  = GenerateWebDAVResponse(Response,tn);
            fireResponse(webdavResponse);
        }
        catch (IOException exception)
        {
            // account for the possibility of JSSE not being installed
            // This dynamic check using reflection prevents a
            // NoClassDefFoundError when JSSE is not installed
            try
            {
                Class c = Class.forName( "javax.net.ssl.SSLPeerUnverifiedException" );
                if( exception.getClass().equals(c) )
                {
                    SSLTrustDialog dlg = new SSLTrustDialog();
                    if( dlg.getTrust( Hostname ) )
                    {
                        TrustManager[] tm = { new RelaxedX509TrustManager() };
                        SSLContext sslContext = SSLContext.getInstance("SSL");
                        sslContext.init( null, tm, new java.security.SecureRandom() );
                        SSLSocketFactory sf = sslContext.getSocketFactory();
                        Con.setSSLSocketFactory( sf );
                        Con.setAllowAnyHostname( true );
                        try
                        {
                            Response = Con.Generic(MethodName, ResourceName, Body, Headers);

                            WebDAVResponseEvent webdavResponse  = GenerateWebDAVResponse(Response,tn);
                            fireResponse(webdavResponse);
                        }
                        catch( IOException ex )
                        {
                            GlobalData.getGlobalData().errorMsg("Connection error: \n" + exception);
                            // give up
                        }
                    }
                }
                else
                {
                    GlobalData.getGlobalData().errorMsg("Connection error: \n" + exception);
                }
            }
            catch( Throwable t )
            {
                // class not found, SSL not available
                GlobalData.getGlobalData().errorMsg("Connection error: \n" + exception);
            }
        }
        catch (ModuleException exception)
        {
            GlobalData.getGlobalData().errorMsg("HTTPClient error: \n" + exception);
        }
    }


    /**
     * 
     * @param l
     */
    public synchronized void addResponseListener(WebDAVResponseListener l)
    {
        Listeners.addElement(l);
    }


    /**
     * 
     * @param l
     */
    public synchronized void removeResponseListener(WebDAVResponseListener l)
    {
        Listeners.removeElement(l);
    }


    /**
     * 
     * @param Response
     * @param Node
     * @return
     */
    public WebDAVResponseEvent GenerateWebDAVResponse( HTTPResponse _Response,
                                                       WebDAVTreeNode Node )
    {
        WebDAVResponseEvent e = new WebDAVResponseEvent( this, Hostname, Port,
                                                         ResourceName, MethodName,
                                                         _Response, ExtraInfo,
                                                         ExtraData, Node );
        return e;
    }


    /**
     * 
     * @param e
     */
    public void fireResponse(WebDAVResponseEvent e)
    {
        Vector ls;
        synchronized (this)
        {
            ls = (Vector) Listeners.clone();
        }


        for (int i=0; i<ls.size();i++) {
            WebDAVResponseListener l = (WebDAVResponseListener) ls.elementAt(i);
            l.responseFormed(e);
        }
    }


    /**
     * 
     * @param logging
     * @param filename
     */
    public void setLogging( boolean logging, String filename )
    {
        this.logging = logging;
        this.logFilename = filename;

        if( Con != null )
        {
            Con.setLogging( logging, filename );
        }
    }


    /**
     * 
     * @param Hostname
     * @param Port
     * @return
     */
    protected WebDAVConnection createProxyConnection( String _Hostname, int _Port )
    {
        if( _Port != 0 )
        {
            return new WebDAVConnection( _Hostname, _Port );
        }
        else
        {
            return new WebDAVConnection( _Hostname );
        }
    }
}
