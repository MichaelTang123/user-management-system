package com.michaeltang.usermanagement.common.constants;

/**
 * Global constants definitions
 * @author tangyh
 *
 */
public class Common {
	/** Thread pool*/
    /**
     * Thread pool for notification handling tasks.
     */
    public static final String WORKER_GROUP_COMMON = "Worker-Common";
    
	/**
	 * Thread pool for read request ingestion.
	 */
	public static final String WORKER_GROUP_READ_REQ = "Worker-Read-Req";
	
	/**
	 * Thread pool for write request ingestion.
	 */
	public static final String WORKER_GROUP_WRITE_REQ = "Worker-Write-Req";
	
	/**
	 * Thread pool for DB async-read operations.
	 */
    public static final String WORKER_GROUP_DB = "Worker-DB-Read";
    
    /**
	 * Thread pool for Email notification sending.
	 */
    public static final String WORKER_GROUP_EMAIL = "Worker-DB-Email";
    
    /**
     * User state MASK definitions.
     */
    public static final int USER_STAT_ACTIVATION = 0;
    
    
    /** Email States */
    /**
     * The initial state.
     */
    public static final String EMAIL_PENDING = "pending";
    
    /**
     * Sending started.
     */
    public static final String EMAIL_SENDING = "sending";
    
    /**
     * fail to send, the detailed error will be recorded in emailError field.
     */
    public static final String EMAIL_FAILED = "failed";
    
    /**
     * Completed.
     */
    public static final String EMAIL_COMPLETED = "completed";
}
