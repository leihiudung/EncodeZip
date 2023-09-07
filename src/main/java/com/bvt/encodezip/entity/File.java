package com.bvt.encodezip.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class File {

    public int id;
    public String fileName;
    public String fileAliasName;
    public String fileSuffix;
    public String filePath;
    public String teleporter;   //  传送人
    public int stats;
    public Date createDate;
    public Date updateDate;


}
