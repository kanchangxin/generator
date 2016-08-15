package com.logomama.web.admin;

import com.github.pagehelper.PageInfo;
import com.logomama.data.dao.mapping.${MapperName};
import com.logomama.data.model.${DomainObjectName};
import com.logomama.data.model.${ExampleName};
import com.logomama.web.commons.Constant;
import com.logomama.web.commons.exception.InvalidParamException;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
/*  v1:初始建造，author:自动生成,date: ${createDate?datetime?string.short}
    <#--
    针对angularjs controller
    生成的模板不同项目需要按照自己的格式修改
    -->
*/

@Controller
@RequestMapping(value = "/admin/${DomainObjectName?lower_case }")
public class ${DomainObjectName}Controller {
    public static final Logger logger = LoggerFactory.getLogger(${DomainObjectName}Controller.class);

    @Autowired
    ${MapperName} mapper;

    /**
     * 查询方法，返回列表，带分页
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public PageInfo list(HttpServletRequest request, @RequestBody ${DomainObjectName} model, @RequestParam int p) {
        ${ExampleName} example = new  ${ExampleName}();
        ${ExampleName}.Criteria criteria = example.createCriteria();
        example.setOrderByClause("Id");//排序

        //组装查询条件
        <#list DomainObjectPropertys as col>
            <#assign colPascalName=col.getJavaProperty()?cap_first>
                    <#if col.getJdbcTypeName()=="VARCHAR">
        if(!StringUtils.isBlank(model.get${colPascalName}() ))
            criteria.and${colPascalName}Like(model.get${colPascalName}());
                    <#else>
        if(model.get${colPascalName}()!=null )
            criteria.and${colPascalName}EqualTo(model.get${colPascalName}());
                    </#if>
        </#list>

        List<${DomainObjectName}> list = mapper.selectByExampleWithRowbounds(example, new RowBounds(p, Constant.PageSize));
        PageInfo page = new PageInfo(list);
        return page;
    }

//    /**
//     * 查询方法，返回列表，不分页,如非必要不要放出来
//     */
//    @RequestMapping(value = "/listall", method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public List<WikiCasetype> listAll(  @RequestBody ${DomainObjectName} model ) {
//        ${ExampleName} example = new  ${ExampleName}();
//        ${ExampleName}.Criteria criteria = example.createCriteria();
//        example.setOrderByClause("Id");//排序
//
//        //组装查询条件
//        <#list DomainObjectPropertys as col>
//        <#assign colPascalName=col.getJavaProperty()?cap_first>
//        <#if col.getJdbcTypeName()=="VARCHAR">
//                if(!StringUtils.isBlank(model.get${colPascalName}() ))
//                criteria.and${colPascalName}Like(model.get${colPascalName}());
//        <#else>
//                if(model.get${colPascalName}()!=null )
//                criteria.and${colPascalName}EqualTo(model.get${colPascalName}());
//        </#if>
//        </#list>
//
//        List<${DomainObjectName}> = mapper.selectByExample(  example);
//        return list;
//    }



    @ResponseBody
    @RequestMapping(value = "/upd", method = {RequestMethod.GET, RequestMethod.POST})
    public void mdf(@RequestBody ${DomainObjectName} model) {
        model.setLastModDate(new Date().getTime());
        mapper.updateByPrimaryKey(model);
    }

    @ResponseBody
    @RequestMapping(value = "/del", method = {RequestMethod.GET, RequestMethod.POST})
    public void del(@RequestBody ${DomainObjectName} model) {
        if (model.getId() == null) {
            throw new InvalidParamException("缺少指定要删除的id");
        }
        mapper.deleteByPrimaryKey(model.getId());
    }


    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public void add(@RequestBody ${DomainObjectName} model) {

        //<editor-fold desc="参数检查">
        String returnErrMsg=null;
        <#list DomainObjectPropertys as col>
            <#if col.getJavaProperty()!="id">
                <#if col.isNullable()==false>
                <#assign colPascalName=col.getJavaProperty()?cap_first>
                    <#if col.getJdbcTypeName()=="INTEGER">
        if(model.get${colPascalName}()==0)
                    <#else>
        if(StringUtils.isBlank(model.get${colPascalName}() ))
                    </#if>
            returnErrMsg="${col.getRemarks()}不能为空";

                </#if>

        if(model.get${colPascalName}().length()>${col.getLength()}){
            returnErrMsg="${col.getRemarks()}长度不能超过${col.getLength()}";
        }
            </#if>
        </#list>

        if (returnErrMsg!=null){
            throw new  InvalidParamException( returnErrMsg);
        }
        //</editor-fold>


        model.setCreateDate(new Date().getTime());
        mapper.insert(model);
    }
}
