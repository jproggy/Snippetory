/*******************************************************************************
 * Copyright (c) 2011-2012 JProggy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * EXCEPT AS EXPRESSLY SET FORTH IN THIS AGREEMENT, THE PROGRAM IS PROVIDED ON AN 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR 
 * IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, 
 * NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE
 *******************************************************************************/

package org.jproggy.snippetory.engine;

import java.util.LinkedHashMap;

/**
 * A token is a portion of the template that fulfills a special purpose, view from
 * the perspective of a syntax to parse this Snippetory template. As Snippetory
 * works with a simple syntax scheme there's only a small number of different purposes
 * such a token can fulfill. They are defined in the enum {@link TokenType}. 
 * 
 *  A token consists of the portion of template code it represents, the start position of
 *  this code, the token type and some data that's depends on token type.
 * 
 * @author B. Ebertz
 */
public class Token {
	public enum TokenType {
		BlockStart, BlockEnd, Field, Syntax, TemplateData, Comment
	}

	private final LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
	private final String name;
	private final String content;
	private final TokenType type;
	private final int position;
	
	public Token(String name, String content, TokenType type, int position) {
		super();
		this.name = name == null ? null : name.intern();
		this.content = content;
		this.type = type;
		this.position = position;
	}

	/**
	 * attributes are only provided for {@link TokenType#BlockStart} and  
	 */
	public LinkedHashMap<String, String> getAttributes() {
		return attributes;
	}

	public String getName() {
		return name;
	}

	public TokenType getType() {
		return type;
	}

	/**
	 *  as it's taken from template data 
	 */
	public String getContent() {
		return content;
	}

	/**
	 *  the position where the content starts within the entire template data.
	 *  It dosen't matter where this syntax started to be used.  
	 */
	public int getPosition() {
		return position;
	}
}
