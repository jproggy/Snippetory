package org.jproggy.snippetory.toolyng.test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jproggy.snippetory.Template;

public class TestRecorder implements Template {
  Template repo;
  Template region;
  Map<String, Template> appends = new HashMap<String, Template>();

  public TestRecorder(Template repo, Template region) {
    super();
    this.repo = repo;
    this.region = region;
  }

  public String getEncoding() {
    return repo.getEncoding();
  }

  public CharSequence toCharSequence() {
    return repo.toCharSequence();
  }

  public Template set(String name, Object val) {
    repo.get("set").set("name", name).set("data", "value").render();
    return this;
  }

  public Template append(String name, Object val) {
    if (!appends.containsKey(name)) {
      appends.put(name, repo.get("append"));
    }
    appends.get(name).get("value").set("name", name).set("data", "value").render();
    return this;
  }

  public Template clear() {
    return this;
  }

  public Template get(String... name) {
    if (name.length == 0) return this;
    @SuppressWarnings("unchecked")
    Template child = new TestRecorder(repo, region.get(name[0]));
    if (name.length > 1) {
      String[] tempNames = new String[name.length - 1];
      System.arraycopy(name, 1, tempNames, 0, tempNames.length);
      return child.get(tempNames);
    }
    return child;
  }

  public Set<String> names() {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<String> regionNames() {
    // TODO Auto-generated method stub
    return null;
  }

  public void render() {
    // TODO Auto-generated method stub

  }

  public void render(String name) {
    // TODO Auto-generated method stub

  }

  public void render(Writer out) throws IOException {
    // TODO Auto-generated method stub

  }

  public boolean isPresent() {
    return false;
  }

  public void render(PrintStream out) throws IOException {
    // TODO Auto-generated method stub

  }

  public void render(Template arg0, String name) {
    // TODO Auto-generated method stub

  }

}
