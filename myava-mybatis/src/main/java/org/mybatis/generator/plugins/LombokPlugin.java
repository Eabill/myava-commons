package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.sql.Types;
import java.util.List;
import java.util.Properties;

/**
 * Lombok插件-用于代码生成
 * 使用说明：class文件放入mybatis-generator-core相应位置即可
 *
 * @author biao
 */
public class LombokPlugin extends PluginAdapter {

	/**
	 * client超类名称
	 */
	private String clientSuperInterfaceName;

	/**
	 * 实体类包名
	 */
	private String javaModelPackageName;

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		clientSuperInterfaceName = properties.getProperty("clientSuperInterfaceName");
		javaModelPackageName = properties.getProperty("javaModelPackageName");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	// 生成导入类及类型注解
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// 引入import
		topLevelClass.addImportedType("javax.persistence.Table");
		// 主键
		if (introspectedTable.hasPrimaryKeyColumns()) {
			topLevelClass.addImportedType("javax.persistence.Id");
			topLevelClass.addImportedType("javax.persistence.GeneratedValue");
			topLevelClass.addImportedType("javax.persistence.GenerationType");
		}
		topLevelClass.addImportedType("javax.persistence.Column");
		topLevelClass.addImportedType("lombok.Data");
		topLevelClass.addImportedType("lombok.Builder");
		topLevelClass.addImportedType("lombok.NoArgsConstructor");
		topLevelClass.addImportedType("lombok.AllArgsConstructor");

		// 引入类型注解
		topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTable() + "\")");
		topLevelClass.addAnnotation("@Data");
		topLevelClass.addAnnotation("@NoArgsConstructor");
		topLevelClass.addAnnotation("@AllArgsConstructor");
		topLevelClass.addAnnotation("@Builder(toBuilder = true)");

		return true;
	}

	// 模型字段生成
	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		// 添加注解
		String actualColumnName = introspectedColumn.getActualColumnName();
		for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
			if (actualColumnName.equals(column.getActualColumnName())) { // 主键
				field.addAnnotation("@Id");
				field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
			}
		}
		field.addAnnotation("@Column(name = \"" + actualColumnName + "\")");
		// 设置TINYINT为Integer类型
		if (Types.TINYINT == introspectedColumn.getJdbcType()) {
			field.setType(PrimitiveTypeWrapper.getIntegerInstance());
		}
		return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
	}
	
	// 不用生成Setter方法
	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		return false;
	}

	// 不用生成Getter方法
	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		return false;
	}
	
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// client添加@Repository注解
		interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
		interfaze.addAnnotation("@Repository");

		if (clientSuperInterfaceName != null) {
			String clientSuperInterfaceSimpleName = clientSuperInterfaceName.substring(clientSuperInterfaceName.lastIndexOf(".") + 1);
			String javaModelSimpleName = interfaze.getType().getShortName().replaceAll("Mapper$", "");
			interfaze.addImportedType(new FullyQualifiedJavaType(clientSuperInterfaceName));
			interfaze.addImportedType(new FullyQualifiedJavaType(javaModelPackageName.concat(".").concat(javaModelSimpleName)));
			interfaze.addSuperInterface(new FullyQualifiedJavaType(clientSuperInterfaceSimpleName.concat("<").concat(javaModelSimpleName).concat(">")));
		}

//		interfaze.getMethods().clear();
//		interfaze.getAnnotations().clear();
		
		return true;
	}
	
	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}
    
	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
}
