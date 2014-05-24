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

package org.jproggy.snippetory.test;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.jproggy.snippetory.Encodings;
import org.jproggy.snippetory.Repo;
import org.jproggy.snippetory.Syntaxes;
import org.jproggy.snippetory.Template;
import org.jproggy.snippetory.spi.EncodedData;

public class Page implements EncodedData {
  private final Template template;
  private final Template page;
  private final Map<String, String> errors;
  private final ResourceBundle labels;

  public Page(String title, String target, Map<String, String> errors, ResourceBundle labels) {
    this.errors = errors;
    this.labels = labels;
    template = Repo.readResource("MetaRep.html").encoding(Encodings.html)
        .syntax(Syntaxes.FLUYT_X).locale(Locale.US).parse();
    page = template.get("page");
    page.set("title", title);
    page.set("target", target);
  }

  public Section createSection(String title) {
    return new Section(title);
  }

  public void render(PrintStream out) throws IOException {
    page.render();
    template.render(out);
  }

  public class Section implements EncodedData {
    private Template section;

    private Section(String title) {
      section = page.get("section").set("title", title);
    }

    public Section addTextAttrib(String name, Object value) {
      Template attrib = getAttrib(name);
      Template control = section.get("controls", "text").set("name", name).set("value", value);
      control.render(attrib, "control");
      attrib.render();
      return this;
    }

    private Template getAttrib(String name) {
      Template attrib = section.get("attribute");
      attrib.set("label", labels.getString(name));
      if (errors.containsKey(name)) attrib.get("msg").set("error", errors.get(name)).render();
      return attrib;
    }

    public Section addSelectionAttrib(String name, Collection<?> values, Object selected) {
      Template attrib = getAttrib(name);
      Template control = section.get("controls", "select");
      control.set("name", name);
      for (Object value : values) {
        String type = value.equals(selected) ? "selected_option" : "option";
        Template option = control.get(type).set("value", value).set("label", labels.getString(value.toString()));
        option.render("option");
      }
      control.render(attrib, "control");
      attrib.render();
      return this;
    }

    public Section addMultiSelectionAttrib(String name, Collection<?> values, Collection<?> selected) {
      Template attrib = getAttrib(name);
      for (Object value : values) {
        String type = selected.contains(value) ? "selected_checkbox" : "checkbox";
        Template control = section.get("controls", type).set("name", name).set("value", value)
            .set("label", labels.getString(value.toString()));
        control.render(attrib, "control");
      }
      attrib.render();
      return this;
    }

    public Section addDescription(Object desc) {
      section.get("description").set("description", desc).render();
      return this;
    }

    public void render() {
      section.render();
    }

    @Override
    public String getEncoding() {
      return Encodings.html.name();
    }

    @Override
    public CharSequence toCharSequence() {
      return section.toCharSequence();
    }
  }

  @Override
  public String toString() {
    page.render();
    return template.toString();
  }

  @Override
  public String getEncoding() {
    return Encodings.html.name();
  }

  @Override
  public CharSequence toCharSequence() {
    return template.toCharSequence();
  }
}
