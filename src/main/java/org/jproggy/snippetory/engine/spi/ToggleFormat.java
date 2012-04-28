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

package org.jproggy.snippetory.engine.spi;

import java.util.Locale;

import org.jproggy.snippetory.spi.Format;
import org.jproggy.snippetory.spi.FormatFactory;

public class ToggleFormat implements Format {
	private int count = 1;
	private final String[] values;

	public ToggleFormat(String[] values) {
		super();
		this.values = values;
	}

	@Override
	public String format(Object value) {
		try {
		if (value instanceof Number) {
			count = ((Number)value).intValue();
		}
		return values[Math.abs((count - 1) % values.length)];
		} finally {
			count++;
		}
	}

	@Override
	public boolean supports(Object value) {
		if (value == null) return true;		
		return value instanceof Number;
	}
	
	public static class Factory implements FormatFactory {
		@Override
		public Format create(String definition, Locale l) {
			return new ToggleFormat(definition.split(";"));
		}
	}

}
