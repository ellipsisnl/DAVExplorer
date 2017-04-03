/*
 * Copyright (c) 2003 Th. Rickert.
 * Copyright (c) 2003-2004 Regents of the University of California.
 * All rights reserved.
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
 * Title:       Progressbar
 * Description: Progress bar control
 * Copyright:   Copyright (c) 2003 Th. Rickert.
 * Copyright:   Copyright (c) 2003-2004 Regents of the University of California. All rights reserved.
 * @author      Th. Rickert, integrated by Joachim Feise
 * @author      Joachim Feise (dav-exp@ics.uci.edu)
 * @date        08 February 2004
 * Changes:     Added Javadoc templates
 */

package edu.uci.ics.DAVExplorer;

import java.awt.Dimension;
import javax.swing.JProgressBar;

import HTTPClient.ProgressListener;
import HTTPClient.ProgressObserver;


/**
 * 
 */
public class ProgressBar extends JProgressBar
    implements ProgressListener
{
    private int unknownEnd = 0;


    /**
     * Constructor
     */
    public ProgressBar()
    {
        super();
        Dimension d = getSize();
        d.height = 20;  // reasonable size
        setPreferredSize( d );
        ProgressObserver.getInstance().addProgressListener(this);
    }


    /**
     * 
     * @param writtenBytes
     * @param len
     * @param method
     */
    public void progressAchieved( long writtenBytes, long len, String method )
    {
        if( len<0 || len<writtenBytes )
        {
            // unknown full length
            unknownEnd++;
            setValue(unknownEnd);
            setStringPainted(true);
            setString(writtenBytes+" Bytes");
        }
        else if( len==writtenBytes )
        {
            // download/upload end
            setValue(0);
            setString("");
            setStringPainted(false);
            unknownEnd = 0;
        }
        else
        {
            setStringPainted(true);
            // scale if data is >2GB
            while( len > Integer.MAX_VALUE )
            {
                len >>= 1;
                writtenBytes >>= 1;
            }
            int percent = (int)((writtenBytes * 100) / len);
            setValue(percent);
            setString(percent+"%");
        }
    }
}
