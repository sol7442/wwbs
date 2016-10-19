package com.sol.wwbs.util.tree;


/**
 * This is thrown when the DAO detects a unique constraint violation,
 * i.e. a Java-programmed check indicates an error.
 */
public class UniqueConstraintViolationException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5939293468441834670L;
	private final TreeNode originator;
	
	public UniqueConstraintViolationException(String message, TreeNode originator) {
		super(message);
		this.originator = originator;
	}

	/** @return a clone of the node that caused this exception, containing the invalid value. */
	public TreeNode getOriginator() {
		return originator;
	}
}
