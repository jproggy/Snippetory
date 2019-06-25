package org.jproggy.snippetory.toolyng.beanery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jproggy.snippetory.Repo;
import org.jproggy.snippetory.Template;

import com.google.common.base.CaseFormat;

public class Beanery {
  private Template beanTpl = Repo.readStream(getClass().getResourceAsStream("Bean.java")).parse();
  private String indent;

  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.out.println("Usage: templatePath targetDir qualifiedClassName");
      return;
    }
    new Beanery().generate(args[0], args[1], args[2]);
  }

  public Beanery() {
    indent = beanTpl.get("class", "i").toString();
  }

  public void generate(String templatePath, String targetDir, String qualifiedClassName) throws IOException {
    Template template = Repo.readFile(templatePath).parse();
    String[] parts = splitPackage(qualifiedClassName);
    Template result = boil(template, parts[0], parts[1]);
    File targetPath = new File(targetDir, qualifiedClassName.replace(".", "/") + ".java");
    targetPath.getParentFile().mkdirs();
    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(targetPath), "utf-8");
    result.render(out);
  }

  private String[] splitPackage(String qualifiedClassName) {
    String[] parts = qualifiedClassName.split("\\.");
    StringBuilder packageHelper = new StringBuilder();
    for (int i = 0; i < parts.length - 1; i++) {
      if (i > 0) packageHelper.append('.');
      packageHelper.append(parts[i]);
    }
    return new String[] { packageHelper.toString(), parts[parts.length - 1] };
  }

  /** 
   * Build an Object-oriented Interface Layer. This layer consists of a get-method for each
   * region, a class for each region used as return type of the getter, and a set method
   * for each location. This construct allows convenient navigation of templates by
   * the syntax completion feature available in most IDEs.
   * 
   * @param subject
   *          The template that shall get the interface layer
   * @param packageName The package where the resulting class of the interface layer is declared
   * @param className The simple name of the class declared 
   * @return A template containing the generated class including necessary inner classes
   */
  public Template boil(Template subject, String packageName, String className) {
    beanTpl.set("package", packageName);
    Template classTpl = beanTpl.get("class");
    generate(classTpl, subject, className, "");
    classTpl.render();
    return beanTpl;
  }

  protected void generate(Template targetTpl, Template subject, String typeName, String i) {
    targetTpl.set("RegionTpl", typeName).set("i", i);
    for (String location : subject.names()) {
      String propName =  CaseHelper.convert(CaseFormat.UPPER_CAMEL, location);
      targetTpl.get("location").set("name", location).set("Name", propName).set("RegionTpl", typeName).set("i", i).render();
    }

    for (String region : subject.regionNames()) {
      String propName = CaseHelper.convert(CaseFormat.UPPER_CAMEL, region);
      String childType =  propName + "Tpl";
      targetTpl.get("region").set("region", region).set("Region", propName).set("RegionTpl", childType).set("i", i).render();
      Template regionTpl = beanTpl.get("class");
      generate(regionTpl, subject.get(region), childType, i + indent);
      regionTpl.render(targetTpl, "classes");
    }
  }
}
