// ${watermark}

${javaSpecs.wrapMultiComment(copyright)}

<#assign aDateTime = .now>
<#assign now = aDateTime?date>
package ${packageName}.${daoSubModPkgName};

import java.util.List;
import org.apache.ibatis.annotations.Param;
import ${organType}.${organName}.${projectName}.common.${moduleName}.${beanSubModPkgName}.${entityName?cap_first};

/**
 * {@link ${entityName?cap_first}}
 *
 * @author ${author}
 * @version ${version}
 * @Date ${now}
 * @since ${since}
 */
public interface ${entityName?cap_first}Dao {

    int insertSelective(${entityName?cap_first} ${entityName?uncap_first});

    int deleteByPrimaryKey(${javaSpecs.toSimpleJavaType(pk.attrType)} ${pk.attrName});

    ${entityName?cap_first} selectByPrimaryKey(${javaSpecs.toSimpleJavaType(pk.attrType)} ${pk.attrName});

    int updateByPrimaryKeySelective(${entityName?cap_first} ${entityName?uncap_first});

    int updateByPrimaryKey(${entityName?cap_first} ${entityName?uncap_first});

    List<${entityName?cap_first}> list(@Param("${entityName?uncap_first}") ${entityName} ${entityName?uncap_first});

}