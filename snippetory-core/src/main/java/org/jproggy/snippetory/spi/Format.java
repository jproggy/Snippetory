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

package org.jproggy.snippetory.spi;

import org.jproggy.snippetory.engine.FormatRegistry;

/**
 * The format allows to encapsulate and reuse portions of view logic in a simple and generic
 * way.
 * <h2>Format definition in template file</h2> 
 * Formats are activated by a single attribute, that's evaluated by a {@link FormatFactory}, 
 * that parses this definition, found within the template file, in order to create an appropriate 
 * {@code Format}. However, before creating he format itself there is another step to do:
 * The FormatFactory returns a {@link FormatConfiguration} which takes additional parameters, called
 * sub-attributes. Sub attributes have the form {@code<main-attribute>.<sub-attribute>} and directly 
 * follow the main attribute. For instance: 
 * <p>
 * {@code stretch="20" stretch.align="left"}
 * </p>
 * Sub-attributes are implemented as bean properties of the {@link FormatConfiguration} implementation:
 * <p>
 * <pre>
 *  enum Alignment {left, right};
 *  public void setAlign(Alignment value) {..
 * </pre>
 * </p>
 * This mechanism provides support for numbers, boolean, enums, and strings. 
 * Alternatively, especially when the attributes are not known at build time, this can be done
 * by implementing {@link DynamicAttributes} instead. However, the separation of Format
 * and FormatConfiguration is for advanced state handling and validation of the configuration. For
 * most Formats extending {@link SimpleFormat} makes things much simpler by combining
 * both.
 * <h2>Binding data to formats at runtime</h2>
 * In order to bind data to a format at runtime you have to implement {@link VoidFormat} and
 * of course you've to take care for handling the state apropriately. Returning a new instance per
 * call of {@link FormatConfiguration#getInstance} will do the job.
 * 
 * @see <a href="http://www.jproggy.org/snippetory/Formats.html">Official documentation on formats</a>
 * @see FormatFactory
 * @see FormatConfiguration
 * @see SimpleFormat
 * @see VoidFormat
 *  
 * @author B. Ebertz
 */
public interface Format {
	/**
	 * Register {@link FormatFactory FormatFactories} here.
	 */
	FormatRegistry REGISTRY = FormatRegistry.INSTANCE;

	/**
	 * Will only be called for supported values.
	 * 
	 * @param location gives some additional information to the this call 
	 * like the target encoding
	 * @param value
	 */
	Object format(TemplateNode location, Object value);
	
	/**
	 * Many formats only apply to a special type or even specific
	 * values. The format method will only be called for supported values
	 */
	boolean supports(Object value);
	
	/**
	 * Resets the state of the format to initialization value.
	 * As most formats are state-less they can safely ignore this method.  
	 * 
	 * @param location
	 */
	void clear(TemplateNode location);
}
