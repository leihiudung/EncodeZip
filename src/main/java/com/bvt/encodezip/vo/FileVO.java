package com.bvt.encodezip.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @projectName: EncodeZip
 * @package: com.bvt.encodezip.vo
 * @className: FileVO
 * @author: Tom
 * @description: TODO
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FileVO {
    public int id;
    public String fileName;
    public String fileAliasName;
    public String fileSuffix;
    public String teleporter;   //  传送人
}
