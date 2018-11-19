package com.blossomproject.showcase.initializr.model.properties;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
public class Version {
  private String blossom;
  private String springboot;
  private String kotlin;
  private String mavenWarPlugin;
  private String mavenVersion;

  public String getBlossom() {
    return blossom;
  }

  public void setBlossom(String blossom) {
    this.blossom = blossom;
  }

  public String getSpringboot() {
    return springboot;
  }

  public void setSpringboot(String springboot) {
    this.springboot = springboot;
  }

  public String getKotlin() {
    return kotlin;
  }

  public void setKotlin(String kotlin) {
    this.kotlin = kotlin;
  }

  public String getMavenWarPlugin() {
    return mavenWarPlugin;
  }

  public void setMavenWarPlugin(String mavenWarPlugin) {
    this.mavenWarPlugin = mavenWarPlugin;
  }

  public String getMavenVersion() {
    return mavenVersion;
  }

  public void setMavenVersion(String mavenVersion) {
    this.mavenVersion = mavenVersion;
  }
}
