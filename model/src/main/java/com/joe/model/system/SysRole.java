package com.joe.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.joe.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色")
//@TableName("sys_role")
public class SysRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	//@NotBlank(message = "角色名称不能为空")
	@ApiModelProperty(value = "角色名称")
	@TableField("role_name")
	private String roleName;

	@ApiModelProperty(value = "角色编码")
	@TableField("role_code")
	private String roleCode;

	@ApiModelProperty(value = "描述")
	@TableField("description")
	private String description;

}

