/* $Id: Constants.java,v 1.29 2006/04/23 16:01:35 totmacherr Exp $
 */
package de.dal33t.powerfolder;

import java.net.InetSocketAddress;

/**
 * Central constants holder for all important constants in PowerFolder.
 * 
 * @author <a href="mailto:totmacher@powerfolder.com">Christian Sprajc </a>
 * @version $Revision: 1.29 $
 */
public class Constants {
    // General settings *******************************************************

    /**
     * URL of the PowerFolder homepage
     */
    public static final String POWERFOLDER_URL = "http://www.powerfolder.com";

    /**
     * URL of the PowerFolder Pro page
     */
    public static final String POWERFOLDER_PRO_URL = "http://www.powerfolder.com/node/pro_edition";

    /**
     * URL of the page to report bugs.
     */
    public static final String BUG_REPORT_URL = "http://forums.powerfolder.com/forumdisplay.php?f=18";

    /**
     * URL of the Online Storage.
     */
    public static final String ONLINE_STORAGE_URL = "https://access.powerfolder.com";

    /**
     * URL of the Online Storage registration
     */
    public static final String ONLINE_STORAGE_REGISTER_URL = "https://access.powerfolder.com/register";

    /**
     * HTTP RPC URL for
     */
    public static final String HTTP_TUNNEL_RPC_URL = "http://access.powerfolder.com/rpc";

    /**
     * The Address of the online storage.
     */
    public static final InetSocketAddress ONLINE_STORAGE_ADDRESS = new InetSocketAddress(
        "access.powerfolder.com", 1337);

    /**
     * The official ID of the online storage.
     */
    public static final String ONLINE_STORAGE_NODE_ID = "WEBSERVICE03";
   // public static final String ONLINE_STORAGE_NODE_ID = "INFRASTRUCTURE01";

    /**
     * The name of the subdirectory in every folder to store powerfolder
     * relevant files
     */
    public static final String POWERFOLDER_SYSTEM_SUBDIR = ".PowerFolder";

    /**
     * The URL where to check the connectivty with.
     */
    public static final String LIMITED_CONNECTIVTY_CHECK_URL = "http://checkconnectivity.powerfolder.com/check.php";

    /**
     * The maximum number of lines in a chat.
     */
    public static final int MAX_CHAT_LINES = 500;

    // Network architecture contants ******************************************

    /**
     * The lenght in seconds of a request of filelist on a unjoined folder
     * (FolderDetails)
     */
    public static final int FOLDER_FILELIST_REQUEST_LENGTH = 60;

    /**
     * The maximum number of files on a FileList. If list ist greater, it is
     * splitted into smaller ones
     */
    public static final int FILE_LIST_MAX_FILES_PER_MESSAGE = 500;

    /**
     * The number of supernodes to connect at all.
     */
    public static final int N_SUPERNODES_TO_CONNECT = 4;

    /**
     * The number of supernodes to contact when a new network folder list is
     * requested
     */
    public static final int N_SUPERNODES_TO_CONTACT_FOR_NETWORK_FOLDER_LIST = 4;

    /**
     * The number of supernodes to contact when perfoming a nodes search
     */
    public static final int N_SUPERNODES_TO_CONTACT_FOR_NODE_SEARCH = 4;

    /**
     * The number of lan nodes to contact when perfoming a nodes search
     */
    public static final int N_LAN_NODES_TO_CONTACT_FOR_NODE_SEARCH = 4;

    /**
     * The number of supernodes to contact to request the full list of nodes.
     */
    public static final int N_SUPERNODES_TO_CONTACT_FOR_NODE_LIST = 4;

    /**
     * The number of seconds until nodel ist is requested. Currently: 10 minutes
     */
    public static final int NODE_LIST_REQUEST_INTERVAL = 5 * 60;

    /** The number of seconds until a new transfer status is broadcasted */
    public static final int TRANSFER_STATUS_BROADCAST_INTERVAL = 10 * 60;

    /**
     * The time in seconds between broadcast of thoses nodes, that went online.
     * Currently: 30 seconds
     */
    public static final long NODES_THAN_WENT_ONLINE_BROADCAST_TIME = 30;

    /**
     * The maximum number of nodes i a message allowed.
     */
    public static final int NODES_LIST_MAX_NODES_PER_MESSAGE = 500;

    /**
     * The Number of seconds of inactivity on a folder that causes removal of
     * folder from internal db. Currently: 14 days
     */
    public static final long MAX_TIME_OF_FOLDER_INACTIVITY_UNTIL_REMOVED = 30 * 24 * 60 * 60;

    /**
     * The maximum numbers of nodes to connection per kilobytes/s upload in
     * supernode mode
     */
    public static final double MAX_NODES_CONNECTIONS_PER_KBS_UPLOAD = 2;

    /**
     * The maximum time for a node to be offline, util no reconnection will be
     * tried. Currently: 10 hours
     */
    public static final long MAX_NODE_OFFLINE_TIME = 10L * 60 * 60 * 1000;

    /** Time until total node invalidation (if not friend). Currently: 60 days */
    public static final long NODE_TIME_TO_INVALIDATE = 1000 * 60 * 60 * 24 * 60;

    /**
     * Maximum time shift before node gets invalid in the future of the last
     * connection time. Currently: 1 day
     */
    public static final long NODE_TIME_MAX_IN_FUTURE = 1000 * 60 * 60 * 24;

    /** The min number of reconnectors to spawn */
    public static final int MIN_NUMBER_RECONNECTORS = 2;

    /** The max number of reconnectors to spawn */
    public static final int MAX_NUMBER_RECONNECTORS = 14;

    /**
     * the number of seconds (aprox) of delay till the connection is tested and
     * a warning may be displayed.
     */
    public static final long LIMITED_CONNECTIVITY_CHECK_DELAY = 180;

    // Basic networking options ***********************************************

    /**
     * The time in ms, how long the timeout for the socket connect method should
     * be used. default: 30s
     */
    public static final int SOCKET_CONNECT_TIMEOUT = 30 * 1000;

    /**
     * The number of incoming connections to queue until the connection is
     * throttled. Socket backlog is two times this value.
     */
    public static final int MAX_INCOMING_CONNECTIONS = 40;

    /**
     * The time interval in seconds when the incoming connections should be
     * checked
     */
    public static final long INCOMING_CONNECTION_CHECK_TIME = 60;

    /**
     * The maxium time to take for a incoming connection to be processed. (in
     * seconds). This includes the exchange of the full filelists.
     */
    public static final long INCOMING_CONNECTION_TIMEOUT = 20 * 60;

    /**
     * The number of seconds with no-response until a connection times out.
     */
    public static final long CONNECTION_KEEP_ALIVE_TIMOUT = 5 * 60;

    /**
     * The time interval to resize the reconnecor pool in seconds.
     */
    public static final long RECONNECTOR_POOL_SIZE_RESIZE_TIME = 30;

    // UDT stuff **************************************************************
    /**
     * Maximal pending udt connection count
     */
    public static final int MAX_PENDING_UDT_CONNECTIONS = 10;

    /**
     * TimeOut for pending UDT connections
     */
    public static final long TO_UDT_CONNECTION = 60 * 1000;

    // Transfer settings ******************************************************

    /**
     * The maximun number of enqued download from a internet connected node
     */
    public static final int MAX_DLS_FROM_INET_MEMBER = 10;

    /**
     * The maximun number of enqued download from a lan connected node
     */
    public static final int MAX_DLS_FROM_LAN_MEMBER = 50;

    /**
     * The download timeout of a request. After that time of inactivity the
     * download is assumend to be timed out. -> Aborts the download.
     */
    public static final long DOWNLOAD_REQUEST_TIMEOUT_LIMIT = 3L * 60 * 1000;

    /**
     * The upload timeout of a part request. After thet time of inactivity the
     * upload is assumed to be timed out. This value is very large since delta sync allows for
     * quite alot of time to pass before requesting. (time it takes to hash)
     */
    public static final long UPLOAD_PART_REQUEST_TIMEOUT = 5L * 60 * 60 * 1000;

    // ConnectionHandler constants ********************************************

    public static final int MIN_SIZE_FOR_PARTTRANSFERS = 8 * 1024;

    // Time estimation constants

    public static final long ESTIMATION_WINDOW_MILLIS = 300 * 1000;

    public static final int ESTIMATION_MINVALUES = 5;

    // Pro related ************************************************************

    /**
     * The classname of the ProLoader
     */
    public static final String PRO_LOADER_PLUGIN_CLASS = "de.dal33t.powerfolder.CD";

    private Constants() {
        // No instance allowed
    }
}