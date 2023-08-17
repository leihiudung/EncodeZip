package com.bvt.encodezip.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class File {

    public int id;
    public String fileName;
    public String fileSuffix;
    public String teleporter;   //  传送人


}
