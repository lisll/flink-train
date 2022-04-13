package com.datapipeline.utils;

import java.io.Serializable;

public class MysqlModel implements Serializable {
    private String dirver ;
    private String username;
    private String password;
    private String url;
    private String schemaAndName;

    public MysqlModel(String dirver, String username, String password, String url,String schemaAndName) {
        this.dirver = dirver;
        this.username = username;
        this.password = password;
        this.url = url;
        this.schemaAndName=schemaAndName;
    }

    public String getSchemaAndName() {
        return schemaAndName;
    }

    public void setSchemaAndName(String schemaAndName) {
        this.schemaAndName = schemaAndName;
    }

    public String getDirver() {
        return dirver;
    }

    public void setDirver(String dirver) {
        this.dirver = dirver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
