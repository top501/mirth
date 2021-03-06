/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 *
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.ant;

import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.model.User;

/**
 * an ant task to add a mirth user
 * 
 * @author andrzej@coalese.com
 */

public class UserAddTask extends AbstractMirthTask {
	protected String userid = "";
	protected String pswd = "";
	protected String firstName = "";
	protected String lastName = "";
	protected String organization = "";
	protected String email = "";

	/**
	 * @param pswd
	 */
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	/**
	 * @param userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @param organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */

	public void executeTask() throws BuildException {
		try {
			if (userid.length() > 0) {
				connectClient();
				commandUserAdd();
				disconnectClient();
			} else {
				throw (new BuildException("Userid no specified"));
			}
		} catch (ClientException e) {
			throw (new BuildException("Mirth client exception caught: " + e.getMessage(), e));
		}
	}

	private void commandUserAdd() throws ClientException, BuildException {
		User newUser = new User();

		newUser.setUsername(userid);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setOrganization(organization);
		newUser.setEmail(email);

		List<User> users = client.getUser(null);

		for (Iterator<User> iter = users.iterator(); iter.hasNext();) {
			User luser = iter.next();
			if (luser.getUsername().equalsIgnoreCase(userid)) {
				throw (new BuildException("Unable to add user, username already in use: " + userid));
			}
		}

		try {
		    List<String> responses = client.checkOrUpdateUserPassword(newUser, pswd);
		    if (responses != null) {
		        for (String response : responses) {
		            System.out.println(response);
		        }
		        return;
		    }
			client.updateUser(newUser);
			// Get the new user object that contains the user id
            User validNewUser = client.getUser(newUser).get(0);
            responses = client.checkOrUpdateUserPassword(validNewUser, pswd);
            
            if (responses != null) {
                System.out.println("User \"" + userid + "\" has been created but the password could not be set:");
                for (String response : responses) {
                    System.out.println(response);
                }
            } else {
                System.out.println("User \"" + userid + "\" added successfully.");
            }
		} catch (Exception e) {
			throw (new BuildException("Unable to add user \"" + userid + "\": " + e));
		}
	}

}
