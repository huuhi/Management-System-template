package DAO.domain;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


/**
* 
* @TableName user
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
    * 用户id
    */

    private Integer id;
    /**
    * 用户名
    */

    private String username;
    /**
    * 用户密码
    */
    private String password;
    /**
    * 用户角色，0:普通用户，1:管理员
    */

    private Integer role;

}
